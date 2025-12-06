# Spring Contacts App

## Overview

This is going to be a sample project created to showcase my understanding of Java 25 and the Spring framework version 7 with PostgreSQL as the database of choice. The browser web application will be written using Vue 3 with TypeScript and Rolldown Vite.

The application is going to be a simple contacts app, where users may log in to their accounts, and perform various kinds of CRUD operations on saved contacts.

## Planning the API Schema and Endpoints

The API will allow the client side to communicate with the Spring server application. Given that this API is not intended for direct public usage, it is expected that the usage of the API fetched data is going to be consistent and there will be little if not no reason for overfetching or underfetching of data. As such, implementing a standard RESTful API is suitable in this case.

Given that the API may be updated over time, we will implement proper versioning. The first version of our API shall be accessible from the endpoint `/api/v1`.

We can divide this into two major categories: Authentication and Contacts Management. Unless otherwise stated, assume that all endpoints for the API from hereafter are assumed to have `/api/v1` as prefix.

### Authentication

We will need one endpoint for allowing users to register a new account, and another one for allowing them to log in. For the time being, we will not be implementing any external authentication, such as "Sign in with Google" or the like.

#### Registeration of new account

The basics of registeration will involve the user to create an account with a Username, a Nickname, and Password. The Username must be unique, and we will enforce the password to be atleast 8 characters long and contain atleast 1 digit, 1 uppercase letter, 1 lowercase letter, and 1 symbol from a QWERTY English keyboard (!, @, #, $, etc.)

Since we will be creating a new resource on server, and we do not want the sensitive details to be hijacked via URL parameters, the POST endpoint is the most suitable for this choice. The endpoint will be accessed via `/register` and require the user to pass in a JSON in the request payload with keys as so:

| Key        | Type     | Additional Constraints                                               |
|------------|----------|----------------------------------------------------------------------|
| `username` | `string` | Must be alphanumeric only                                            |
| `nickname` | `string` | Must only contain characters in the standard QWERTY English keyboard |
| `password` | `string` | Must only contain characters in the standard QWERTY English keyboard |

After the request is sent, the response received from the server can be one of the following:

**Success:**

If the creation of the user is successful, the server will return the response of Status 200 and response payload as JSON with the following keys:

| Key                | Type     | Description                                         |
|--------------------|----------|-----------------------------------------------------|
| `id`               | `string` | A unique id for each account                        |
| `createdTimestamp` | `string` | An ISO 8601 formatted timestamp of account creation |
| `username`         | `string` | The unique username for each account                |
| `nickname`         | `string` | The user's preferred nickname                       |

Behind the scenes, the server must undertake the following:

- Validate `username` for uniqueness by querying the database (the client app is expected to do the same)
- Scan `username` and `nickname` for any illegal, offensive, or hateful content
- Validate `password` using RegEx for the rules listed above (the client app is expected to do the same)

After performing the necessary validation, the server may begin to insert the data into the database:

- Generate an ID for the user using a customized snowflake based upon the 2020 epoch:
  - The id will be a 64-bit integer (`long`)
  - The first 45 bits represent the number of milliseconds since 00:00:00 January 01, 2020, UTC
  - The next 8 bits represent the server id (must be passed in via environmental variables)
  - The next 8 bits represent the thread id
  - The remaining 3 bits will be used for an autoincrementer to disambiguate any ids that are created within the same millisecond and on the same server and thread. We will keep this at 0 for now due to simplicity
  - The overall formula would be: `((timestamp - 1577836800000L) << 19) + (id1 << 11) + (id2 << 3) + (inc)`
- Hash `password` using a strong computationally expensive hashing process such as Argon2
- Set the same timestamp used for ID as the `recent_password_update_timestamp`.
- Store `id`, `username`, `nickname`, `hashed_password`, and `recent_password_update_timestamp` in the PostgreSQL database

Given the information we have gathered, we shall name the database as `spring_contacts_db` and it's first table shall be:

```sql
CREATE TABLE users (
    id bigint NOT NULL PRIMARY KEY,
    username varchar(63) NOT NULL UNIQUE,
    nickname varchar(63) NOT NULL,
    hashed_password varchar(63) NOT NULL,
    recent_password_update_timestamp bigint NOT NULL
);
```

**Basic error format:**

All errors and exceptions will be returned in a standard format as so:

| Key           | Type     | Description                                                                                        |
|---------------|----------|----------------------------------------------------------------------------------------------------|
| `status`      | `number` | The HTTP Status code of the response                                                               |
| `timestamp`   | `string` | An ISO 8601 formatted timestamp for when error was thrown                                          |
| `errorType`   | `string` | The title for the HTTP status                                                                      |
| `description` | `string` | A user-friendly description stripped of any information that could expose underlying functionality |

**Username already taken:**

This error is unlikely to occur as the client is expected to poll the username (endpoint for that later) and inform the user that a username is taken before they are even allowed to proceed. However, given that clientside code can always be bypassed, a server check is mandatory, and will return the error response of Status 409: Conflict

**Username or Nickname contains invalid characters:**

(This error is unlikely to occur as the client is expected to perform RegEx to provide feedback to the user and disable various buttons)

Returns an error response of Status 400 Bad Request with message "Your <username/nickname> contains invalid characters. Please ensure your <username/nickname> only includes characters from within the "

**Username of Nickname contains prohibited content:**

WIP

**Password is Invalid:**

WIP

**Internal Server Error:**

WIP

#### Logging in

After the registration is complete, the user will need to log in with their username and password to authenticate. In this process, the user sends their username and password to the server. If the server finds it to be satisfactory, it will return a JWT token that the client must use for requests under the Contacts Management section.

The API endpoint the client must use to perform this shall be a POST to `/auth` and requires a JSON with the following keys:

| Key        | Type     |
|------------|----------|
| `username` | `string` |
| `password` | `string` |

Behind the scenes, the server must first check if the username exists on the database. After that, it will perform Argon2 verification of the submitted password with what was stored on the database. If it matches, the server shall begin token generation. It must be done on the fly in order to ensure it remains stateless. This is how it shall be done:

- The header of the JWT must be a JSON with the following keys:

| Key   | Type     | Value     |
|-------|----------|-----------|
| `alg` | `string` | `"HS256"` |
| `typ` | `string` | `"JWT"`   |

- The payload of the JWT must be a JSON with the following keys:

| Key        | Type     | Description                                                                  |
|------------|----------|------------------------------------------------------------------------------|
| `aud`      | `string` | RFC 7519: The domain URL of the contacts app                                 |
| `iat`      | `number` | RFC 7519: Timestamp in seconds from Unix Epoch of most recent password reset |
| `iss`      | `string` | RFC 7519: The domain URL of the contacts app                                 |
| `sub`      | `string` | RFC 7519: The account ID in string format                                    |
| `username` | `string` | The username of the account                                                  |

- The server must maintain a global secret key of atleast 256 bits. To maintain statelessness, this cannot be stored on the database, and must be passed in via environmental variables. This secret key must never be leaked. If the worst comes to pass, where both the database is breached and the secret key is leaked; the attackers will be able to obtain full control of every single account. As a countermeasure, we may create a backup global secret key that is airgapped at all times, but within reach to quickly replace and invalidate all tokens if the worst comes to pass.

- This global secret key will be used to perform Hmac (either 256 or 512) on the hashed password in the database, the resultant of that shall be used as the signature of the JWT

- This signature will be used to sign the JWT using the HmacSha256 algorithm before returning the token in the response.

Upon sending the request, the client may expect any one of these following responses:

**Success:**

If the authentication process is successful, the server will return the token in the form of a JWT as so:

| Key         | Type     | Description                                                            |
|-------------|----------|------------------------------------------------------------------------|
| `timestamp` | `string` | When the authentication process was performed                          |
| `token`     | `string` | A stateless JWT that the client must cache and use for resource access |


**Password is invalid:**

WIP

**Username does not exist:**

WIP

### Contacts Management

<!-- This section is still work in progress -->
-----

Rough notes not yet formalized:

- Database ID generation will follow the customized snowflake format based off the 2020 Epoch
- JWT for user authentication to use a serverwide master secret and the hashed password
- Nickname must only contain characters from the QWERTY English keyboard to make it easier to enforce prohibited content
- Users table must have column: id, username, nickname, hashed_pass, password_reset_timestamp
- Contacts table must have column: id, user_id, name, email, phone... WAIT HOW DO I MAP MULTIPLE PHONE NUMBERS WITH LABEL? NEED MORE BRAINSTORMING

Banned words initial brainstorming with character mapping:

| QWERTY Character      | Common Leetspeak Look-Alike |      Mapped To      |
|:----------------------|:---------------------------:|:-------------------:|
| **Uppercase** (`A-Z`) |             N/A             | `Lowercase` (`a-z`) |
| `1`                   |         `i` or `l`          |         `i`         |
| `!`                   |             `i`             |         `i`         |
| `0`                   |             `o`             |         `o`         |
| `3`                   |             `e`             |         `e`         |
| `7`                   |             `t`             |         `t`         |
| `@`                   |             `a`             |         `a`         |
| `4`                   |             `a`             |         `a`         |
| `$`                   |             `s`             |         `s`         |
| `5`                   |             `s`             |         `s`         |
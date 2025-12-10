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

moved to REGISTER.md

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
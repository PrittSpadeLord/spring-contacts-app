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

Since we will be creating a new resource on server and we do not want the sensitive details to be hijacked via URL parameters, the POST endpoint is the most suitable for this choice. The endpoint will be accessed via `/register` and require the user to pass in a JSON in the request payload with keys as so:

| Key        | Type     | Additional Constraints                                               |
|------------|----------|----------------------------------------------------------------------|
| `username` | `string` | Must be alphanumeric only                                            |
| `nickname` | `string` | Must only contain characters in the standard QWERTY English keyboard |
| `password` | `string` | Must only contain characters in the standard QWERTY English keyboard |

After the request is sent, the response received from the server can be one of the following:

**Success:**

If the creation of the user is successful, the server will return the response of Status 200 and response payload as JSON with the following keys:

| Key         | Type     | Description                                         |
|-------------|----------|-----------------------------------------------------|
| `id`        | `number` | A unique id for each account                        |
| `timestamp` | `string` | An ISO 8601 formatted timestamp of account creation |
| `username`  | `string` | The unique username for each account                |
| `nickname`  | `string` | The user's preferred nickname                       |

Behind the scenes, the server must undertake the following:

- Validate `username` for uniqueness by querying the database (the client app is expected to do the same)
- Scan `username` and `nickname` for any illegal, offensive, or hateful content
- Validate `password` using RegEx for the rules listed above (the client app is expected to do the same)
- Hash `password` using a strong computationally expensive hashing process such as Argon2
- Store the hashed password in the database

**Username already taken:**

This error is unlikely to occur as the client is expected to poll the username (endpoint for that later) and inform the user that a username is taken before they are even allowed to proceed. However, given that clientside code can always be bypassed, a server check is mandatory, and will return a response of Status 409 with the following JSON payload:

| Key           | Type     | Description                                               |
|---------------|----------|-----------------------------------------------------------|
| `status`      | `number` | The HTTP Status code of the response                      |
| `timestamp`   | `string` | An ISO 8601 formatted timestamp for when error was thrown |
| `errorType`   | `string` | The title for the HTTP status                             |
| `description` | `string` | A user-friendly description mentioning the username was already taken; stripped of any information that could expose underlying functionality |

**Username or Nickname contains invalid characters:**

WIP

**Username of Nickname contains prohibited content:**

WIP

**Password is Invalid:**

WIP

<!-- This section is still work in progress -->
-----

Rough notes not yet formalized:

- Database ID generation will follow the customized snowflake format based off the 2020 Epoch
- JWT for user authentication to use a serverwide master secret and the hashed password
- Nickname must only contain characters from the QWERTY English keyboard to make it easier to enforce prohibited content
# Spring Contacts App

## Overview

This is going to be a sample project created to showcase my understanding of Java 25 and the Spring framework version 7 with PostgreSQL as the database of choice. The browser web application will be written using Vue 3 with TypeScript and Rolldown Vite.

The application is going to be a simple contacts app, where users may log in to their accounts, and perform various kinds of CRUD operations on saved contacts.

## Planning the API Schema and Endpoints

The API will allow the client side to communicate with the Spring server application. Given that this API is not intended for direct public usage, it is expected that the usage of the API fetched data is going to be consistent and there will be little if not no reason for overfetching or underfetching of data. As such, implementing a standard RESTful API is suitable in this case.

Given that the API may be updated over time, we will implement proper versioning. The first version of our API shall be accessible from the endpoint `/api/v1`.

We can divide this into two major categories: Authentication and Contacts Management. Unless otherwise stated, assume that all endpoints for the API from hereafter are assumed to have `/api/v1` as prefix.

But before we proceed further, let us decide upon a common schema for error handling. It shall be returned in this format for any kind of error that might be returned from the application:

|       Key        |   Type   | Description                                                                                        |
|:----------------:|:--------:|:---------------------------------------------------------------------------------------------------|
|     `status`     | `number` | The HTTP Status code of the response                                                               |
|   `timestamp`    | `string` | An ISO 8601 formatted timestamp for when error was thrown                                          |
|   `errorType`    | `string` | The title for the HTTP status                                                                      |
|  `description`   | `string` | A user-friendly description stripped of any information that could expose underlying functionality |
| `additionalData` | `object` | Additional object-type data that is inconvenient to be placed within `description`                 |

It would also be a good moment to mention a generic "catch-all" error for any kind of expected server error that our application might encounter during its operation. Naturally, it shall be returned as a 500 Internal server error with this message: "An unknown error has occured! Don't worry, we have recorded what just happened and will investigate shortly! We apologize for the inconvenience".

### Authentication

We will need one endpoint for allowing users to register a new account, and another one for allowing them to log in. For the time being, we will not be implementing any external authentication, such as "Sign in with Google" or the like.

#### Registeration of new account

The basics of registeration will involve the user to create an account with a Username, a Nickname, and Password. The Username must be unique, and we will enforce the password to be atleast 8 characters long and contain atleast 1 digit, 1 uppercase letter, 1 lowercase letter, and 1 symbol from a QWERTY English keyboard (!, @, #, $, etc.)

Since we will be creating a new resource on server, and we do not want the sensitive details to be hijacked via URL parameters, the POST endpoint is the most suitable for this choice. The endpoint will be accessed via `/register` and require the user to pass in a JSON in the request payload with keys as so:

|    Key     |   Type   | Additional Constraints                                               |
|:----------:|:--------:|:---------------------------------------------------------------------|
| `username` | `string` | Must be alphanumeric only                                            |
| `nickname` | `string` | Must only contain characters in the standard QWERTY English keyboard |
| `password` | `string` | Must only contain characters in the standard QWERTY English keyboard |

The implementation details of how this request will be processed will be found in `REGISTER_USER.md`. After the request is sent, the response received from the server can be one of the following:

**Success:**

If the creation of the user is successful, the server will return the response of Status 200 and response payload as JSON with the following keys:

|        Key         |   Type   | Description                                         |
|:------------------:|:--------:|:----------------------------------------------------|
|        `id`        | `string` | A unique id for each account                        |
| `createdTimestamp` | `string` | An ISO 8601 formatted timestamp of account creation |
|     `username`     | `string` | The unique username for each account                |
|     `nickname`     | `string` | The user's preferred nickname                       |

**Username already taken:**

This error is unlikely to occur as the client is expected to poll the username (endpoint for that later) and inform the user that a username is taken before they are even allowed to proceed. However, given that clientside code can always be bypassed, a server check is mandatory, and will return the error response of Status 409: Conflict

**Data supplied is invalid:**

WIP (This error is unlikely to occur as the client is expected to perform RegEx to provide feedback to the user and disable various buttons)

Returns an error response of Status 400 Bad Request with description: "The input you have provided is invalid! See additionalData for more information". In the additional data, it will provide a list of fields and a user-friendly explanation for how it has failed the validation.

**Username of Nickname contains prohibited content:**

WIP

#### Logging in

After the registration is complete, the user will need to log in with their username and password to authenticate. In this process, the user sends their username and password to the server. If the server finds it to be satisfactory, it will return a JWT token that the client must use for requests under the Contacts Management section.

The API endpoint the client must use to perform this shall be a POST to `/auth` and requires a JSON with the following keys:

|    Key     |   Type   |
|:----------:|:--------:|
| `username` | `string` |
| `password` | `string` |

Implementation details of how the server will process it will be found in `LOGGING_IN.md`. Upon sending the request, the client may expect any one of these following responses:

**Success:**

If the authentication process is successful, the server will return the token in the form of a JWT as so:

|     Key     |   Type   |                              Description                               |
|:-----------:|:--------:|:----------------------------------------------------------------------:|
| `timestamp` | `string` |             When the authentication process was performed              |
|   `token`   | `string` | A stateless JWT that the client must cache and use for resource access |


**Password is invalid:**

WIP

**Username does not exist:**

WIP

### Contacts Management

Now that we have our JWTs to provide identity proof, We may devise the various CRUD operations:

- Creating a new contact
- Reading a contact (as list or as details)
- Updating some fields in a contact
- Deleting the contact

The reading, updating, and deleting contacts need to be designed in such a way that each token is only allowed to access contacts it has "ownership" of. If the contacts was an SQL table, it would have a user_id column as foreign key, and only those entries should be permissible for access. In practice, we will also have a client-side layer that never loads content the user does not have access to, so this will rarely occur in practice, but having the security on serverside is paramount nontheless

It may be time to decide what are the fields needed in a contacts table, such as phone, email, etc.

<!-- This section is still work in progress -->
-----

Rough notes not yet formalized:

- Database ID generation will follow the customized snowflake format based off the 2020 Epoch
- JWT for user authentication to use a serverwide master secret and the hashed password
- Nickname must only contain characters from the QWERTY English keyboard to make it easier to enforce prohibited content
- Users table must have column: id, username, nickname, hashed_pass, password_reset_timestamp
- Contacts table must have column: id, user_id, name, email, phone... WAIT HOW DO I MAP MULTIPLE PHONE NUMBERS WITH LABEL? NEED MORE BRAINSTORMING
- Browser client app should use: Vue, TypeScript, Vite, Vitest, Rolldown, Oxlint, Tanstack Query, Zod validation

Banned words initial brainstorming with character mapping:

|   QWERTY Character    | Common Leetspeak Look-Alike |      Mapped To      |
|:---------------------:|:---------------------------:|:-------------------:|
| **Uppercase** (`A-Z`) |             N/A             | `Lowercase` (`a-z`) |
|          `1`          |         `i` or `l`          |     `i` or `l`      |
|          `!`          |             `i`             |         `i`         |
|          `0`          |             `o`             |         `o`         |
|          `3`          |             `e`             |         `e`         |
|          `7`          |             `t`             |         `t`         |
|          `@`          |             `a`             |         `a`         |
|          `4`          |             `a`             |         `a`         |
|          `$`          |             `s`             |         `s`         |
|          `5`          |             `s`             |         `s`         |
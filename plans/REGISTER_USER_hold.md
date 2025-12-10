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
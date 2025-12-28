-how will it be

we need to model the entire flow and the process

first set forth the schema, then the implementation

things we need: endpoint, payload format, and response formats

maybe the stuff above can be retained within plan, and only implementation stuff here

------

Behind the scenes, the server must undertake the following:

- Validate `username` for uniqueness by querying the database (the client app is expected to do the same)
- Scan `username` and `nickname` for any illegal, offensive, or hateful content
- Validate `password` using RegEx for the rules listed above (the client app is expected to do the same)

Let us look at these three steps one by one:

# Validation

WIP

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

CREATE TYPE authorization_level AS enum (
    'NONE',
    'USER',
    'ADMIN'
);

CREATE TABLE users (
     id bigint NOT NULL PRIMARY KEY,
     recent_password_update_timestamp bigint NOT NULL, --should type be timestamptz instead?
     username varchar(63) NOT NULL UNIQUE,
     nickname varchar(63) NOT NULL,
     hashed_password varchar(127) NOT NULL
);
```

SHOULD 
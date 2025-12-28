Behind the scenes, the server must first check if the username exists on the database. After that, it will perform Argon2 verification of the submitted password with what was stored on the database. If it matches, the server shall begin token generation. It must be done on the fly in order to ensure it remains stateless. This is how it shall be done:

- The header of the JWT must be a JSON with the following keys:

| Key   | Type     | Value     |
|-------|----------|-----------|
| `alg` | `string` | `"HS256"` |
| `typ` | `string` | `"JWT"`   |

- The payload of the JWT must be a JSON with the following keys:

| Key   | Type     | Description                                                                       |
|-------|----------|-----------------------------------------------------------------------------------|
| `aud` | `string` | RFC 7519: The domain URL of the contacts app                                      |
| `iat` | `number` | RFC 7519: Timestamp in seconds of issue time                                      |
| `iss` | `string` | RFC 7519: The domain URL of the contacts app                                      |
| `pst` | `string` | RFC 7519: Timestamp in milliseconds from Unix Epoch of most recent password reset |
| `sub` | `string` | RFC 7519: The account ID in string format                                         |

- The server must maintain a global secret key of atleast 256 bits. To maintain statelessness, this cannot be stored on the database, and must be passed in via environmental variables. This secret key must never be leaked. If the worst comes to pass, where both the database is breached and the secret key is leaked; the attackers will be able to obtain full control of every single account. As a countermeasure, we may create a backup global secret key that is airgapped at all times, but within reach to quickly replace and invalidate all tokens if the worst comes to pass.

- This global secret key will be used to sign the JWT

- This signature will be used to sign the JWT using the HmacSha256 algorithm before returning the token in the response.
SELECT
    id,
    authorization_level,
    recent_password_update_timestamp,
    username, nickname,
    hashed_password
FROM users
WHERE username = :username
INSERT INTO users (
    id,
    authorization_level,
    recent_password_update_timestamp,
    username,
    nickname,
    hashed_password
)
VALUES (
    :id,
    :authorization_level::authorization_level,
    :recent_password_update_timestamp,
    :username,
    :nickname,
    :hashed_password
)
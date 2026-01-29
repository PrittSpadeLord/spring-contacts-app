SELECT
    id,
    user_id,
    name_prefix,
    first_name,
    last_name,
    home_phone_number_country_code,
    home_phone_number,
    mobile_phone_number_country_code,
    mobile_phone_number,
    personal_email_address,
    work_email_address,
    address_line1,
    address_line2,
    city,
    province,
    country,
    postal_code
FROM contacts
WHERE user_id = :user_id
ORDER BY first_name ASC
LIMIT :limit OFFSET :offset
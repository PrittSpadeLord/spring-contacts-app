These notes will be rather unorganized as I have a good mental model of what the project should be, and no longer need them to guide my thoughts.


the SQL table for storing contacts may be like:

```sql
CREATE TYPE name_prefix AS enum (
    'DR',
    'MR',
    'MRS',
    'MS',
    'SIR'
);

CREATE TABLE contacts (
    id bigint NOT NULL PRIMARY KEY,
    user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name_prefix name_prefix,
    first_name varchar(63),
    last_name varchar(63),
    home_phone_number_country_code varchar(7),
    home_phone_number varchar(15),
    mobile_phone_number_country_code varchar(7),
    mobile_phone_number varchar(15),
    personal_email_address varchar(63),
    work_email_address varchar(63),
    address_line1 varchar(255),
    address_line2 varchar(255),
    city varchar(63),
    province varchar(63),
    country varchar(63),
    postal_code varchar(7)
);

CREATE INDEX idx_contacts_user_id ON contacts USING btree (user_id);
CREATE INDEX idx_contacts_first_name ON contacts USING btree (first_name);
```
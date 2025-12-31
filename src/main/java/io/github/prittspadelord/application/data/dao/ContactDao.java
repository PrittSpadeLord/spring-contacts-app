package io.github.prittspadelord.application.data.dao;

import io.github.prittspadelord.application.data.models.Contact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ContactDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void createContact(Contact contact) {
        String sql = """
            INSERT INTO contacts (
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
             ) VALUES (
                 :id,
                 :user_id,
                 :name_prefix,
                 :first_name,
                 :last_name,
                 :home_phone_number_country_code,
                 :home_phone_number,
                 :mobile_phone_number_country_code,
                 :mobile_phone_number,
                 :personal_email_address,
                 :work_email_address,
                 :address_line1,
                 :address_line2,
                 :city,
                 :province,
                 :country,
                 :postal_code
             )
            """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", contact.getId())
                .addValue("user_id", contact.getUserId())
                .addValue("name_prefix", contact.getNamePrefix())
                .addValue("first_name", contact.getFirstName())
                .addValue("last_name", contact.getLastName())
                .addValue("home_phone_number_country_code", null)
                .addValue("home_phone_number", null)
                .addValue("mobile_phone_number_country_code", null)
                .addValue("mobile_phone_number", null)
                .addValue("personal_email_address", null)
                .addValue("work_email_address", null)
                .addValue("address_line1", null)
                .addValue("address_line2", null)
                .addValue("city", null)
                .addValue("province", null)
                .addValue("country", null)
                .addValue("postal_codeid", null)
                .addValue("user_id", null)
                .addValue("name_prefix", null)
                .addValue("first_name", null)
                .addValue("last_name", null)
                .addValue("home_phone_number_country_code", null)
                .addValue("home_phone_number", null)
                .addValue("mobile_phone_number_country_code", null)
                .addValue("mobile_phone_number", null)
                .addValue("personal_email_address", null)
                .addValue("work_email_address", null)
                .addValue("address_line1", null)
                .addValue("address_line2", null)
                .addValue("city", null)
                .addValue("province", null)
                .addValue("country", null)
                .addValue("postal_code", null);

    }
}

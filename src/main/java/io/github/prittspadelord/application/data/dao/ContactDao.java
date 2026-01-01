package io.github.prittspadelord.application.data.dao;

import io.github.prittspadelord.application.data.models.Contact;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ContactDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void addContact(Contact contact) {
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
                :name_prefix::name_prefix,
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
            .addValue("name_prefix", contact.getNamePrefix() != null ? contact.getNamePrefix().name() : null)
            .addValue("first_name", contact.getFirstName())
            .addValue("last_name", contact.getLastName())
            .addValue("home_phone_number_country_code", contact.getHomePhoneNumberCountryCode())
            .addValue("home_phone_number", contact.getHomePhoneNumber())
            .addValue("mobile_phone_number_country_code", contact.getMobilePhoneNumberCountryCode())
            .addValue("mobile_phone_number", contact.getMobilePhoneNumber())
            .addValue("personal_email_address", contact.getPersonalEmailAddress())
            .addValue("work_email_address", contact.getWorkEmailAddress())
            .addValue("address_line1", contact.getAddressLine1())
            .addValue("address_line2", contact.getAddressLine2())
            .addValue("city", contact.getCity())
            .addValue("province", contact.getProvince())
            .addValue("country", contact.getCountry())
            .addValue("postal_code", contact.getPostalCode());

        int rowsAffected = this.namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Added contact with id {} for user with id {}, with {} rows affected", contact.getId(), contact.getUserId(), rowsAffected);
    }

    public void deleteContact(long id, long userId) {
        String sql = "DELETE FROM contacts WHERE id = :id AND user_id = :user_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("user_id", userId);

        int rowsAffected = this.namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Deleted contact with id {} for user with id {}, with {} rows affected", id, userId, rowsAffected);
    }

    public Contact getContact(long id) {
        String sql = """
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
            WHERE id = :id
            """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        RowMapper<Contact> rowMapper = new BeanPropertyRowMapper<>(Contact.class);

        return this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    public List<Contact> listContacts(long userId) {
        String sql = """
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
            """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("user_id", userId);

        RowMapper<Contact> rowMapper = new BeanPropertyRowMapper<>(Contact.class);

        return this.namedParameterJdbcTemplate.queryForStream(sql, parameterSource, rowMapper).toList();
    }

    public void updateContact(long id, Contact newContact) {
        String sql = """
            UPDATE contacts
            SET
                user_id = :user_id,
                name_prefix = :name_prefix::name_prefix,
                first_name = :first_name,
                last_name = :last_name,
                home_phone_number_country_code = :home_phone_number_country_code,
                home_phone_number = :home_phone_number,
                mobile_phone_number_country_code = :mobile_phone_number_country_code,
                mobile_phone_number = :mobile_phone_number,
                personal_email_address = :personal_email_address,
                work_email_address = :work_email_address,
                address_line1 = :address_line1,
                address_line2 = :address_line2,
                city = :city,
                province = :province,
                country = :country,
                postal_code = :postal_code
            WHERE id = :id
            """;

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", newContact.getId())
            .addValue("user_id", newContact.getUserId())
            .addValue("name_prefix", newContact.getNamePrefix() != null ? newContact.getNamePrefix().name() : null)
            .addValue("first_name", newContact.getFirstName())
            .addValue("last_name", newContact.getLastName())
            .addValue("home_phone_number_country_code", newContact.getHomePhoneNumberCountryCode())
            .addValue("home_phone_number", newContact.getHomePhoneNumber())
            .addValue("mobile_phone_number_country_code", newContact.getMobilePhoneNumberCountryCode())
            .addValue("mobile_phone_number", newContact.getMobilePhoneNumber())
            .addValue("personal_email_address", newContact.getPersonalEmailAddress())
            .addValue("work_email_address", newContact.getWorkEmailAddress())
            .addValue("address_line1", newContact.getAddressLine1())
            .addValue("address_line2", newContact.getAddressLine2())
            .addValue("city", newContact.getCity())
            .addValue("province", newContact.getProvince())
            .addValue("country", newContact.getCountry())
            .addValue("postal_code", newContact.getPostalCode());

        int rowsAffected = this.namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Updated contact with id {} for user with id {}, with {} rows affected", newContact.getId(), newContact.getUserId(), rowsAffected);
    }
}

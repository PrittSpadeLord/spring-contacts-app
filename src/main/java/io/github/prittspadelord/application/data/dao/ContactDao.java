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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ContactDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void addContact(Contact contact) {
        String sql = this.sqlFromFile("insert_into_contacts.sql");

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
        String sql = this.sqlFromFile("delete_where_id_and_userid.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("user_id", userId);

        int rowsAffected = this.namedParameterJdbcTemplate.update(sql, parameterSource);

        log.info("Deleted contact with id {} for user with id {}, with {} rows affected", id, userId, rowsAffected);
    }

    public Contact getContact(long id) {
        String sql = this.sqlFromFile("select_contact_where_id.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        RowMapper<Contact> rowMapper = new BeanPropertyRowMapper<>(Contact.class);

        return this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    public long getUserId(long id) {
        String sql = this.sqlFromFile("select_userid_where_id.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return Objects.requireNonNull(this.namedParameterJdbcTemplate.queryForObject(sql, parameterSource, Long.class));
    }

    public List<Contact> listContacts(long userId) {
        String sql = this.sqlFromFile("select_contacts_where_userid.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("user_id", userId);

        RowMapper<Contact> rowMapper = new BeanPropertyRowMapper<>(Contact.class);

        return this.namedParameterJdbcTemplate.queryForStream(sql, parameterSource, rowMapper).toList();
    }

    public void updateContact(long id, Contact newContact) {
        String sql = this.sqlFromFile("update_contact_where_id.sql");

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("id", id)
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

    private String sqlFromFile(String fileUrlString) {
        InputStream sqlStream = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/contacts/" + fileUrlString));

        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));

        StringBuilder sb = new StringBuilder();

        int character;

        try {
            while((character = reader.read()) != -1) {
                sb.append((char) character);
            }

            return sb.toString();
        }
        catch(IOException e) {
            log.error("Critical failure (edit this later)");
            throw new RuntimeException(e);
        }
    }
}

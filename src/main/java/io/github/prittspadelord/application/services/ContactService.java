package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.ContactDao;
import io.github.prittspadelord.application.data.models.Contact;
import io.github.prittspadelord.application.rest.models.CreateContactRequest;
import io.github.prittspadelord.application.rest.models.CreateContactResponse;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContactService {

    private final JwtDecoder jwtDecoder;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final ContactDao contactDao;

    public CreateContactResponse createContact(CreateContactRequest createContactRequest, HttpServletRequest request) {

        long userId = Long.parseLong(jwtDecoder.decode(request.getHeader("Authorization")).getSubject());

        Instant now = Instant.now();
        long snowflakeId = this.snowflakeIdGenerator.generateSnowflakeId(now);

        Contact contact = new Contact();
        contact.setId(snowflakeId);
        contact.setUserId(userId);
        contact.setNamePrefix(createContactRequest.getNamePrefix());
        contact.setFirstName(createContactRequest.getFirstName());
        contact.setLastName(createContactRequest.getLastName());
        contact.setHomePhoneNumberCountryCode(createContactRequest.getHomePhoneNumberCountryCode());
        contact.setHomePhoneNumber(createContactRequest.getHomePhoneNumber());
        contact.setMobilePhoneNumberCountryCode(createContactRequest.getMobilePhoneNumberCountryCode());
        contact.setMobilePhoneNumber(createContactRequest.getMobilePhoneNumber());
        contact.setPersonalEmailAddress(createContactRequest.getPersonalEmailAddress());
        contact.setWorkEmailAddress(createContactRequest.getWorkEmailAddress());
        contact.setAddressLine1(createContactRequest.getAddressLine1());
        contact.setAddressLine2(createContactRequest.getAddressLine2());
        contact.setCity(createContactRequest.getCity());
        contact.setProvince(createContactRequest.getProvince());
        contact.setCountry(createContactRequest.getCountry());
        contact.setPostalCode(createContactRequest.getPostalCode());

        contactDao.addContact(contact);

        CreateContactResponse createContactResponse = new CreateContactResponse();
        createContactResponse.setId(String.valueOf(snowflakeId));
        createContactResponse.setTimestamp(now);

        return createContactResponse;
    }
}
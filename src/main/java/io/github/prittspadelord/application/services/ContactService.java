package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.ContactDao;
import io.github.prittspadelord.application.data.models.Contact;
import io.github.prittspadelord.application.rest.models.CreateContactRequest;
import io.github.prittspadelord.application.rest.models.CreateContactResponse;

import io.github.prittspadelord.application.rest.models.DeleteContactResponse;
import io.github.prittspadelord.application.rest.models.GetContactResponse;
import io.github.prittspadelord.application.rest.models.ListContactsResponse;
import io.github.prittspadelord.application.rest.UnauthorizedResourceAccessException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContactService {

    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final ContactDao contactDao;

    public CreateContactResponse createContact(CreateContactRequest createContactRequest, long userId) {

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

        this.contactDao.addContact(contact);

        CreateContactResponse createContactResponse = new CreateContactResponse();
        createContactResponse.setId(String.valueOf(snowflakeId));
        createContactResponse.setTimestamp(now);

        return createContactResponse;
    }

    public DeleteContactResponse deleteContact(long contactId) {
        //wip

        return null;
    }

    public GetContactResponse getContact(long contactId) {
        Contact contact = this.contactDao.getContact(contactId);

        GetContactResponse getContactResponse = new GetContactResponse();
        getContactResponse.setTimestamp(Instant.now());
        getContactResponse.setContact(contact);

        return getContactResponse;
    }

    public long getUserId(long contactId) {
        return this.contactDao.getUserId(contactId);
    }

    public ListContactsResponse listContacts(long userId) {
        List<Contact> contacts = this.contactDao.listContacts(userId);

        ListContactsResponse listContactsResponse = new ListContactsResponse();
        listContactsResponse.setTimestamp(Instant.now());
        listContactsResponse.setContacts(contacts);

        return listContactsResponse;
    }
}
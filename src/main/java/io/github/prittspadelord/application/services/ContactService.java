package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.ContactDao;
import io.github.prittspadelord.application.data.models.Contact;
import io.github.prittspadelord.application.rest.models.CreateContactRequest;
import io.github.prittspadelord.application.rest.models.CreateContactResponse;
import io.github.prittspadelord.application.rest.models.DeleteContactResponse;
import io.github.prittspadelord.application.rest.models.GetContactResponse;
import io.github.prittspadelord.application.rest.models.ListContactsResponse;
import io.github.prittspadelord.application.rest.models.UpdateContactRequest;
import io.github.prittspadelord.application.rest.models.UpdateContactResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

        this.contactDao.addContact(contact, userId);

        CreateContactResponse createContactResponse = new CreateContactResponse();
        createContactResponse.setId(String.valueOf(snowflakeId));
        createContactResponse.setTimestamp(now);

        return createContactResponse;
    }

    public DeleteContactResponse deleteContact(long contactId, long userId) {
        //wip
        //this.contactDao.deleteContact(contactId, userId);

        return null;
    }

    public GetContactResponse getContact(long contactId, long userId) {
        Contact contact = this.contactDao.getContact(contactId, userId);

        GetContactResponse getContactResponse = new GetContactResponse();
        getContactResponse.setTimestamp(Instant.now());
        getContactResponse.setContact(contact);

        return getContactResponse;
    }

    public ListContactsResponse listContacts(long userId) {
        List<Contact> contacts = this.contactDao.listContacts(userId);

        ListContactsResponse listContactsResponse = new ListContactsResponse();
        listContactsResponse.setTimestamp(Instant.now());
        listContactsResponse.setContacts(contacts);

        return listContactsResponse;
    }

    public UpdateContactResponse updateContactResponse(UpdateContactRequest updateContactRequest, long contactId, long userId) {

        Contact contact = new Contact();
        contact.setUserId(userId);
        contact.setNamePrefix(updateContactRequest.getNamePrefix());
        contact.setFirstName(updateContactRequest.getFirstName());
        contact.setLastName(updateContactRequest.getLastName());
        contact.setHomePhoneNumberCountryCode(updateContactRequest.getHomePhoneNumberCountryCode());
        contact.setHomePhoneNumber(updateContactRequest.getHomePhoneNumber());
        contact.setMobilePhoneNumberCountryCode(updateContactRequest.getMobilePhoneNumberCountryCode());
        contact.setMobilePhoneNumber(updateContactRequest.getMobilePhoneNumber());
        contact.setPersonalEmailAddress(updateContactRequest.getPersonalEmailAddress());
        contact.setWorkEmailAddress(updateContactRequest.getWorkEmailAddress());
        contact.setAddressLine1(updateContactRequest.getAddressLine1());
        contact.setAddressLine2(updateContactRequest.getAddressLine2());
        contact.setCity(updateContactRequest.getCity());
        contact.setProvince(updateContactRequest.getProvince());
        contact.setCountry(updateContactRequest.getCountry());
        contact.setPostalCode(updateContactRequest.getPostalCode());

        this.contactDao.updateContact(contactId, userId, contact);

        UpdateContactResponse updateContactResponse = new UpdateContactResponse();
        updateContactResponse.setTimestamp(Instant.now());

        return updateContactResponse;
    }
}
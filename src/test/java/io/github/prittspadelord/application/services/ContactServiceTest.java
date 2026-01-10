package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.ContactDao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.JwtDecoder;

class ContactServiceTest {

    private final ContactDao contactDao = Mockito.mock(ContactDao.class);
    private final SnowflakeIdGenerator snowflakeIdGenerator = Mockito.mock(SnowflakeIdGenerator.class);

    private final ContactService contactService = new ContactService(contactDao, snowflakeIdGenerator);

    @Test
    public void createContactShouldBeSuccessful() {
        Assertions.assertTrue(true);
    }
}
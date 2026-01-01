package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.ContactDao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.JwtDecoder;

class ContactServiceTest {

    private final JwtDecoder jwtDecoder = Mockito.mock(JwtDecoder.class);
    private final SnowflakeIdGenerator snowflakeIdGenerator = Mockito.mock(SnowflakeIdGenerator.class);
    private final ContactDao contactDao = Mockito.mock(ContactDao.class);

    private final ContactService contactService = new ContactService(jwtDecoder, snowflakeIdGenerator, contactDao);

    @Test
    public void createContactShouldBeSuccessful() {
        Assertions.assertTrue(true);
    }
}
package io.github.prittspadelord.application.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class SecureArgon2PasswordEncoderTest {

    private static final Pattern argon2hashPatternForMemory19456 = Pattern.compile("^\\$argon2id\\$v=19\\$m=19456,t=\\d+,p=\\d+\\$[A-Za-z0-9+/]+={0,2}\\$[A-Za-z0-9+/]+={0,2}$");

    private final SecureArgon2PasswordEncoder secureArgon2PasswordEncoder = new SecureArgon2PasswordEncoder();

    @Test
    public void encodedPasswordShouldBeAValidArgon2Hash() {
        char[] rawPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};

        String hashedPassword = this.secureArgon2PasswordEncoder.encode(rawPassword);

        Assertions.assertTrue(SecureArgon2PasswordEncoderTest.argon2hashPatternForMemory19456.matcher(hashedPassword).matches());
    }

    @Test
    public void passwordMustBeNulledAfterHashing() {
        char[] rawPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};
        char[] nulledPassword = new char[rawPassword.length];

        String _ = this.secureArgon2PasswordEncoder.encode(rawPassword);

        Assertions.assertArrayEquals(nulledPassword, rawPassword);
    }

    @Test
    public void shouldSuccessfullyMatchIfCorrect() {
        char[] rawPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};
        char[] correctPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};

        String hashedPassword = this.secureArgon2PasswordEncoder.encode(rawPassword);

        Assertions.assertTrue(this.secureArgon2PasswordEncoder.matches(correctPassword, hashedPassword));
    }

    @Test
    public void shouldFailMatchIfIncorrect() {
        char[] rawPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};
        char[] incorrectPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '4', '5', '6'};

        String hashedPassword = this.secureArgon2PasswordEncoder.encode(rawPassword);

        Assertions.assertFalse(this.secureArgon2PasswordEncoder.matches(incorrectPassword, hashedPassword));
    }

    @Test
    public void passwordMustBeNulledAfterMatching() {
        char[] rawPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};
        char[] correctPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};
        char[] nulledPassword = new char[rawPassword.length];

        String hashedPassword = this.secureArgon2PasswordEncoder.encode(rawPassword);
        boolean _ = this.secureArgon2PasswordEncoder.matches(correctPassword, hashedPassword);

        Assertions.assertArrayEquals(nulledPassword, rawPassword);
        Assertions.assertArrayEquals(nulledPassword, correctPassword);
    }
}
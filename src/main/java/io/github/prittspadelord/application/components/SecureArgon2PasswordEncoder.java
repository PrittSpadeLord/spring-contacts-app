package io.github.prittspadelord.application.components;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.Arrays;

import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SecureArgon2PasswordEncoder {

    private final Pattern argon2idHashedPasswordPattern = Pattern.compile("^\\$argon2(?:i|d|id)\\$v=\\d+\\$m=(\\d+),t=(\\d+),p=(\\d+)\\$([^$]+)\\$([^$]+)$");
    private final BytesKeyGenerator saltGenerator = KeyGenerators.secureRandom(16);

    public String encode(char[] rawPassword) {

        byte[] hash = new byte[32];

        try {
            Argon2Parameters params = new Argon2Parameters
                .Builder(Argon2Parameters.ARGON2_id)
                .withSalt(this.saltGenerator.generateKey())
                .withParallelism(1)
                .withMemoryAsKB(19456)
                .withIterations(2)
                .build();

            Argon2BytesGenerator generator = new Argon2BytesGenerator();
            generator.init(params);
            generator.generateBytes(rawPassword, hash);

            return this.encode(hash, params);
        }
        finally {
            Arrays.fill(hash, (byte) 0);
            if(rawPassword != null) Arrays.fill(rawPassword, '\0');
        }
    }

    public boolean matches(char[] rawPassword, String encodedPassword) {

        byte[] actualHash = null;
        byte[] expectedHash = null;
        byte[] salt = null;

        try {
            Matcher matcher = argon2idHashedPasswordPattern.matcher(encodedPassword);

            if (!matcher.find()) {
                return false;
            }

            int memory = Integer.parseInt(matcher.group(1));
            int iterations = Integer.parseInt(matcher.group(2));
            int parallelism = Integer.parseInt(matcher.group(3));
            salt = Base64.getDecoder().decode(matcher.group(4));
            expectedHash = Base64.getDecoder().decode(matcher.group(5));

            Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(parallelism)
                .withMemoryAsKB(memory)
                .withIterations(iterations)
                .build();

            Argon2BytesGenerator generator = new Argon2BytesGenerator();
            generator.init(params);

            actualHash = new byte[expectedHash.length];
            generator.generateBytes(rawPassword, actualHash);

            return MessageDigest.isEqual(actualHash, expectedHash);
        }
        finally {
            if(actualHash != null) Arrays.fill(actualHash, (byte) 0);

            if(expectedHash != null)  Arrays.fill(expectedHash, (byte) 0);

            if(rawPassword != null) Arrays.fill(rawPassword, '\0');

            if(salt != null) Arrays.fill(salt, (byte) 0);
        }
    }

    private String encode(byte[] hash, Argon2Parameters parameters) throws IllegalArgumentException {

        Base64.Encoder b64encoder = Base64.getEncoder().withoutPadding();
        StringBuilder stringBuilder = new StringBuilder();

        String type = switch (parameters.getType()) {
            case Argon2Parameters.ARGON2_d -> "$argon2d";
            case Argon2Parameters.ARGON2_i -> "$argon2i";
            case Argon2Parameters.ARGON2_id -> "$argon2id";
            default -> throw new IllegalArgumentException("Invalid algorithm type: " + parameters.getType());
        };

        stringBuilder.append(type);
        stringBuilder.append("$v=")
            .append(parameters.getVersion())
            .append("$m=")
            .append(parameters.getMemory())
            .append(",t=")
            .append(parameters.getIterations())
            .append(",p=")
            .append(parameters.getLanes());

        if (parameters.getSalt() != null) {
            stringBuilder.append("$").append(b64encoder.encodeToString(parameters.getSalt()));
        }

        stringBuilder.append("$").append(b64encoder.encodeToString(hash));

        return stringBuilder.toString();
    }
}
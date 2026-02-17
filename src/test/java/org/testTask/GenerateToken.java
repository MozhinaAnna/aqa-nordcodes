package org.testTask;

import java.security.SecureRandom;
import java.util.Random;

public class GenerateToken {

    private static final String HEX_CHARACTERS = "0123456789ABCDEF";
    private static final String CHARACTERS = "GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int TOKEN_LENGTH = 32;
    private static final Random random = new SecureRandom(); // может быть переменную надо в методы?

    public static String generateValidToken() {
        StringBuilder tokenBuilder = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = random.nextInt(HEX_CHARACTERS.length());
            char randomChar = HEX_CHARACTERS.charAt(randomIndex);
            tokenBuilder.append(randomChar);
        }
        return tokenBuilder.toString();
    }

    public static String generateInvalidToken() {
        StringBuilder tokenBuilder = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            tokenBuilder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return tokenBuilder.toString();
    }

    public static String generateShortToken() {
        StringBuilder tokenBuilder = new StringBuilder(TOKEN_LENGTH - 1);
        for (int i = 0; i < TOKEN_LENGTH - 1; i++) {
            int randomIndex = random.nextInt(HEX_CHARACTERS.length());
            char randomChar = HEX_CHARACTERS.charAt(randomIndex);
            tokenBuilder.append(randomChar);
        }
        return tokenBuilder.toString();
    }

    public static String generateLongToken() {
        StringBuilder tokenBuilder = new StringBuilder(TOKEN_LENGTH + 2);
        for (int i = 0; i < TOKEN_LENGTH + 2; i++) {
            int randomIndex = random.nextInt(HEX_CHARACTERS.length());
            char randomChar = HEX_CHARACTERS.charAt(randomIndex);
            tokenBuilder.append(randomChar);
        }
        return tokenBuilder.toString();
    }
}

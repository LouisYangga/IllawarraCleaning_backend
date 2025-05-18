package com.example.user_service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHashUtil {
    private static final SecureRandom SALT_GENERATOR = new SecureRandom();

    public static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SALT_GENERATOR.nextBytes(salt);
        return salt;
    }

    public static boolean verifyPassword(String password, String hashedPassword, byte[] salt) {
        String newHashedPassword = hashPassword(password, salt);
        return hashedPassword.equals(newHashedPassword);
    }
}

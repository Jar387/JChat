package com.jar36.jchat;

import com.jar36.jchat.server.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path checkFileExist(String path) throws IOException {
        Path path1 = Paths.get(path);
        if (!Files.exists(path1)) {
            Files.createFile(path1);
            return null;
        }
        return path1;
    }

    public static User verifySessionToken(long sessionToken) {
        for (User u : User.users) {
            if (u.getSessionToken() == sessionToken) {
                return u;
            }
        }
        return null;
    }

    public static User verifyUsername(String username) {
        for (User u : User.users) {
            if (u.getUserData().getName().compareTo(username) == 0) {
                return u;
            }
        }
        return null;
    }
}

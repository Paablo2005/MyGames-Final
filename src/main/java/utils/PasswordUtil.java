package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import models.User;

public class PasswordUtil {
	
	private static User loggedUser;
	
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
	public static User getLoggedUser() {
		return loggedUser;
	}

	public static void setLoggedUser(User loggedUser) {
		PasswordUtil.loggedUser = loggedUser;
	}
    
}

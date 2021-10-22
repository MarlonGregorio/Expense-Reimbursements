package utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Encrypt {
	
	//Creates an encrypted sequence of characters based on a string
	//to be stored on in the database instead of actual passwords
	public static String getHash(String password) {	
		if(password == null) {
			return null;
		}
		
		try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes(), 0, password.length());
            return new BigInteger(1, digest.digest()).toString(16);
  
        } catch (Exception e) {
            return null;
        }
	}
}
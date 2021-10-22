package services;

import daos.UserDao;
import models.User;
import utils.Encrypt;


public class UserService {
	
	public static User getUser(String username, String password) {
		String hashedPassword = Encrypt.getHash(password);
		User currUser = UserDao.getUser(username, hashedPassword);
		return currUser;
	}
}

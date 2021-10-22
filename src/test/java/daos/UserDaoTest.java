package daos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import models.User;

class UserDaoTest {

	@Test
	void getUserTest() {
		//similar tests to UserService
		
		//Non existent user
		User u1 = UserDao.getUser("", "");
		
		//Correct user
		User u2 = UserDao.getUser("Bob22", "81dc9bdb52d04dc20036dbd8313ed055");
		
		//incorrect password
		User u3 = UserDao.getUser("BoB22", "adsfasfda");
		
		assertNull(u1);
		assertNotNull(u2);
		assertEquals("Bob22", u2.getUsername());
		assertNull(u3);
	}
}

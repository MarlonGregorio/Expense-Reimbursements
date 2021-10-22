package services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import models.User;

class UserServiceTest {

	@Test
	void getUserTest() {
		
		//Non existent user
		User u1 = UserService.getUser("", "");
		
		//Correct user
		User u2 = UserService.getUser("Bob22", "1234");
		
		//incorrect password
		User u3 = UserService.getUser("BoB22", "adsfasfda");
		
		assertNull(u1);
		assertNotNull(u2);
		assertEquals("Bob22", u2.getUsername());
		assertNull(u3);
	}

}

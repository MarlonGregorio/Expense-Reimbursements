package utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EncryptTest {

	@Test
	void passwordHashTest() {
		String p1 = "Hello World";
		String e1 = "b10a8db164e0754105b7a99be72e3fe5";
		
		String p2 = "40138ad!dlfd";
		String e2 = "15818d4928eca8c1e8c5a2ecd51c67ae";
		
		String p3 = "dl;wy2uvmn21eyxtygv138fas";
		String e3 = "256801107386ef85b5bb742af40ff132";
		
		assertEquals(e1, Encrypt.getHash(p1));
		assertEquals(e2, Encrypt.getHash(p2));
		assertEquals(e3, Encrypt.getHash(p3));
	}
}
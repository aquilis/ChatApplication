package com.sirma.itt.javacourse.chat.serverSide;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the testable methods of the Server class.
 */
public class ServerTest {

	/**
	 * Tests the nickname validation for forbidden characters.
	 */
	@Test
	public void testNicknameValidation() {
		assertTrue(Server.isNicknameValid("sampleNickname"));
		assertFalse(Server.isNicknameValid("sampleNickname" + Server.FORBIDDEN_CHARACTERS[0]));
	}
}

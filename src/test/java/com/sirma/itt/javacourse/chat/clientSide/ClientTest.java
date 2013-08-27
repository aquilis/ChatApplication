package com.sirma.itt.javacourse.chat.clientSide;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * Tests the public testable methods of the client class.
 */
public final class ClientTest {
	private Client client = new Client();
	
	/**
	 * A constructor throwing the IOException.
	 * 
	 * @throws IOException
	 *             may be thrown by the client
	 */
	public ClientTest() throws IOException {
	}

	/**
	 * Tests the nickname length validation.
	 */
	@Test
	public void testNicknameLengthValidation() {
		assertTrue(client.isNicknameLengthValid("sampleNickname"));
		assertFalse(client.isNicknameLengthValid("1"));
	}

	/**
	 * Tests the user message length validation.
	 */
	@Test
	public void testMessageLengthValidation() {
		assertTrue(client.isMessageLengthValid("sample message"));
		assertFalse(client.isMessageLengthValid(""));
	}
}

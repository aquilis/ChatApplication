package com.sirma.itt.javacourse.chat.clientSide;

import java.io.IOException;

import com.sirma.itt.javacourse.chat.FileLogger;


/**
 * Launches the client-side chat application.
 */
public final class ClientLauncher {
	/**
	 * Not designed to be instantiated.
	 */
	private ClientLauncher() {
	}

	/**
	 * Entry point.
	 * 
	 * @param args
	 *            are the commands arguments
	 * @throws IOException
	 *             thrown by the client's constructor if the INET address can't
	 *             be instantiated.
	 */
	public static void main(String[] args) throws IOException {
		FileLogger.log("this is a test log");
		ClientGUI gui = new ClientGUI();
		Client client = new Client();
		new ClientController(gui, client);
	}
}

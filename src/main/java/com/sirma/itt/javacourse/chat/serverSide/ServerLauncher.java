package com.sirma.itt.javacourse.chat.serverSide;


/**
 * Launches the server-side application.
 */
public final class ServerLauncher {
	/**
	 * Not designed to be instantiated.
	 */
	private ServerLauncher() {
	}
	/**
	 * entry point.
	 * 
	 * @param args
	 *            are the command arguments
	 */
	public static void main(String[] args) {
		ServerGUI gui = new ServerGUI();
		Server server = new Server();
		new ServerController(gui, server);
	}
}

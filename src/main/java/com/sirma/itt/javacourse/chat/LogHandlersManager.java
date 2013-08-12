package com.sirma.itt.javacourse.chat;

import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * Provides global file handlers for output of the log information that can be
 * used from all classes via the access methods. This way, all server/client
 * side classes can log to their respective single file predefined here.
 */
public final class LogHandlersManager {
	private static FileHandler serverHandler = null;
	private static FileHandler clientHandler = null;
	private static final String SERVER_LOG_FILENAME = "serverLogfile.txt";
	private static final String CLIENT_LOG_FILENAME = "clientLogfile.txt";

	/**
	 * Utility class.
	 */
	private LogHandlersManager() {
	}

	/**
	 * Gets the file handler for the server log file.
	 * 
	 * @return the file handler for the server log file
	 */
	public static synchronized FileHandler getServerHandler() {
		if (serverHandler == null) {
			try {
				serverHandler = new FileHandler(SERVER_LOG_FILENAME, true);
				serverHandler.setFormatter(new LogFormatter());
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
		}
		return serverHandler;
	}

	/**
	 * Gets the file handler for the client log file.
	 * 
	 * @return the file handler for the client log file
	 */
	public static synchronized FileHandler getClientHandler() {
		if (clientHandler == null) {
			try {
				clientHandler = new FileHandler(CLIENT_LOG_FILENAME, true);
				clientHandler.setFormatter(new LogFormatter());
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
			clientHandler.setFormatter(new LogFormatter());
		}
		return clientHandler;
	}
}
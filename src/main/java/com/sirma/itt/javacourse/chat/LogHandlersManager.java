package com.sirma.itt.javacourse.chat;

import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * Provides global file handlers for the loggers. This way, all server/client
 * side classes can log to their respective single file predefined here. Uses
 * singleton for the handler instances.
 */
public final class LogHandlersManager {
	private static FileHandler serverHandler = null;
	private static FileHandler clientHandler = null;
	private static final String SERVER_LOG_FILENAME = "serverLogs.txt";
	private static final String CLIENT_LOG_FILENAME = "clientLogs.txt";

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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return clientHandler;
	}
}

package com.sirma.itt.javacourse.chat;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class logging all messages into a file.
 */
public class FileLogger {
	private static final Logger LOGGER = Logger.getLogger("MyLog");
	private FileHandler fileTxt = null;

	/**
	 * Constructs the logger.
	 */
	public FileLogger() {
		try {
			fileTxt = new FileHandler("MyLogFile.txt");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(fileTxt);
		LOGGER.setLevel(Level.ALL);
	}

	/**
	 * Logs the given message into the logger.
	 * 
	 * @param msg
	 *            is the msg to log
	 */
	public static void log(String msg) {
		LOGGER.info(msg);
	}
}

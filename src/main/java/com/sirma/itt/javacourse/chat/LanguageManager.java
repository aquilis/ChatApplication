package com.sirma.itt.javacourse.chat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * A sole utility class that manages the GUI language for the application.
 */
public final class LanguageManager {

	private static String language = null;
	private static Locale locale = null;
	private static ResourceBundle words = null;
	private static final String FILENAME = "lang.properties";
	/**
	 * A utility class.
	 */
	private LanguageManager() {
	}

	/**
	 * Gets the string corresponding to the given key from the resource bundle.
	 * Reads the user-chosen language from the properties file.
	 * 
	 * @param key
	 *            is the string key to get
	 * @return the word from the resource bundle corresponding to the given key
	 */
	public static String getString(String key) {
		if (language == null) {
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(FILENAME));
				language = prop.getProperty("language");
			} catch (IOException e) {
				// if the properties file can't be found, set EN as default
				language = "en";
			}
			locale = new Locale(language);
			words = ResourceBundle.getBundle("UI", locale, new UTF8Control());
		}
		return words.getString(key);
	}
}

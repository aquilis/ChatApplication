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
	/**
	 * A utility class.
	 */
	private LanguageManager() {
	}

	// TODO both strings should not be hard-coded here, but chosen by the user
	// using a menu
	private static String language = null;
	private static Locale locale = null;
	private static ResourceBundle words = null;

	/**
	 * Reads the properties file again (in case the user has chosen a new
	 * language) and loads the corresponding resource bundle.
	 */
	public static void refreshLanguage() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		language = prop.getProperty(language);
		locale = new Locale(language);
		words = ResourceBundle.getBundle("UI", locale, new UTF8Control());
	}

	/**
	 * Gets the string corresponding to the given key from the resource bundle.
	 * Reads the language from the properties file.
	 * 
	 * @param key
	 *            is the string key to get
	 * @return the word from the resource bundle corresponding to the given key
	 */
	public static String getString(String key) {
		if (language == null) {
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream("config.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			language = prop.getProperty("language");
			locale = new Locale(language);
			words = ResourceBundle.getBundle("UI", locale, new UTF8Control());
		}
		return words.getString(key);
	}
}

package com.sirma.itt.javacourse.chat.serverSide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import com.sirma.itt.javacourse.chat.LanguageManager;
import com.sirma.itt.javacourse.chat.LogHandlersManager;

/**
 * The controller class that implements the MVC architecture. Mediates all
 * interactions between the front-end (ServerGUI) and the back-end (Server,
 * Client commands). The front and the back-end classes don't know about each
 * other. Both sides interact implicitly using this class.
 */
public class ServerController {
	// the view
	private ServerGUI gui = null;
	// the model
	private Server server = null;
	// logger
	private static final Logger LOGGER = Logger
			.getLogger(ServerController.class.getName());
	private final FileHandler fileHandler = LogHandlersManager
			.getServerHandler();
	// The properties files and variables needed to be saved for
	// further use
	private static final String LANGUAGE_FILE = "lang.properties";
	private static final String PROPERTIES_FILE = "serverConfig.properties";
	private int port;
	private String address;
	private String currentLanguage;
	private boolean hasLanguageChanged;

	/**
	 * Constructs the controller with a server GUI and a server class that
	 * contains the business logic.
	 * 
	 * @param gui
	 *            is the server GUI
	 * @param server
	 *            is the back-end server class
	 */
	public ServerController(ServerGUI gui, Server server) {
		this.gui = gui;
		this.server = server;
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(fileHandler);
		attachButtonsActionListeners();
		loadProperties();
	}

	/**
	 * Loads the properties from the properties files and updates the server
	 * GUI.
	 */
	private void loadProperties() {
		Properties prop = new Properties();
		Properties langProp = new Properties();
		try {
			prop.load(new FileInputStream(PROPERTIES_FILE));
			langProp.load(new FileInputStream(LANGUAGE_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gui.getPortTextBox().setText(prop.getProperty("port"));
		gui.getAddressTextBox().setText(prop.getProperty("address"));
		gui.getLanguagesBox().setSelectedItem(langProp.getProperty("language"));
		hasLanguageChanged = false;
	}

	/**
	 * Gets the chosen properties from the server GUI text fields and save them
	 * to the properties files.
	 */
	private void saveProperties() {
		Properties prop = new Properties();
		Properties langProp = new Properties();
		prop.setProperty("port", Integer.toString(port));
		prop.setProperty("address", address);
		langProp.setProperty("language", currentLanguage);
		// save properties to project root folder
		try {
			prop.store(new FileOutputStream(PROPERTIES_FILE), null);
			langProp.store(new FileOutputStream(LANGUAGE_FILE), null);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * Using the server class functionality, handles the newly connecting
	 * clients attaching them listener and sender threads.
	 */
	private void handleClients() {
		while (true) {
			try {
				ClientWrapper lastClient = server.waitForClient();
				server.attachClientSender(lastClient);
				server.attachClientListener(lastClient,
						server.getTransmitter(), this);
			} catch (IOException e) {
				break;
			}
		}
	}

	/**
	 * Attaches action listeners to the GUI buttons.
	 */
	private void attachButtonsActionListeners() {
		// when the stop server button is hit
		ActionListener stopButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOGGER.info("Connection stopped from server");
				log(LanguageManager.getString("connectionStoppedFromServer"));
				gui.deactivate();
				server.stopServer();
			}
		};
		// executed when the user clicks the Start server button
		ActionListener startServerButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					port = Integer.parseInt(gui.getPortTextBox().getText());
				} catch (NumberFormatException e1) {
					gui.showError(LanguageManager.getString("portFormatError"),
							LanguageManager.getString("portFormatErrorCaption"));
					return;
				}
				address = gui.getAddressTextBox().getText();
				saveProperties();
				if (hasLanguageChanged) {
					gui.showInfoMessage(LanguageManager
							.getString("languageChangesMessage"),
							LanguageManager
									.getString("languageChangesMessageCaption"));
				}
				try {
					if ("".equals(address)) {
						throw new IOException();
					}
					server.startServer(port, address);
				} catch (IOException e1) {
					LOGGER.log(Level.WARNING, e1.getMessage(), e1);
					gui.showError(
							LanguageManager.getString("serverStartError"),
							LanguageManager
									.getString("serverStartErrorCaption"));
					return;
				} catch (IllegalArgumentException e2) {
					LOGGER.log(Level.WARNING, e2.getMessage(), e2);
					gui.showError(LanguageManager
							.getString("portOutOfRangeError"), LanguageManager
							.getString("portOutOfRangeErrorCaption"));
					return;
				}
				gui.moveToMainForm();
				new Thread(new Runnable() {
					@Override
					public void run() {
						handleClients();
					}
				}).start();
			}
		};
		// executed when a new language from the combo box is chosen by the user
		ActionListener languagesComboBoxListener = new ActionListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String language = (String) cb.getSelectedItem();
				currentLanguage = language;
				hasLanguageChanged = true;
			}
		};
		gui.setStopButtonListener(stopButtonListener);
		gui.setLanguagesBoxListener(languagesComboBoxListener);
		gui.setStartButtonListener(startServerButtonListener);
	}

	/**
	 * Logs the given message in the server GUI panel.
	 * 
	 * @param msg
	 *            is the message to log
	 */
	public void log(String msg) {
		gui.log(msg);
	}
}

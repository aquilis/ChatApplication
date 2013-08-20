package com.sirma.itt.javacourse.chat.clientSide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import com.sirma.itt.javacourse.chat.LanguageManager;
import com.sirma.itt.javacourse.chat.LogHandlersManager;
import com.sirma.itt.javacourse.chat.clientSide.clientCommands.DisconnectCommand;
import com.sirma.itt.javacourse.chat.clientSide.clientCommands.MessageToServerCommand;

/**
 * The controller class that implements the MVC architecture. Mediates all
 * interactions between the front-end (ClientGUI) and the back-end (Client,
 * Server commands). The front and the back-end classes don't know about each
 * other. Both sides interact implicitly using this class.
 */
public class ClientController {
	// the view
	private ClientGUI gui = null;
	// the model
	private Client client = null;
	// all online users that will be shown in the GUI
	private ArrayList<String> onlineClients = new ArrayList<String>();
	// reference used inside the action listeners
	private final ClientController thisController = this;
	// logger
	private static final Logger LOGGER = Logger
			.getLogger(ClientController.class.getName());
	private final FileHandler fileHandler = LogHandlersManager
			.getClientHandler();
	// The properties files and variables needed to be saved for
	// further use
	private static final String LANGUAGE_FILE = "lang.properties";
	private static final String PROPERTIES_FILE = "clientConfig.properties";
	private int port;
	private String address;
	private String currentLanguage;
	private boolean hasLanguageChanged;

	/**
	 * Constructs the controller with a view and model classes.
	 * 
	 * @param gui
	 *            is the client GUI
	 * @param client
	 *            is the client class that contains the business logic
	 */
	public ClientController(final ClientGUI gui, final Client client) {
		this.gui = gui;
		this.client = client;
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(fileHandler);
		attachButtonsActionListeners();
		loadProperties();
	}

	/**
	 * Loads the properties from the properties files and updates the client GUI
	 * and language.
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
		gui.getNicknameTextBox().setText(prop.getProperty("nickname"));
		gui.getLanguagesBox().setSelectedItem(langProp.getProperty("language"));
		hasLanguageChanged = false;
	}

	/**
	 * Gets the chosen properties from the client GUI and save them to the
	 * properties files.
	 */
	private void saveProperties() {
		Properties prop = new Properties();
		Properties langProp = new Properties();
		prop.setProperty("port", Integer.toString(port));
		prop.setProperty("address", address);
		prop.setProperty("nickname", gui.getNicknameTextBox().getText().trim());
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
	 * Attaches action listeners to the GUI's buttons that will interact with
	 * the client class.
	 */
	private void attachButtonsActionListeners() {
		// executed when the user clicks Join
		ActionListener joinButonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.setNickname(gui.getNicknameTextBox().getText().trim());
				try {
					port = Integer.parseInt(gui.getPortTextBox().getText());
				} catch (NumberFormatException e1) {
					gui.showError(LanguageManager.getString("portFormatError"),
							LanguageManager.getString("portFormatErrorCaption"));
				}
				address = gui.getAddressTextBox().getText();
				saveProperties();
				if (hasLanguageChanged) {
					gui.showInfoMessage(LanguageManager
							.getString("languageChangesMessage"),
							LanguageManager
									.getString("languageChangesMessageCaption"));
				}
				if (client.isNicknameLengthValid(gui.getNicknameTextBox()
						.getText().trim())) {
					try {
						client.join(port, address, thisController);
					} catch (IOException e1) {
						LOGGER.warning("Can't find host on port " + port
								+ " | address: " + address);
						showError(LanguageManager.getString("noHostError"),
								LanguageManager.getString("noHostErrorCaption"));
					}
				} else {
					LOGGER.info("Client specified invalid length for the nickname. Join cancelled");
					showError(LanguageManager.getString("nicknameLengthError"),
							LanguageManager
									.getString("nicknameLengthErrorCaption"));
					gui.getNicknameTextBox().setText("");
				}
			}
		};
		// executed when the user clicks Send
		ActionListener sendButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (client.isMessageLengthValid(gui.getTextBox().getText())) {
					client.sendCommand(new MessageToServerCommand(client
							.formatMessage(gui.getTextBox().getText().trim())));
					gui.getTextBox().setText("");
				} else {
					showError(LanguageManager.getString("messageLengthError"),
							LanguageManager
									.getString("messageLengthErrorCaption"));
					gui.getNicknameTextBox().setText("");
				}
			}
		};
		// executed when the user clicks the disconnect button
		ActionListener disconnectButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOGGER.info("Client left the chat room");
				log(LanguageManager.getString("youLeftChat"));
				client.sendCommand(new DisconnectCommand());
				deactivate();
			}
		};
		// executed when a language from the combo box is chosen by the user
		ActionListener langugaesComboBoxListener = new ActionListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String language = (String) cb.getSelectedItem();
				currentLanguage = language;
				hasLanguageChanged = true;
			}
		};
		// attach all listeners to the buttons using the setter methods of the
		// GUI
		gui.setJoinButtonListener(joinButonListener);
		gui.setSendButtonListener(sendButtonListener);
		gui.setDisconnectButtonListener(disconnectButtonListener);
		gui.setLanguagesBoxListener(langugaesComboBoxListener);
	}

	/*
	 * Provide some external access for modifying the view class.
	 */
	/**
	 * Logs the given message into the log box of the GUI.
	 * 
	 * @param msg
	 *            is the message to log
	 */
	public void log(String msg) {
		gui.log(msg);
	}

	/**
	 * Deactivates all active GUI elements - the text box and the buttons.
	 */
	public void deactivate() {
		gui.deactivate();
	}

	/**
	 * Shows an error message on the current form with the given text and
	 * caption.
	 * 
	 * @param text
	 *            is the message of the error
	 * @param caption
	 *            is the title of the error message box.
	 */
	public void showError(String text, String caption) {
		gui.showError(text, caption);
	}

	/**
	 * Sets a new list of online users.
	 * 
	 * @param listUsers
	 *            is the new list of connected clients to set
	 */
	public void setListOnlineUsers(ArrayList<String> listUsers) {
		this.onlineClients = listUsers;
		gui.updateOnlineUsersPanel(onlineClients);
	}

	/**
	 * Adds the new nickname to the list of online users and updates the GUI.
	 * 
	 * @param nickname
	 *            is the nickname to add
	 */
	public void addOnlineUser(String nickname) {
		if (!onlineClients.contains(nickname)) {
			this.onlineClients.add(nickname);
			gui.updateOnlineUsersPanel(onlineClients);
		}
	}

	/**
	 * Removes the given nickname from the list of online users and updates the
	 * GUI.
	 * 
	 * @param nickname
	 *            is the nickname to remove
	 */
	public void removeOnlineUser(String nickname) {
		this.onlineClients.remove(nickname);
		gui.updateOnlineUsersPanel(onlineClients);
	}

	/**
	 * Hides the login form and draws the main form of the GUI. Sets the main
	 * form a new title including the client's nickname.
	 */
	public void moveToMainForm() {
		gui.moveToMainForm();
		gui.setMainFormTitle(LanguageManager.getString("clientCaption") + " "
				+ client.getNickname());
	}
}

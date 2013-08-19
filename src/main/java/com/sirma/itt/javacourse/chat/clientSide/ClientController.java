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
	// TODO make these fields not hard-coded, but chosen by the user from the
	// GUI module
	private final int port = 7000;
	private final String address = "localhost";
	private final String language = "bg";

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
		// load the configuration file and fill the text fields with the last
		// user properties
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gui.getPortTextBox().setText(prop.getProperty("port"));
		gui.getAddressTextBox().setText(prop.getProperty("address"));
		gui.getNicknameTextBox().setText(prop.getProperty("nickname"));
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
				// save to properties file
				Properties prop = new Properties();
				prop.setProperty("port", Integer.toString(port));
				prop.setProperty("host", address);
				prop.setProperty("nickname", gui.getNicknameTextBox().getText()
						.trim());
				// prop.setProperty("language", language);
				// save properties to project root folder
				try {
					prop.store(new FileOutputStream("config.properties"), null);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				//
				if (client.isNicknameLengthValid(gui.getNicknameTextBox()
						.getText().trim())) {
					try {
						client.join(port, address, thisController);
					} catch (IOException e1) {
						LOGGER.warning("Can't find host on the specified port and address");
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
				Properties prop = new Properties();
				prop.setProperty("language", language);
				try {
					prop.store(new FileOutputStream("config.properties"), null);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				LanguageManager.refreshLanguage();
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

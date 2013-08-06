package com.sirma.itt.javacourse.chat.clientSide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

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
		attachButtonsActionListeners();
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
				if (client.isNicknameLengthValid(gui.getNicknameTextBox()
						.getText().trim())) {
					try {
						client.join();
					} catch (IOException e1) {
						showError("Unable to find server", "Connection error");
					}
				} else {
					showError(
							"Your nickname must be at least 3 characters long and no more than 20",
							"nickname length error");
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
					showError(
							"You can't send zero-sized messages or messages longer than 200 characters",
							"message length error");
					gui.getNicknameTextBox().setText("");
				}
			}
		};
		// executed when the user clicks the disconnect button
		ActionListener disconnectButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log("You left the chat room");
				client.sendCommand(new DisconnectCommand());
				deactivate();
			}
		};
		// attach all listeners to the buttons using the setter methods of the
		// GUI
		gui.setJoinButtonListener(joinButonListener);
		gui.setSendButtonListener(sendButtonListener);
		gui.setDisconnectButtonListener(disconnectButtonListener);
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
		gui.setMainFormTitle("Chat application | Logged as "
				+ client.getNickname());
	}
}

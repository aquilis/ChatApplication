package com.sirma.itt.javacourse.chat.clientSide;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The user interface of the client application. Creates and draws all GUI
 * components and provides access methods for the controller class (MVC).
 */
public class ClientGUI {
	private int currentForm = 1;
	// login form components
	private JFrame loginForm = null;
	private JTextField nicknameTextBox = null;
	private JButton joinButton = null;
	private JLabel welcomeLabel = null;
	// main form components
	private JFrame mainForm = null;
	private JTextArea logBox = null;
	private JScrollPane logBoxPane = null;
	private JTextArea onlineUsersBox = null;
	private JScrollPane onlineUsersPane = null;
	private JTextField textBox = null;
	private JButton sendButton = null;
	private JButton disconnectButton = null;

	/**
	 * Constructs the interface drawing the login form first.
	 */
	public ClientGUI() {
		createLoginForm();
		createMainForm();
		loginForm.setVisible(true);
	}

	/**
	 * Instantiates the login form with all of its components.
	 */
	private void createLoginForm() {
		loginForm = new JFrame("Chat application | Login");
		loginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginForm.setLayout(new FlowLayout(FlowLayout.LEADING, 24, 24));
		loginForm.setSize(new Dimension(350, 300));
		loginForm.setResizable(false);
		createWelcomeLabel();
		createJoinButton();
		createNicknameTextBox();
		loginForm.getContentPane().add(welcomeLabel);
		loginForm.getContentPane().add(nicknameTextBox);
		loginForm.getContentPane().add(joinButton);
	}

	/**
	 * Instantiates the main form of the chat application with all of its
	 * components.
	 */
	private void createMainForm() {
		mainForm = new JFrame("Chat application");
		mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainForm.setLayout(new FlowLayout(FlowLayout.LEADING, 24, 24));
		mainForm.setSize(new Dimension(700, 480));
		mainForm.setResizable(false);
		createLogBoxPane();
		createOnlineUsersPane();
		createTextBox();
		createSendButton();
		createDisconnectButton();
		mainForm.getContentPane().add(logBoxPane);
		mainForm.getContentPane().add(onlineUsersPane);
		mainForm.getContentPane().add(textBox);
		mainForm.getContentPane().add(sendButton);
		mainForm.getContentPane().add(disconnectButton);
	}

	/**
	 * Hides the login form and draws the main form.
	 */
	public void moveToMainForm() {
		loginForm.setVisible(false);
		mainForm.setVisible(true);
		currentForm++;
	}

	/**
	 * Sets the title of the main form.
	 * 
	 * @param title
	 *            is the new title
	 */
	public void setMainFormTitle(String title) {
		mainForm.setTitle(title);
	}

	/**
	 * Creates the text box where the user will input his nickname.
	 */
	private void createNicknameTextBox() {
		nicknameTextBox = new JTextField(23);
		nicknameTextBox.setPreferredSize(new Dimension(100, 24));
	}

	/**
	 * Creates the log box and the scroll pane where all activity will be
	 * printed.
	 */
	private void createLogBoxPane() {
		logBox = new JTextArea(5, 30);
		logBox.setBorder(BorderFactory.createLoweredBevelBorder());
		logBox.setEditable(false);
		logBox.append("Client activity log:\n");
		logBoxPane = new JScrollPane(logBox);
		logBoxPane.setPreferredSize(new Dimension(400, 300));
	}

	/**
	 * Creates the scroll pane where all online users will be listed.
	 */
	private void createOnlineUsersPane() {
		onlineUsersBox = new JTextArea(5, 15);
		onlineUsersBox.setBorder(BorderFactory.createLoweredBevelBorder());
		onlineUsersBox.setEditable(false);
		onlineUsersBox.append("Online users:");
		onlineUsersPane = new JScrollPane(onlineUsersBox);
		onlineUsersPane.setPreferredSize(new Dimension(180, 300));
	}

	/**
	 * Creates the text box where the user will input the messages.
	 */
	private void createTextBox() {
		textBox = new JTextField();
		textBox.setPreferredSize(new Dimension(400, 32));
	}

	/**
	 * Sets an action listener for the send button that sends the user message
	 * from the text box.
	 * 
	 * @param listener
	 *            is the listener to set
	 */
	public void setSendButtonListener(ActionListener listener) {
		sendButton.addActionListener(listener);
	}

	/**
	 * Creates the send button which sends the message to the server and
	 * attaches it an action listener.
	 */
	private void createSendButton() {
		sendButton = new JButton("Send");
		sendButton.setPreferredSize(new Dimension(80, 32));
	}

	/**
	 * Sets an action listener for the disconnect button that the user uses to
	 * leave the chat room.
	 * 
	 * @param listener
	 *            is the listener to set
	 */
	public void setDisconnectButtonListener(ActionListener listener) {
		disconnectButton.addActionListener(listener);
	}

	/**
	 * Creates the disconnect button that ends the current session.
	 */
	private void createDisconnectButton() {
		disconnectButton = new JButton("Leave");
		disconnectButton.setPreferredSize(new Dimension(80, 32));
	}

	/**
	 * Creates the welcome label that welcomes the user when join.
	 */
	private void createWelcomeLabel() {
		welcomeLabel = new JLabel(
				"<html>Welcome to chat application.<br><br>Please, enter your nickname");
		welcomeLabel.setPreferredSize(new Dimension(350, 64));
	}

	/**
	 * Sets an action listener for the join button.
	 * 
	 * @param listener
	 *            is the action listener performing the button actions.
	 */
	public void setJoinButtonListener(ActionListener listener) {
		joinButton.addActionListener(listener);
	}

	/**
	 * Creates the join button used to request join to the server.
	 */
	private void createJoinButton() {
		joinButton = new JButton("Join");
		joinButton.setPreferredSize(new Dimension(80, 32));
	}

	/**
	 * Returns the text box where the user inputs the messages.
	 * 
	 * @return the text box where the user inputs the messages
	 */
	public JTextField getTextBox() {
		return textBox;
	}

	/**
	 * Gets the nickname text box.
	 * 
	 * @return the nickname text box
	 */
	public JTextField getNicknameTextBox() {
		return nicknameTextBox;
	}

	/**
	 * Logs the given message into the log box of the GUI.
	 * 
	 * @param message
	 *            is the message to log
	 */
	public void log(String message) {
		if (logBox != null) {
			logBox.append(message + "\n");
		}
	}

	/**
	 * Deactivates all active GUI elements - the text box and the buttons.
	 */
	public void deactivate() {
		sendButton.setEnabled(false);
		disconnectButton.setEnabled(false);
		textBox.setEnabled(false);
	}

	/**
	 * Draws all user names from the given list onto the GUI panel of online
	 * users.
	 * 
	 * @param onlineClients
	 *            is the list of client nicknames to draw onto the GUI panel
	 */
	public void updateOnlineUsersPanel(ArrayList<String> onlineClients) {
		onlineUsersBox.setText("Online users:");
		for (String nickname : onlineClients) {
			onlineUsersBox.append("\n" + nickname);
		}
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
		JFrame currentFrame = null;
		if (currentForm == 1) {
			currentFrame = loginForm;
		} else {
			currentFrame = mainForm;
		}
		JOptionPane.showMessageDialog(currentFrame, text, caption,
				JOptionPane.ERROR_MESSAGE);
	}
}
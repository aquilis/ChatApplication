package com.sirma.itt.javacourse.chat.serverSide;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sirma.itt.javacourse.chat.LanguageManager;

/**
 * The GUI part of the server application.
 */
public class ServerGUI {

	private int currentForm = 1;
	private boolean isActive = true;
	// login form components
	private JFrame loginForm = null;
	private JButton startButton = null;
	private JLabel portLabel = null;
	private JTextField portTextBox = null;
	private JLabel addressLabel = null;
	private JTextField addressTextBox = null;
	private JLabel languagesLabel = null;
	@SuppressWarnings("rawtypes")
	private JComboBox languagesBox = null;
	// main form components
	private JFrame mainForm = null;
	private JButton stopButton;
	private JScrollPane scrollPane = null;
	private JTextArea logBox = null;

	/**
	 * Constructs the interface.
	 */
	public ServerGUI() {
		createLoginForm();
		createMainForm();
		loginForm.setVisible(true);
	}

	/**
	 * Gets the text box where the user inputs the server port.
	 * 
	 * @return the text box where the suer inputs the server port
	 */
	public JTextField getPortTextBox() {
		return portTextBox;
	}

	/**
	 * Gets the text box where the user inputs the server address where to open
	 * the connection.
	 * 
	 * @return the text box where the user inputs the server address
	 */
	public JTextField getAddressTextBox() {
		return addressTextBox;
	}

	/**
	 * Gets the combo box where the user chooses the language.
	 * 
	 * @return The combo box where the user chooses the language
	 */
	@SuppressWarnings("rawtypes")
	public JComboBox getLanguagesBox() {
		return languagesBox;
	}
	
	/**
	 * Instantiates all the components of the login form where the user chooses
	 * the server address, port and language.
	 */
	private void createLoginForm() {
		loginForm = new JFrame(LanguageManager.getString("serverLoginCaption"));
		loginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginForm.setLayout(new FlowLayout(FlowLayout.LEADING, 24, 24));
		loginForm.setSize(new Dimension(320, 300));
		loginForm.setResizable(false);
		createStartButton();
		createPortInput();
		createAddressInput();
		createLanguagesBox();
		loginForm.getContentPane().add(portLabel);
		loginForm.getContentPane().add(portTextBox);
		loginForm.getContentPane().add(addressLabel);
		loginForm.getContentPane().add(addressTextBox);
		loginForm.getContentPane().add(languagesLabel);
		loginForm.getContentPane().add(languagesBox);
		loginForm.getContentPane().add(startButton);
	}

	/**
	 * Instantiates all components of the main form of the server application.
	 */
	private void createMainForm() {
		mainForm = new JFrame(LanguageManager.getString("serverCaption"));
		mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainForm.setLayout(new FlowLayout(FlowLayout.LEADING, 6, 6));
		mainForm.setSize(new Dimension(650, 350));
		mainForm.setResizable(false);
		createScrollPane();
		createStopButton();
		mainForm.getContentPane().add(scrollPane);
		mainForm.getContentPane().add(stopButton);
	}

	/**
	 * Hides the login form and shows the main form.
	 */
	public void moveToMainForm() {
		loginForm.setVisible(false);
		mainForm.setVisible(true);
		currentForm++;
	}

	/**
	 * Creates the label and the text box where he user will input the desired
	 * port to join.
	 */
	private void createPortInput() {
		portLabel = new JLabel(
				LanguageManager.getString("clientPortInputLabel"));
		portTextBox = new JTextField(7);
	}

	/**
	 * Creates the label and the text box where he user will input the desired
	 * INET address to join.
	 */
	private void createAddressInput() {
		addressLabel = new JLabel(
				LanguageManager.getString("clientAddressInputLabel"));
		addressTextBox = new JTextField(7);
	}

	/**
	 * Attaches an action listener to the combo box where the user chooses the
	 * GUI language.
	 * 
	 * @param listener
	 *            is the new listener to set
	 */
	public void setLanguagesBoxListener(ActionListener listener) {
		languagesBox.addActionListener(listener);
	}

	/**
	 * Creates the combo box used by the user to select a language and the label
	 * for the combo box.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createLanguagesBox() {
		languagesLabel = new JLabel(
				LanguageManager.getString("clientLanguagesLabel"));
		String[] languages = { "en", "bg" };
		languagesBox = new JComboBox(languages);
		languagesBox.setSelectedIndex(0);
	}

	/**
	 * Sets an action listener for the send button that sends the user message
	 * from the text box.
	 * 
	 * @param listener
	 *            is the listener to set
	 */
	public void setStartButtonListener(ActionListener listener) {
		startButton.addActionListener(listener);
	}

	/**
	 * Creates the send button which sends the message to the server and
	 * attaches it an action listener.
	 */
	private void createStartButton() {
		startButton = new JButton(
				LanguageManager.getString("serverStartButton"));
		startButton.setPreferredSize(new Dimension(120, 32));
	}

	/**
	 * Creates the text label that will show the server activity.
	 */
	private void createScrollPane() {
		logBox = new JTextArea(5, 30);
		logBox.setBorder(BorderFactory.createLoweredBevelBorder());
		logBox.setEditable(false);
		logBox.append(LanguageManager.getString("serverLogBoxCaption"));
		scrollPane = new JScrollPane(logBox);
		scrollPane.setPreferredSize(new Dimension(450, 300));
	}

	/**
	 * Sets an action listener for the server stop button.
	 * 
	 * @param listener
	 *            is the new listener to set
	 */
	public void setStopButtonListener(ActionListener listener) {
		this.stopButton.addActionListener(listener);
	}

	/**
	 * Creates the stop button that will stop the server.
	 */
	private void createStopButton() {
		stopButton = new JButton(LanguageManager.getString("stopServerButton"));
		stopButton.setPreferredSize(new Dimension(150, 50));
	}

	/**
	 * Logs the given message in the server GUI panel. Includes the current time
	 * in the log.
	 * 
	 * @param msg
	 *            is the message to log
	 */
	public void log(String msg) {
		if (!isActive) {
			return;
		}
		logBox.append("\n["
				+ new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] "
				+ msg);
	}

	/**
	 * Deactivates all active GUI components.
	 */
	public void deactivate() {
		isActive = false;
		stopButton.setEnabled(false);
		logBox.setEnabled(false);
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

	/**
	 * Shows an info message on the current form with the given text and
	 * caption.
	 * 
	 * @param text
	 *            is the message of the error
	 * @param caption
	 *            is the title of the error message box.
	 */
	public void showInfoMessage(String text, String caption) {
		JFrame currentFrame = null;
		if (currentForm == 1) {
			currentFrame = loginForm;
		} else {
			currentFrame = mainForm;
		}
		JOptionPane.showMessageDialog(currentFrame, text, caption,
				JOptionPane.INFORMATION_MESSAGE);
	}
}
package com.sirma.itt.javacourse.chat.clientSide;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The main client application started on the client side.
 */
public final class Client {
	private final int port = 7000;
	private final InetAddress address = InetAddress.getByName("localhost");

	private String nickname = "";
	private Socket clientSocket = null;
	private final PrintWriter out = null;
	private final ClientGUI gui;
	private ServerSender sender = null;
	private ServerListener listener = null;

	/**
	 * Runs the client application.
	 * 
	 * @throws IOException
	 *             if problem with the IO
	 */
	private Client() throws IOException {
		gui = new ClientGUI();
	}

	/**
	 * The entry point for the client. Self-instantiate.
	 * 
	 * @param args
	 *            are the cmd args
	 * @throws IOException
	 *             if problem with the IO
	 * @throws UnknownHostException
	 *             if host can not be found
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client();
	}

	/**
	 * Runs the main client logic. Should be run when the join button is clicked.
	 * 
	 * @throws IOException
	 *             id there is a problem with the I/O streams
	 */
	private void runClient() throws IOException {
		clientSocket = new Socket(address, port);
		sender = new ServerSender(clientSocket);
		sender.sendMessage("cmd " + nickname);
		listener = new ServerListener(clientSocket, sender, gui);
	}

	/**
	 * Formats the client's message to the server putting it date and nickname.
	 * 
	 * @param msg
	 *            is the pure message from the gui
	 * @return the formatted message to be sent to the server
	 */
	private String formatMessage(String msg) {
		String capitalizedMsg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
		return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + nickname + ": "
				+ capitalizedMsg;
	}

	/**
	 * The user interface of the client application.
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
			loginForm.setVisible(true);
		}

		/**
		 * Creates the login form showed when the application starts.
		 */
		private void createLoginForm() {
			loginForm = new JFrame("Chat application");
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
		 * Creates the main form representing the chat application.
		 */
		private void createMainForm() {
			mainForm = new JFrame("Chat application | Logged as " + nickname);
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
			mainForm.setVisible(true);
		}

		/**
		 * Hides the login form and draws the main form. Public method used by the listener to
		 * update the gui.
		 */
		public void moveToMainForm() {
			loginForm.setVisible(false);
			createMainForm();
			currentForm++;
		}

		/**
		 * Creates the text box where the user will input his nickname.
		 */
		private void createNicknameTextBox() {
			nicknameTextBox = new JTextField(23);
			nicknameTextBox.setPreferredSize(new Dimension(100, 24));
		}

		/**
		 * Creates the log box and the scroll pane where all activity will be printed.
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
		 * Creates the scroll pane where all online users will be shown.
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
		 * Creates the send button which sends the message to the server.
		 */
		private void createSendButton() {
			sendButton = new JButton("Send");
			sendButton.setPreferredSize(new Dimension(80, 32));
			sendButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sender.sendMessage(formatMessage(textBox.getText().trim()));
					textBox.setText("");
				}
			});
		}

		/**
		 * Creates the disconnect button that ends the current session.
		 */
		private void createDisconnectButton() {
			disconnectButton = new JButton("Leave");
			disconnectButton.setPreferredSize(new Dimension(80, 32));
			disconnectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					log("You left the chat room");
					sender.sendMessage("cmd disconnect");
					sendButton.setEnabled(false);
					disconnectButton.setEnabled(false);
					textBox.setEnabled(false);
				}
			});
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
		 * Creates the join button used to request join to the server.
		 */
		private void createJoinButton() {
			joinButton = new JButton("Join");
			joinButton.setPreferredSize(new Dimension(80, 32));
			joinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nickname = nicknameTextBox.getText().trim();
					try {
						if ((nickname.length() < 3) || (nickname.length() >= 20)) {
							gui.showError(
									"Your nickname must be at least 3 characters long and no more than 20",
									"nickname length error");
							nicknameTextBox.setText("");
							return;
						}
						runClient();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
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
		 * Shows an error message on the current form with the given text and caption.
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
			JOptionPane.showMessageDialog(currentFrame, text, caption, JOptionPane.ERROR_MESSAGE);
		}
	}
}

package com.sirma.itt.javacourse.chat.clientSide;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sirma.itt.javacourse.chat.clientSide.clientCommands.ClientCommand;
import com.sirma.itt.javacourse.chat.clientSide.clientCommands.JoinRequestCommand;

/**
 * Contains the basic business (back-end) logic for the client-side application.
 */
public final class Client {
	private final int port = 7000;
	private final InetAddress address = InetAddress.getByName("localhost");
	private String nickname = null;
	private Socket clientSocket = null;
	private ServerSender sender = null;
	private ClientController controller = null;

	/**
	 * Sets a client controller for this client.
	 * 
	 * @param controller
	 *            is the client controller to set
	 */
	public void setController(ClientController controller) {
		this.controller = controller;
	}

	/**
	 * Using the sender thread, sends the given command to the server.
	 * 
	 * @param cmd
	 *            is the command to send to the server
	 */
	public void sendCommand(ClientCommand cmd) {
		this.sender.sendCommand(cmd);
	}

	/**
	 * sets the client's nickname.
	 * 
	 * @param nickname
	 *            is the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Gets the client's nickname.
	 * 
	 * @return is the client;s nickname
	 */
	public String getNickname() {
		return this.nickname;
	}

	/**
	 * Throws the checked exception from the INEt address upper in the
	 * hierarchy.
	 * 
	 * @throws IOException
	 *             if problem with the IO
	 */
	Client() throws IOException {
	}

	/**
	 * Checks is the given nickname's length is within the allowed borders.
	 * 
	 * @param nickname
	 *            is the nickname to check
	 * @return true if the nickname length is valid
	 */
	public boolean isNicknameLengthValid(String nickname) {
		if ((nickname.length() < 3) || (nickname.length() >= 20)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the input message's length is in the allowed borders.
	 * 
	 * @param msg
	 *            is the user input message
	 * @return true if the message length is within the allowed borders
	 */
	public boolean isMessageLengthValid(String msg) {
		if ((msg.length() < 1) || (msg.length() > 200)) {
			return false;
		}
		return true;
	}

	/**
	 * Opens the connection to the server and starts the sender and listener
	 * threads.
	 * 
	 * @throws IOException
	 *             if a socket to the server can't be opened
	 */
	public void join() throws IOException {
		clientSocket = new Socket(address, port);
		sender = new ServerSender(clientSocket);
		new ServerListener(clientSocket, sender, controller);
		sender.sendCommand(new JoinRequestCommand(nickname));
	}

	/**
	 * Formats the client's output message putting it the current time and
	 * nickname and capitalizing its first letter.
	 * 
	 * @param msg
	 *            is the pure message from the GUI
	 * @return the formatted message to be sent to the server
	 */
	public String formatMessage(String msg) {
		String capitalizedMsg = msg.substring(0, 1).toUpperCase()
				+ msg.substring(1);
		return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] "
				+ nickname + ": " + capitalizedMsg;
	}
}

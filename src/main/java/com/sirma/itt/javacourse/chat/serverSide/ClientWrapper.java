package com.sirma.itt.javacourse.chat.serverSide;

import java.net.Socket;

/**
 * Encapsulates each client socket connected to the server with the main client attributes - its
 * sender and listener threads and its nickname in one single, wrapper class that the server will
 * handle.
 */
public class ClientWrapper {

	private Socket socket = null;
	private ClientListener listener = null;
	private ClientSender sender = null;
	private String nickname = "";

	/**
	 * Gets the nickname of the client.
	 * 
	 * @return the nickname of the client
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the client's nickname.
	 * 
	 * @param nickname
	 *            is the client's nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * a getter for the sender thread of this client.
	 * 
	 * @return the sender thread of this client
	 */
	public ClientSender getSender() {
		return sender;
	}

	/**
	 * A setter for the sender thread of this client.
	 * 
	 * @param sender
	 *            is the new sender thread to be set for this client.
	 */
	public void setSender(ClientSender sender) {
		this.sender = sender;
	}

	/**
	 * A getter for the socket of the client.
	 * 
	 * @return the client socket of this client
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * a getter for the listener thread of this client.
	 * 
	 * @return the listener thread of this client.
	 */
	public ClientListener getListener() {
		return listener;
	}

	/**
	 * Sets the client socket.
	 * 
	 * @param socket
	 *            is the client socket to be set for this client object
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * is the listener thread for this client.
	 * 
	 * @param listener
	 *            is the listener thread to be set for this client object.
	 */
	public void setListener(ClientListener listener) {
		this.listener = listener;
	}
}

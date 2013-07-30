package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server-side application.
 */
public final class Server {
	private final int port = 7000;
	private ServerSocket serverSocket = null;
	private Transmitter transmitter = null;
	public static final char[] FORBIDDEN_CHARACTERS = { '[', ']' };

	/**
	 * Starts the server.
	 */
	private Server() {
		openSocket();
		transmitter = new Transmitter();
		transmitter.start();
		handleClients();
	}

	/**
	 * Entry point for the server app. Self-instantiate.
	 * 
	 * @param args
	 *            are the cmd rags.
	 */
	public static void main(String[] args) {
		new Server();
	}

	/**
	 * Opens a new server socket on the chosen port where the server will listen
	 * for new clients connecting.
	 */
	private void openSocket() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Waits for new clients and when a new one connects, encapsulates it in the
	 * client wrapper class, assigns it listener and sender threads and adds it
	 * to the transmitter's list of connected clients.
	 */
	private void handleClients() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ClientWrapper lastClient = new ClientWrapper();
				lastClient.setSocket(socket);
				// create the sender thread for the new client
				ClientSender sender = new ClientSender(lastClient);
				lastClient.setSender(sender);
				sender.start();

				// create the listener thread for the new client
				ClientListener listener = new ClientListener(transmitter,
						lastClient);
				lastClient.setListener(listener);
				listener.start();

				// the client connected successfully and has to be added to the
				// transmitter's list
				transmitter.addClient(lastClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

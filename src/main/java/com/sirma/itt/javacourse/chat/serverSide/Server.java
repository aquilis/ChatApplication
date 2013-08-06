package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sirma.itt.javacourse.chat.serverSide.serverCommands.CloseConnectionCommand;

/**
 * The server-side application.
 */
public final class Server {
	private final int port = 7000;
	private ServerSocket serverSocket = null;
	private Transmitter transmitter = null;
	public static final char[] FORBIDDEN_CHARACTERS = { '[', ']' };
	private ServerController controller = null;

	/**
	 * Starts the server.
	 * 
	 * @param controller
	 *            is the server controller
	 */
	Server(ServerController controller) {
		this.controller = controller;
		openSocket();
		transmitter = new Transmitter();
		transmitter.start();
		handleClients();
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
	 * Stops the server application.
	 */
	public void stopServer() {
		transmitter.sendCommand(new CloseConnectionCommand());
		try {
			serverSocket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Waits for new clients and when a new one connects, encapsulates it in the
	 * client wrapper class and assigns it listener and sender threads that make
	 * the rest.
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
						lastClient, controller);
				lastClient.setListener(listener);
				listener.start();
				controller.log("listener thread started");
			} catch (IOException e) {
				break;
			}
		}
	}
}

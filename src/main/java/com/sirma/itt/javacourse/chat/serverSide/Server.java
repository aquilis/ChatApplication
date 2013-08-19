package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sirma.itt.javacourse.chat.LanguageManager;
import com.sirma.itt.javacourse.chat.LogHandlersManager;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.CloseConnectionCommand;

/**
 * The server-side application. Contains the basic business logic for the
 * server. The rest is handled by the handler threads and the client commands.
 */
public final class Server {
	private final int port = 7000;
	private ServerSocket serverSocket = null;
	private Transmitter transmitter = null;
	public static final char[] FORBIDDEN_CHARACTERS = { '[', ']' };

	// the logger instance and handlers
	private static final Logger LOGGER = Logger.getLogger(Server.class
			.getName());
	private final FileHandler fileHandler = LogHandlersManager
			.getServerHandler();


	/**
	 * Constructs the server instantiating its socket and the transmitter.
	 */
	Server() {
		// logger
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(fileHandler);
		//
		openSocket();
		transmitter = new Transmitter();
		transmitter.start();
	}

	/**
	 * Opens a new server socket on the chosen port where the server will listen
	 * for new clients connecting.
	 */
	private void openSocket() {
		try {
			serverSocket = new ServerSocket(port);
			LOGGER.info("Server socket opened at port " + port);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
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
			LOGGER.log(Level.WARNING, e1.getMessage(), e1);
		}
		LOGGER.info("Server socket closed");
	}

	/**
	 * Gets the transmitter object.
	 * 
	 * @return the transmitter object
	 */
	public Transmitter getTransmitter() {
		return transmitter;
	}

	/**
	 * Waits for a new client to connect, and when that happens, encapsulates it
	 * inside the wrapper class and return a reference to it.
	 * 
	 * @return the lastly connected client
	 * @throws IOException
	 *             if an I/O problem occurs
	 */
	public ClientWrapper waitForClient() throws IOException {
		Socket socket = serverSocket.accept();
		ClientWrapper lastClient = new ClientWrapper();
		lastClient.setSocket(socket);
		LOGGER.info("new client socket connected");
		return lastClient;
	}

	/**
	 * Attaches a sender thread to the given client.
	 * 
	 * @param client
	 *            is the client to attach a sender thread to
	 */
	public void attachClientSender(ClientWrapper client) {
		ClientSender sender = new ClientSender(client);
		client.setSender(sender);
		sender.start();
	}

	/**
	 * Attaches a listener thread to the given client.
	 * 
	 * @param client
	 *            is the client to attach a listener thread to
	 * @param transmitter
	 *            the listener thread needs the transmitter to send the received
	 *            commands to all
	 * @param controller
	 *            the listener thread also needs the controller so that the
	 *            incoming commands can execute using its functionality
	 */
	public void attachClientListener(ClientWrapper client,
			Transmitter transmitter, ServerController controller) {
		ClientListener listener = new ClientListener(transmitter, client,
				controller);
		client.setListener(listener);
		listener.start();
		controller.log(LanguageManager.getString("listenerStarted"));
		LOGGER.info("New client listener thread started");
	}
}

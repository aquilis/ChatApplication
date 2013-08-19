package com.sirma.itt.javacourse.chat.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sirma.itt.javacourse.chat.LanguageManager;
import com.sirma.itt.javacourse.chat.LogHandlersManager;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.ServerCommand;

/**
 * That thread listens for messages from the server and forwards them to the
 * client controller class to handle them.
 */
public class ServerListener extends Thread {
	private ObjectInputStream in = null;
	private ServerSender sender = null;
	private ClientController controller = null;
	private Socket socket = null;
	// the logger instance and handlers
	private static final Logger LOGGER = Logger.getLogger(ServerListener.class
			.getName());
	private final FileHandler fileHandler = LogHandlersManager
			.getClientHandler();

	/**
	 * Constructs the client listener thread.
	 * 
	 * @param socket
	 *            is the client socket to the server.
	 * @param sender
	 *            is the sender thread that has to be deactivated when the
	 *            server denies access
	 * @param controller
	 *            the commands need the client controller object in order to
	 *            command the client's back-end logic and the GUI indirectly
	 */
	public ServerListener(Socket socket, ServerSender sender,
			ClientController controller) {
		this.socket = socket;
		this.sender = sender;
		this.controller = controller;
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(fileHandler);
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			LOGGER.warning("Error opening Object input stream");
			e.printStackTrace();
		}
		start();
	}

	/**
	 * Gets the client socket.
	 * 
	 * @return the client socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Returns the client controller of this listener.
	 * 
	 * @return the client controller of this listener used by the command
	 *         classes
	 */
	public ClientController getClientController() {
		return controller;
	}

	/**
	 * Gets the corresponding sender thread of this listener.
	 * 
	 * @return the corresponding sender thread of this listener
	 */
	public ServerSender getSender() {
		return sender;
	}

	@Override
	public void run() {
		Object input = null;
		try {
			while ((input = in.readObject()) != null) {
				ServerCommand incomingCommand = (ServerCommand) input;
				LOGGER.info("Command received from server. Command type: "
						+ incomingCommand.getClass().getCanonicalName());
				incomingCommand.execute(this);
			}
		} catch (IOException e) {
			// block entered when the connection to server unexpectedly drops.
			LOGGER.info("Connection to server lost");
			controller.log(LanguageManager.getString("connectionToServerLost"));
			controller.deactivate();
			// the sender thread is not needed anymore
			sender.deactivate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		} finally {
			LOGGER.info("server listener thread terminated.");
			// clean up
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sirma.itt.javacourse.chat.LanguageManager;
import com.sirma.itt.javacourse.chat.LogHandlersManager;
import com.sirma.itt.javacourse.chat.clientSide.clientCommands.ClientCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.MessageToClientCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.RemoveOnlineClientCommand;

/**
 * A handler class for each client. Waits for commands from the client and
 * executes them.
 */
public class ClientListener extends Thread {
	private Transmitter transmitter = null;
	private ObjectInputStream in = null;
	private ClientWrapper client = null;
	private ServerController controller = null;
	// the logger instance and handlers
	private static final Logger LOGGER = Logger.getLogger(ClientListener.class
			.getName());
	private final FileHandler fileHandler = LogHandlersManager
			.getServerHandler();

	/**
	 * Gets the server application's GUI.
	 * 
	 * @return the server GUI.
	 */
	public ServerController getController() {
		return controller;
	}

	/**
	 * Gets the transmitter of the current client listener. used by the command
	 * classes.
	 * 
	 * @return the transmitter object.
	 */
	public Transmitter getTransmitter() {
		return transmitter;
	}

	/**
	 * Gets the client object that the listener listens to. Used by the commands
	 * classes.
	 * 
	 * @return the current client object
	 */
	public ClientWrapper getClient() {
		return client;
	}

	/**
	 * Constructs the client listener with the server's transmitter object and a
	 * reference to the client instance the listener is assigned to.
	 * 
	 * @param transmitter
	 *            is the server's transmitter where the incoming message has to
	 *            be sent
	 * @param client
	 *            is the client object where the listener is assigned
	 * @param controller
	 *            is the server controller that will interact with the GUI
	 */
	public ClientListener(Transmitter transmitter, ClientWrapper client,
			ServerController controller) {
		this.controller = controller;
		this.transmitter = transmitter;
		this.client = client;
		Socket socket = client.getSocket();
		// logger
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(fileHandler);
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}

	@Override
	public void run() {
		Object input = null;
		try {
			while ((input = in.readObject()) != null) {
				ClientCommand incomingCommand = (ClientCommand) input;
				LOGGER.info("New commmand from client " + client.getNickname()
						+ " received. Command type: "
						+ incomingCommand.getClass().getCanonicalName());
				incomingCommand.execute(this);
			}
		} catch (IOException e) {
			// block entered when the client unexpectedly disconnects without
			// sending the appropriate command.
			client.getSender().deactivate();
			if (!"".equals(client.getNickname())) {
				LOGGER.info("Listener lost connection with "
						+ client.getNickname());
				controller.log(LanguageManager.getString("connectionLost")
						+ " " + client.getNickname());
				transmitter.sendCommand(new MessageToClientCommand(client
						.getNickname()
						+ " "
						+ LanguageManager.getString("disconnected")));
				controller.log(LanguageManager
						.getString("disconnectNotificationSent"));
				transmitter.removeClient(client);
				transmitter.sendCommand(new RemoveOnlineClientCommand(client
						.getNickname()));
			}
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		} finally {
			LOGGER.info("Client listener thread for " + client.getNickname()
					+ " terminated.");
			// clean up
			try {
				in.close();
			} catch (IOException e) {
				LOGGER.warning("Error closing the input stream for client "
						+ client.getNickname());
			}
		}
	}
}

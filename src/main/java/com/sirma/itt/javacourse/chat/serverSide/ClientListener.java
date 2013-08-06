package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.sirma.itt.javacourse.chat.clientSide.clientCommands.ClientCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.MessageToClientCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.RemoveOnlineClientCommand;

/**
 * A handler class for each client. Waits for messages and commands from the
 * client socket. If a command arrives, parses it and executes it. If a message
 * arrives, send it to the transmitter to be sent to all other clients.
 */
public class ClientListener extends Thread {
	private Transmitter transmitter = null;
	private ObjectInputStream in = null;
	private ClientWrapper client = null;
	private ServerController controller = null;

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
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Object input = null;
		try {
			while ((input = in.readObject()) != null) {
				ClientCommand incomingCommand = (ClientCommand) input;
				incomingCommand.execute(this);
			}
		} catch (IOException e) {
			// block entered when the client unexpectedly disconnects without
			// sending the appropriate command.
			client.getSender().deactivate();
			if (!"".equals(client.getNickname())) {
				controller.log("Connection with " + client.getNickname()
						+ " lost");
				transmitter.sendCommand(new MessageToClientCommand(client
						.getNickname() + " disconnected"));
				controller
						.log("Disconnect notifying message sent to all clients ");
				transmitter.removeClient(client);
				transmitter.sendCommand(new RemoveOnlineClientCommand(client
						.getNickname()));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// clean up
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

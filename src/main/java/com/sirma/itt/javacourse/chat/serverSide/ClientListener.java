package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.sirma.itt.javacourse.chat.clientSide.clientCommands.ClientCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.IncomingMessageCommand;

/**
 * A handler class for each client. Waits for messages and commands from the
 * client socket. If a command arrives, parses it and executes it. If a message
 * arrives, send it to the transmitter to be sent to all other clients.
 */
public class ClientListener extends Thread {
	private Transmitter transmitter = null;
	private ObjectInputStream in = null;
	private ClientWrapper client = null;

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
	 */
	public ClientListener(Transmitter transmitter, ClientWrapper client) {
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
			transmitter.sendCommand(new IncomingMessageCommand(client
					.getNickname() + " disconnected"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			client.getSender().deactivate();
			transmitter.removeClient(client);
		}
	}
}

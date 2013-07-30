package com.sirma.itt.javacourse.chat.serverSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * A handler class for each client. Waits for messages and commands from the client socket. If a
 * command arrives, parses it and executes it. If a message arrives, send it to the transmitter to
 * be sent to all other clients.
 */
public class ClientListener extends Thread {
	private Transmitter transmitter = null;
	private BufferedReader in = null;
	private ClientWrapper client = null;

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
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the given user nickname is valid, according to the array of forbidden characters
	 * defined by the server.
	 * 
	 * @param nickname
	 *            is the nickname to check
	 * @return true if the nickname is valid
	 */
	private boolean isNicknameValid(String nickname) {
		for (char ch : Server.FORBIDDEN_CHARACTERS) {
			if (nickname.contains(String.valueOf(ch))) {
				return false;
			}
		}
		return true;
	}

	// TODO fix uniqueness validation
	/**
	 * Checks if the nickname of the clients is unique, taking the nicknames of
	 * all other connected clients. Not case-sensitive.
	 * 
	 * @param nickname
	 *            is the nickname to check for
	 * @return true if the nickname is unique
	 */
	private boolean isNicknameUnique(String nickname) {
		for (ClientWrapper clnt : transmitter.getListClients()) {
			if (clnt != client) {
				if (clnt.getNickname().equalsIgnoreCase(nickname)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Parses the given command that comes from the client-side application and executes them.
	 * 
	 * @param cmd
	 *            is the canonical name of the command
	 */
	private void parseCommand(String cmd) {
		if ("disconnect".equals(cmd)) {
			transmitter.sendMessage(client.getNickname() + " disconnected");
			client.getSender().deactivate();
			transmitter.removeClient(client);
		} else {
			if (isNicknameValid(cmd) && (isNicknameUnique(cmd))) {
				client.getSender().sendMessage("cmd access allowed");
				client.setNickname(cmd);
				transmitter.sendMessage(cmd + " connected");
			} else {
				// nickname not valid. Send the client command that it is not accepted and
				// disconnect him
				client.getSender().sendMessage("cmd access denied");
				client.getSender().deactivate();
				transmitter.removeClient(client);
			}
		}
	}

	@Override
	public void run() {
		String input = "";
		try {
			while ((input = in.readLine()) != null) {
				if ("cmd".equals(input.substring(0, 3))) {
					parseCommand(input.substring(4));
					continue;
				}
				transmitter.sendMessage(input);
			}
		} catch (IOException e) {
			transmitter.sendMessage(client.getNickname() + " disconnected");
		} finally {
			// deactivate the client's sender object if the client is no longer
			// online
			client.getSender().deactivate();
			// remove the client form the transmitter's list of clients
			transmitter.removeClient(client);
		}
	}
}

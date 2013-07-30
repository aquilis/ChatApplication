package com.sirma.itt.javacourse.chat.clientSide.clientCommands;

import com.sirma.itt.javacourse.chat.serverSide.ClientListener;
import com.sirma.itt.javacourse.chat.serverSide.ClientWrapper;
import com.sirma.itt.javacourse.chat.serverSide.Server;
import com.sirma.itt.javacourse.chat.serverSide.Transmitter;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.AccessAllowedCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.AccessDeniedCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.IncomingMessageCommand;


/**
 * Sent from the client to the server to request join to the chat room. Contains
 * the client's nickname.
 */
public class JoinRequestCommand implements ClientCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private String nickname = null;
	private Transmitter transmitter = null;
	private ClientWrapper client = null;

	/**
	 * Constructs the command with the client nickname.
	 * 
	 * @param nickname
	 *            is the client nickname that the listener has to validate.
	 */
	public JoinRequestCommand(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Checks if the given user nickname is valid, according to the array of
	 * forbidden characters defined by the server.
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

	@Override
	public void execute(ClientListener listener) {
		this.transmitter = listener.getTransmitter();
		this.client = listener.getClient();
		if (isNicknameValid(nickname) && (isNicknameUnique(nickname))) {
			client.getSender().sendCommand(new AccessAllowedCommand());
			client.setNickname(nickname);
			transmitter.sendCommand(new IncomingMessageCommand(nickname
					+ " connected"));

			// TODO Create a command UpdatedListCommand
			// and send it to all clients using the transmitter
		} else {
			// nickname not valid. Send the client command that it is not
			// accepted and
			// disconnect him
			client.getSender().sendCommand(new AccessDeniedCommand());
			client.getSender().deactivate();
			transmitter.removeClient(client);
		}
	}
}

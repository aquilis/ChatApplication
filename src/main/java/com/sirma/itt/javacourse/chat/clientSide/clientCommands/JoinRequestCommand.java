package com.sirma.itt.javacourse.chat.clientSide.clientCommands;

import com.sirma.itt.javacourse.chat.serverSide.ClientListener;
import com.sirma.itt.javacourse.chat.serverSide.ClientWrapper;
import com.sirma.itt.javacourse.chat.serverSide.Server;
import com.sirma.itt.javacourse.chat.serverSide.Transmitter;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.AccessAllowedCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.AccessDeniedCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.AddOnlineClientCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.MessageToClientCommand;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.SendOnlineClientsCommand;

/**
 * Sent from the client to the server to request join to the chat room.
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
		// if the requested nickname gets approved
		if (isNicknameValid(nickname) && (isNicknameUnique(nickname))) {
			// update server's GUI
			listener.getController().log(nickname + " joined");
			// send the client access allowed command
			client.getSender().sendCommand(new AccessAllowedCommand());
			client.setNickname(nickname);
			// send all other clients the commands to add the new one to their
			// lists
			transmitter.sendCommand(new AddOnlineClientCommand(client
					.getNickname()));
			transmitter.addClient(client);
			listener.getController().log(
					nickname + " was added to the list of online clients");
			transmitter.sendCommand(new MessageToClientCommand(nickname
					+ " connected"));
			listener.getController().log(
					"Notifying message for a new client sent to all clients");
			// send the new client the list of all online clients
			client.getSender().sendCommand(
					new SendOnlineClientsCommand(transmitter
							.getAllClientNicknames()));
			// send the new client a welcome message
			client.getSender().sendCommand(
					new MessageToClientCommand("Welcome to chat room!"));

		} else {
			client.getSender().sendCommand(new AccessDeniedCommand());
			// try {
			// // wait for 1 second before terminating the client sender thread
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			client.getSender().deactivate();
		}
	}
}

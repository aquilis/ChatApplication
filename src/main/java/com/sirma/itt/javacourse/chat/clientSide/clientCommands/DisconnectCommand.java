package com.sirma.itt.javacourse.chat.clientSide.clientCommands;

import java.io.IOException;

import com.sirma.itt.javacourse.chat.LanguageManager;
import com.sirma.itt.javacourse.chat.serverSide.ClientListener;
import com.sirma.itt.javacourse.chat.serverSide.ClientWrapper;
import com.sirma.itt.javacourse.chat.serverSide.Transmitter;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.RemoveOnlineClientCommand;

/**
 * Notifies the server that this client disconnects.
 */
public class DisconnectCommand implements ClientCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private Transmitter transmitter = null;
	private ClientWrapper client = null;

	@Override
	public void execute(ClientListener listener) {
		this.transmitter = listener.getTransmitter();
		this.client = listener.getClient();
		listener.getController().log(
				client.getNickname() + " "
						+ LanguageManager.getString("disconnected"));
		client.getSender().deactivate();
		try {
			client.getSocket().close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing socket");
		}
		transmitter.removeClient(client);
		transmitter.sendCommand(new RemoveOnlineClientCommand(client
				.getNickname()));
	}
}

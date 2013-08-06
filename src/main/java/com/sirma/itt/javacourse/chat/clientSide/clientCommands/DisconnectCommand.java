package com.sirma.itt.javacourse.chat.clientSide.clientCommands;

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
		// transmitter.sendCommand(new
		// MessageToClientCommand(client.getNickname()
		// + " disconnected"));
		listener.getController().log(client.getNickname() + " disconnencted");
		client.getSender().deactivate();
		transmitter.removeClient(client);
		transmitter.sendCommand(new RemoveOnlineClientCommand(client
				.getNickname()));
	}
}

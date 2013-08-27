package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * Encapsulates a message that has to be sent to a client and displayed on its
 * GUI.
 */
public class MessageToClientCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String message = null;

	/**
	 * Constructs the commands with a message to be displayed on the client's
	 * GUI.
	 * 
	 * @param message
	 *            is the incoming message.
	 */
	public MessageToClientCommand(String message) {
		this.message = message;
	}

	@Override
	public void execute(ServerListener listener) {
			listener.getClientController().log(message);
	}
}

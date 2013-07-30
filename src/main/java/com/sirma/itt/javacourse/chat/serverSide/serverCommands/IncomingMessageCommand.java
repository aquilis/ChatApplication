package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import com.sirma.itt.javacourse.chat.clientSide.Client;
import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * Encapsulates an incoming message from another client, that has to be
 * displayed onto the GUI of the client module.
 */
public class IncomingMessageCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String message = null;
	private Client.ClientGUI gui = null;

	/**
	 * Constructs the commands with a message to be displayed on the client's
	 * GUI.
	 * 
	 * @param message
	 *            is the incoming message.
	 */
	public IncomingMessageCommand(String message) {
		this.message = message;
	}

	@Override
	public void execute(ServerListener listener) {
		this.gui = listener.getGui();
		gui.log(message);
	}
}

package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import java.util.Observable;

import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * This command is sent from the server when the client nickname is invalid.
 * Shows an error on the client GUI and stops the client sender and listener
 * threads.
 */
public class AccessDeniedCommand extends Observable implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(ServerListener listener) {
		listener.getClientController().showError(
						"Please, make sure that your nickname is unique and doesn't have invalid characters",
						"Invalid nickname");
		listener.getSender().deactivate();
	}
}

package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import java.util.logging.Level;

import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * Sent from the server when the client nickname is valid and unique.
 */
public class AccessAllowedCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(ServerListener listener) {
		listener.getClientController().moveToMainForm();
		listener.getClientController().logToFile(
				"Nickname approved by server. Join successfull", Level.INFO);
	}
}

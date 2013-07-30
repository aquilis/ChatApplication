package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import com.sirma.itt.javacourse.chat.clientSide.Client;
import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * This commands is sent from the server when the client nickname is invalid.
 * Shows an error on the client GUI and stops the client sender and listener
 * threads.
 */
public class AccessDeniedCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private Client.ClientGUI gui = null;

	@Override
	public void execute(ServerListener listener) {
		this.gui = listener.getGui();
		gui.showError(
				"Please, make sure that your nickname is unique and doesn't have invalid characters",
				"Invalid nickname");
		listener.setMustTerminate(true);
	}
}

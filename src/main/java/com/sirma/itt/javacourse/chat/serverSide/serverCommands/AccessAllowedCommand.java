package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import com.sirma.itt.javacourse.chat.clientSide.Client;
import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * Sent from the server when the client nickname is valid and unique.
 */
public class AccessAllowedCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private Client.ClientGUI gui = null;

	@Override
	public void execute(ServerListener listener) {
		this.gui = listener.getGui();
		gui.moveToMainForm();
	}
}

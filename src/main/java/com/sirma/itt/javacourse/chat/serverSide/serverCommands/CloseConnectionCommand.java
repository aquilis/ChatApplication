package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import java.io.IOException;

import com.sirma.itt.javacourse.chat.LanguageManager;
import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * Notifies the client that the server closed the connection.
 */
public class CloseConnectionCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(ServerListener listener) {
		listener.getClientController().log(
				LanguageManager.getString("serverClosedConnection"));
		try {
			listener.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listener.getClientController().deactivate();
	}
}

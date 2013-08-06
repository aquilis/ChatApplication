package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * When a client disconnects, this command removes its nickname on each client
 * module and updates the GUI.
 */
public class RemoveOnlineClientCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String nickname = null;

	/**
	 * Constructs the command with a nickname to remove.
	 * 
	 * @param nickname
	 *            is the nickname to remove
	 */
	public RemoveOnlineClientCommand(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public void execute(ServerListener listener) {
		listener.getClientController().removeOnlineUser(nickname);
	}
}

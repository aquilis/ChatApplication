package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * When a new client connects, this command adds its nickname on each client
 * module and updates their GUI.
 */
public class AddOnlineClientCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String nickname = null;

	/**
	 * Constructs the command with a nickname to add.
	 * 
	 * @param nickname
	 *            is the nickname to remove
	 */
	public AddOnlineClientCommand(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public void execute(ServerListener listener) {
		listener.getClientController().addOnlineUser(nickname);
	}
}

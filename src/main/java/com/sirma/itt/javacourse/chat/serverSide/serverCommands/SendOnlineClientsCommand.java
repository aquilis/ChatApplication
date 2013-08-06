package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import java.util.ArrayList;

import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * Sends the full list of currently connected clients to the new client.
 */
public class SendOnlineClientsCommand implements ServerCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> listNicknames = null;

	/**
	 * Constructs the commands with the updated list of connected clients.
	 * 
	 * @param listNicknames
	 *            is the updated list of connected clients nicknames sent to all.
	 */
	public SendOnlineClientsCommand(ArrayList<String> listNicknames) {
		this.listNicknames = listNicknames;
	}

	@Override
	public void execute(ServerListener listener) {
		listener.getClientController().setListOnlineUsers(listNicknames);
	}
}

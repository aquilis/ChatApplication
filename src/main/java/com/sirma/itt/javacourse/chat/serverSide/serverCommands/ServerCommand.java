package com.sirma.itt.javacourse.chat.serverSide.serverCommands;

import java.io.Serializable;

import com.sirma.itt.javacourse.chat.clientSide.ServerListener;

/**
 * A server command is a command sent by the server, executed by the server
 * listener located on the client-side. This interface represents an abstract
 * command for all other server commands. Every client-server interaction is
 * encapsulated inside a Command class.
 */
public interface ServerCommand extends Serializable {
	/**
	 * Executes the commands encapsulated in the subclasses.
	 * 
	 * @param listener
	 *            is the server listener (on the client-side) that has the
	 *            needed resources to execute the command (such as the client
	 *            GUI, the client socket, etc)
	 */
	void execute(ServerListener listener);
}

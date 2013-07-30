package com.sirma.itt.javacourse.chat.clientSide.clientCommands;

import java.io.Serializable;

import com.sirma.itt.javacourse.chat.serverSide.ClientListener;

/**
 * A client commands is a command sent by the client, executed by the client
 * listener located on the server-side. This interface represents an abstract
 * commands for all other client commands. Every client-server interaction is
 * encapsulated inside a Command class.
 */
public interface ClientCommand extends Serializable {

	/**
	 * Executes the command encapsulated in the subclasses.
	 * 
	 * @param listener
	 *            is the client listener (on the server-side) that has all the
	 *            needed resources to execute the commands coming from the
	 *            client. Needed only to get the transmitter object and the
	 *            client object.
	 */
	void execute(ClientListener listener);
}

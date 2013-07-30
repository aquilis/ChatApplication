package com.sirma.itt.javacourse.chat.clientSide.clientCommands;

import com.sirma.itt.javacourse.chat.serverSide.ClientListener;
import com.sirma.itt.javacourse.chat.serverSide.Transmitter;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.IncomingMessageCommand;

/**
 * This command sends the client message to all other clients using the
 * transmitter class.
 */
public class OutputMessageCommand implements ClientCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String message = null;
	private Transmitter transmitter = null;

	/**
	 * Constructs the command with a client message to be sent to all.
	 * 
	 * @param message
	 *            is the client message
	 */
	public OutputMessageCommand(String message) {
		this.message = message;
	}

	@Override
	public void execute(ClientListener listener) {
		this.transmitter = listener.getTransmitter();
		transmitter.sendCommand(new IncomingMessageCommand(message));
	}
}

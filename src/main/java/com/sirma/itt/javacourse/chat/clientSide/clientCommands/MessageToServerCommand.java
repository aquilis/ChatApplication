package com.sirma.itt.javacourse.chat.clientSide.clientCommands;

import com.sirma.itt.javacourse.chat.serverSide.ClientListener;
import com.sirma.itt.javacourse.chat.serverSide.Transmitter;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.MessageToClientCommand;

/**
 * Encapsulates a message that has to be sent to the server and then transmitted
 * to all other clients.
 */
public class MessageToServerCommand implements ClientCommand {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String message = null;
	private final boolean logToGUI;
	private Transmitter transmitter = null;

	/**
	 * Constructs the command with a client message to be sent to all.
	 * 
	 * @param message
	 *            is the client message
	 * @param logToGUI
	 *            if set to true, the command's message will be logged on the
	 *            server's GUI panel
	 */
	public MessageToServerCommand(String message, boolean logToGUI) {
		this.message = message;
		this.logToGUI = logToGUI;
	}

	@Override
	public void execute(ClientListener listener) {
		this.transmitter = listener.getTransmitter();
		transmitter.sendCommand(new MessageToClientCommand(message));
		if (logToGUI) {
			listener.getController().log(message);
		}
	}
}

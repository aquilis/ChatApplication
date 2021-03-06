package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sirma.itt.javacourse.chat.LogHandlersManager;
import com.sirma.itt.javacourse.chat.serverSide.serverCommands.ServerCommand;

/**
 * A sender thread for each client that sends its commands to the server.
 */
public class ClientSender extends Thread {
	private final Queue<ServerCommand> queueCommands = new LinkedList<ServerCommand>();
	private Socket socket = null;
	private ObjectOutputStream out = null;
	// logger
	private static final Logger LOGGER = Logger.getLogger(ClientSender.class.getName());
	private final FileHandler fileHandler = LogHandlersManager.getServerHandler();

	/**
	 * Deactivates and terminates the sender thread. Used when the client disconnects or when the
	 * connection is lost.
	 */
	public synchronized void deactivate() {
		interrupt();
	}

	/**
	 * Construct the sender where the messages will be sent.
	 * 
	 * @param client
	 *            is the client to send messages to
	 */
	public ClientSender(ClientWrapper client) {
		this.socket = client.getSocket();
		LOGGER.setUseParentHandlers(false);
		if (LOGGER.getHandlers().length > 0) {
			LOGGER.removeHandler(LOGGER.getHandlers()[0]);
		}
		LOGGER.addHandler(fileHandler);
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error opening I/O stream.", e);
		}
	}

	/**
	 * Enqueues the message into the queue of messages to be sent.
	 * 
	 * @param cmd
	 *            is the command to send to the client
	 */
	public synchronized void sendCommand(ServerCommand cmd) {
		queueCommands.offer(cmd);
		notify();
	}

	/**
	 * If there are enqueued messages in the queue, retrieve and remove the first one, otherwise,
	 * pause the thread until the queue fills.
	 * 
	 * @return the oldest message in the queue, if any
	 */
	private synchronized ServerCommand getCommandFromQueue() {
		while (queueCommands.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		return queueCommands.poll();
	}

	@Override
	public void run() {
		while (true) {
			ServerCommand cmd = getCommandFromQueue();
			if (cmd == null) {
				break;
			}
			try {
				out.writeObject(cmd);
			} catch (IOException e) {
				break;
			}
		}
	}
}

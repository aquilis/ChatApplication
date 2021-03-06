package com.sirma.itt.javacourse.chat.clientSide;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sirma.itt.javacourse.chat.LogHandlersManager;
import com.sirma.itt.javacourse.chat.clientSide.clientCommands.ClientCommand;

/**
 * A sender thread that sends the client commands to the server.
 */
public class ServerSender extends Thread {
	private final Queue<ClientCommand> queueCommands = new LinkedList<ClientCommand>();
	private ObjectOutputStream out = null;
	private static final Logger LOGGER = Logger.getLogger(ServerListener.class.getName());
	private final FileHandler fileHandler = LogHandlersManager.getClientHandler();

	/**
	 * Deactivates and terminates the sender thread. Used when the client disconnects or when the
	 * connection is lost.
	 */
	public synchronized void deactivate() {
		interrupt();
	}

	/**
	 * Constructs the sender.
	 * 
	 * @param socket
	 *            is the socket where the client messages will be sent
	 */
	public ServerSender(Socket socket) {
		LOGGER.setUseParentHandlers(false);
		if (LOGGER.getHandlers().length > 0) {
			LOGGER.removeHandler(LOGGER.getHandlers()[0]);
		}
		LOGGER.addHandler(fileHandler);
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error opening I/O stream. Thread start cancelled.", e);
			return;
		}
		start();
	}

	/**
	 * Enqueues the message into the queue of messages to be sent.
	 * 
	 * @param cmd
	 *            is the command to be sent to the server
	 */
	public synchronized void sendCommand(ClientCommand cmd) {
		queueCommands.offer(cmd);
		notify();
	}

	/**
	 * If there are enqueued messages in the queue, retreive and remove the
	 * first one, otherwise, pause the thread until the queue fills.
	 * 
	 * @return the oldest message in the queue, if any
	 */
	private synchronized ClientCommand getCommandFromQueue() {
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
			ClientCommand cmd = getCommandFromQueue();
			if (cmd == null) {
				break;
			}
			try {
				out.writeObject(cmd);
			} catch (IOException e) {
				break;
			}
		}
		LOGGER.info("Server sender thread terminated");
	}
}

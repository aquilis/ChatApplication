package com.sirma.itt.javacourse.chat.clientSide;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import com.sirma.itt.javacourse.chat.clientSide.clientCommands.ClientCommand;

/**
 * A sender thread that sends the client commands to the server.
 */
public class ServerSender extends Thread {
	private final Queue<ClientCommand> queueCommands = new LinkedList<ClientCommand>();
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private final boolean isActive = true;

	/**
	 * Deactivates and terminates the sender thread. Used when the client
	 * disconnects or when the connection is lost.
	 */
	public synchronized void deactivate() {
		interrupt();
	}

	/**
	 * Construct the sender where the messages will be sent.
	 * 
	 * @param socket
	 *            is the socket where to send the client messages
	 */
	public ServerSender(Socket socket) {
		this.socket = socket;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
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
	 * If there are enqueued messages in the queue, retrieve and remove the
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
		while (isActive && (socket != null)) {
			ClientCommand cmd = getCommandFromQueue();
			if (cmd == null) {
				break;
			}
			try {
				out.writeObject(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

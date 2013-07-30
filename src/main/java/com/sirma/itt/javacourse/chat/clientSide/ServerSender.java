package com.sirma.itt.javacourse.chat.clientSide;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A sender thread that sends the client messages to the server.
 */
public class ServerSender extends Thread {
	private final Queue<String> queueMessages = new LinkedList<String>();
	private Socket socket = null;
	private PrintWriter out = null;
	private boolean isActive = true;

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
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	/**
	 * Enqueues the message into the queue of messages to be sent.
	 * 
	 * @param msg
	 *            is the message to send
	 */
	public synchronized void sendMessage(String msg) {
		queueMessages.offer(msg);
		notify();
	}

	/**
	 * If there are enqueued messages in the queue, retrieve and remove the
	 * first one, otherwise, pause the thread until the queue fills.
	 * 
	 * @return the oldest message in the queue, if any
	 */
	private synchronized String getMessageFromQueue() {
		while (queueMessages.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		return queueMessages.poll();
	}

	@Override
	public void run() {
		while (isActive && (socket != null)) {
			String message = getMessageFromQueue();
			if (message == null) {
				break;
			}
			out.println(message);
			out.flush();
		}
	}
}

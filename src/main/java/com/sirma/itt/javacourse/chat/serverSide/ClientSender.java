package com.sirma.itt.javacourse.chat.serverSide;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import com.sirma.itt.javacourse.chat.serverSide.serverCommands.ServerCommand;

/**
 * A separate thread that deals with sending commands to its parent client
 * wrapper instance.
 */
public class ClientSender extends Thread {
	private final Queue<ServerCommand> queueCommands = new LinkedList<ServerCommand>();
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private boolean isActive = true;

	/**
	 * Deactivates and terminates the sender thread. Used when the client
	 * disconnects or when the connection is lost.
	 */
	public synchronized void deactivate() {
		isActive = false;
		notify();
	}

	/**
	 * Construct the sender where the messages will be sent.
	 * 
	 * @param client
	 *            is the client to send messages to
	 */
	public ClientSender(ClientWrapper client) {
		this.socket = client.getSocket();
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
		return queueCommands.poll();
	}

	@Override
	public void run() {
		while (isActive) {
			ServerCommand cmd = getCommandFromQueue();
			try {
				out.writeObject(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

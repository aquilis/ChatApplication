package com.sirma.itt.javacourse.chat.serverSide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.sirma.itt.javacourse.chat.serverSide.serverCommands.ServerCommand;

/**
 * A mediator class that has methods for sending the certain command to all
 * connected clients to the server. Maintains a list of enqueued messages to be
 * sent, and a list of all connected clients updated regularly.
 */
public class Transmitter extends Thread {
	private final ArrayList<ClientWrapper> listClients = new ArrayList<ClientWrapper>();
	private final Queue<ServerCommand> queueCommands = new LinkedList<ServerCommand>();

	/**
	 * Adds a new client to the transmitter's list of clients.
	 * 
	 * @param client
	 *            is the new client to add.
	 */
	public void addClient(ClientWrapper client) {
		this.listClients.add(client);
	}

	/**
	 * Gets the current list of clients of the transmitter.
	 * 
	 * @return the list of clients of the transmitter
	 */
	public ArrayList<ClientWrapper> getListClients() {
		return listClients;
	}

	/**
	 * Removes the given client from the transmitter's list. The client will no
	 * longer receive commands.
	 * 
	 * @param client
	 *            is the client to remove
	 */
	public void removeClient(ClientWrapper client) {
		listClients.remove(client);
	}

	/**
	 * Enqueue the given command to be later sent to all connected clients.
	 * 
	 * @param cmd
	 *            is the command to be sent to all connected clients
	 */
	public synchronized void sendCommand(ServerCommand cmd) {
		queueCommands.offer(cmd);
		notify();
	}

	/**
	 * If there are enqueued commands in the queue, retrieve and remove the
	 * first one, otherwise, pause the thread until the queue fills.
	 * 
	 * @return the oldest command in the queue, if any
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

	/**
	 * Sends the command to all connected clients.
	 * 
	 * @param cmd
	 *            is the command to send to all clients
	 */
	private void sendToAll(ServerCommand cmd) {
		for (ClientWrapper client : listClients) {
			client.getSender().sendCommand(cmd);
		}
	}
	
	/**
	 * Returns a list with just the nicknames of all currently connected clients. Preferred over
	 * serializing the whole list of ClientWrapper classes and sending it to the clients to be then
	 * unpacked again.
	 * 
	 * @return a list with just the nicknames of the clients
	 */
	public ArrayList<String> getAllClientNicknames() {
		ArrayList<String> stringList = new ArrayList<String>();
		for (ClientWrapper client : listClients) {
			stringList.add(client.getNickname());
		}
		return stringList;
	}

	@Override
	public void run() {
		while (true) {
			ServerCommand cmd = getCommandFromQueue();
			sendToAll(cmd);
		}
	}
}

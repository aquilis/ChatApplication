package com.sirma.itt.javacourse.chat.serverSide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A mediator class that has methods for sending the certain message to all connected clients to the
 * server. Maintains a list of enqueued messages to be sent, and a list of all connected clients
 * updated regularly.
 */
public class Transmitter extends Thread {
	private final List<ClientWrapper> listClients = new ArrayList<ClientWrapper>();
	private final Queue<String> queueMessages = new LinkedList<String>();

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
	public List<ClientWrapper> getListClients() {
		return listClients;
	}

	/**
	 * Removes the given client from the transmitter's list. The client will no
	 * longer receive messages.
	 * 
	 * @param client
	 *            is the client to remove
	 */
	public void removeClient(ClientWrapper client) {
		listClients.remove(client);
	}

	/**
	 * Enqueue the given message to be later sent to all connencted clients.
	 * 
	 * @param msg
	 *            is the message to be sent to all
	 */
	public synchronized void sendMessage(String msg) {
		queueMessages.offer(msg);
		notify();
	}

	/**
	 * If there are enqueued messages in the queue, retrieve and remove the first one, otherwise,
	 * pause the thread until the queue fills.
	 * 
	 * @return the oldest message in the queue, if any
	 */
	private synchronized String getMessageFromQueue() {
		while (queueMessages.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return queueMessages.poll();
	}

	/**
	 * Sends the message to all connected clients.
	 * 
	 * @param msg
	 *            is the message to send
	 */
	private void sendToAll(String msg) {
		for (ClientWrapper client : listClients) {
			client.getSender().sendMessage(msg);
		}
	}

	@Override
	public void run() {
		while (true) {
			String message = getMessageFromQueue();
			sendToAll(message);
		}
	}
}

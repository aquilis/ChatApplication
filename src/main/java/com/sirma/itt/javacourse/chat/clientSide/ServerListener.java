package com.sirma.itt.javacourse.chat.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.sirma.itt.javacourse.chat.serverSide.serverCommands.ServerCommand;

/**
 * Listens for messages from the server and updates the client application's
 * GUI.
 */
public class ServerListener extends Thread {
	private Client.ClientGUI gui = null;
	private ObjectInputStream in = null;
	private Socket socket = null;
	private ServerSender sender = null;
	private boolean mustTerminate = false;

	/**
	 * Constructs the client listener thread.
	 * 
	 * @param socket
	 *            is the client socket to the server.
	 * @param sender
	 *            is the sender thread that has to be deactivated when the
	 *            server denies access
	 * @param gui
	 *            is the client GUI that the listener has to update when new
	 *            messages arrive
	 */
	public ServerListener(Socket socket, ServerSender sender,
			Client.ClientGUI gui) {
		this.socket = socket;
		this.sender = sender;
		this.gui = gui;
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	/**
	 * Gets the client GUI. Used by the server commands that have to update the
	 * interface.
	 * 
	 * @return the client GUI.
	 */
	public Client.ClientGUI getGui() {
		return gui;
	}

	/**
	 * sets the mustTerminate variable if the listener has to be stopped from
	 * outside.
	 * 
	 * @param mustTerminate
	 *            is the variable that stops the thread
	 */
	public void setMustTerminate(boolean mustTerminate) {
		this.mustTerminate = mustTerminate;
	}


	@Override
	public void run() {
		Object input = null;
		try {
			while ((input = in.readObject()) != null) {
				ServerCommand incomingCommand = (ServerCommand) input;
				incomingCommand.execute(this);
			}
		} catch (IOException e) {
			sender.deactivate();
			try {
				if (socket != null) {
					socket.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e1) {
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

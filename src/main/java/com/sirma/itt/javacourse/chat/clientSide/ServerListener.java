package com.sirma.itt.javacourse.chat.clientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Listens for messages from the server and updates the client application's GUI.
 */
public class ServerListener extends Thread {
	private Client.ClientGUI gui = null;
	private BufferedReader in = null;
	private Socket socket = null;
	private ServerSender sender = null;
	private boolean mustTerminate = false;

	/**
	 * Constructs the client listener thread.
	 * 
	 * @param socket
	 *            is the client socket to the server.
	 * @param sender
	 *            is the sender thread that has to be deactivated when the server denies access
	 * @param gui
	 *            is the client GUI that the listener has to update when new messages arrive
	 */
	public ServerListener(Socket socket, ServerSender sender, Client.ClientGUI gui) {
		this.socket = socket;
		this.sender = sender;
		this.gui = gui;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	/**
	 * Parses the commands coming from the server.
	 * 
	 * @param cmd
	 *            is the command to parse and execute
	 */
	private void parseCommand(String cmd) {
		if ("access denied".equals(cmd)) {
			gui.showError(
					"Please, make sure that your nickname is unique and doesn't have invalid characters",
					"Invalid nickname");
			mustTerminate = true;
		} else if ("access allowed".equals(cmd)) {
			gui.moveToMainForm();
		}
	}

	@Override
	public void run() {
		String input = "";
		try {
			while ((input = in.readLine()) != null) {
				if ("cmd".equals(input.substring(0, 3))) {
					parseCommand(input.substring(4));
					if (mustTerminate) {
						break;
					}
					continue;
				}
				gui.log(input);
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
		}
	}
}

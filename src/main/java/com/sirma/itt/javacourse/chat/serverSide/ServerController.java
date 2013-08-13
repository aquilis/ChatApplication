package com.sirma.itt.javacourse.chat.serverSide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sirma.itt.javacourse.chat.LogHandlersManager;

/**
 * The controller class that implements the MVC architecture. Mediates all
 * interactions between the front-end (ServerGUI) and the back-end (Server,
 * Client commands). The front and the back-end classes don't know about each
 * other. Both sides interact implicitly using this class.
 */
public class ServerController {
	// the view
	private ServerGUI gui = null;
	// the model
	private Server server = null;
	// logger
	private static final Logger LOGGER = Logger
			.getLogger(ServerController.class.getName());
	private final FileHandler fileHandler = LogHandlersManager
			.getServerHandler();

	/**
	 * Constructs the controller with a server GUI and a server class that
	 * contains the business logic.
	 * 
	 * @param gui
	 *            is the server GUI
	 * @param server
	 *            is the back-end server class
	 */
	public ServerController(ServerGUI gui, Server server) {
		this.gui = gui;
		this.server = server;
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(fileHandler);
		attachButtonsActionListeners();
		handleClients();
	}

	/**
	 * Using the server class functionality, handles the newly connecting
	 * clients attaching them listener and sender threads.
	 */
	private void handleClients() {
		while (true) {
			try {
				ClientWrapper lastClient = server.waitForClient();
				server.attachClientSender(lastClient);
				server.attachClientListener(lastClient,
						server.getTransmitter(), this);
			} catch (IOException e) {
				break;
			}
		}
	}

	/**
	 * Attaches action listeners to the GUI buttons.
	 */
	private void attachButtonsActionListeners() {
		ActionListener stopButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LOGGER.info("Connection stopped from server");
				log("Connection stopped from server");
				gui.deactivate();
				server.stopServer();
			}
		};
		gui.setStopButtonListener(stopButtonListener);
	}

	/**
	 * Logs the given message in the server GUI panel.
	 * 
	 * @param msg
	 *            is the message to log
	 */
	public void log(String msg) {
		gui.log(msg);
	}
}

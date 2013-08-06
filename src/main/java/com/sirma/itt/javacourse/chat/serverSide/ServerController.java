package com.sirma.itt.javacourse.chat.serverSide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		attachButtonsActionListeners();
	}

	/**
	 * Sets a server model object for this controller, if it can't be passed in
	 * the object's creation.
	 * 
	 * @param server
	 *            is the back-end server class
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * Attach action listeners to the GUI buttons.
	 */
	private void attachButtonsActionListeners(){
		ActionListener stopButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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

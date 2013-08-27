package com.sirma.itt.javacourse.chat;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import com.sirma.itt.javacourse.chat.clientSide.Client;
import com.sirma.itt.javacourse.chat.clientSide.ClientController;
import com.sirma.itt.javacourse.chat.clientSide.ClientGUI;
import com.sirma.itt.javacourse.chat.clientSide.ServerSender;
import com.sirma.itt.javacourse.chat.clientSide.clientCommands.MessageToServerCommand;

/**
 * Launches a certain number of automated clients that will send the given
 * amount of messages to the server each TIME_INTERVAL_MILLIS.
 */
public final class StressTester {
	private static final int MESSAGES_TO_SEND = 300;
	private static final int TIME_INTERVAL_MILLIS = 3000;
	private static final String LANGUAGE_FILE = "lang.properties";
	private static final String PROPERTIES_FILE = "clientConfig.properties";
	private static int port;
	private static String address;
	private static int currentTester = 0;

	/**
	 * Not designed to be instantiated.
	 */
	private StressTester() {
	}

	/**
	 * Loads the properties from the properties files and updates the client GUI
	 * and language.
	 */
	private static void loadProperties() {
		Properties prop = new Properties();
		Properties langProp = new Properties();
		try {
			prop.load(new FileInputStream(PROPERTIES_FILE));
			langProp.load(new FileInputStream(LANGUAGE_FILE));
		} catch (IOException e) {
			return;
		}
		port = Integer.parseInt(prop.getProperty("port"));
		address = prop.getProperty("address");
	}

	/**
	 * Starts just a sender thread without controller, listener, GUI and other
	 * components, to send the certain number of messages flooding the server.
	 */
	private static void startSenderThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String name = "Tester" + (++currentTester);
				Socket clientSocket = null;
				try {
					clientSocket = new Socket(address, port);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ServerSender sender = new ServerSender(clientSocket);
				for (int i = 0; i < MESSAGES_TO_SEND; i++) {
					sender.sendCommand(new MessageToServerCommand(name
							+ ": Message " + (i + 1), true));
					try {
						Thread.sleep(TIME_INTERVAL_MILLIS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Starts a full client module with the GUI and all of its components.
	 * Automatically sends messages to the server and waits for responses.
	 */
	private static void startGUITester() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Client client = null;
				ClientGUI gui = null;
				ClientController controller = null;
				try {
					client = new Client();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				gui = new ClientGUI();
				client.setNickname("tester" + (++currentTester));
				try {
					controller = new ClientController(gui, client);
					client.join(port, address, controller);
				} catch (NumberFormatException | IOException e1) {
				}
				for (int i = 0; i < MESSAGES_TO_SEND; i++) {
					if (gui != null) {
						if (gui.isActive()) {
							gui.deactivate();
						}
					}
					client.sendCommand(new MessageToServerCommand(client
							.formatMessage("Message #" + (i + 1)), false));
					try {
						Thread.sleep(TIME_INTERVAL_MILLIS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Launches the specified number of testers and starts the stress test.
	 * 
	 * @param numberOfTesters
	 *            is the amount of tester units to be created
	 * @param startFullClient
	 *            if set to true, real conditions will be simulated - Full
	 *            client classes with GUI windows will be initialized. If false,
	 *            just sender threads will be started to flood the server with
	 *            messages.
	 */
	private static void stressTest(int numberOfTesters, boolean startFullClient) {
		for (int i = 0; i < numberOfTesters; i++) {
			if (startFullClient) {
				startGUITester();
			} else {
				startSenderThread();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Entry point.
	 * 
	 * @param args
	 *            are the commands arguments
	 * @throws IOException
	 *             thrown by the client's 1constructor if the INET address can't
	 *             be instantiated.
	 */
	public static void main(String[] args) throws IOException {
		loadProperties();
		stressTest(50, false);
	}
}

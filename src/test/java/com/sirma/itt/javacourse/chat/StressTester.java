package com.sirma.itt.javacourse.chat;

import java.io.IOException;

import com.sirma.itt.javacourse.chat.clientSide.Client;
import com.sirma.itt.javacourse.chat.clientSide.ClientController;
import com.sirma.itt.javacourse.chat.clientSide.ClientGUI;
import com.sirma.itt.javacourse.chat.clientSide.clientCommands.MessageToServerCommand;

/**
 * Launches a certain number of automated clients that will send the given
 * amount of messages to the server each TIME_INTERVAL_MILLIS.
 */
public final class StressTester {
	private static final int MESSAGES_TO_SEND = 300;
	private static final int TIME_INTERVAL_MILLIS = 4000;
	private static int currentTester = 0;

	/**
	 * Not designed to be instantiated.
	 */
	private StressTester() {
	}

	/**
	 * Starts a new tester thread.
	 */
	private static void startTester() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Client client = null;
				ClientGUI gui = new ClientGUI();
				try {
					client = new Client();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ClientController controller = new ClientController(gui, client);
				client.setNickname("tester" + (++currentTester));
				try {
					client.join(
							Integer.parseInt(gui.getPortTextBox().getText()),
							gui.getAddressTextBox().getText(), controller);
				} catch (NumberFormatException | IOException e1) {
					e1.printStackTrace();
				}
				// the user shouldn't be able to send messages
				for (int i = 0; i < MESSAGES_TO_SEND; i++) {
					if (gui.isActive()) {
						gui.deactivate();
					}
					client.sendCommand(new MessageToServerCommand(client
							.formatMessage("Message #" + (i + 1))));
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
	 *            is the amount of tester threads to be created
	 */
	private static void stressTest(int numberOfTesters) {
		for (int i = 0; i < numberOfTesters; i++) {
			startTester();
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
		stressTest(5);
	}
}

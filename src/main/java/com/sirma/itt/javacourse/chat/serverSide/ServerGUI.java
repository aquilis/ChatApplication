package com.sirma.itt.javacourse.chat.serverSide;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The GUI part of the server application.
 */
public class ServerGUI extends JFrame {
	/**
	 * Comment for serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private JButton stopButton;
	private JScrollPane scrollPane = null;
	private JTextArea logBox = null;

	/**
	 * Constructs the interface.
	 */
	public ServerGUI() {
		setTitle("Server application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEADING, 6, 6));
		setSize(new Dimension(550, 250));
		setResizable(false);
		createScrollPane();
		createStopButton();
		this.getContentPane().add(scrollPane);
		this.getContentPane().add(stopButton);
		setVisible(true);
	}

	/**
	 * Logs the given message in the server GUI panel. Includes the current time
	 * in the log.
	 * 
	 * @param msg
	 *            is the message to log
	 */
	public void log(String msg) {
		logBox.append("\n["
				+ new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] "
				+ msg);
	}

	/**
	 * Deactivates all active GUI components.
	 */
	public void deactivate() {
		stopButton.setEnabled(false);
		logBox.setEnabled(false);
	}

	/**
	 * Creates the text label that will show the server activity.
	 */
	private void createScrollPane() {
		logBox = new JTextArea(5, 30);
		logBox.setBorder(BorderFactory.createLoweredBevelBorder());
		logBox.setEditable(false);
		logBox.append("Server activity log:");
		scrollPane = new JScrollPane(logBox);
		scrollPane.setPreferredSize(new Dimension(400, 200));
	}

	/**
	 * Sets an action listener for the server stop button.
	 * 
	 * @param listener
	 *            is the new listener to set
	 */
	public void setStopButtonListener(ActionListener listener) {
		this.stopButton.addActionListener(listener);
	}

	/**
	 * Creates the stop button that will stop the server.
	 */
	private void createStopButton() {
		stopButton = new JButton("Stop server");
		stopButton.setPreferredSize(new Dimension(100, 50));
	}
}
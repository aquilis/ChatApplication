package com.sirma.itt.javacourse.chat;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A formatter class used by the loggers to format the output logs.
 */
public class LogFormatter extends Formatter {
	private final String newLine = System.getProperty("line.separator");

	/**
	 * Formats the log message putting it the current date and time, the name of
	 * the logger, log level, and attached throwable (if any). {@inheritDoc}
	 */
	@Override
	public String format(LogRecord record) {
		return "[" + new Date() + "] " + record.getLoggerName() + ":"
				+ newLine + record.getLevel() + ": " + record.getMessage()
				+ (record.getThrown() == null ? "" : newLine
						+ record.getThrown()) + newLine;
	}
}

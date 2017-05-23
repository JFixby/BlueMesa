
package com.jfixby.bluemesa;

import com.jfixby.bluemesa.sqs.MessagesConsumer;
import com.jfixby.scarabei.api.log.MESSAGE_MARKER;
import com.jfixby.scarabei.red.log.AbstractLogger;
import com.jfixby.scarabei.red.log.SimpleLogger;

public class ScreenLogger extends AbstractLogger {
	MessagesConsumer ep;

	public ScreenLogger (final MessagesConsumer consumer) {
		this.ep = consumer;
	}

	final SimpleLogger simple = new SimpleLogger();

	@Override
	public void logLine (final MESSAGE_MARKER marker, final Object string) {
		this.simple.logLine(marker, string);
		this.ep.append(string + "", marker);
	}

	@Override
	public void logAppend (final MESSAGE_MARKER marker, final Object string) {
		this.simple.logAppend(marker, string);
		this.ep.append(string + "", marker);
	}

	@Override
	public String toString (final Object[] array) {
		return this.arrayToString(0, array);
	}

	@Override
	public void logLine (final MESSAGE_MARKER marker) {
		this.simple.logLine(marker);
		this.ep.append("", marker);
	}

	@Override
	public void logAppend (final MESSAGE_MARKER marker) {
		this.simple.logAppend(marker);
		this.ep.append("", marker);
	}

	@Override
	public String arrayToString (final int indent, final Object[] array) {
		return this.simple.arrayToString(indent, array);
	}
}

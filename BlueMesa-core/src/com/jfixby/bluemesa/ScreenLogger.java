
package com.jfixby.bluemesa;

import com.jfixby.scarabei.api.log.MESSAGE_MARKER;
import com.jfixby.scarabei.red.log.AbstractLogger;
import com.jfixby.scarabei.red.log.SimpleLogger;

public class ScreenLogger extends AbstractLogger {

	final SimpleLogger simple = new SimpleLogger();

	@Override
	public void logLine (final MESSAGE_MARKER marker, final Object string) {
		this.simple.logLine(marker, string);

// PrintStream stream = System.out;
// if (marker == MESSAGE_MARKER.NORMAL) {
// stream = System.out;
// }
// if (marker == MESSAGE_MARKER.ERROR) {
// stream = System.err;
// }
// stream.println(string);
	}

	@Override
	public void logAppend (final MESSAGE_MARKER marker, final Object string) {
		this.simple.logAppend(marker, string);
// final PrintStream stream = System.out;
// if (marker == MESSAGE_MARKER.NORMAL) {
// stream = System.out;
// }
// if (marker == MESSAGE_MARKER.ERROR) {
// stream = System.err;
// }
// stream.print(string);
	}

	@Override
	public String toString (final Object[] array) {
		return this.arrayToString(0, array);
	}

	@Override
	public void logLine (final MESSAGE_MARKER marker) {
		this.simple.logLine(marker);
// PrintStream stream = System.out;
// if (marker == MESSAGE_MARKER.NORMAL) {
// stream = System.out;
// }
// if (marker == MESSAGE_MARKER.ERROR) {
// stream = System.err;
// }
// stream.println();
	}

	@Override
	public void logAppend (final MESSAGE_MARKER marker) {
		this.simple.logAppend(marker);
// PrintStream stream = System.out;
// if (marker == MESSAGE_MARKER.NORMAL) {
// stream = System.out;
// }
// if (marker == MESSAGE_MARKER.ERROR) {
// stream = System.err;
// }
// stream.println();
	}

	@Override
	public String arrayToString (final int indent, final Object[] array) {
		return this.simple.arrayToString(indent, array);
	}
}

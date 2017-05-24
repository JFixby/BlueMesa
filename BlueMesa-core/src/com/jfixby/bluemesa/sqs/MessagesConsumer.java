
package com.jfixby.bluemesa.sqs;

import com.jfixby.scarabei.api.log.MESSAGE_MARKER;

public interface MessagesConsumer {

	void append (String messageText, MESSAGE_MARKER mode);

	boolean isReady ();

}

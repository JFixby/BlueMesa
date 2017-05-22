
package com.jfixby.bluemesa.sqs;

import com.jfixby.bluemesa.GasSensorMessage;

public interface MessageTransport {

	void send (GasSensorMessage message);

}

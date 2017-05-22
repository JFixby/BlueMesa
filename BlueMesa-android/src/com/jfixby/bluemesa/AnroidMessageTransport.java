
package com.jfixby.bluemesa;

import com.jfixby.bluemesa.sqs.MessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;

public class AnroidMessageTransport implements MessageTransport {

	public AnroidMessageTransport (final MessageTransportSpecs t_specs) {
	}

	@Override
	public void send (final GasSensorMessage message) {
		message.print();
	}

}


package com.jfixby.bluemesa;

import com.jfixby.bluemesa.sqs.MessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;
import com.jfixby.bluemesa.sqs.MessagesConsumer;

public class AnroidMessageTransport implements MessageTransport {

	private final MessagesConsumer ep;

	public AnroidMessageTransport (final MessageTransportSpecs t_specs, final MessagesConsumer ep) {
		this.ep = ep;
	}

	@Override
	public void send (final GasSensorMessage message) {
		message.print();
	}

}

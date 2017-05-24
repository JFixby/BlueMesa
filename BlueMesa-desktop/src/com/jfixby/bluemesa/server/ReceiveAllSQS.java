
package com.jfixby.bluemesa.server;

import com.jfixby.bluemesa.GasSensorMessage;
import com.jfixby.bluemesa.sqs.BlureMesaMessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.api.time.TimeStream;
import com.jfixby.scarabei.aws.api.sqs.SQS;
import com.jfixby.scarabei.aws.desktop.sqs.DesktopSQS;
import com.jfixby.scarabei.gson.GoogleGson;

public class ReceiveAllSQS {

	public static void main (final String[] args) {
		ScarabeiDesktop.deploy();
		Json.installComponent(new GoogleGson());
		SQS.installComponent(new DesktopSQS());
		final String deviceID = "98D331B2B6D3";
		final MessageTransportSpecs specs = new MessageTransportSpecs();
		final BlureMesaMessageTransport transport = new BlureMesaMessageTransport(specs, null);
		while (true) {
			try {
				final List<GasSensorMessage> list = transport.receive(deviceID);
				print(list);
			} catch (final Throwable e) {
				L.e(e);
				Sys.sleep(emergencySleep);
			} finally {
				Sys.sleep(1);
// L.d(" retry");
			}
// Sys.sleep(150);
		}

	}

	private static void print (final List<GasSensorMessage> list) {
		for (final GasSensorMessage m : list) {
			m.print();
		}
	}

	static private final long emergencySleep = TimeStream.MINUTE;

}

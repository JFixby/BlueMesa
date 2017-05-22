
package com.jfixby.bluemesa.server;

import com.jfixby.bluemesa.GasSensorMessage;
import com.jfixby.bluemesa.MessageTransport;
import com.jfixby.bluemesa.MessageTransportSpecs;
import com.jfixby.scarabei.amazon.aws.RedAWS;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.api.time.TimeStream;
import com.jfixby.scarabei.aws.api.AWS;
import com.jfixby.scarabei.gson.GoogleGson;

public class ReceiveAllSQS {

	public static void main (final String[] args) {
		ScarabeiDesktop.deploy();
		AWS.installComponent(new RedAWS());
		Json.installComponent(new GoogleGson());

		final String deviceID = "98D331B2B6D3";

		final MessageTransportSpecs specs = new MessageTransportSpecs();
		specs.deviceID = deviceID;
		final MessageTransport transport = new MessageTransport(specs);
		while (true) {
			try {
				final List<GasSensorMessage> list = transport.receive();
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


package com.jfixby.bluemesa.server;

import com.jfixby.scarabei.amazon.aws.RedAWS;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.aws.api.AWS;
import com.jfixby.scarabei.gson.GoogleGson;

public class ReceiveAllSQS {

	public static void main (final String[] args) {
		ScarabeiDesktop.deploy();
		AWS.installComponent(new RedAWS());
		Json.installComponent(new GoogleGson());
	}

}


package com.jfixby.bluemesa;

import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;

public class GasSensorMessage {

	public long globalCounter;
	public DataST data;

	public void print () {
		L.d("---GasSensorMessage[" + this.globalCounter + "]-----------");
		L.d("data", Json.serializeToString(this.data));
		L.d();
	}

}

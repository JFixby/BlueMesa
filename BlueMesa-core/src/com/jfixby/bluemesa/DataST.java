
package com.jfixby.bluemesa;

import com.jfixby.scarabei.api.log.L;

public class DataST {
	final float[] R_x = new float[8];
	final float[] H_r = new float[8];
	final float[] V_h = new float[8];
	final byte[] heater_setting = new byte[8];
	byte[] reference_setting = new byte[8];
	float temp_board;
	float temp;
	float RH;
	int[] heater_state = new int[8];

	public void print () {
		L.d("R_x", this.R_x);
		L.d("H_r", this.H_r);
		L.d("V_h", this.V_h);
		L.d("heater_setting", this.heater_setting);
		L.d("reference_setting", this.reference_setting);
		L.d("temp_board", this.temp_board);
		L.d("temp", this.temp);
		L.d("heater_state", this.heater_state);
		L.d("RH", this.RH);

	};
}

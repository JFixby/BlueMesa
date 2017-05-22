
package com.jfixby.bluemesa;

public class DataST {
	final float[] R_x = new float[8];
	final float[] H_r = new float[8];
	final float[] V_h = new float[8];
	final byte[] heater_setting = new byte[8];
	byte[] reference_setting = new byte[8];
	float temp_board;
	float temp;
	float RH;
	int[] heater_state = new int[8];;
}

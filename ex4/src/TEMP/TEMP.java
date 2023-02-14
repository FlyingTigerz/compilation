/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import java.util.HashMap;
import java.util.Map;

/*******************/

public class TEMP
{
	private final int serial;
	private final boolean serialized;
	private final boolean immediate;
	private final String name;
	private final int val;
	public static Map<TEMP, Integer> tempToReg = new HashMap<>();  /* TEMP serial number to MIPS register (t0,...,t9) */
	
	public TEMP(int serial) {
		this.serialized = true;
		this.immediate = false;
		this.name = "$t";
		this.serial = serial;
		this.val = 0;
	}

	public TEMP(String label) {
		this.serialized = false;
		this.immediate = false;
		this.name = label;
		this.serial = 0;
		this.val = 0;
	}

	// TODO - make TEMP, IMMEDIATE, and LABEL into distinct classes, extending REG
	public TEMP(int val, boolean immediate) {
		this.serialized = false;
		this.immediate = immediate;
		this.val = val;
		this.name = null;
		this.serial = 0;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}

	public String toString() {
		if(this.immediate){
			return "" + this.val;
		} else if(tempToReg.containsKey(this)) {
			return "$t" + tempToReg.get(this);
		} else if (this.serialized) {
			return this.name + String.valueOf(serial);
		}
		return this.name;
	}
}

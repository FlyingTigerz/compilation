/***********/
/* PACKAGE */
/***********/

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRCommandPrintString extends IRcommand {
	TEMP t;

	public IRCommandPrintString(TEMP t) {
		this.t = t;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().printString(t);
	}
}

/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Exit extends IRcommand
{
	public IRcommand_Exit()
	{
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().exit();
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("exit"); }
}

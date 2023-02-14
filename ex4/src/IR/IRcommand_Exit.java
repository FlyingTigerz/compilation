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

import MIPS.MIPSGenerator;
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
		MIPSGenerator.getInstance().exit();
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("exit"); }
}

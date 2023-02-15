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
import TEMP.*;

public class IRcommand_Print_String extends IRcommand
{
	TEMP str;
	public IRcommand_Print_String(TEMP str)
	{
		this.str = str;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		System.out.println(str + "KIKIKIKK");
		//MIPSGenerator.getInstance().print_string(str);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("printString(%s)" + str.toString()); }
}

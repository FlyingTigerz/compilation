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

public class IRcommand_Jump_And_Link extends IRcommand
{
	String label_name;

	public IRcommand_Jump_And_Link(String label_name)
	{
		this.label_name = label_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().jal(label_name);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("jal " + label_name); }
}

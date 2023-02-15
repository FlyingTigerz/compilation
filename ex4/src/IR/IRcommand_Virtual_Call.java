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

public class IRcommand_Virtual_Call extends IRcommand
{
	/* TODO - copy-pasted from another IRcommand, adjustments required */
	String label_name;

	public IRcommand_Virtual_Call(String label_name)
	{
		this.label_name = label_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().jump(label_name);
	}

	/* TODO
	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(); }
	*/
}

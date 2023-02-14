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
		sir_MIPS_a_lot.getInstance().jump(label_name);
	}

	/* TODO
	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(); }
	*/
}

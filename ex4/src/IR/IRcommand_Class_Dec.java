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
/* TODO - copy-pasted from another IRcommand, adjustments required */

public class IRcommand_Class_Dec extends IRcommand
{
	String var_name;

	public IRcommand_Class_Dec(String var_name)
	{
		this.var_name = var_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocate(var_name);
	}

}

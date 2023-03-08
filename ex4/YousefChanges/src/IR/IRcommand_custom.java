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
import TEMP.*;
import MIPS.*;

import java.util.HashSet;
import java.util.Set;

import static MIPS.sir_MIPS_a_lot.WORD_SIZE;

public class IRcommand_custom extends IRcommand
{
	String msg;
	
	public IRcommand_custom(String msg)
	{
		this.msg = msg;
	}


	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().custom(msg);
	}

	public void printMe() { }
}

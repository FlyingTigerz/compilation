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

import static MIPS.MIPSGenerator.WORD_SIZE;

public class IRcommand_Custom extends IRcommand
{
	TEMP dst;
	String msg;
	
	public IRcommand_Custom(String msg)
	{
		this.msg = msg;
	}

	public TEMP modifiedReg() { return dst;}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().custom(msg);
	}

	public void printMe() {}
}

/***********/
/* PACKAGE */
/***********/
package IR;
import TEMP.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import MIPS.sir_MIPS_a_lot;

import java.util.HashSet;
import java.util.Set;


public class IRcommand_Return extends IRcommand
{
	TEMP val;

	public IRcommand_Return(TEMP val)
	{
		this.val = val;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(val);
		return used_regs;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().ret(val);
	}

	public void printMe() {IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("return " + (val == null ? "" : val)); }
}

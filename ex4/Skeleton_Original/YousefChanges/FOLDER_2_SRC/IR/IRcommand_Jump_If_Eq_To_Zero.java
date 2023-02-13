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

public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand
{
	TEMP t;
	String label_name;
	
	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label_name)
	{
		this.t          = t;
		this.label_name = label_name;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(t);
		return used_regs;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().beqz(t,label_name);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("jifz " + t + ", " + label_name); }
}

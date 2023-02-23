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

import AST.AST_VAR;
import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

import java.util.HashSet;
import java.util.Set;

public class IRcommand_Store_Var extends IRcommand
{
	String dst_name;
	TEMP src;
	int offset;

	public IRcommand_Store_Var(String dst_name, TEMP src)
	{
		this.src = src;
		this.dst_name = dst_name;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(src);
		return used_regs;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/

	public void MIPSme()
	{
		//sir_MIPS_a_lot.getInstance().store(dst_name, src, offset);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(dst_name + " = " + src); }
}

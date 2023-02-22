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
import TEMP.TEMP;

import java.util.HashSet;
import java.util.Set;

public class IRcommand_Binop_Div_Integers extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;

	public IRcommand_Binop_Div_Integers(TEMP dst, TEMP t1, TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(t1);
		used_regs.add(t2);
		return used_regs;
	}
	public TEMP modifiedReg() { return dst;}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// check for division by zero
		MIPSGenerator.getInstance().beqz(t2, IR.exitOnZeroDiv);
		MIPSGenerator.getInstance().div(dst,t1,t2);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(dst + " = div " + t1 + ", " + t2); }
}
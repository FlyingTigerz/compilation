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
import TEMP.TEMP;

import java.util.HashSet;
import java.util.Set;

public class IRcommand_Move extends IRcommand
{
	public TEMP dst;
	public TEMP src;

	public IRcommand_Move(TEMP dst, TEMP src)
	{
		this.dst = dst;
		this.src = src;
	}
	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(src);
		return used_regs;
	}
	public TEMP modifiedReg() { return dst;}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().move(dst, src);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(dst + " = move " + src ); }
}

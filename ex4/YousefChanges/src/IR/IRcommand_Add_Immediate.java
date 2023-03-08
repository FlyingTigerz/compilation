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

public class IRcommand_Add_Immediate extends IRcommand
{
	public TEMP dst;
	public TEMP src;
	public int immediate;

	public IRcommand_Add_Immediate(TEMP dst, TEMP src, int immediate)
	{
		this.dst = dst;
		this.src = src;
		this.immediate = immediate;
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
		sir_MIPS_a_lot.getInstance().addi(dst, src, immediate);
	}
	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(dst + " = addi " + src + ", " + immediate); }
}

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

public class IRcommand_Store_Temp extends IRcommand
{
	TEMP dst;
	TEMP src;
	int offset;
	
	public IRcommand_Store_Temp(TEMP src, TEMP dst, int offset)
	{
		this.src = src;
		this.dst = dst;
		this.offset = offset;
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
		sir_MIPS_a_lot.getInstance().store(src, dst, offset);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("" + offset * WORD_SIZE + "(" +dst + ") = " + src); }
}

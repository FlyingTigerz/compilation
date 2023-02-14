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

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	TEMP src;
	int offset;
	
	public IRcommand_Load(TEMP dst, TEMP src, int offset)
	{
		this.dst = dst;
		this.src = src;
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
		sir_MIPS_a_lot.getInstance().load(dst, src, offset);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(dst + " = " + offset * WORD_SIZE + "(" + src + ")"); }
}

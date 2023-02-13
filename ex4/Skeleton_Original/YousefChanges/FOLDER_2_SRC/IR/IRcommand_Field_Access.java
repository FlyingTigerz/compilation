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
/* TODO - copy-pasted from another IRcommand, adjustments required */

public class IRcommand_Field_Access extends IRcommand
{
	TEMP dst;
	TEMP base;
	String fieldName;

	public IRcommand_Field_Access(TEMP dst, TEMP base, String fieldName)
	{
		this.dst      = dst;
		this.base      = base;
		this.fieldName = fieldName;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(base);
		return used_regs;
	}
	public TEMP modifiedReg() { return dst;}


	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// TODO: implement me!
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(dst + " = field_access " + base + ", " + fieldName); }
}

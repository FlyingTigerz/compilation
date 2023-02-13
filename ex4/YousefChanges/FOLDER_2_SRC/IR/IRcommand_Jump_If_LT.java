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

public class IRcommand_Jump_If_LT extends IRcommand
{
	TEMP t1, t2;
	int immed;
	String label_name;

	public IRcommand_Jump_If_LT(TEMP t1, TEMP t2, String label_name)
	{
		this.t1 = t1;
		this.t2 = t2;
		this.label_name = label_name;
	}
	public IRcommand_Jump_If_LT(TEMP t1, int immed, String label_name)
	{
		this.t1 = t1;
		this.immed = immed;
		this.label_name = label_name;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(t1);
		if (t2!=null) used_regs.add(t2);
		return used_regs;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (t2 != null) sir_MIPS_a_lot.getInstance().blt(t1, t2, label_name);
		else sir_MIPS_a_lot.getInstance().blt(t1, immed, label_name);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("jilt " + t1 + ", " + (t2 != null ? t2 : immed) + ", " + label_name); }
}

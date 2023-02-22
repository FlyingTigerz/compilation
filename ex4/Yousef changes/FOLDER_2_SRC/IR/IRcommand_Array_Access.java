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

public class IRcommand_Array_Access extends IRcommand
{
	TEMP dst;
	TEMP pointer;
	TEMP offset;

	public IRcommand_Array_Access(TEMP dst, TEMP pointer, TEMP offset)
	{
		this.dst = dst;
		this.pointer = pointer;
		this.offset = offset;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(pointer);
		used_regs.add(offset);
		return used_regs;
	}
	public TEMP modifiedReg() { return dst;}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		TEMP s0 = IR.getInstance().s0;
		//$t2 = offset, $t1 = pointer, $t0 = dst
		// bltz $t2, abort (abort if offset < 0)
		sir_MIPS_a_lot.getInstance().bltz(offset, IR.exitOnAccessViolation);
		// lw $s0, 0($t1)
		sir_MIPS_a_lot.getInstance().load(s0, pointer, 0);
		// bge $t2, $s0, abort (abort if offset >= len)
		sir_MIPS_a_lot.getInstance().bge(offset, s0, IR.exitOnAccessViolation);
		// move $s0, $t2
		sir_MIPS_a_lot.getInstance().move(s0, offset);
		// add $s0, $s0, 1
		sir_MIPS_a_lot.getInstance().addi(s0, s0, 1);
		// mul $s0, $s0, 4
		sir_MIPS_a_lot.getInstance().mul(s0, s0, IR.getInstance().wordSizeTemp);
		// add $s0, $t1, $s0
		sir_MIPS_a_lot.getInstance().add(s0, pointer, s0);
		// lw $t0, 0($s0)
		sir_MIPS_a_lot.getInstance().load(dst, s0, 0);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(dst + " = array_access " + pointer + "[ " + offset + " ]"); }
}

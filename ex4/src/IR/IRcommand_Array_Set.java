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
/* TODO - copy-pasted from another IRcommand, adjustments required */

public class IRcommand_Array_Set extends IRcommand
{
	TEMP pointer;
	TEMP offset;
	TEMP src;

	public IRcommand_Array_Set(TEMP pointer, TEMP offset, TEMP src)
	{
		this.src = src;
		this.pointer = pointer;
		this.offset = offset;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(pointer);
		used_regs.add(offset);
		used_regs.add(src);
		return used_regs;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		TEMP s0 = IR.getInstance().s0;
		//$t2 = offset, $t1 = pointer, $t0 = dst
		// bltz $t2, abort (abort if offset < 0)
		MIPSGenerator.getInstance().bltz(offset, IR.exitOnAccessViolation);
		// lw $s0, 0($t1)
		MIPSGenerator.getInstance().load(s0, pointer, 0);
		// bge $t2, $s0, abort (abort if offset >= len)
		MIPSGenerator.getInstance().bge(offset, s0, IR.exitOnAccessViolation);
		// move $s0, $t2
		MIPSGenerator.getInstance().move(s0, offset);
		// add $s0, $s0, 1
		MIPSGenerator.getInstance().addi(s0, s0, 1);
		// mul $s0, $s0, 4
		MIPSGenerator.getInstance().mul(s0, s0, IR.getInstance().wordSizeTemp);
		// add $s0, $t1, $s0
		MIPSGenerator.getInstance().add(s0, pointer, s0);
		// lw $t0, 0($s0)
		MIPSGenerator.getInstance().store(src, s0, 0);	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln("array_set " + pointer + ", " + offset + ", " + src); }
}

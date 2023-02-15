/***********/
/* PACKAGE */
/***********/
package IR;
import TEMP.*;
import TYPES.*;
/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import MIPS.MIPSGenerator;

import java.util.HashSet;
import java.util.Set;
/* TODO - copy-pasted from another IRcommand, adjustments required */

public class IRcommand_New_Array extends IRcommand
{
	TEMP pointer;
	TEMP len;

	public IRcommand_New_Array(TEMP pointer,  TEMP len)
	{
		this.pointer = pointer;
		this.len = len;
	}

	public Set<TEMP> usedRegs() {
		Set<TEMP> used_regs = new HashSet<TEMP>();
		used_regs.add(len);
		return used_regs;
	}
	public TEMP modifiedReg() { return pointer;}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		//TODO: implement MIPSme

		// li $v0, 9
		MIPSGenerator.getInstance().li(IR.getInstance().v0, 9);
		// move $a0, len
		MIPSGenerator.getInstance().move(IR.getInstance().a0, len);
		// move $a0, len
		MIPSGenerator.getInstance().move(IR.getInstance().s0, len);
		// addi $a0, $a0, 1
		MIPSGenerator.getInstance().addi(IR.getInstance().a0, IR.getInstance().a0, 1);
		// mul $a0, $a0, 4
		MIPSGenerator.getInstance().mul(IR.getInstance().a0, IR.getInstance().a0, IR.getInstance().wordSizeTemp);
		// syscall
		MIPSGenerator.getInstance().syscall();
		// move pointer, $v0
		MIPSGenerator.getInstance().move(pointer, IR.getInstance().v0);
		// sw len, 0(pointer)
		MIPSGenerator.getInstance().store(IR.getInstance().s0, pointer, 0);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(pointer + " = new_array " + len); }
}

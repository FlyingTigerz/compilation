/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;

public class IR_general_jump_command extends IRcommand {
	String label;
	TEMP addr;
	boolean isJR;
	
	public IR_general_jump_command(){
		System.out.println("IN IR_general_jump_command");

		this.isJR = true;
	}

	public IR_general_jump_command(String label) {
		System.out.println("IN IR_general_jump_command with null addr");
		if (label == null)
			System.out.println("ERROR : label is null");

		this.label = label;
		this.addr = null;
		this.isJR = false;
    }
    
	public IR_general_jump_command(TEMP addr) {
		System.out.println("IN IR_general_jump_command with ADDER >>>> ");
		if(addr ==null)
			System.out.println("ERROR : adder is null");
		this.label = null;
		this.addr = addr;
		this.isJR = false;
    }
    
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		if (isJR)
			MIPSGenerator.getInstance().jr();
		else{
			if (label != null) {
				MIPSGenerator.getInstance().jal(label);
			}
			else {
				MIPSGenerator.getInstance().jal(addr);
			}
		}
	}
}

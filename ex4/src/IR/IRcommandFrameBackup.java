/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.MIPSGenerator;

public class IRcommandFrameBackup extends IRcommand {
	int numberOfParams;

	public static final int NUM_REG = 8;
	
	public IRcommandFrameBackup(int numberOfParams) {
		this.numberOfParams = numberOfParams;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
    	int i = 4 * (1 + this.numberOfParams); // offset of ra
    	String cmd = String.format("sw $ra,%d($sp)", i);
    	MIPSGenerator.getInstance().runCmd(cmd);
    	i += 4;
    	for(int j = 0; j < NUM_REG; j++) {
        	cmd = String.format("sw $t%d,%d($sp)", j, i);
			MIPSGenerator.getInstance().runCmd(cmd);
    		i += 4;
    	}
	}
}

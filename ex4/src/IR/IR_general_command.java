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
import MIPS.*;
import TEMP.TEMP;

public class IR_general_command extends IRcommand {
	String cmd;
	Integer tmp1;
	boolean isDef;
    
	public IR_general_command(String cmd, TEMP tmp, boolean isDef) {
		if (tmp != null){
			this.cmd = cmd;
			this.tmp1 = tmp.getSerialNumber();
			this.isDef = isDef;
		}
		else
			this.cmd = cmd;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
        
		if(tmp1 == null) {
			MIPSGenerator.getInstance().runCmd(cmd);
		}
		else {
			MIPSGenerator.getInstance().runCmd(cmd, tmp1, isDef);
		}
	}
}

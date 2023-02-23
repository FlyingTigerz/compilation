/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import TEMP.TEMP;

import java.util.HashSet;
import java.util.Set;

/*******************/

public abstract class IRcommand
{
	/*****************/
	/* Label Factory */
	/*****************/
	protected static int label_counter=0;
	public    static String getFreshLabel(String msg)
	{
		return String.format("Label_%d_%s",label_counter++,msg);
	}
	// Used for liveliness analysis
	public Set<TEMP> regIn = new HashSet<>();
	public Set<TEMP> regOut = new HashSet<>();
	public Set<TEMP> usedRegs() {return new HashSet<TEMP>();}
	public TEMP modifiedReg() {return null;}

	/***************/
	/* MIPS me !!! */
	/***************/
	public abstract void MIPSme();


	public void printMe() { System.out.println(this.getClass().getSimpleName()); }

}

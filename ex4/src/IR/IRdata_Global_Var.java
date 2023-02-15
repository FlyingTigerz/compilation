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

public class IRdata_Global_Var extends IRdata
{
	public TEMP label;
	public String word;

	public IRdata_Global_Var(TEMP label, String word)
	{
		this.label = label;
		this.word = word;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeGlobalVariable(label, word);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(label.toString() + " : " + word ); }
}

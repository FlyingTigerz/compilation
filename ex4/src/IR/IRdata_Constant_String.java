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

public class IRdata_Constant_String extends IRdata
{
	public TEMP label;
	public String str;

	public IRdata_Constant_String(TEMP label, String str)
	{
		this.label = label;
		this.str = str;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeString(label, str);
	}

	public void printMe() { IR.getInstance().fileNewLine(); IR.getInstance().filePrintln(label.toString() + " : " + str ); }
}

/***********/
/* PACKAGE */
/***********/
package IR;
import TEMP.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.TEMP;
import MIPS.MIPSGenerator;

public class IRcommand_Binop_Add_Integers extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;
	
	public IRcommand_Binop_Add_Integers(TEMP dst,TEMP t1,TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(){
		MIPSGenerator.getInstance().add(dst, t1, t2);

		TEMP intMin = TEMP_FACTORY.getInstance().getFreshTEMP();
		MIPSGenerator.getInstance().li(intMin, -32767);
		String label_underflow = getFreshLabel("UNDERFLOW");
		MIPSGenerator.getInstance().blt(dst, intMin, label_underflow);

		TEMP intMax = TEMP_FACTORY.getInstance().getFreshTEMP();
		MIPSGenerator.getInstance().li(intMax, 32767);
		String label_overflow = getFreshLabel("OVERFLOW");
		MIPSGenerator.getInstance().blt(intMax,dst, label_overflow);

		String label_end = getFreshLabel("PLUS_END");
		MIPSGenerator.getInstance().jump(label_end);

		MIPSGenerator.getInstance().label(label_underflow);
		MIPSGenerator.getInstance().li(dst, -32768);
		MIPSGenerator.getInstance().jump(label_end);

		MIPSGenerator.getInstance().label(label_overflow);
		MIPSGenerator.getInstance().li(dst, 32767);

		MIPSGenerator.getInstance().label(label_end);
	}
}

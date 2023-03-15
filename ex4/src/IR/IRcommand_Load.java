/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/
/*
What This IR Command Do ?
Given context laod the src Temp of this context into dest temp

*/
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;
import CONTEXT.*;

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	TEMP src;
	int offset;
	Context context;
	boolean loadVarAddress;

	public IRcommand_Load(TEMP dst, TEMP src) {
		this(dst, src, 0, null, false);
	}

	public IRcommand_Load(TEMP dst, TEMP src, int offset, Context cntx, boolean loadVar) {
		if (cntx == null)
			System.out.println("NULL");
		this.dst = dst;
		this.src = src;
		this.offset = offset;
		this.context = cntx;
		this.loadVarAddress = loadVar;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {

		if (loadVarAddress)
			MIPSGenerator.getInstance().loadAddressVar(dst, context, src);
		else
			MIPSGenerator.getInstance().load(dst, src, offset);
	}
}

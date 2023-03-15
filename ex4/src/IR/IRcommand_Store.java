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
import TEMP.*;
import MIPS.*;

public class IRcommand_Store extends IRcommand {

	TEMP dst;
	TEMP src;
	int offset;
	boolean withOffset;


	public IRcommand_Store(TEMP dst, TEMP src, int offset, boolean wOffset){
		this.dst = dst;
		this.src = src;
		this.offset = offset;
		this.withOffset = wOffset;
	}

	public IRcommand_Store(TEMP dst, int offset) {
		this(dst, null, offset, true);
	}

	public IRcommand_Store(TEMP dst, TEMP src) {
		this(dst, src, -1, false);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		if (this.withOffset)
			MIPSGenerator.getInstance().storeToFpAddressWithOffset(dst, offset);
		else
			MIPSGenerator.getInstance().store(dst,src);
	}
}
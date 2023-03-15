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
import TEMP.*;

public class IRcommand_Binop_EQ_String extends IRcommand  {
		TEMP dst;
		TEMP str1;
		TEMP str2;
		static MIPSGenerator Mips_instance =  MIPSGenerator.getInstance();

	public IRcommand_Binop_EQ_String(TEMP dst, TEMP str1, TEMP str2) {
		this.dst = dst;
		this.str1 = str1;
		this.str2 = str2;
  }


	private static TEMP getStringLength(TEMP str) {
		TEMP strLen = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP strPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP charTEMP = TEMP_FACTORY.getInstance().getFreshTEMP();

		String loopLabel = IRcommand.getFreshLabel("str_len_loop");
		String loopEnd = IRcommand.getFreshLabel("str_len_loop_end");

		Mips_instance.li(strLen, 0);
		Mips_instance.addi(strPointer, str, 0);

		Mips_instance.label(loopLabel);

		Mips_instance.load_byte(charTEMP, strPointer);

		Mips_instance.beqz(charTEMP, loopEnd);

		Mips_instance.add_plus_plus(strLen);

		Mips_instance.add_plus_plus(strPointer);

		Mips_instance.jump(loopLabel);

		Mips_instance.label(loopEnd);

		return strLen;
	}


	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {

		TEMP char1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP char2 = TEMP_FACTORY.getInstance().getFreshTEMP();

		String loopStart = getFreshLabel("str_comparison_loop");
		String notEq = getFreshLabel("str_comparison_not_equal");
		String loopEnd  = getFreshLabel("str_comparison_end");
		Mips_instance.label(loopStart);

		Mips_instance.load_byte(char1, str1);
		Mips_instance.load_byte(char2, str2);

		Mips_instance.add_plus_plus(str1);
		Mips_instance.add_plus_plus(str2);

		Mips_instance.bne(char1, char2, notEq);

		Mips_instance.bnz(char2, loopStart);

		Mips_instance.li(dst, 1);
		Mips_instance.jump(loopEnd);

		Mips_instance.label(notEq);
		Mips_instance.li(dst, 0);

		Mips_instance.label(loopEnd);

	}

}

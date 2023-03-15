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
import TEMP.*;

public class IRcommand_Binop_Concat_String extends IRcommand  {
		TEMP dst;
		TEMP str1;
		TEMP str2;
		static MIPSGenerator Mips_instance =  MIPSGenerator.getInstance();

	public IRcommand_Binop_Concat_String(TEMP dst, TEMP str1, TEMP str2) {
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

		TEMP newStrPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP charTEMP = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP newStr = TEMP_FACTORY.getInstance().getFreshTEMP();

		TEMP len1 = getStringLength(str1);
		TEMP len2 = getStringLength(str2);

		Mips_instance.add(len1, len1, len2);
		Mips_instance.add_plus_plus(len1);

		Mips_instance.malloc(dst, len1, false);
		Mips_instance.addi(newStrPointer, dst, 0);

		String str1Loop = IRcommand.getFreshLabel("str1_while");
		String str2Loop = IRcommand.getFreshLabel("str2_while");
		String endLoop = IRcommand.getFreshLabel("str_concat_end");

		Mips_instance.label(str1Loop);
		Mips_instance.load_byte(charTEMP, str1);

		Mips_instance.getInstance().beqz(charTEMP, str2Loop);

		Mips_instance.store_byte(newStrPointer, charTEMP);

		Mips_instance.add_plus_plus(newStrPointer);

		Mips_instance.add_plus_plus(str1);


		Mips_instance.jump(str1Loop);

		Mips_instance.label(str2Loop);

		Mips_instance.load_byte(charTEMP, str2);
		Mips_instance.store_byte(newStrPointer, charTEMP);
		Mips_instance.add_plus_plus(str2);
		Mips_instance.add_plus_plus(newStrPointer);

		Mips_instance.beqz(charTEMP, endLoop);
		Mips_instance.jump(str2Loop);

		Mips_instance.label(endLoop);

	}

}

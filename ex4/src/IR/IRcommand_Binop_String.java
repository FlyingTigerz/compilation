/***********/
/* PACKAGE */
/***********/

/*******************/
/* GENERAL IMPORTS */
/*******************/
package IR;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_Binop_String extends IRcommand  {
		TEMP dst;
		TEMP str1;
		TEMP str2;
		boolean isAdd;
		static MIPSGenerator sirMips_instance =  MIPSGenerator.getInstance();

	public IRcommand_Binop_String(TEMP dst, TEMP str1, TEMP str2, boolean isAdd) {
		this.dst = dst;
		this.str1 = str1;
		this.str2 = str2;
		this.isAdd = isAdd;
  }


	private static TEMP getStringLength(TEMP str) {
		TEMP strLen = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP strPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP charTEMP = TEMP_FACTORY.getInstance().getFreshTEMP();

		String loopLabel = IRcommand.getFreshLabel("str_len_loop");
		String loopEnd = IRcommand.getFreshLabel("str_len_loop_end");

		sirMips_instance.li(strLen, 0);
		sirMips_instance.addi(strPointer, str, 0);

		sirMips_instance.label(loopLabel);

		sirMips_instance.load_byte(charTEMP, strPointer);

		sirMips_instance.beqz(charTEMP, loopEnd);

		sirMips_instance.add_plus_plus(strLen);

		sirMips_instance.add_plus_plus(strPointer);

		sirMips_instance.jump(loopLabel);

		sirMips_instance.label(loopEnd);

		return strLen;
	}


	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		if (isAdd){
			TEMP newStrPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
			TEMP charTEMP = TEMP_FACTORY.getInstance().getFreshTEMP();
			TEMP newStr = TEMP_FACTORY.getInstance().getFreshTEMP();

			TEMP len1 = getStringLength(str1);
			TEMP len2 = getStringLength(str2);

			sirMips_instance.add(len1, len1, len2);
			sirMips_instance.add_plus_plus(len1);

			sirMips_instance.malloc(dst, len1, false);
			sirMips_instance.addi(newStrPointer, dst, 0);

			String str1Loop = IRcommand.getFreshLabel("str1_while");
			String str2Loop = IRcommand.getFreshLabel("str2_while");
			String endLoop = IRcommand.getFreshLabel("str_concat_end");

			sirMips_instance.label(str1Loop);
			sirMips_instance.load_byte(charTEMP, str1);

			MIPSGenerator.getInstance().beqz(charTEMP, str2Loop);

			sirMips_instance.store_byte(newStrPointer, charTEMP);
			
			sirMips_instance.add_plus_plus(newStrPointer);

			sirMips_instance.add_plus_plus(str1);


			sirMips_instance.jump(str1Loop);

			sirMips_instance.label(str2Loop);

			sirMips_instance.load_byte(charTEMP, str2);
			sirMips_instance.store_byte(newStrPointer, charTEMP);
			sirMips_instance.add_plus_plus(str2);
			sirMips_instance.add_plus_plus(newStrPointer);

			sirMips_instance.beqz(charTEMP, endLoop);
			sirMips_instance.jump(str2Loop);

			sirMips_instance.label(endLoop);
		}
		
		else{
			
	        TEMP char1 = TEMP_FACTORY.getInstance().getFreshTEMP();
	        TEMP char2 = TEMP_FACTORY.getInstance().getFreshTEMP();

	        String loopStart = getFreshLabel("str_comparison_loop");
	        String notEq = getFreshLabel("str_comparison_not_equal");
	        String loopEnd  = getFreshLabel("str_comparison_end");
	        sirMips_instance.label(loopStart);

	        sirMips_instance.load_byte(char1, str1);
	        sirMips_instance.load_byte(char2, str2);

	        sirMips_instance.add_plus_plus(str1);
	        sirMips_instance.add_plus_plus(str2);

	        sirMips_instance.bne(char1, char2, notEq);

	        sirMips_instance.bnz(char2, loopStart);

	        sirMips_instance.li(dst, 0);
	        sirMips_instance.jump(loopEnd);

	        sirMips_instance.label(notEq);
	        sirMips_instance.li(dst, 1);

	        sirMips_instance.label(loopEnd);
    	}

	}

}

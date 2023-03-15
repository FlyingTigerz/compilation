package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

import java.util.ArrayList;
import java.util.List;

public class AST_EXP_STRING extends AST_EXP
{
	public String str;
	public int lineNumber;
	private static List<String> strings = new ArrayList<>();
	public String str_label;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(String str, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("in exp string constructure%s\n", str);
		this.str = str;
		this.lineNumber=lineNumber;
	}


	/******************************************************/
	/* The printing message for a STRING EXP AST node */
	/******************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST STRING EXP */
		/*******************************/
		System.out.format("AST NODE STRING( %s )\n", str);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STRING\n%s", str.replace('"','\'')));
	}

	public TYPE SemantMe() throws RuntimeException{

		if (this.str.charAt(0) == '"' && this.str.charAt(this.str.length()-1) == '"') {
			this.str = this.str.substring(1, this.str.length()-1);
		}
		this.str_label = getStringLabel(strings.size());
		strings.add(this.str);

		return TYPE_STRING.getInstance();
	}

	public static List<String> allStrings() {
		return strings;
	}

	public static String getStringLabel(int i) {
		return String.format("string_%d", i);
	}

	public boolean isConst() {
		return true;
	}

	public TEMP IRme() {
		System.out.println("IRME IN AST_EXP_STRING");

		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IR_load_address(t, this.str_label));
		return t;
	}
}

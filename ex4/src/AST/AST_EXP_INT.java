package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public class AST_EXP_INT extends AST_EXP
{
	public int value;
	public int sign;
	public int lineNumber;
	int MAX_INT = 32768;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_INT(int value, int sign,int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> INT(%c%d )\n", sign * 2 + 43, value);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
		this.sign = sign;
		this.lineNumber=lineNumber;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE INT( %c%d )\n", sign * 2 + 43, value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("INT(%c%d)", sign * 2 + 43, value));
	}
	public TYPE SemantMe() throws RuntimeException
	{
		if(this.value >= MAX_INT) throw new RuntimeException(String.valueOf(lineNumber));
		return TYPE_INT.getInstance();
	}

	public TEMP IRme() {
		System.out.println("IRME IN AST_EXP_INT");

		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		System.out.println(this.value);
		System.out.println(this.sign);
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(t, this.sign * this.value));

		return t;
	}
}

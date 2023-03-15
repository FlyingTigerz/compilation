package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;


public class AST_EXP_PAREN extends AST_EXP
{

	public AST_EXP exp;
	public int lineNumber;

	public AST_EXP_PAREN(AST_EXP exp,int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
		this.lineNumber=lineNumber;
	}



	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE EXP PAREN\n");

		AST_GRAPHVIZ.getInstance().logNode(SerialNumber,"EXP\nPAREN\n");

		if (exp != null) 	AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);

	}
	
	public TYPE SemantMe()
	{
		return exp.SemantMe();

	}
	public TEMP IRme(){
		System.out.println("IRME IN AST_EXP_PAREN");

		return exp.IRme();
	}

}

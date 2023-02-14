package AST;

import TYPES.TYPE;
import TYPES.TYPE_STRING;
import TEMP.*;
import IR.*;

public class AST_EXP_STRING extends AST_EXP
{
	public String s;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(int LineNum,String s)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> STRING( %s )\n", s);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.s = s;
		this.LineNum=++LineNum;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE STRING( %s )\n",s);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STRING(%s)",s));
	}
	public TYPE SemantMe() { 
		this.se = TYPE_STRING.getInstance();
		return TYPE_STRING.getInstance(); }

	public TEMP IRme(){
		TEMP dataStorage = TEMP_FACTORY.getInstance().getFreshNamedTEMP(IR.strPrefix + IR.getInstance().getLabelIndex());
		IR.getInstance().Add_IRdata(new IRdata_Constant_String(dataStorage, s));
		return dataStorage;
	}
}

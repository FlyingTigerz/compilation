package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_TYPE extends AST_Node
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	TYPE type;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_TYPE(int LineNum,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.LineNum=++LineNum;
		
		
		switch (typeName) {
		case "void":
			this.type = TYPE_VOID.getInstance();
			break;
		case "int":
			this.type = TYPE_INT.getInstance();
			break;
		case "string":
			this.type = TYPE_STRING.getInstance();
			break;
			}

	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST TYPE */
		/**********************************/
		System.out.format("AST NODE TYPE( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("TYPE\n...->%s",type));
	}
	public TYPE SemantMe() throws semanticExc {
		SYMBOL_TABLE_ENTRY t;
		/****************************/
		/* [1] Check If Type exists */
		/****************************/


		t = SYMBOL_TABLE.getInstance().find(this.name);
		if(this.name!="null"){

		if (t == null || t.type == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,this.name);
			throw new semanticExc(this.LineNum);
		}
		}

		/** check that this is in fact declarable type (i.e. class, array, string, or int) **/
		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return t.type;
	}
}

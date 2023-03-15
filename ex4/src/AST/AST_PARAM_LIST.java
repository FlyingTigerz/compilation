package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_PARAM_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	String name;
    AST_TYPE type;
    AST_PARAM_LIST paramList;
	int line_number;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PARAM_LIST(AST_TYPE type, String name, AST_PARAM_LIST paramList,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (paramList != null) System.out.print("====================== params -> param params\n");
		if (paramList == null) System.out.print("====================== params -> param      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.type = type;
		this.paramList = paramList;
		this.line_number = line_number;
	}

	/******************************************************/
	/* The printing message for a variables list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST PARAMETERS LIST */
		/**************************************/
		System.out.print("AST NODE PARAMETERS LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT LIST ... */
		/*************************************/
		if (type != null) type.PrintMe();
		if (paramList != null) paramList.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("PARAM LIST\n(%s %s)\n", name, type));		
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (paramList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,paramList.SerialNumber);
	}
	
}

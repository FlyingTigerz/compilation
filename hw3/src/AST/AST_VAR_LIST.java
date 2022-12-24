package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.Objects;
import TYPES.TYPE;

public class AST_VAR_LIST extends AST_VAR
{
	AST_TYPE type;
	String name;
	AST_VAR_LIST restoflist;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_LIST(int LineNum, AST_TYPE type,String name,AST_VAR_LIST restoflist)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (restoflist != null) System.out.print("====================== type,name -> type, name\n");
		if (restoflist == null) System.out.print("====================== type,name      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.LineNum = ++LineNum;
		this.name = name;
		this.type=type;
		this.restoflist=restoflist;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		if (type != null) type.PrintMe();
		if (restoflist != null) restoflist.PrintMe();

		/************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR\nLIST\n");
		
		/**************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/**************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (restoflist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,restoflist.SerialNumber);	
		String sOP="";		
	}
	public TYPE SemantMe() throws semanticExc {
		if (type != null) type.SemantMe();
		if (restoflist != null) restoflist.SemantMe();

		return null;
	}
}

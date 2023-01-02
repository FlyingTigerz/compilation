package AST;

import TYPES.TYPE;

public class AST_EXP_LIST extends AST_EXP
{
	AST_EXP first;
	AST_EXP_LIST restoflist;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_LIST(int LineNum,AST_EXP first,AST_EXP_LIST restoflist)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (restoflist != null) System.out.print("====================== exps -> exp, exps\n");
		if (restoflist == null) System.out.print("====================== exps -> exp      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.first = first;
		this.restoflist=restoflist;
		this.LineNum=++LineNum;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		
		System.out.print("AST NODE EXP LIST\n");
		
		
		if (first != null) first.PrintMe();
		if (restoflist != null) restoflist.PrintMe();

		/************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nLIST\n");
		
		/**************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/**************/
		if (first != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,first.SerialNumber);
		if (restoflist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,restoflist.SerialNumber);	
	
	}
	public TYPE SemantMe() throws semanticExc
	{
		if (first != null) first.SemantMe();
		if (restoflist != null) restoflist.SemantMe();

		return null;
	}
}

package AST;

public class AST_CFIELD_LIST extends AST_EXP
{
	AST_CFIELD first;
	AST_CFIELD_LIST restoflist;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_LIST(AST_CFIELD first,AST_CFIELD_LIST restoflist)
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
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
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
		String sOP="";		
	}
}

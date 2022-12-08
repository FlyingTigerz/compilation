package AST;

public class AST_EXPLST_VAR_ID extends AST_EXP
{
	public AST_VAR var;
	public AST_EXP_LIST explist;
	String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXPLST_VAR_ID(AST_VAR var,AST_EXP_LIST explist,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> var, explist, name\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.explist = explist;
		this.name=name;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null) var.PrintMe();
		if (explist != null) explist.PrintMe();
		System.out.format("AST NODE EXPLS VAR ID( %s )\n",name);
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nVAR");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);}

		if (explist != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,explist.SerialNumber);}
			
	}
}

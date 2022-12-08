package AST;

public class AST_VAR_SUBSCRIPT_NEWEXP extends AST_STMT
{
	public AST_VAR var;
	public AST_EXP e;
	public AST_NEWEXP newe;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT_NEWEXP(AST_VAR var,AST_EXP e,AST_NEWEXP newe)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ e,newe ]\n");
		
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.e=e;
		this.newe=newe;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR EXP NEWEXP\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (e != null) e.PrintMe();
		if (newe != null) newe.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\nEXPNEWEXP\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (e       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);
		if (newe != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newe.SerialNumber);
	}
}

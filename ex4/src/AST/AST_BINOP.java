package AST;

public class AST_BINOP extends AST_Node
{
	char OP;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_BINOP(String OP)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== BINOP -> %s\n", OP);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.OP = OP.charAt(0);
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST BINOP */
		/*************************************/
		System.out.format("AST NODE OP\n", OP);
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%c)",OP));
	}
}

package AST;

public class AST_CFIELD extends AST_Node
{
	public AST_VARDEC varDec;
	public AST_FUNCDEC funcDec;
	/******/
	/* CONSTRUCTOR(S) */
	/******/
	public AST_CFIELD(AST_VARDEC varDec,AST_FUNCDEC funcDec)
	{
		/**********/
		/* SET A UNIQUE SERIAL NUMBER */
		/**********/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/*************/
		System.out.print("====================== exp -> [obj.]f(varDec);\n");
		System.out.print("====================== exp -> [obj.]f(funcdec);\n");
		/***********/
		/* COPY INPUT DATA NENBERS ... */
		/***********/
		this.varDec = varDec;
		this.funcDec = funcDec;
	}

	/*****************/
	/* The printing message for a binop exp AST node */
	/*****************/
	public void PrintMe()
	{

		/***********/
		/* CONVERT OP to a printable sOP */
		/***********/

		/*************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************/
		System.out.print("AST NODE CFIELD\n");

		/**************/
		/* RECURSIVELY PRINT left + right ... */
		/**************/

		if (varDec != null) varDec.PrintMe();
		if (funcDec != null) funcDec.PrintMe();

		/*************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/*************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FUNCCALL(%s)",varDec));

		/**************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/**************/
		if (varDec  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
		if (funcDec  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcDec.SerialNumber);
	}
}

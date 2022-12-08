package AST;

public class AST_STMT_FUNCCALL extends AST_STMT
{
	public String fname;
	public AST_VAR v;
	public AST_EXP_LIST l;

	/******/
	/* CONSTRUCTOR(S) */
	/******/
	public AST_STMT_FUNCCALL(String fname, AST_VAR v, AST_EXP_LIST l)
	{
		/**********/
		/* SET A UNIQUE SERIAL NUMBER */
		/**********/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/*************/
		System.out.print("====================== exp -> [obj.]f(expLst);\n");

		/***********/
		/* COPY INPUT DATA NENBERS ... */
		/***********/
		this.fname = fname;
		this.v = v;
		this.l = l;
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
		System.out.print("AST NODE FUNC CALL STMT\n");

		/**************/
		/* RECURSIVELY PRINT left + right ... */
		/**************/
		if (fname != null) System.out.format("function %s", fname);
		if (v != null) v.PrintMe();
		if (l != null) l.PrintMe();

		/*************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/*************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FUNCCALL(%s)",fname));

		/**************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/**************/
		if (l  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
		if (v  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
	}
}

package AST;

import TYPES.TYPE;

public class AST_VAR_SUBSCRIPT_NEWEXP extends AST_STMT
{
	public AST_VAR var;
	public AST_EXP e;
	public AST_NEWEXP newe;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT_NEWEXP(int LineNum,AST_VAR var,AST_EXP e,AST_NEWEXP newe)
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
		this.LineNum=++LineNum;
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
	public TYPE SemantMe() throws semanticExc{
		TYPE v;
		v = var.SemantMe();
		if (v == null)
		{
			System.out.format(">> ERROR [%d:%d] non existent identifier\n",2,2);
			throw new semanticExc(this.LineNum);
		}
		if(e==null){
			TYPE ex = newe.SemantMe();
			if (!ex.isInstanceOf(v))
			{
				System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, ex.name, v.name);
				throw new semanticExc(this.LineNum);
			}
		}
		if (newe==null){
			TYPE ex = e.SemantMe();
			if (!ex.isInstanceOf(v))
			{
				System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, ex.name, v.name);
				throw new semanticExc(this.LineNum);
			}
		}
		return null;
	}
}

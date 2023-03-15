package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	int line_number;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
		this.line_number = line_number;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException
	{
		TYPE t1 = null;
		if (var != null) t1 = var.SemantMe();

		TYPE t2 = null;
		if (exp != null) t2 = exp.SemantMe();

		if (t1 == null)
		{
			throw new RuntimeException(String.valueOf(SYMBOL_TABLE.getInstance().lineNumber));
		}
		else if (t1.isClass() && t2.isClass())
		{
			if (!(((TYPE_CLASS)t2).isAncestor((TYPE_CLASS)t1)) && ((TYPE_CLASS)t1).name != ((TYPE_CLASS)t2).name)
			{
				throw new RuntimeException(String.valueOf(SYMBOL_TABLE.getInstance().lineNumber));
			}
		}
		else if (t1.isArray() && t2.isArray())
		{
			if (((TYPE_ARRAY)t1).type != ((TYPE_ARRAY)t2).type)
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}
		}
		else if (t1.isArray() || t1.isClass())
		{
			if (t2 != TYPE_NIL.getInstance())
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}
		}
		else if (t1 != t2)
		{
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		return null;
	}

	@Override
	public TEMP IRme() {
		System.out.println("IRME IN AST_STMT_ASSIGN");


		TEMP v = this.var.IRme();
		TEMP e;
		//if(exp!=null)
		//{
			e = this.exp.IRme();
		//}

		IR.getInstance().Add_IRcommand(new IRcommand_Store(v, e));
		return null;
	}
}

package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;


public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	/***************/
	/*  var := newexp */
	/***************/
	public AST_VAR var;
	public AST_NEW_EXP newexp;
	int line_number;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN_NEW(AST_VAR var,AST_NEW_EXP newexp,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.newexp = newexp;
		this.line_number =line_number;
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
		if (newexp != null) newexp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN NEW\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newexp.SerialNumber);

	}

	public TYPE SemantMe()
	{
		TYPE t1 = null;
		if (var != null) t1 = var.SemantMe();

		TYPE t2 = null;
		if (newexp != null) t2 = newexp.SemantMe();

		if (t1 == null || t2 == null)
		{
			throw new RuntimeException(String.valueOf(this. line_number));
		}
		else if (t1.isClass() && t2.isClass())
		{
			if (!(((TYPE_CLASS)t2).isAncestor((TYPE_CLASS)t1)))
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}
			//if(t1.isClass()){((TYPE_CLASS) t1).dynamic=(TYPE_CLASS) t2;}

		}
		else if (t1.isArray())
		{
			if (!(((TYPE_ARRAY)t1).type == t2))
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}
		}
		else if (t1 != t2)
			throw new RuntimeException(String.valueOf(this.line_number));

		return null;
	}

	@Override
	public TEMP IRme() {
		System.out.println("IRME IN AST_STMT_ASSIGN_NEW");

		TEMP v = this.var.IRme();
		TEMP e;
		//if(newexp!=null)
		//{
		e = this.newexp.IRme();
		//}
		//TODO : handle unintilized e
		IR.getInstance().Add_IRcommand(new IRcommand_Store(v, e));
		return null;
	}
	

}

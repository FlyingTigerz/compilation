package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_DEC_FUNC extends AST_CFIELD
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_TYPE type;
	public AST_STMT_LIST sl;
	public AST_ARG_LIST al;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CFIELD_DEC_FUNC(int line, AST_TYPE type, String name, AST_ARG_LIST al, AST_STMT_LIST sl)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.println(type.typeName);
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== funcDec -> type ID(argList){stmtLst}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.type = type;
		this.name = name;
		this.sl = sl;
		this.al = al;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE DEC FUNC\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (type != null) type.PrintMe();
		if (sl != null) sl.PrintMe();
		if (al != null) al.PrintMe();
		if (name != null) System.out.format("%s\n", name);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
				String.format("DEC\ntype %s(args){stmtLst}",name)
		);
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (al != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,al.SerialNumber);
		if (sl != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,sl.SerialNumber);
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
	}
	public TYPE SemantMe() throws SemanticException {
		TYPE arg_type;
		TYPE returnType = null;
		TYPE_LIST reverse_type_list = null;
		TYPE_LIST type_list = null;

		/*******************/
		/* [0] check return type */
		/*******************/
		if(type != null) {
			returnType = type.SemantMe();
		}


		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AST_ARG_LIST it = al; it  != null; it = it.tail)
		{
			it.head.SemantMe();
			arg_type = it.head.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
			SYMBOL_TABLE.getInstance().enter(it.head.name, arg_type);
		}
		/* reverse type list to preserve original argument order */
		for (TYPE_LIST it = reverse_type_list; it  != null; it = it.tail) {type_list = new TYPE_LIST(it.head,type_list);}

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name, new TYPE_FUNCTION(returnType, name,type_list));

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/*******************/
		/* [3] Semant Body */
		/*******************/
		sl.SemantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		this.semanticLabel = new TYPE_FUNCTION(returnType, name, type_list);
		return this.semanticLabel;
	}
}

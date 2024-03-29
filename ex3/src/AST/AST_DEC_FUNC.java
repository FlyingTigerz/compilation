package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_FUNC extends AST_DEC
{
	public AST_TYPE returnTypeName;
	public String name;
	public AST_VAR_LIST params;
	public AST_STMT_LIST body;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_FUNC(int LineNum,
						AST_TYPE returnTypeName,
						String name,
						AST_VAR_LIST params,
						AST_STMT_LIST body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.returnTypeName = returnTypeName;

		this.name = name;
		this.params = params;
		this.body = body;
		this.LineNum=++LineNum;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{/*****************************/
		/* RECURSIVELY PRINT typename ... */
		/*****************************/
		if (returnTypeName != null) returnTypeName.PrintMe();
		if(params!=null)params.PrintMe();
		if(body!=null)body.PrintMe();
		System.out.format("AST FUNC DEC ( %s )\n",name);
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("DEC\ntype %s(args){stmtLst}",name)
		);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (returnTypeName != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,returnTypeName.SerialNumber);}
		if (body != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);}
		if (params != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);}
	}


	public TYPE SemantMe() throws semanticExc
	{
		TYPE arg_type;
		TYPE returnType = null;
		TYPE_LIST reverse_type_list = null;
		TYPE_LIST type_list = null;
		TYPE_FUNCTION func_t=null;
		func_t=SYMBOL_TABLE.getInstance().cur_func;

		/*******************/
		/* [0] check return type */
		/*******************/

		if(returnTypeName != null) {
			returnType = returnTypeName.SemantMe();
		}

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AST_VAR_LIST it = this.params; it  != null; it = it.restoflist)
		{
			it.typename.SemantMe();
			arg_type = it.typename.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
		}
		/* reverse type list to preserve original argument order */
		for (TYPE_LIST it = reverse_type_list; it  != null; it = it.tail) {type_list = new TYPE_LIST(it.head,type_list);}

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		TYPE_FUNCTION sym=new TYPE_FUNCTION(returnType,name,null);
		SYMBOL_TABLE.getInstance().cur_func=sym;
		SYMBOL_TABLE.getInstance().enter(name,sym);

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope();
		for (AST_VAR_LIST it = this.params; it  != null; it = it.restoflist)
		{
			it.typename.SemantMe();
			arg_type = it.typename.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
			SYMBOL_TABLE.getInstance().enter(it.typename.name, arg_type);
		}
		sym.params=type_list;
		/*******************/
		/* [3] Semant Body */
		/*******************/
		this.body.SemantMe();
		
		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();
		SYMBOL_TABLE.getInstance().cur_func=func_t;

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/

		func_t=new TYPE_FUNCTION(returnType,name,type_list);

		
		this.se=func_t;

		return returnType;
	}
	
}

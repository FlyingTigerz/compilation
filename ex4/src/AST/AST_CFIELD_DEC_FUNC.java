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
	public AST_VAR_LIST al;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CFIELD_DEC_FUNC(int line, AST_TYPE type, String name, AST_VAR_LIST al, AST_STMT_LIST sl)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== funcDec -> type ID(argList){stmtLst}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.LineNum = ++line;
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
		System.out.print("AST NODE DEC FUNC IN CFIELD\n");

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
	public TYPE SemantMe() throws semanticExc {
		TYPE arg_type;
		TYPE returnType = null;
		TYPE_LIST reverse_type_list = null;
		TYPE_LIST type_list = null;
		TYPE_FUNCTION hold=null;

		hold=SYMBOL_TABLE.getInstance().cur_func;
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name);
		if (prevDec != null)
		{
			SYMBOL_TABLE_ENTRY scope = SYMBOL_TABLE.getInstance().getScope();
			/* print error only if declaration shadows a previous declaration in the same scope*/
			if(scope == null || scope.prevtop_index < prevDec.prevtop_index) {
				System.out.format(">> ERROR [%d:%d] %s already exists in scope\n", 2, 2, name);
				throw new semanticExc(LineNum);
			}
		}
		/*******************/
		/* [0] check return type */
		/*******************/
		if(type != null) {
			returnType = type.SemantMe();
		}
		

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AST_VAR_LIST it = al; it  != null; it = it.restoflist)
		{
			it.typename.SemantMe();
			arg_type = it.typename.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
			SYMBOL_TABLE.getInstance().enter(it.typename.name, arg_type);
		}
		/* reverse type list to preserve original argument order */
		for (TYPE_LIST it = reverse_type_list; it  != null; it = it.tail) {type_list = new TYPE_LIST(it.head,type_list);}

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		TYPE_FUNCTION sym=new TYPE_FUNCTION(returnType,name,null);
		SYMBOL_TABLE.getInstance().cur_func=sym;
		SYMBOL_TABLE.getInstance().enter(name,sym);
		SYMBOL_TABLE.getInstance().enter(name, new TYPE_FUNCTION(returnType, name,type_list));


		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope();
		for (AST_VAR_LIST it = al; it  != null; it = it.restoflist)
		{
			it.typename.SemantMe();
			arg_type = it.typename.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
			SYMBOL_TABLE.getInstance().enter(it.typename.name, arg_type);
		}


		/*******************/
		/* [3] Semant Body */
		/*******************/
		if(sl!=null){
		sl.SemantMe();
		}
		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();
		SYMBOL_TABLE.getInstance().cur_func=hold;

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		
		
		//this migh need to be changes
		System.out.print("====================== AST_CFIELD_DEC_FUNC , might be problem here \n");

		this.se = new TYPE_FUNCTION(returnType, returnType.name,null);
		return new TYPE_CLASS_VAR_DEC(new TYPE_FUNCTION(returnType, returnType.name,null),name);
	}

}

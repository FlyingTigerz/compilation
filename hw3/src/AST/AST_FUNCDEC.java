package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
public class AST_FUNCDEC extends AST_STMT
{

	public AST_TYPE type;
	public AST_STMT_LIST stmtlist;
	public AST_VAR_LIST varlist;
	String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNCDEC(int LineNum,AST_TYPE type,AST_STMT_LIST stmtlist,AST_VAR_LIST varlist,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.print("====================== funcDec -> type ID(argList){stmtLst}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.stmtlist=stmtlist;
		this.varlist=varlist;
		this.name=name;
		this.LineNum=++LineNum;

	}

	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{



		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
		if(stmtlist!=null)stmtlist.PrintMe();
		if(varlist!=null)varlist.PrintMe();
		if (name != null) System.out.format("%s\n", name);

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("DEC\ntype %s(args){stmtLst}",name)
		);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);}
		if (stmtlist != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtlist.SerialNumber);}
		if (varlist != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varlist.SerialNumber);}

	}
	public TYPE SemantMe() throws semanticExc
	{
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
		for (AST_VAR_LIST it = varlist; it  != null; it = it.restoflist)
		{
			it.type.SemantMe();
			arg_type = it.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
			SYMBOL_TABLE.getInstance().enter(it.type.name, arg_type);
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
		stmtlist.SemantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}

}

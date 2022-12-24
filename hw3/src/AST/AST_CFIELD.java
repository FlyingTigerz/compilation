package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD extends AST_Node
{
	public AST_VARDEC varDec;
	public AST_FUNCDEC funcDec;
	/******/
	/* CONSTRUCTOR(S) */
	/******/
	public AST_CFIELD(int LineNum,AST_VARDEC varDec,AST_FUNCDEC funcDec)
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
		this.LineNum=++LineNum;
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
	public TYPE SemantMe() throws semanticExc {
		TYPE arg_type;
		TYPE returnType = null;
		TYPE_LIST reverse_type_list = null;
		TYPE_LIST type_list = null;
		TYPE t = varDec.type.SemantMe();
		/*******************/
		/* [0] check return type */
		/*******************/
		if(funcDec.type != null) {
			returnType = funcDec.type.SemantMe();
		}
		AST_STMT_LIST sl = funcDec.stmtlist;
		AST_VAR_LIST al=funcDec.varlist;
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(varDec.name);
		if (prevDec != null)
		{
			SYMBOL_TABLE_ENTRY scope = SYMBOL_TABLE.getInstance().getScope();
			/* print error only if declaration shadows a previous declaration in the same scope*/
			if(scope == null || scope.prevtop_index < prevDec.prevtop_index) {
				System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, varDec.name);
				throw new semanticExc(LineNum);
			}
		}

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AST_VAR_LIST it = al; it  != null; it = it.restoflist)
		{
			it.type.SemantMe();
			arg_type = it.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
			SYMBOL_TABLE.getInstance().enter(it.name, arg_type);
		}
		/* reverse type list to preserve original argument order */
		for (TYPE_LIST it = reverse_type_list; it  != null; it = it.tail) {type_list = new TYPE_LIST(it.head,type_list);}

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(this.funcDec.name, new TYPE_FUNCTION(returnType, this.funcDec.name,type_list));
		SYMBOL_TABLE.getInstance().enter(varDec.name, t);
		if(varDec.exp != null && (varDec.exp.SemantMe() == null || !varDec.exp.SemantMe().isInstanceOf(t))){
			System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, varDec.exp.SemantMe().name, t.name);
			throw new semanticExc(LineNum);
		}

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
		return new TYPE_FUNCTION(returnType, this.funcDec.name, type_list);
	}
}

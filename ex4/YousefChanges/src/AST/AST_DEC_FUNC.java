package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import MIPS.*;

import java.util.Objects;

public class AST_DEC_FUNC extends AST_DEC
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_TYPE type;
	public String name;
	public AST_STMT_LIST sl;
	public AST_ARG_LIST al;
	private int localVarCount = 0;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_FUNC(int line, AST_TYPE type, String name, AST_ARG_LIST al, AST_STMT_LIST sl)
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
		if (name != null) System.out.format("%s\n", name);
		if (type != null) type.PrintMe();
		if (sl != null) sl.PrintMe();
		if (al != null) al.PrintMe();

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
	public TYPE SemantMe() throws SemanticException
	{
		TYPE arg_type;
		TYPE returnType = null;
		TYPE_LIST reverse_type_list = null;
		TYPE_LIST type_list = null;
		TYPE_FUNCTION func_type = null;

		/*******************/
		/* [0] check return type */
		/*******************/
		if(type != null) {
			returnType = type.SemantMe();
		}

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		TYPE_FUNCTION funcSymbol = new TYPE_FUNCTION(returnType, name, null);
		SYMBOL_TABLE.getInstance().enter(name, funcSymbol);

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope();


		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AST_ARG_LIST it = al; it  != null; it = it.tail)
		{
			it.head.SemantMe();
			arg_type = it.head.type.SemantMe();
			reverse_type_list = new TYPE_LIST(arg_type,reverse_type_list);
			//SYMBOL_TABLE.getInstance().enter(it.head.name, arg_type);
		}
		/* reverse type list to preserve original argument order */
		for (TYPE_LIST it = reverse_type_list; it  != null; it = it.tail) {type_list = new TYPE_LIST(it.head,type_list);}
		funcSymbol.params = type_list;
		/*******************/
		/* [3] Semant Body */
		/*******************/
		sl.SemantMe();

		localVarCount = ((TYPE_FOR_SCOPE_BOUNDARIES)SYMBOL_TABLE.getInstance().getScope().type).getVarCount();
		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		func_type = new TYPE_FUNCTION(returnType, name, type_list);
		this.semanticLabel = func_type;
		return func_type;
	}
	public TEMP IRme()
	{
		// label
		IR.getInstance().Add_IRcommand(new IRcommand_Label(IR.funcLabelPrefix + name));
		// prologue
		TEMP sp = IR.getInstance().sp;
		TEMP fp = IR.getInstance().fp;
		TEMP ra = IR.getInstance().ra;
		// addi $sp, $sp, -4
		IR.getInstance().Add_IRcommand(new IRcommand_Add_Immediate(sp, sp, -sir_MIPS_a_lot.WORD_SIZE));
		// sw $ra, 0($sp)
		IR.getInstance().Add_IRcommand(new IRcommand_Store_Temp(ra, sp, 0));
		// addi $sp, $sp, -4
		IR.getInstance().Add_IRcommand(new IRcommand_Add_Immediate(sp, sp, -sir_MIPS_a_lot.WORD_SIZE));
		// sw $fp, 0($sp)
		IR.getInstance().Add_IRcommand(new IRcommand_Store_Temp(fp, sp, 0));
		// move $fp $sp
		IR.getInstance().Add_IRcommand(new IRcommand_Move(fp, sp));
		// addi $sp, $sp, -4 * localVarCount
		IR.getInstance().Add_IRcommand(new IRcommand_Add_Immediate(sp, sp, sir_MIPS_a_lot.WORD_SIZE * localVarCount));

		// function body
		sl.IRme();
		// epilogue is built in to return statement.
		// we return automatically once end of code is reached;

		//if(name.equals("main")) IR.getInstance().Add_IRcommand(new IRcommand_Exit());
		if(type.typeName.equals("void")) IR.getInstance().Add_IRcommand(new IRcommand_Return(null));

		return null;
	}
}

package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_CLASS extends AST_DEC
{
	/***************/
	/*  var := exp */
	/***************/
	public String name;
	public String extendsClass;
	public AST_CFIELD_LIST cfl;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_CLASS(int line, String name, String extendsClass, AST_CFIELD_LIST cfl)
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
		this.line = ++line;
		this.cfl = cfl;
		this.name = name;
		this.extendsClass = extendsClass;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST DEC CLASS\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (cfl != null) cfl.PrintMe();
		if (name != null) System.out.format("%s\n", name);
		if (extendsClass != null) System.out.format("%s\n", extendsClass);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("DEC\nclass %s [extends class] {cFieldList}",name)
		);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cfl.SerialNumber);
	}
	public TYPE_CLASS SemantMe() throws SemanticException {
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		SYMBOL_TABLE_ENTRY extending = null;

		/****************************/
		/* [1] Check if class extends valid class */
		/****************************/
		if(extendsClass != null) {
			extending = SYMBOL_TABLE.getInstance().find(extendsClass);
			if (extending == null)
			{
				System.out.format(">> ERROR [%d:%d] non existing class %s\n",2,2,extendsClass);
				throw new SemanticException(this.line);
			}
		}

		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name);
		if (prevDec != null)
		{
			SYMBOL_TABLE_ENTRY scope = SYMBOL_TABLE.getInstance().getScope();
			/* print error only if declaration shadows a previous declaration in the same scope*/
			if(scope.prevtop_index < prevDec.prevtop_index) {
				System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, name);
				throw new SemanticException(this.line);
			}
		}

		TYPE_CLASS father = extending == null ? null : (TYPE_CLASS) extending.type;

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(name, new TYPE_CLASS(null, name, null));

		/***************************/
		/* [2] Semant Data Members */
		/***************************/

		TYPE_CLASS_VAR_DEC_LIST fields = cfl == null ? null : cfl.SemantMe();
		TYPE_CLASS t = new TYPE_CLASS(father, name, fields);

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(name,t);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		this.semanticLabel = t;
		return t;
	}
}

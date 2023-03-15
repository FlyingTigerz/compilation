package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;
import CONTEXT.Func_LocalContext;
import CONTEXT.Context;
import CONTEXT.Var_GlobalContext;
import CONTEXT.Var_ClassContext;



public class AST_DEC_VAR extends AST_DEC
{
	/*****************/
	/*  ID ID := exp */
	/*****************/
	String name;
	public AST_TYPE type;
	public AST_EXP exp;
	public AST_NEW_EXP newexp;
	public int line_number;
	public Context context;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_VAR(AST_TYPE type, String name, AST_EXP exp, AST_NEW_EXP newexp,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if(newexp != null) 	System.out.print("====================== dec -> var ASSIGN NEW EXP SEMICOLON\n");
		if (exp != null) 	System.out.print("====================== dec -> var ASSIGN exp SEMICOLON\n");
		if (exp == null) 	System.out.print("====================== dec -> var SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.exp = exp;
		this.newexp = newexp;
		this.type = type;
		this.line_number = line_number;
	}

	/*********************************************************/
	/* The printing message for a variable declaration AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST VARIABLE DECLARATION */
		/********************************************/
		System.out.print("AST NODE VARIABLE DEC\n");

		/***********************************/
		/* RECURSIVELY EXP. */
		/***********************************/
		if (type != null) type.PrintMe();
		if (exp != null) exp.PrintMe();
		if (newexp != null) newexp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VAR_DEC\n (%s) \n", name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
		if (newexp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newexp.SerialNumber);
	}
	public TYPE SemantMe() throws RuntimeException
	{
		TYPE t1 = type.SemantMe();
		if(SYMBOL_TABLE.getInstance().findInScope(name) != null){
			throw new RuntimeException(String.valueOf(this.line_number));
		}


		TYPE t3 = null;
		if(exp != null){
			t3 = exp.SemantMe();
			System.out.println("compare exp "+ t3 + " with "+ t1);

			if(t1.isArray() && t3 == TYPE_NIL.getInstance()){
				System.out.println("I'm here---------------------------");
			}
			if(t3 == null ||
					!(t1 == t3 || (t1.isClass() && ((t3.isClass() && ((TYPE_CLASS)t3).isAncestor((TYPE_CLASS)t1))) || t3 == TYPE_NIL.getInstance()) ||
					(t1.isArray() && ((t3.isArray() && ((TYPE_ARRAY)t1).type == ((TYPE_ARRAY)t1).type))) ||
					(t1.isArray() && t3 == TYPE_NIL.getInstance()))
			)
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}
		}else if(newexp != null){
			t3 = newexp.SemantMe();
			System.out.println("compare new exp "+ t3 + " with "+ t1);
			if(t3 == null ||
					!(t1 == t3 || (t1.isClass() && ((t3.isClass() && ((TYPE_CLASS)t3).isAncestor((TYPE_CLASS)t1)) || t3 == TYPE_NIL.getInstance())) ||
							(t1.isArray() && (((TYPE_ARRAY)t1).type == t3 || t3 == TYPE_NIL.getInstance())) )){
				throw new RuntimeException(String.valueOf(this.line_number));
			}
		}

		if (AST_DEC_FUNC.localVarsCounter >= 0) {
			this.context = new Func_LocalContext(AST_DEC_FUNC.localVarsCounter++);
		} else if (AST_DEC_CLASS.fieldsCounter >= 0) {
			this.context = new Var_ClassContext(AST_DEC_CLASS.fieldsCounter++);
		} else {
			final String labelName = "global_var_" + name;
			this.context = new Var_GlobalContext(labelName);
			MIPSGenerator.addGlobalVariable(labelName);
		}

		SYMBOL_TABLE.getInstance().enter(name, t1, context);

		return t1;
	}
	public TEMP IRme() {
		System.out.println("DEC VAR IRME");
		if (this.exp==null && newexp==null) {
			System.out.println("EXP & NEXP IS NULL");
			return null;}
		TEMP address = TEMP_FACTORY.getInstance().getFreshTEMP();
		System.out.println("DEC VAR IRME");
		if(context == null)
			System.out.println("context is not null   ---------  " + name);
		IR.getInstance().Add_IRcommand(new IRcommand_Load(address, null, 0, context,true ));
		TEMP fresh;
		if (this.exp != null)
			fresh = exp.IRme();
		else
			fresh = newexp.IRme();
		if(fresh == null)
			System.out.println("fresh is null at "+ name);

		IR.getInstance().Add_IRcommand(new IRcommand_Store(address, fresh));
		return null;
	}
}

package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;
import CONTEXT.*;

public class AST_DEC_FUNC extends AST_DEC
{
	/*****************************/
	/*  ID ID ([ID ID]*) {stmts} */
	/*****************************/
	public AST_TYPE returnTypeName;
	public String func;
	public AST_PARAM_LIST params;
	public AST_STMT_LIST body;
	public int line_number;
	public String startLabel;
	public String endLabel;
	public int numOfLocals;
	private boolean isMainFunction;
	public static int localVarsCounter = -1;
	public int _offset = 0;


	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_FUNC(AST_TYPE returnTypeName, String func, AST_PARAM_LIST params, AST_STMT_LIST body,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (params != null) System.out.print("====================== dec -> func (params) {body}\n");
		if (params == null) System.out.print("====================== dec -> func () {body}\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.returnTypeName = returnTypeName;
		this.func = func;
		this.params = params;
		this.body = body;
		this.line_number = line_number;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST FUNCTION DECLARATION */
		/********************************************/
		System.out.print("AST NODE FUNCTION DEC\n");

		/***********************************/
		/* RECURSIVELY PRINT MEMBERS ... */
		/***********************************/
		if (returnTypeName != null) returnTypeName.PrintMe();
		if (params != null) params.PrintMe();
		if (body != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNC DEC\n(%s)\n", func));
		
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (returnTypeName != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,returnTypeName.SerialNumber);
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException
	{
		boolean isBelongToClass = AST_DEC_CLASS.fieldsCounter >= 0;
		TYPE t;
		TYPE returnType = null;
		TYPE_LIST reversed_list = null;
		TYPE_LIST type_list = null;
		TYPE funct = null;

		if(func != null){
			if(SYMBOL_TABLE.getInstance().findInScope(func) != null)
				throw new RuntimeException(String.valueOf(this.line_number));
		}

		if ("main".equals(this.func)) { this.startLabel = "user_main"; this.isMainFunction = true;}
		else { this.startLabel = IRcommand.getFreshLabel(this.func); }



		/*******************/
		/* [0] return type */
		/*******************/
		returnType = returnTypeName.SemantMe();
		if(SYMBOL_TABLE.getInstance().func_scope){
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		SYMBOL_TABLE.getInstance().funcRetType = returnType;
		SYMBOL_TABLE.getInstance().func_scope = true;

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope();
		this.endLabel = IRcommand.getFreshLabel(this.func + "_end");
		SYMBOL_TABLE.getInstance().setCurrentEndLabel(endLabel);
		localVarsCounter = 0;
		String funcNameDec = "func_" + func + ": .asciiz \"" + func + "\"\n";
		if (!MIPSGenerator.funcNames.contains(funcNameDec)){
			MIPSGenerator.funcNames += (funcNameDec);
		}


		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		System.out.println("------------------");
		AST_PARAM_LIST it = params;
		int numOPParam = 0;
		while(it != null)
		{
			numOPParam ++;
			t = it.type.SemantMe();
			System.out.println("In dec func param name:  "+ it.name);
			if(t.isArray()){
				System.out.println("is Array");
			}
			if (t == null )
			{
				System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.type);
				throw new RuntimeException(String.valueOf(this.line_number));
			}else if( !(t == TYPE_INT.getInstance() || t == TYPE_STRING.getInstance() ||
					(t.isClass() && SYMBOL_TABLE.getInstance().find(((TYPE_CLASS)t).name) != null)  ||
							(t.isArray() && SYMBOL_TABLE.getInstance().find(((TYPE_ARRAY)t).name) != null))){
				System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.type);
				throw new RuntimeException(String.valueOf(this.line_number));
			}
			else
			{
				reversed_list = new TYPE_LIST(t,reversed_list);
				Context context_parm;
				if(t.isFunc())
					context_parm = new Func_ParamContext(_offset++, isBelongToClass );
				else
					context_parm = new Func_ParamContext(_offset++, false );
				SYMBOL_TABLE.getInstance().enter(it.name,t, context_parm);
			}
			it = it.paramList;
		}

		for (TYPE_LIST rt = reversed_list; rt != null; rt = rt.tail)
		{
			System.out.println("Adding to type list " + rt.head + " named " + rt.head.name);
			type_list = new TYPE_LIST(rt.head, type_list);
		}

		// Used so function can call itself
		SYMBOL_TABLE.getInstance().enter(func,new TYPE_FUNCTION(returnType,func,type_list, numOPParam, startLabel, endLabel));



		/*******************/
		/* [3] Semant Body */
		/*******************/
		body.SemantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/

//		if(SYMBOL_TABLE.getInstance().return_exist || returnType == TYPE_VOID.getInstance()){
//			SYMBOL_TABLE.getInstance().return_exist = false;
//		}else{
//			throw new RuntimeException(String.valueOf(SYMBOL_TABLE.getInstance().lineNumber));
//		}



		SYMBOL_TABLE.getInstance().endScope();
		SYMBOL_TABLE.getInstance().setCurrentEndLabel(null);

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		TYPE func_type = new TYPE_FUNCTION(returnType,func,type_list, numOPParam, startLabel, endLabel);
		SYMBOL_TABLE.getInstance().enter(func, func_type);
		this.numOfLocals = localVarsCounter;
		localVarsCounter = -1;

		SYMBOL_TABLE.getInstance().func_scope = false;

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return func_type;//null;
	}


	public TEMP IRme() {
		System.out.println("IRME IN AST_DEC_FUNC");

		Runnable irBody = () -> { body.IRme(); };
		IRFuncDec(irBody, this.startLabel, this.numOfLocals, this.endLabel, this.isMainFunction, true);
		return null;
	}


	public static void IRFuncDec(Runnable irBody, String startLabel, int numOfLocals,
								 String endLabel, boolean isMainFunction, boolean isFunction) {

		int offset;
		offset = isFunction ? 1 : 0;
		IR.getInstance().Add_IRcommand(new IRcommand_Label(startLabel));

		if(isMainFunction) {
			IR.getInstance().Add_IRcommand(new IR_general_command("addi $fp,$sp,0", null, false));
			IR.getInstance().Add_IRcommand(new IR_general_command("sw $zero,0($fp)", null, false));
		}

		String allocate_cmd = String.format("addi $sp,$sp,%d", -4 * (numOfLocals+offset));
		IR.getInstance().Add_IRcommand(new IR_general_command(allocate_cmd, null, false));

		irBody.run();

		IR.getInstance().Add_IRcommand(new IRcommand_Label(endLabel));

		if(!isMainFunction) {
			String fold_cmd = String.format("addi $sp,$sp,%d", 4 * (numOfLocals+offset));
			IR.getInstance().Add_IRcommand(new IR_general_command(fold_cmd, null, false));
		}

		IR.getInstance().Add_IRcommand(new IR_general_jump_command());
	}


	int findNumOfParams(TYPE_LIST paramTypes){

		int numOfParams = 0;

		for(TYPE_LIST functionParams = paramTypes; functionParams != null; functionParams = functionParams.tail) {

			numOfParams++;

		}
		return numOfParams;
	}

	public static void IRPrintInt() {

		IR.getInstance().Add_IRcommand(new IRcommand_Label("PrintInt"));

		IR.getInstance().Add_IRcommand(new IR_general_command("lw $a0,4($fp)", null, false));
		IR.getInstance().Add_IRcommand(new IR_general_command("li $v0,1", null, false));
		IR.getInstance().Add_IRcommand(new IR_general_command("syscall", null, false));

		IR.getInstance().Add_IRcommand(new IR_general_command("li $a0,32", null, false));
		IR.getInstance().Add_IRcommand(new IR_general_command("li $v0,11", null, false));
		IR.getInstance().Add_IRcommand(new IR_general_command("syscall", null, false));

		IR.getInstance().Add_IRcommand(new IR_general_jump_command());
	}

	public static void IRPrintString() {

		IR.getInstance().Add_IRcommand(new IRcommand_Label("PrintString"));

		IR.getInstance().Add_IRcommand(new IR_general_command("lw $a0,4($fp)", null, false));
		IR.getInstance().Add_IRcommand(new IR_general_command("li $v0,4", null, false));
		IR.getInstance().Add_IRcommand(new IR_general_command("syscall", null, false));

		IR.getInstance().Add_IRcommand(new IR_general_jump_command());
	}

}

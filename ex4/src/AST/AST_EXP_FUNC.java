package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public class AST_EXP_FUNC extends AST_EXP
{
	/*****************************/
	/*  ID ID ([ID ID]*) {stmts} */
	/*****************************/
	public AST_VAR caller_var;
	public String func;
	public AST_EXP_LIST params;
	public int line_number;
	public String label;
	public int numOfParams = 0;
	public int virTableOffset;
	boolean isMethod;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_EXP_FUNC(AST_VAR caller_var, String func, AST_EXP_LIST params,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (params != null) System.out.print("====================== exp -> returnType func (params) {body}\n");
		if (params == null) System.out.print("====================== exp -> func {body}\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.caller_var = caller_var;
		this.func = func;
		this.params = params;
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
		System.out.print("AST NODE EXP FUNCTION\n");

		/***********************************/
		/* RECURSIVELY PRINT MEMBERS ... */
		/***********************************/
        if (caller_var != null) caller_var.PrintMe();
		if (params != null) params.PrintMe();


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nFUNC\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (caller_var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,caller_var.SerialNumber);
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
	}
	public TYPE SemantMe() throws RuntimeException
	{
		TYPE t2 = null;

		TYPE t1 = null;
		if(caller_var != null) {
			TYPE t_seman=SYMBOL_TABLE.getInstance().find(caller_var.SemantMe().name);
			if(!(t_seman instanceof TYPE_CLASS))
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}

			t2 = ((TYPE_CLASS)SYMBOL_TABLE.getInstance().find(caller_var.SemantMe().name)).findMembers(func, true);
			if(t2 == null )
				System.out.println("ts is nullllllllllllllllllllllllllllllll" + this.func);
			t2 = ((TYPE_CLASS_VAR_DEC)t2).t;

		}else {
			t2 = (TYPE_FUNCTION)SYMBOL_TABLE.getInstance().find(func);
		}

		if(t2 == null){
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		if(!t2.isFunc()){
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		this.label = ((TYPE_FUNCTION)t2).startLabel;
		if(((TYPE_FUNCTION)t2).belongsToClass != null)
		{
			this.numOfParams++;
			this.isMethod = true;
			this.virTableOffset = ((TYPE_FUNCTION)t2).virTableOffset;
		}

		TYPE_LIST exp_params = ((TYPE_FUNCTION)t2).params;
		AST_EXP_LIST actual_params = params;
		while (actual_params != null && exp_params != null){
			numOfParams++;
			TYPE exp_p = exp_params.head;
			TYPE act_p = (actual_params.head).SemantMe();
			if(exp_p.isClass() && act_p.isClass() && !((TYPE_CLASS)exp_p).isAncestor((TYPE_CLASS)act_p))
				throw new RuntimeException(String.valueOf(this.line_number));
			else if(exp_p.isArray() && act_p.isArray() && ((TYPE_ARRAY)exp_p).type != ((TYPE_ARRAY)act_p).type){
				throw new RuntimeException(String.valueOf(this.line_number));
			}
			else if( exp_p != act_p ){
				throw new RuntimeException(String.valueOf(this.line_number));
			}
			actual_params = actual_params.tail;
			exp_params = exp_params.tail;
		}
		if(actual_params != null || exp_params != null){
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		return ((TYPE_FUNCTION)t2).returnType;
	}

	/************************************************/
	/* IRme the AST node AST_EXP_FUNC */
	/************************************************/
	@Override
	public TEMP IRme() {
		System.out.println("IRME IN AST_EXP_FUNC");


		int prologSize = -4 * (10+this.numOfParams);

		AST_EXP_LIST paramsLst = this.params;
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $sp,$sp,%d", prologSize), null, false));

		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $fp,0($sp)"), null, false));

		if (!this.func.contains("class_init")) {
			TEMP funcName = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IR_general_command(String.format("la $t%%d,%s", "func_"+this.func), funcName, true));
			IR.getInstance().Add_IRcommand(new IRcommand_Store(funcName, -4));
		}

		int count = 0;
		if(this.isMethod) {
			TEMP ths;
			if(this.caller_var != null) {


				AST_EXP_VAR ev = new AST_EXP_VAR(this.caller_var, line_number);
				ths = ev.IRme();
				IR.getInstance().Add_IRcommand(new IRcommand_Check_For_Null_Pointer_Dereference(ths));
			} else {

				ths = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", ths, true));
			}
			IR.getInstance().Add_IRcommand(new IR_general_command("sw $t%d,4($sp)", ths, false));
			count++;
		}

		TEMP argument;
		if(count < this.numOfParams){
			argument = paramsLst.head.IRme();
			IR.getInstance().Add_IRcommand(new IR_general_command( String.format("sw $t%%d,%d($sp)", 4 * (count + 1)), argument, false));
			count++;


			AST_EXP_LIST params = paramsLst.tail;
			for(; count < this.numOfParams; count++) {
				argument = params.head.IRme();
				IR.getInstance().Add_IRcommand(new IR_general_command( String.format("sw $t%%d,%d($sp)", 4 * (count + 1)), argument, false));
				params= params.tail;
			}
		}

		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(this.numOfParams, false));

		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $fp,$sp,0"), null, false));

		if(!this.isMethod) {
			if(this.label == null)
			{
				System.out.println("Label Is null !!!!!!!!!!!!!!!!!!!!!!!11");
			}
			IR.getInstance().Add_IRcommand(new IR_general_jump_command(this.label));
		}
		else {
			IRmethod();
		}

		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(numOfParams, true));

		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IR_move_command(null, null, res, "$v0"));
		return res;
	}

	public void IRmethod(){
		TEMP temp_0 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP temp_1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP temp_2 = TEMP_FACTORY.getInstance().getFreshTEMP();

		IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", temp_0, true));

		IR.getInstance().Add_IRcommand(new IRcommand_Load(temp_1, temp_0, 0, null, false));

		IR.getInstance().Add_IRcommand(new IRcommand_Load(temp_2, temp_1, this.virTableOffset * 4, null, false));

		IR.getInstance().Add_IRcommand(new IR_general_jump_command(temp_2));
	}
}


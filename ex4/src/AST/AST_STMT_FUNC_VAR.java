package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public class AST_STMT_FUNC_VAR extends AST_STMT
{
	/************************/
	/* simple variable name */
	/************************/
	public AST_VAR var;
	public String name;
	public AST_EXP_LIST expList;
	int line_number;
	public String startLabel;
	public int numOfParams;
	public int virTableOffset;
	boolean isMethod;

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_FUNC_VAR(AST_VAR var, String name, AST_EXP_LIST expList, int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== stmt -> var . ID( %s ) ( exps )\n",name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.name = name;
		this.expList = expList;
		this.line_number = line_number;
	}

	/**************************************************/
	/* The printing message for a VAR FUNC AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST VAR FUNC */
		/**********************************/
		System.out.format("AST NODE STMT VAR FUNC var . ( %s ) ( expList )\n",name);

		/*************************************/
		/* RECURSIVELY PRINT EXPLIST ... */
		/*************************************/
		if (expList != null) expList.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nVAR FUNC\n(%s)",name));
			
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException {

		TYPE t1 = null;
		if (var != null) t1 = var.SemantMe();

		if (!t1.isClass()) {
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		TYPE t2 = null;
		if (name != null) t2 = ((TYPE_CLASS) t1).findMembers(name, true);
		if (t2 == null) {
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		t2 = ((TYPE_CLASS_VAR_DEC)t2).t;
		this.startLabel = ((TYPE_FUNCTION)t2).startLabel;
		this.numOfParams = ((TYPE_FUNCTION)t2).numOfParams;
		if (((TYPE_FUNCTION)t2).belongsToClass != null) {
			this.numOfParams++;
			this.isMethod = true;
			this.virTableOffset = ((TYPE_FUNCTION)t2).virTableOffset;
		}

		TYPE_LIST parmstypes = ((TYPE_FUNCTION) t2).params;
		AST_EXP_LIST p = expList;

		while (p != null && parmstypes != null) {
			AST_EXP parm = p.head;
			TYPE expectedType = parmstypes.head;

			TYPE actualType = parm.SemantMe();

			if (expectedType.isClass()) {
				if (!(((TYPE_CLASS) expectedType).isAncestor((TYPE_CLASS) actualType) || TYPE_NIL.getInstance() == actualType)) {
					throw new RuntimeException(String.valueOf(this.line_number));
				}
			} else if (expectedType.isArray()) {
				if (!(actualType.isArray() || TYPE_NIL.getInstance() == actualType)) {
					throw new RuntimeException(String.valueOf(this.line_number));
				}
				if (((TYPE_ARRAY) expectedType).type != ((TYPE_ARRAY) actualType).type) {
					throw new RuntimeException(String.valueOf(this.line_number));
				}
			} else {
				if (actualType != expectedType) {
					throw new RuntimeException(String.valueOf(this.line_number));
				}
			}
			expList = expList.tail;
			parmstypes = parmstypes.tail;
		}

		if (expList != null || parmstypes != null) {
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		SYMBOL_TABLE.getInstance().return_exist = true;
		return null;
		}

	@Override
	public TEMP IRme() {
		System.out.println("IRME IN AST_STMT_FUNC_VAR");

		if("PrintTrace".equals(name) && !this.isMethod) {
			IR.getInstance().Add_IRcommand(new IRCommandPrintTrace());
			return null;
		}

		AST_EXP_LIST paramsLst = this.expList;

		int prologSize = - 4 * ( 10+this.numOfParams );
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $sp,$sp,%d", prologSize), null, false));

		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $fp,0($sp)"), null, false));

		if (!this.name.contains("class_init")) {
			TEMP funcName = TEMP_FACTORY.getInstance().getFreshTEMP();
			if( ("PrintInt".equals(name) || "PrintString".equals(name)) &&  !this.isMethod )
				IR.getInstance().Add_IRcommand(new IR_general_command(String.format("la $t%%d,%s", this.name), funcName, true));
			else
				IR.getInstance().Add_IRcommand(new IR_general_command(String.format("la $t%%d,%s", "func_"+this.name), funcName, true));
			IR.getInstance().Add_IRcommand(new IRcommand_Store(funcName, -4));
		}

		int i = 0;
		if(this.isMethod) {
			TEMP ths;
			if(this.var != null) {
				AST_EXP_VAR ev = new AST_EXP_VAR(this.var, line_number);
				ths = ev.IRme();
				IR.getInstance().Add_IRcommand(new IRcommand_Check_For_Null_Pointer_Dereference(ths));
			} else {
				ths = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", ths, true));
			}
			IR.getInstance().Add_IRcommand(new IR_general_command("sw $t%d,4($sp)", ths, false));
			i++;
		}

		if(i < this.numOfParams){
			TEMP argValue = paramsLst.head.IRme();
			IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $t%%d,%d($sp)", 4 * (i + 1)), argValue, false));
			i++;

			AST_EXP_LIST params = paramsLst.tail;
			for(; i < this.numOfParams; i++) {
				argValue = params.head.IRme();
				IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $t%%d,%d($sp)", 4 * (i + 1)), argValue, false));
				params= params.tail;
			}
		}

		IR.getInstance().Add_IRcommand(new IRcommandFrameBackup(this.numOfParams));
		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(this.numOfParams, false));
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $fp,$sp,0"), null, false));

		if(this.isMethod) {
			irStmtMethod();
		}
		else {
			if("PrintInt".equals(name) || "PrintString".equals(name) )
				IR.getInstance().Add_IRcommand(new IR_general_jump_command(name));
			else
				IR.getInstance().Add_IRcommand(new IR_general_jump_command(this.startLabel));
		}

		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(numOfParams, true));

		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IR_move_command(null, null, res, "$v0"));
		return res;
	}


	public void irStmtMethod(){
		TEMP t0 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t2 = TEMP_FACTORY.getInstance().getFreshTEMP();

		IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", t0, true));

		IR.getInstance().Add_IRcommand(new IRcommand_Load(t1, t0, 0, null, false));

		IR.getInstance().Add_IRcommand(new IRcommand_Load(t2, t1, this.virTableOffset * 4, null, false));

		IR.getInstance().Add_IRcommand(new IR_general_jump_command(t2));
	}
}


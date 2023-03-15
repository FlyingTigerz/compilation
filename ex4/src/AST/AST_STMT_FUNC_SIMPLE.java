package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public class AST_STMT_FUNC_SIMPLE extends AST_STMT
{
	/************************/
	/* simple variable name */
	/************************/
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
	public AST_STMT_FUNC_SIMPLE(String name, AST_EXP_LIST expList, int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== stmt -> ID( %s ) ( exps )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.expList = expList;
		this.line_number = line_number;
	}

	/**************************************************/
	/* The printing message for a simple FUNC AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE FUNC */
		/**********************************/
		System.out.format("AST NODE SIMPLE FUNC( %s ) ( expList )\n",name);

		/*************************************/
		/* RECURSIVELY PRINT EXPLIST ... */
		/*************************************/
		if (expList != null) expList.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nFUNC\n(%s)",name));
			
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException{

		TYPE t2 = null;
		if(name != null) t2 = SYMBOL_TABLE.getInstance().find(name);

		this.startLabel = ((TYPE_FUNCTION)t2).startLabel;
		this.numOfParams = ((TYPE_FUNCTION)t2).numOfParams;

		if(((TYPE_FUNCTION)t2).belongsToClass != null) {
			this.numOfParams++;
			this.isMethod = true;
			this.virTableOffset = ((TYPE_FUNCTION)t2).virTableOffset;
		}

		TYPE_LIST parmstypes = ((TYPE_FUNCTION)t2).params;
		AST_EXP_LIST p = expList;

		while(p != null && parmstypes != null){
			System.out.println("IN WHILE");
			AST_EXP parm = p.head;
			TYPE expectedType = parmstypes.head;

			TYPE actualType = parm.SemantMe();

			if(expectedType.isClass()){
				System.out.println("param is class");
				if(!(((TYPE_CLASS)expectedType).isAncestor((TYPE_CLASS)actualType) || TYPE_NIL.getInstance() == actualType)){
					throw new RuntimeException(String.valueOf(this.line_number));
				}
			}else if(expectedType.isArray()) {
				System.out.println("param is array");
				if(!(actualType.isArray() || TYPE_NIL.getInstance() == actualType)){
					throw new RuntimeException(String.valueOf(this.line_number));
				}
				if(((TYPE_ARRAY)expectedType).type != ((TYPE_ARRAY)actualType).type){
					throw new RuntimeException(String.valueOf(this.line_number));
				}
			} else{
				if(expectedType == TYPE_INT.getInstance() ){
					System.out.println("param is int");
				}
				System.out.println("param is int or string");
				if(actualType != expectedType){

					throw new RuntimeException(String.valueOf(this.line_number));
				}
			}
			p = p.tail;
			parmstypes = parmstypes.tail;
		}

		if (p != null || parmstypes != null) {
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		SYMBOL_TABLE.getInstance().return_exist = true;
		return null;
	}

	@Override
	public TEMP IRme() {
		System.out.println("IRME IN AST_STMT_FUNC_SIMPLE");


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
			ths = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", ths, true));
			IR.getInstance().Add_IRcommand(new IR_general_command("sw $t%d,4($sp)", ths, false));
			i++;
		}

		if(i < this.numOfParams){
			TEMP argValue = expList.head.IRme();
			IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $t%%d,%d($sp)", 4 * (i + 1)), argValue, false));
			i++;

			AST_EXP_LIST params = expList.tail;
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
			System.out.println("IS METHOD");
		}
		else {
			System.out.println("IS NOT METHOD");
			if (startLabel == null){
				System.out.println("label is null");
			}
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

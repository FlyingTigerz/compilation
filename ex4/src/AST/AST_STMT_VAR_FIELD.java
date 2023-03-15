//package AST;
//
//import TYPES.*;
//import SYMBOL_TABLE.*;
//import IR.*;
//import TEMP.*;
//import MIPS.MIPSGenerator;
//
//public class AST_STMT_VAR_FIELD extends AST_STMT
//{
//	public AST_VAR var;
//	public String fieldName;
//	int line_number;
//	public String startLabel;
//	public int numOfParams;
//	public int virTableOffset;
//	boolean isMethod;
//
//	/******************/
//	/* CONSTRUCTOR(S) */
//	/******************/
//	public AST_STMT_VAR_FIELD(AST_VAR var,String fieldName, int line_number)
//	{
//		/******************************/
//		/* SET A UNIQUE SERIAL NUMBER */
//		/******************************/
//		SerialNumber = AST_Node_Serial_Number.getFresh();
//
//		/***************************************/
//		/* PRINT CORRESPONDING DERIVATION RULE */
//		/***************************************/
//		System.out.format("====================== stmt -> var DOT ID( %s ) ()\n",fieldName);
//
//		/*******************************/
//		/* COPY INPUT DATA NENBERS ... */
//		/*******************************/
//		this.var = var;
//		this.fieldName = fieldName;
//		this.line_number = line_number;
//	}
//
//	/*************************************************/
//	/* The printing message for a STMT field var AST node */
//	/*************************************************/
//	public void PrintMe()
//	{
//		/*********************************/
//		/* AST NODE TYPE = AST STMT FIELD VAR */
//		/*********************************/
//		System.out.print("AST NODE STMT FIELD VAR\n");
//
//		/**********************************************/
//		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
//		/**********************************************/
//		if (var != null) var.PrintMe();
//		System.out.format("FIELD NAME( %s )\n",fieldName);
//
//		/***************************************/
//		/* PRINT Node to AST GRAPHVIZ DOT file */
//		/***************************************/
//		AST_GRAPHVIZ.getInstance().logNode(
//			SerialNumber,
//			String.format("STMT\nFIELD VAR\n...->%s",fieldName));
//
//		/****************************************/
//		/* PRINT Edges to AST GRAPHVIZ DOT file */
//		/****************************************/
//		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
//	}
//
//	public TYPE SemantMe() throws RuntimeException{
//
//		TYPE t1 = null;
//		if(var != null) t1 = var.SemantMe();
//
//		if(!t1.isClass()) {
//			throw new RuntimeException(String.valueOf(this.line_number));
//		}
//
//		TYPE t2 = null;
//		if(fieldName != null) t2 = ((TYPE_CLASS)t1).findMembers(fieldName, true);
//		if(t2 == null){
//			throw new RuntimeException(String.valueOf(this .line_number));
//		}
//
//		this.startLabel = t2.startLabel;
//		this.numOfParams = t2.numOfParams;
//		if(function.belongsToClass != null) {
//			this.numOfParams++;
//			this.isMethod = true;
//			this.virTableOffset = t2.virTableOffset;
//		}
//
//		return null;
//	}
//
//	@Override
//	public TEMP IRme() {
//
//		if("PrintTrace".equals(name) && !this.isMethod) {
//			IR.getInstance().Add_IRcommand(new IRCommandPrintTrace());
//			return null;
//		}
//
//		AST_EXP_PARAMS paramsLst = this.params;
//
//		int prologSize = - 4 * ( 10+this.numOfParams );
//		IR.getInstance().Add_IRcommand(new IR.IR_general_command(String.format("addi $sp,$sp,%d", prologSize), null, false));
//
//		IR.getInstance().Add_IRcommand(new IR.IR_general_command(String.format("sw $fp,0($sp)"), null, false));
//
//		if (!this.id.contains("class_init")) {
//			TEMP funcName = TEMP_FACTORY.getInstance().getFreshTEMP();
//			if( ("PrintInt".equals(name) || "PrintString".equals(name)) &&  !this.isMethod )
//				IR.getInstance().Add_IRcommand(new IR.IR_general_command(String.format("la $t%%d,%s", this.id), funcName, true));
//			else
//				IR.getInstance().Add_IRcommand(new IR.IR_general_command(String.format("la $t%%d,%s", "func_"+this.id), funcName, true));
//			IR.getInstance().Add_IRcommand(new IR.IRcommand_Store(funcName, -4));
//		}
//
//		int i = 0;
//		if(this.isMethod) {
//			TEMP ths;
//			if(this.var != null) {
//				AST_EXP_VAR ev = new AST_EXP_VAR(this.var, lineNumber);
//				ths = ev.IRme();
//				IR.getInstance().Add_IRcommand(new IRcommand_Check_For_Null_Pointer_Dereference(ths));
//			} else {
//				ths = TEMP_FACTORY.getInstance().getFreshTEMP();
//				IR.getInstance().Add_IRcommand(new IR.IR_general_command("lw $t%d, 4($fp)", ths, true));
//			}
//			IR.getInstance().Add_IRcommand(new IR.IR_general_command("sw $t%d,4($sp)", ths, false));
//			i++;
//		}
//
//		if(i < this.numOfParams){
//			TEMP argValue = paramsLst.head.IRme();
//			IR.getInstance().Add_IRcommand(new IR.IR_general_command(String.format("sw $t%%d,%d($sp)", 4 * (i + 1)), argValue, false));
//			i++;
//
//			AST_EXP_PARAMS_LIST params = paramsLst.tail;
//			for(; i < this.numOfParams; i++) {
//				argValue = params.head.IRme();
//				IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $t%%d,%d($sp)", 4 * (i + 1)), argValue, false));
//				params= params.tail;
//			}
//		}
//
//		IR.getInstance().Add_IRcommand(new IRcommandFrameBackup(this.numOfParams));
//		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(this.numOfParams, false));
//		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $fp,$sp,0"), null, false));
//
//		if(this.isMethod) {
//			irStmtMethod();
//		}
//		else {
//			if("PrintInt".equals(fieldName) || "PrintString".equals(fieldName) )
//				IR.getInstance().Add_IRcommand(new IR_general_jump_command(fieldName));
//			else
//				IR.getInstance().Add_IRcommand(new IR_general_jump_command(this.startLabel));
//		}
//
//		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(numOfParams, true));
//
//		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();
//		IR.getInstance().Add_IRcommand(new IR_move_command(null, null, res, "$v0"));
//		return res;
//	}
//
//
//	public void irStmtMethod(){
//		TEMP t0 = TEMP_FACTORY.getInstance().getFreshTEMP();
//		TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
//		TEMP t2 = TEMP_FACTORY.getInstance().getFreshTEMP();
//
//		IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", t0, true));
//
//		IR.getInstance().Add_IRcommand(new IRcommand_Load(t1, t0, 0, null, false));
//
//		IR.getInstance().Add_IRcommand(new IRcommand_Load(t2, t1, this.virTableOffset * 4, null, false));
//
//		IR.getInstance().Add_IRcommand(new IR_general_jump_command(t2));
//	}
//}

package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	public int line_number;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
		this.line_number =line_number;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException{
		TYPE t1 = null;
		TYPE t2 = null;
		if (var != null) t1 = var.SemantMe();
		if (subscript != null) t2 = subscript.SemantMe();

		if(t1 == null){
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		if(!t1.isArray() || t2 != TYPE_INT.getInstance()){
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		return ((TYPE_ARRAY)t1).type;
	}

	public TEMP IRme()
	{
		System.out.println("IRME IN AST_VAR_SUBSCRIPT");

		TEMP arrayAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP arraySize = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP zeroReg = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();

		String not_too_small = IRcommand.getFreshLabel("Not_Too_Small");
		String not_too_big = IRcommand.getFreshLabel("Not_Too_Big");

		TEMP subscriptIR = subscript.IRme();

		TEMP varIR = var.IRme() ;

		IR.getInstance().Add_IRcommand(new IRcommand_Load(arrayAddress, varIR));
		IR.getInstance().Add_IRcommand(new IRcommand_Check_For_Null_Pointer_Dereference(arrayAddress));

		IR.getInstance().Add_IRcommand(new IRcommand_Load(arraySize, arrayAddress));
		IR.getInstance().Add_IRcommand(new IR_branch_less(subscriptIR, arraySize, not_too_big, false));
		IR.getInstance().Add_IRcommand(new IRcommand_Runtime_error("string_access_violation"));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(not_too_big));

		IR.getInstance().Add_IRcommand(new IRcommandConstInt(zeroReg, 0));
		IR.getInstance().Add_IRcommand(new IR_branch_less(zeroReg, subscriptIR, not_too_small, true));
		IR.getInstance().Add_IRcommand(new IRcommand_Runtime_error("string_access_violation"));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(not_too_small));

		IR.getInstance().Add_IRcommand(new IRcommand_array_access(res, arrayAddress, subscriptIR));

		return res;
	}
}

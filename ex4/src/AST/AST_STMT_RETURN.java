package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;


public class AST_STMT_RETURN extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP exp;
	int line_number;
	String funcEndLabel;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp, int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp != null) System.out.print("====================== stsmt -> RETURN exp;\n");
		if (exp == null) System.out.print("====================== stsmt -> RETURN ;\n");

		this.exp = exp;
		this.line_number = line_number;
	}


	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE RETURN STMT				 */
		/*************************************/
		System.out.print("AST NODE STMT RETURN\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (exp != null) exp.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nRETURN\n");

		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}


	public TYPE SemantMe() throws RuntimeException{
		this.funcEndLabel = SYMBOL_TABLE.getInstance().getCurrentEndLabel();
		if (this.funcEndLabel == null)
			System.out.println("ERROR 9");
		TYPE t = null;
		TYPE expec_type = SYMBOL_TABLE.getInstance().funcRetType;

		if (exp != null){
			t = exp.SemantMe();

			if( expec_type.isClass() && ((t.isClass()  && ((TYPE_CLASS)expec_type).isAncestor((TYPE_CLASS)t)) || t == TYPE_NIL.getInstance())
					&& SYMBOL_TABLE.getInstance().func_scope ){
				SYMBOL_TABLE.getInstance().return_exist = true;
				return null;
			}
			if( expec_type.isArray() && ((t.isArray() && ((TYPE_ARRAY)t).type == ((TYPE_ARRAY)expec_type).type)
					|| t == TYPE_NIL.getInstance())
					&& SYMBOL_TABLE.getInstance().func_scope ){
				SYMBOL_TABLE.getInstance().return_exist = true;
				return null;
			}
			if( expec_type == t
					&& SYMBOL_TABLE.getInstance().func_scope ){
				SYMBOL_TABLE.getInstance().return_exist = true;
				return null;
			}

		}
		else if (exp == null){
			if(SYMBOL_TABLE.getInstance().funcRetType == TYPE_VOID.getInstance() && SYMBOL_TABLE.getInstance().func_scope) {
				SYMBOL_TABLE.getInstance().return_exist = true;
				return null;
			}
		}
		throw new RuntimeException(String.valueOf(this. line_number));
	}

	public TEMP IRme() {
		System.out.println("IRME IN AST_STMT_RETURN");

		if (exp != null) {
			TEMP expTemp = exp.IRme();
			IR.getInstance().Add_IRcommand(new IR_move_command("$v0", expTemp, null, null));
		}
		if (funcEndLabel == null)
			System.out.println("ERROR 8");
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(funcEndLabel));
		return null;
	}

}

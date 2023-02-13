package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.Objects;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(int line, AST_EXP cond,AST_STMT_LIST body)
	{
		this.line = ++line;
		this.cond = cond;
		this.body = body;
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> WHILE(exp){stmtList}\n");
	}
	/*********************************************************/
	/* Printed Text */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("WHILE STATEMENT\n");
		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("WHILE\ncond {body}"));
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
	public TYPE SemantMe() throws SemanticException {
		TYPE condType = cond.SemantMe();
		if(!Objects.equals(condType.name, TYPE_INT.getInstance().name)){
			System.out.format(">> ERROR [%d:%d] invalid condition\n",2,2);
			throw new SemanticException(this.line);
		}
	        /***************/
		/* begin scope */
		/***************/
		SYMBOL_TABLE.getInstance().beginScope();
		TYPE_LIST bodyType = body.SemantMe();
	        /***************/
		/* end scope */
		/***************/
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}
	public TEMP IRme()
	{
		int labelCounter = IR.getInstance().getLabelIndex();
		String endWhileLabel = "WHILE_FALSE"+labelCounter;
		String whileLabel = "WHILE_TRUE"+labelCounter;
		IR.getInstance().Add_IRcommand(new IRcommand_Label(whileLabel));
		TEMP t1 = cond.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(t1, endWhileLabel));
		body.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(whileLabel));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(endWhileLabel));
		return null;
	}
}

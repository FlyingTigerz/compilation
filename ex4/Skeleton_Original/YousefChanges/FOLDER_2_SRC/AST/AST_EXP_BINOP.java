package AST;
import IR.*;
import TEMP.*;
import TYPES.*;

import java.util.Objects;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	String sOP;
	public AST_EXP left;
	public AST_EXP right;
	TYPE leftType ;
	TYPE rightType;

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(int line, AST_EXP left,AST_EXP right,int OP)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.leftType = null;
		this.rightType = null;


		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}

	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}
	public TYPE SemantMe() throws SemanticException
	{
		TYPE left_type = left.SemantMe();
		TYPE right_type = right.SemantMe();

		// for IRme usage
		leftType = left_type;
		rightType = right_type;
		this.semanticLabel = TYPE_INT.getInstance();
		/****************************************/
		/* Type check for equality testing */
		/****************************************/
		if(Objects.equals(sOP, "=")){
			if(!left_type.isInstanceOf(right_type)){
				System.out.format(">> ERROR [%d:%d] cannot perform operation on types %s %s %s\n",2,2,left_type.name, sOP, right_type.name);
				throw new SemanticException(this.line);
			}
		}
		/****************************************/
		/* Type check for addition/concatenation */
		/****************************************/
		else if(Objects.equals(sOP, "+")){
			if(
					!(left_type.isInstanceOf(TYPE_INT.getInstance()) && right_type.isInstanceOf(TYPE_INT.getInstance()))
					&& !(left_type.isInstanceOf(TYPE_STRING.getInstance()) && right_type.isInstanceOf(TYPE_STRING.getInstance()))
			){
				System.out.format(">> ERROR [%d:%d] cannot perform operation on types %s %s %s\n",2,2,left_type.name, sOP, right_type.name);
				throw new SemanticException(this.line);
			}
			if(left_type.isInstanceOf(TYPE_STRING.getInstance()) && right_type.isInstanceOf(TYPE_STRING.getInstance())){
				this.semanticLabel = TYPE_STRING.getInstance();
			}
		}
		/****************************************/
		/* Type check for all other BINOPs */
		/****************************************/
		else{
			if(left_type == null){
				System.out.print("");
			}
			if(
					!(left_type.isInstanceOf(TYPE_INT.getInstance()) && right_type.isInstanceOf(TYPE_INT.getInstance()))
			){
				System.out.format(">> ERROR [%d:%d] cannot perform operation on types %s %s %s\n",2,2,left_type.name, sOP, right_type.name);
				throw new SemanticException(this.line);
			}
		}
		/****************************************/
		/* Division by 0 check */
		/****************************************/
		if(Objects.equals(sOP, "/")){
			if(right instanceof AST_EXP_INT){
				if(((AST_EXP_INT) right).value == 0){
					System.out.format(">> ERROR [%d:%d] division by 0\n",2,2);
					throw new SemanticException(this.line);
				}
			}
		}
		return this.semanticLabel;
	}
	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();

		if (OP == 0)
		{
			if (leftType.isInstanceOf(TYPE_STRING.getInstance()) && rightType.isInstanceOf(TYPE_STRING.getInstance())){
/*
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_Concatenate_Strings(dst,t1,t2));*/
			}
			else{
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
				IR.getInstance().truncate_int(dst);
			}

		}
		if (OP == 1)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
			IR.getInstance().truncate_int(dst);
		}
		if (OP == 2)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
			IR.getInstance().truncate_int(dst);
		}
		if (OP == 3)
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
		}
		if (OP == 4)
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		if (OP == 5)
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
		}
		if (OP == 6) {
			if (leftType.isInstanceOf(TYPE_STRING.getInstance()) && rightType.isInstanceOf(TYPE_STRING.getInstance())) {

				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst, t1, t2));
			} else {
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, t1, t2));
			}
		}

		return dst;
	}
}

package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public class AST_EXP_BINOP extends AST_EXP
{
	public char OP;
	public AST_EXP left;
	public AST_EXP right;
	public int line_number;
	boolean stringsOperator;


	public boolean IS_BINOP(){return true;}

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,char OP,int line_number)
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
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.line_number = line_number;
		this.stringsOperator = false;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		//if (OP != null) OP.PrintMe();
		if (right != null) right.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"exp BINOP exp\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		//if (OP  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,OP.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException
	{
		TYPE t1 = null;
		TYPE t2 = null;

		if (left  != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();
		System.out.println(this.OP);
		if(t1 == null ){
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance()) )
		{
			return TYPE_INT.getInstance();
		}
		else if(t1 == TYPE_STRING.getInstance() && t2 == TYPE_STRING.getInstance() && OP == '+'){
			this.stringsOperator = true;
			return TYPE_STRING.getInstance();
		}else if((OP == '=') && (t1 == t2 ||
				(t1.isClass() && t2.isClass() &&
						(((TYPE_CLASS)t1).isAncestor((TYPE_CLASS)t2) || ((TYPE_CLASS)t2).isAncestor((TYPE_CLASS)t1))))){
			return TYPE_INT.getInstance();
		}else if((OP == '=') && (((t1.isArray() ||t1.isClass()) && t2 == TYPE_NIL.getInstance()) ||
				((t2.isArray() ||t2.isClass()) && t1 == TYPE_NIL.getInstance()))){
			return TYPE_INT.getInstance();
		}else if((OP == '=') && t1.isArray() && t2.isArray() && ((TYPE_ARRAY)t1).type == ((TYPE_ARRAY)t2).type)
			return TYPE_INT.getInstance();
		throw new RuntimeException(String.valueOf(this.line_number));
	}

	public TEMP IRme()
	{
		System.out.println("IRME IN AST_EXP_BINOP");


		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();


		if (OP == '+')
		{
			if (!this.stringsOperator) {
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
			}
			else {
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_Concat_String(dst, t1, t2));
			}
		}
		else if (OP == '-') // -
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
		}

		else if (OP == '*') // *
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
		}
		else if (OP == '/') // /
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
		}
		else if (OP == '<') // <
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		else if (OP == '>') // >
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
		}
		else if (OP == '=')
		{
			if (!this.stringsOperator) {
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst,t1,t2));
			}
			else {
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_EQ_String(dst, t1, t2));
			}
		}

		return dst;
	}



}

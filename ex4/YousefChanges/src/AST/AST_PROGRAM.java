package AST;
import IR.IR;
import IR.IRcommand_Jump_Label;
import IR.IRcommand_Jump_And_Link;
import IR.IRcommand_Label;
import TEMP.TEMP;
import TYPES.*;

public class AST_PROGRAM extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC head;
	public AST_PROGRAM tail;
	static public boolean root = true;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PROGRAM(int line, AST_DEC head, AST_PROGRAM tail)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== program -> dec program\n");
		if (tail == null) System.out.print("====================== program -> dec      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.head = head;
		this.tail = tail;
	}

	public TYPE SemantMe() throws SemanticException
	{
		if (head != null) head.SemantMe();
		if (tail != null) tail.SemantMe();
		return null;
	}

	public TEMP IRme()
	{
		boolean imRoot = false;
		if(root) {
			imRoot = true;
			root = false;
		}
		/* IR Prefix */
		if(imRoot) {
			IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label("main"));
		}
		/* IR Body */
		if (head != null) head.IRme();
		if (tail != null) tail.IRme();

		/* IR Suffix */
		if(imRoot) {
			IR.getInstance().Add_IRcommand(new IRcommand_Label("main"));
			IR.getInstance().Add_IRcommand(new IRcommand_Jump_And_Link(IR.funcLabelPrefix + "main"));
		}
		return null;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE PROGRAM\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) {
			head.PrintMe();
		}
		if (tail != null) tail.PrintMe();
		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PROGRAM\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	
}

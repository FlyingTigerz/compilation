package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_C_FIELD_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_C_FIELD head;
	public AST_C_FIELD_LIST tail;
	public int lineNumber;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD_LIST(AST_C_FIELD head,AST_C_FIELD_LIST tail,int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== cFields -> cField cFields\n");
		if (tail == null) System.out.print("====================== cFields -> cField      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
		this.lineNumber=lineNumber;
	}

	/******************************************************/
	/* The printing message for a c field list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST C FIELD LIST */
		/**************************************/
		System.out.print("AST NODE C FIELD LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	public TYPE SemantMe(){
		SYMBOL_TABLE.getInstance().lineNumber++;
		if(tail!=null){//not the last member of the c-field list
			return new TYPE_CLASS_VAR_DEC_LIST((TYPE_CLASS_VAR_DEC)head.SemantMe(),(TYPE_CLASS_VAR_DEC_LIST)tail.SemantMe());
		}
		//last member of the cfield list
		return new TYPE_CLASS_VAR_DEC_LIST((TYPE_CLASS_VAR_DEC)head.SemantMe(),null);
	}
	
}

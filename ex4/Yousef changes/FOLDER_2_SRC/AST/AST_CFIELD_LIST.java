package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public int line;
	public AST_CFIELD head;
	public AST_CFIELD_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_LIST(int line, AST_CFIELD head, AST_CFIELD_LIST tail)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== fields -> field fields\n");
		if (tail == null) System.out.print("====================== fields -> field      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.line = ++line;
		this.head = head;
		this.tail = tail;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE CFIELD LIST\n");

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
	public TYPE_CLASS_VAR_DEC_LIST SemantMe() throws SemanticException {
		TYPE_CLASS_VAR_DEC_LIST reverse_type_list = null;
		TYPE_CLASS_VAR_DEC_LIST type_list = null;
		SYMBOL_TABLE_ENTRY t;

		/* TODO - make sure there are no duplicate field names*/
		AST_CFIELD_LIST it = this;
		while(it != null){
			TYPE fieldType = it.head.SemantMe();
			reverse_type_list = new TYPE_CLASS_VAR_DEC_LIST(new TYPE_CLASS_VAR_DEC(fieldType, it.head.name), reverse_type_list);
			it = it.tail;
		}
		/* reverse type list to preserve original argument order */
		TYPE_CLASS_VAR_DEC_LIST r_it = reverse_type_list;
		while (r_it  != null) {
			type_list = new TYPE_CLASS_VAR_DEC_LIST(r_it.head, type_list);
			r_it = r_it.tail;
		}

		return type_list;
	}

	public TEMP_LIST IRme(){
		TEMP_LIST field_temps = new TEMP_LIST();
		for (AST_CFIELD_LIST it=this; it != null; it=it.tail) {
			field_temps.append(it.head.IRme());
		}
		return field_temps;
	}
}

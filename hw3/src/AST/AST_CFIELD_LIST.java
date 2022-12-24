package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_LIST extends AST_Node
{
	AST_CFIELD first;
	AST_CFIELD_LIST restoflist;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_LIST(int LineNum,AST_CFIELD first,AST_CFIELD_LIST restoflist)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (restoflist != null) System.out.print("====================== exps -> exp, exps\n");
		if (restoflist == null) System.out.print("====================== exps -> exp      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.first = first;
		this.restoflist=restoflist;
		this.LineNum=++LineNum;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		if (first != null) first.PrintMe();
		if (restoflist != null) restoflist.PrintMe();

		/************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nLIST\n");
		
		/**************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/**************/
		if (first != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,first.SerialNumber);
		if (restoflist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,restoflist.SerialNumber);	
		String sOP="";		
	}
	public TYPE_CLASS_VAR_DEC_LIST SemantMe() throws semanticExc {
		TYPE_CLASS_VAR_DEC_LIST reverse_type_list = null;
		TYPE_CLASS_VAR_DEC_LIST type_list = null;
		SYMBOL_TABLE_ENTRY t;

		/* TODO - make sure there are no duplicate field names*/
		AST_CFIELD_LIST it = this;
		while(it != null){
			TYPE fieldType = it.first.SemantMe();
			reverse_type_list = new TYPE_CLASS_VAR_DEC_LIST(new TYPE_CLASS_VAR_DEC(fieldType, it.first.varDec.name), reverse_type_list);
			it = it.restoflist;
		}
		/* reverse type list to preserve original argument order */
		TYPE_CLASS_VAR_DEC_LIST r_it = reverse_type_list;
		while (r_it  != null) {
			type_list = new TYPE_CLASS_VAR_DEC_LIST(r_it.head, type_list);
			r_it = r_it.tail;
		}

		return type_list;
	}
}

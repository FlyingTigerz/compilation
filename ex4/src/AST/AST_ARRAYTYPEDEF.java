package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ARRAYTYPEDEF extends AST_DEC
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public AST_TYPE type;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ARRAYTYPEDEF(int LineNum,String name,AST_TYPE type )
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== arrayTypedefDec -> array ID = type[];\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.LineNum=++LineNum;
		this.name = name;
		this.type=type;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE ARRAY TYPEDEF( %s )\n",name);
		if(type!=null){
			type.PrintMe();
		}
		if (name != null) System.out.format("%s\n", name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("array %s = type[];\n", name));
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
	}
	public TYPE_ARRAY SemantMe() throws semanticExc
	{
		TYPE t = type.SemantMe();
		TYPE_ARRAY at = new TYPE_ARRAY(name,t);

		/**************************************/
		/* [1] Check That Name is available */
		/**************************************/
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name);
		if (prevDec != null)
		{
			SYMBOL_TABLE_ENTRY scope = SYMBOL_TABLE.getInstance().getScope();
			/* print error only if declaration shadows a previous declaration in the same scope*/
			if(scope.prevtop_index < prevDec.prevtop_index) {
				System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, name);
				throw new semanticExc(this.LineNum);
			}
		}

		/***************************************************/
		/* [2] Enter the Array Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name, at);
		this.se=at;
		return at;
	}

}

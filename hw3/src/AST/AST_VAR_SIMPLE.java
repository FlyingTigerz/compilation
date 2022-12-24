package AST;
import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(int LineNum,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);
		System.out.format("waaaaaaaaaaaAAAAAAAA");
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.LineNum=++LineNum;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}
	public TYPE SemantMe() throws semanticExc {
		/**************************************/
		/* [2] Check That Identifier Exists */
		/**************************************/
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name);
		if (prevDec == null)
		{
			/* print error only if declaration shadows a previous declaration in the same scope*/
			System.out.format(">> ERROR [%d:%d] variable %s does not exist in scope\n", 2, 2, name);
			throw new semanticExc(this.LineNum);
		}
		return prevDec.type;
	}
}

package AST;

public class AST_ARRAYTYPEDEF extends AST_Node
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public AST_TYPE type;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ARRAYTYPEDEF(String name,AST_TYPE type )
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);
		System.out.format("WE ARE HERE");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
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
		System.out.format("AST NODE ARRAY TYPE DEF( %s )\n",name);
		System.out.format("WE ARE HERE");
		if(type!=null){
			type.PrintMe();
		}	
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}
}

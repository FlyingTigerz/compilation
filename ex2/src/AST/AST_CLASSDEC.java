package AST;

public class AST_CLASSDEC extends AST_Node
{
	public String name1;
	public String name2;
	public AST_CFIELD_LIST astfieldlist;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CLASSDEC(String name1,String name2 ,AST_CFIELD_LIST astfieldlist)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name1 = name1;
		this.name2=name2;
		this.astfieldlist=astfieldlist;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (astfieldlist != null) astfieldlist.PrintMe();
		if(name1!=name2){
		System.out.format("AST NODE ID AST_TYPE_EXP_NEWE ( %s )\n",name1);
		System.out.format("AST NODE ID AST_TYPE_EXP_NEWE ( %s )\n",name2);
		}	
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (astfieldlist != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,astfieldlist.SerialNumber);}
			
	}
}

package AST;

public class AST_VARDEC extends AST_STMT
{
	
	public AST_TYPE type;	
	public AST_EXP exp;
	public AST_NEWEXP ne;
	public String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC(AST_TYPE type,AST_EXP exp,AST_NEWEXP ne,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.ne=ne;
		this.exp=exp;
		this.name=name;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
		if(ne!=null)ne.PrintMe();
		if(exp!=null)exp.PrintMe();
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);}
		if (ne != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ne.SerialNumber);}
		if (exp != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);}
			
	}
}

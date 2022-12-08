package AST;

public class AST_FUNCDEC extends AST_STMT
{
	
	public AST_TYPE type;	
	public AST_STMT_LIST stmtlist;
	public AST_VAR_LIST varlist;
	String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNCDEC(AST_TYPE type,AST_STMT_LIST stmtlist,AST_VAR_LIST varlist,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.stmtlist=stmtlist;
		this.varlist=varlist;
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
		if(stmtlist!=null)stmtlist.PrintMe();
		if(varlist!=null)varlist.PrintMe();
		System.out.format("AST NODE ID AST_TYPE_EXP_NEWE ( %s )\n",name);
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);}
		if (stmtlist != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtlist.SerialNumber);}
		if (varlist != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varlist.SerialNumber);}
			
	}
}

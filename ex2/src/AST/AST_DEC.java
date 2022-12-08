package AST;

public class AST_DEC extends AST_Node
{
	public AST_VARDEC vd;
	public AST_FUNCDEC fd;
	public AST_CLASSDEC cd;
	public AST_ARRAYTYPEDEF atd;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC(AST_VARDEC vd,AST_FUNCDEC fd ,AST_CLASSDEC cd, AST_ARRAYTYPEDEF atd)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.vd = vd;
		this.fd=fd;
		this.cd=cd;
		this.atd=atd;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (vd != null) vd.PrintMe();
		if (fd != null) fd.PrintMe();
		if (cd != null) cd.PrintMe();
		if (atd != null) atd.PrintMe();
	
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (vd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vd.SerialNumber);}
		if (fd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fd.SerialNumber);}
		if (cd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cd.SerialNumber);}
		if (atd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,atd.SerialNumber);}
			
	}
}

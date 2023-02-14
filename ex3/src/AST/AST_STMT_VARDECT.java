package AST;

public class AST_STMT_VARDECT extends AST_STMT
{
	/************************/
	/* simple variable name */
	/************************/
	public AST_DEC_VAR vard;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_VARDECT(int LineNum,AST_DEC_VAR vard )
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.vard =vard ;
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
		if(vard!=null){
			vard.PrintMe();
		}	
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (vard != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vard.SerialNumber);
	}
}

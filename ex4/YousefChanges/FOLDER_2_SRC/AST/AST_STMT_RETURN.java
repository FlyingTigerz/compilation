package AST;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_RETURN extends AST_STMT
{
	public AST_EXP val;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(int line, AST_EXP val)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.line = ++line;
		this.val = val;
	}
	/*********************************************************/
	/* Printed Text */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("RETURN STATEMENT\n");
		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (val != null) val.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("Return\nstatement"));
		if (val != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,val.SerialNumber);

	}
	public TYPE SemantMe() throws SemanticException
	{
		if(val != null){
			val.SemantMe();
		}
		return null;
	}
	public TEMP IRme(){
		TEMP res = null;
		if(val!=null){
			res = val.IRme();
		}
		IR.getInstance().Add_IRcommand(new IRcommand_Return(res));
		return res;
	}
}

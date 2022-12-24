package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
public class AST_STMT_RETURN extends AST_STMT
{
	public AST_EXP cond;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(int LineNum,AST_EXP cond)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.LineNum = ++LineNum;
		this.cond = cond;
	}
	public void PrintMe(){
		if (cond!= null) cond.PrintMe();
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("Return\nstatement"));
		if (cond!= null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);

	}
	public TYPE SemantMe() throws semanticExc{
		
	}

}

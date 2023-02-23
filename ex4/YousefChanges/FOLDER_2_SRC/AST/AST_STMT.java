package AST;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_STMT extends AST_Node
{
	public TYPE SemantMe() throws SemanticException {return null;}
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE\n");
	}
	public TEMP IRme(){return null;}
}

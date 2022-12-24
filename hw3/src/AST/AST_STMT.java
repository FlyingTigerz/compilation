package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_STMT extends AST_Node
{
	public TYPE SemantMe() throws semanticExc {return null;}
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE\n");
	}
}

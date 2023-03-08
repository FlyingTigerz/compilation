package AST;
import TYPES.*;
import TEMP.*;
import SYMBOL_TABLE.*;

public abstract class AST_DEC extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST DEC NODE");
	}
	public TYPE SemantMe() throws SemanticException {return null;}
	public TEMP IRme(){return null;}
}

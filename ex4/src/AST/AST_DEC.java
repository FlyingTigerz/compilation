package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public abstract class AST_DEC extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST DEC NODE");
	}
	public TYPE SemantMe() throws semanticExc {return null;}
	
	public TEMP IRme() { return null; }
}

package AST;
import TYPES.*;

public abstract class AST_DEC extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST declaration node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST DECLARATION NODE");
	}
	public TYPE SemantMe() throws RuntimeException{ return null;}

}

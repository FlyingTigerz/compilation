package AST;
import TYPES.*;
import TEMP.*;
import SYMBOL_TABLE.*;

public abstract class AST_CFIELD extends AST_Node
{
	public AST_TYPE type;
	public String name;
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST CLASS FIELD NODE");
	}
	public TYPE SemantMe() throws SemanticException {
		this.semanticLabel = type.SemantMe();
		return this.semanticLabel;
	}
	public TEMP IRme(){return null;}
}

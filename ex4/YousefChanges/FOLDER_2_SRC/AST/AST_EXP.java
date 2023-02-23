package AST;
import TEMP.TEMP;
import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_EXP extends AST_Node
{
	public TYPE SemantMe() throws SemanticException {return null;}
	public TEMP IRme()
	{
		return null;
	}
}

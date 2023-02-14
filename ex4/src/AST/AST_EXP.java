package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.TEMP;

public abstract class AST_EXP extends AST_Node
{
	public AST_TYPE type;
	public TYPE SemantMe() throws semanticExc {
return null;}
	public TEMP IRme()
	{
		return null;
	}
}

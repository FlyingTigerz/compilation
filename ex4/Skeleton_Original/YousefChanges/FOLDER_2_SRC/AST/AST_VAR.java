package AST;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_VAR extends AST_Node
{
    /* TODO: add SemantMe implementation to classes inheriting from AST_VAR */
    public TYPE SemantMe() throws SemanticException { return null; }
    public TEMP IRme() { return null; }
    /* for var side of assignments only: skip loading into new TEMP */
    public TEMP IRme(boolean storeInTemp) { return null; }
}

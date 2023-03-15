package AST;
import TYPES.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;
import CONTEXT.Context;

public abstract class AST_VAR extends AST_Node
{
    public Context context;
    public TYPE SemantMe() throws RuntimeException{return null;}
}


package AST;

import TYPES.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

public abstract class AST_EXP extends AST_Node
{
	public int moish;

	public abstract TEMP IRme();

	public boolean IS_BINOP(){return false;}

	public void PrintMe()
	{
		System.out.print("UNKNOWN AST EXP NODE");
	}
}

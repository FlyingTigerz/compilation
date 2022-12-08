package AST;

public class AST_STMT_RETURN extends AST_STMT
{
	public AST_EXP cond;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP cond)
	{
		this.cond = cond;
	}
}

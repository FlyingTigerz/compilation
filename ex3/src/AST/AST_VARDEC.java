package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VARDEC extends AST_STMT
{
	public AST_DEC_VAR adv;
	

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC(int LineNum, AST_DEC_VAR adv)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.adv=adv;
		this.LineNum=++LineNum;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/

	public TYPE SemantMe() throws semanticExc
	{
		return adv.SemantMe();
	}
}

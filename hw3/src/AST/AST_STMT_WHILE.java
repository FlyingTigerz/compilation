package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(int LineNum,AST_EXP cond,AST_STMT_LIST body)
	{
		this.cond = cond;
		this.body = body;
		this.LineNum=++LineNum;
		System.out.print("====================== stmt -> WHILE(exp){stmtList}\n");
	}
	public void PrintMe()
	{
		System.out.print("WHILE STATEMENT\n");

		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("WHILE\ncond {body}"));
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
	public TYPE SemantMe() throws semanticExc {
		TYPE condType = cond.SemantMe();
		if(!Objects.equals(condType.name, TYPE_INT.getInstance().name)){
			System.out.format(">> ERROR [%d:%d] invalid condition\n",2,2);
			throw new semanticExc(this.LineNum);
		}
		/***************/
		/* begin scope */
		/***************/
		SYMBOL_TABLE.getInstance().beginScope();
		TYPE_LIST bodyType = body.SemantMe();
		/***************/
		/* end scope */
		/***************/
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}


}

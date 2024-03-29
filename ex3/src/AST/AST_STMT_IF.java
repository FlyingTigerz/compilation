package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.Objects;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(int LineNum,AST_EXP cond,AST_STMT_LIST body)
	{

		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.cond = cond;
		this.body = body;
		this.LineNum=++LineNum;
		System.out.print("====================== stmt -> IF(exp){stmtList}\n");
	}
	public void PrintMe()
	{
		System.out.print("IF STATEMENT\n");

		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();


		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("IF\n(cond) {body}"));
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
	public TYPE SemantMe() throws semanticExc{
		TYPE condType = cond.SemantMe();
		if(!Objects.equals(condType.name, TYPE_INT.getInstance().name)){
			System.out.format(">> ERROR [%d:%d] invalid condition\n",2,2);
			throw new semanticExc(this.LineNum);
		}

		SYMBOL_TABLE.getInstance().beginScope();
		TYPE_LIST bodyType = body.SemantMe();

		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}

}
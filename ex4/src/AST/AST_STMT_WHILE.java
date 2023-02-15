package AST;
import SYMBOL_TABLE.*;
import TYPES.*;
import java.util.Objects;
import IR.*;
import TEMP.*;


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



	public TYPE SemantMe() throws semanticExc {
		System.out.print("WE ARE HERE");
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
	
	public TEMP IRme()
	{
		int labelCounter = IR.getInstance().getLabelIndex();
		String endWhileLabel = "WHILE_FALSE"+labelCounter;
		String whileLabel = "WHILE_TRUE"+labelCounter;
		IR.getInstance().Add_IRcommand(new IRcommand_Label(whileLabel));
		TEMP t1 = cond.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(t1, endWhileLabel));
		body.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(whileLabel));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(endWhileLabel));
		return null;
	}


}

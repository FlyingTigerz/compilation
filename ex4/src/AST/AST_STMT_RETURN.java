package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;
import java.util.Objects;

public class AST_STMT_RETURN extends AST_STMT
{
	public AST_EXP cond;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(int LineNum,AST_EXP cond)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.LineNum = ++LineNum;
		this.cond = cond;
	}
	public void PrintMe(){
		System.out.print("RETURN STATEMENT\n");
		if (cond!= null) cond.PrintMe();


		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("Return\nstatement"));
		if (cond!= null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);

	}
	public TYPE SemantMe() throws semanticExc{
		if(cond==null){
			if(SYMBOL_TABLE.getInstance().cur_func.returnType.name!="void"){
				System.out.print("not void return type\n");
				throw new semanticExc(this.LineNum);
			}
			return null;
		}
		TYPE condType = cond.SemantMe();
		if(!Objects.equals(SYMBOL_TABLE.getInstance().cur_func.returnType,condType)){
			System.out.format("func return type is %s , but return is %s\n",SYMBOL_TABLE.getInstance().cur_func.returnType.name,
					condType.name);
			throw new semanticExc(this.LineNum);
		}
		return condType;
	}

	public TEMP IRme(){
		TEMP res = null;
		if(cond!=null){
			res = cond.IRme();
		}
		IR.getInstance().Add_IRcommand(new IRcommand_Return(res));
		return res;
	}

}

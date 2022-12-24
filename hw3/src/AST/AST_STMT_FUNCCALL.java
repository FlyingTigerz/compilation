package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.Objects;
public class AST_STMT_FUNCCALL extends AST_STMT
{
	public String fname;
	public AST_VAR v;
	public AST_EXP_LIST l;

	/******/
	/* CONSTRUCTOR(S) */
	/******/
	public AST_STMT_FUNCCALL(int LineNum,String fname, AST_VAR v, AST_EXP_LIST l)
	{

		SerialNumber = AST_Node_Serial_Number.getFresh();


		System.out.print("====================== exp -> [obj.]f(expLst);\n");


		this.fname = fname;
		this.v = v;
		this.l = l;
		this.LineNum=++LineNum;
	}


	public void PrintMe()
	{
		System.out.print("AST NODE FUNC CALL STMT\n");

		if (fname != null) System.out.format("function %s", fname);
		if (v != null) v.PrintMe();
		if (l != null) l.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FUNCCALL(%s)",fname));


		if (l  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
		if (v  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
	}
	public TYPE SemantMe() throws semanticExc {
		SYMBOL_TABLE_ENTRY func = SYMBOL_TABLE.getInstance().find(fname);
		if(func == null || !(func.type instanceof TYPE_FUNCTION)){
			System.out.format(">> ERROR [%d:%d] can't find function %s\n",2,2, fname);
			throw new semanticExc(this.LineNum);
		}
		TYPE returnType =  (TYPE_FUNCTION)func.type;
		TYPE_LIST expectedParams = ((TYPE_FUNCTION) func.type).params;
		for (AST_EXP_LIST it=l; it != null; it=it.restoflist)
		{
			if(expectedParams == null){
				System.out.format(">> ERROR [%d:%d] too many arguments for function %s\n",2,2, fname);
				throw new semanticExc(this.LineNum);
			}
			String argType = it.first.SemantMe().name;
			String expected = expectedParams.head.name;
			if (!Objects.equals(argType, expected))
			{
				System.out.format(">> ERROR [%d:%d] argument type mismatch in %s: can't convert %s to %s \n",2,2, fname, argType, expected);
				throw new semanticExc(this.LineNum);
			}
			expectedParams = expectedParams.tail;
		}
		if(expectedParams != null){
			System.out.format(">> ERROR [%d:%d] function %s expect more arguments\n",2,2, fname);
			throw new semanticExc(this.LineNum);
		}
		return returnType;
	}

}

package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

import java.util.Objects;

public class AST_EXP_ID extends AST_EXP
{
	String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_ID(int LineNum,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> [obj.]f(expLst)\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name=name;
		this.LineNum=++LineNum;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		System.out.print("AST EXP ID\n");
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FUNCCALL(%s)",name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
			
	}
	public TYPE SemantMe() throws semanticExc {
		TYPE func = null;
		if(!(func instanceof TYPE_FUNCTION)){
			System.out.format(">> ERROR [%d:%d] can't find function %s\n",2,2, name);
			throw new semanticExc(this.LineNum);
		}
		TYPE returnType =  ((TYPE_FUNCTION) func).returnType;
		TYPE_LIST expectedParams = ((TYPE_FUNCTION) func).params;
		if(expectedParams != null){
			System.out.format(">> ERROR [%d:%d] function %s expect more arguments\n",2,2, name);
			throw new semanticExc(this.LineNum);
		}
		return returnType;
	}
}

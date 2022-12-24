package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VARDEC extends AST_DEC
{
	
	public AST_TYPE type;	
	public AST_EXP exp;
	public AST_NEWEXP ne;
	public String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC(int LineNum,AST_TYPE type,AST_EXP exp,AST_NEWEXP ne,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.ne=ne;
		this.exp=exp;
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
		if (type != null) type.PrintMe();
		if(ne!=null)ne.PrintMe();
		if(exp!=null)exp.PrintMe();
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("DEC\ntype %s = exp;",name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);}
		if (ne != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ne.SerialNumber);}
		if (exp != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);}
			
	}
	public TYPE SemantMe() throws semanticExc
	{
		TYPE t = type.SemantMe();

		/**************************************/
		/* [1] Check That Name is available */
		/**************************************/
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name);
		if (prevDec != null)
		{
			SYMBOL_TABLE_ENTRY scope = SYMBOL_TABLE.getInstance().getScope();
			/* print error only if declaration shadows a previous declaration in the same scope*/
			if(scope == null || scope.prevtop_index < prevDec.prevtop_index) {
				System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, name);
				throw new semanticExc(LineNum);
			}
		}

		/***************************************************/
		/* [2] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name, t);

		/***************************************************/
		/* [3] check assigned expression type validity */
		/***************************************************/
		if(exp != null && (exp.SemantMe() == null || !exp.SemantMe().isInstanceOf(t))){
			System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, exp.SemantMe().name, t.name);
			throw new semanticExc(LineNum);
		}

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}
}

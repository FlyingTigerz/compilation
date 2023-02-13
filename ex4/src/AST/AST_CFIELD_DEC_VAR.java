package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_DEC_VAR extends AST_CFIELD
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_EXP exp;
	
	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CFIELD_DEC_VAR(int line, AST_TYPE type, String name, AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cField -> type varName [assignExp [newExp] e];\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.LineNum = ++line;
		this.type = type;
		this.name = name;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST CFIELD NODE DEC VAR\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (type != null) type.PrintMe();
		if (name != null) System.out.format("%s\n", name);
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
				String.format("DEC\ntype %s = exp;",name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (name != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws semanticExc {
		System.out.format("%s,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n",type.name);
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
		if(exp != null && exp.SemantMe().name=="null" &&t.name!="int"&&t.name!="string")
		{
		return null;
		}
		/***************************************************/
		/* [3] check assigned expression type validity */
		/***************************************************/
		System.out.print(">> You are ok bro\n");
		if(exp != null && (exp.SemantMe() == null || !exp.SemantMe().isInstanceOf(t))){
			if(!exp.SemantMe().isInstanceOf(t)){
			if( exp.SemantMe().isClass() == true && ((TYPE_CLASS)exp.SemantMe()).father != null)
				System.out.format("!!!!!!!!!!HERE IS THE BUG3 !!!!!!!!!!!!!!!!!!!!!!!!!2 : %s is the name, %s is the father\n",  					exp.SemantMe().name, ((TYPE_CLASS)exp.SemantMe()).father.name);
			else
				System.out.format("!!!!!!!!!!HERE IS THE BUG3 !!!!!!!!!!!!!!!!!!!!!!!!!2 : %s is maidenless\n",  exp.SemantMe().name);
			}
			System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, exp.SemantMe().name, t.name);
			throw new semanticExc(LineNum);
		}

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return new TYPE_CLASS_VAR_DEC(t,name) ;
	}
}

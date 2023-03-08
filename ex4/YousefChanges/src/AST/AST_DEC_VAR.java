package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_VAR extends AST_DEC
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_TYPE type;
	public String name;
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_VAR(int line, AST_TYPE type, String name, AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec -> type ID [ASSIGN exp] SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
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
		System.out.print("AST NODE DEC VAR\n");

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

	public TYPE SemantMe() throws SemanticException
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
				throw new SemanticException(line);
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
			throw new SemanticException(line);
		}

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/

		this.semanticLabel = t;
		return t;
	}
	public TEMP IRme()
	{
		TEMP dataStorage = TEMP_FACTORY.getInstance().getFreshNamedTEMP(IR.globalVarPrefix + name);
		if(exp != null) {
			TEMP t = exp.IRme();
			if(exp instanceof AST_EXP_INT){
				IR.getInstance().Add_IRdata(new IRdata_Global_Var(dataStorage, ""+((AST_EXP_INT)exp).value));
			}
			else {
				IR.getInstance().Add_IRdata(new IRdata_Global_Var(dataStorage, t.toString()));
			}
		}
		return dataStorage;
	}
}

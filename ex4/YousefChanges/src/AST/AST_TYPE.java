package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE extends AST_Node
{
	public String typeName;
	TYPE type;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_TYPE(String typeName)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== type ->  %s \n", typeName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.typeName = typeName;
		switch (typeName) {
			case "void":
				this.type = TYPE_VOID.getInstance();
				break;
			case "int":
				this.type = TYPE_INT.getInstance();
				break;
			case "string":
				this.type = TYPE_STRING.getInstance();
				break;
		}
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.format("AST NODE TYPE ( %s )\n", typeName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("TYPE\n...->%s",type));
	}

	public TYPE SemantMe() throws SemanticException {
		SYMBOL_TABLE_ENTRY t;

		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = SYMBOL_TABLE.getInstance().find(this.typeName);
		if (t == null || t.type == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
			throw new SemanticException(this.line);
		}

		/** check that this is in fact declarable type (i.e. class, array, string, or int) **/
		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return t.type;
	}
}

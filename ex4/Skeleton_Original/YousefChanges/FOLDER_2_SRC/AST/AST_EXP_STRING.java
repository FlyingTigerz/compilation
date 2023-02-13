package AST;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_STRING extends AST_EXP
{
	public String value;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(int line, String value)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> STRING(\\\"%s\\\")\n", value);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
	 	/*******************************/
		this.line = ++line;
		this.value = value;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE STRING(\\\"%s\\\")\n",value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STRING(\\\"%s\\\")",value));
	}
	public TYPE SemantMe() {
		this.semanticLabel = TYPE_STRING.getInstance();
		return this.semanticLabel;
	}
	public TEMP IRme(){
		TEMP dataStorage = TEMP_FACTORY.getInstance().getFreshNamedTEMP(IR.strPrefix + IR.getInstance().getLabelIndex());
		IR.getInstance().Add_IRdata(new IRdata_Constant_String(dataStorage, value));
		return dataStorage;
	}
}

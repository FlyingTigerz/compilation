package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_NEW_EXP extends AST_EXP
{
	public AST_TYPE t;
	public AST_EXP e;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEW_EXP(int line, AST_TYPE t, AST_EXP e)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== newExp -> new type [[exp]]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.t = t;
		this.e = e;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE New Exp\n");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("New Exp")
		);
	}

	public TYPE SemantMe() throws SemanticException {
		if(e == null) {
			this.semanticLabel = t.SemantMe();
			return this.semanticLabel;
		}
		else{
			this.semanticLabel =  new TYPE_ARRAY(t.SemantMe());
			return this.semanticLabel;
		}
	}

	public TEMP IRme(){
		TEMP dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		if(e == null) {
			IR.getInstance().Add_IRcommand(new IRcommand_New_Class(dest, semanticLabel));
		}
		else{
			IR.getInstance().Add_IRcommand(new IRcommand_New_Array(dest, e.IRme()));
		}
		return dest;
	}
}

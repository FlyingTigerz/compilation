package AST;
import TYPES.*;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	public TEMP base;
	public TEMP offset;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(int line, AST_VAR var,AST_EXP subscript)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}
	public TYPE SemantMe() throws SemanticException {
		TYPE arr_type = var.SemantMe();
		TYPE exp_type = subscript.SemantMe();
		if(!arr_type.isArray()){
			System.out.format(">> ERROR [%d:%d] %s is not an array type\n", 2, 2, arr_type.name);
			throw new SemanticException(line);
		}
		if(!exp_type.isInstanceOf(TYPE_INT.getInstance())){
			System.out.format(">> ERROR [%d:%d] can't cast %s to int\n", 2, 2, exp_type.name);
			throw new SemanticException(line);
		}
		this.semanticLabel =  ((TYPE_ARRAY)arr_type).arrayType;
		return this.semanticLabel;
	}
	public TEMP IRme(){
		return this.IRme(true);
	}
	public TEMP IRme(boolean storeInTemp){
		this.base = var.IRme();
		this.offset = subscript.IRme();
		TEMP storeTo = null;
		if(storeInTemp) {
			storeTo = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_Array_Access(storeTo, this.base, offset));
		}
		return storeTo;
	}
}

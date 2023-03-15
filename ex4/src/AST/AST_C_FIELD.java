package AST;

import CONTEXT.Context;
import TEMP.TEMP;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_C_FIELD extends AST_Node 
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_VAR var;
	public AST_DEC_FUNC func;
	public int lineNumber;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD(AST_DEC_VAR var,AST_DEC_FUNC func,int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();



		this.var = var;
		this.func = func;
		this.lineNumber=lineNumber;
    }

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		System.out.format("AST NODE CFIELD\n");

		/***************************************/
		/* RECURSIVELY PRINT params + body ... */
		/***************************************/
        if (var != null) var.PrintMe();
        if(func != null) func.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST NODE CFIELD\n"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);		
        if(func != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,func.SerialNumber);		
	}
	public TYPE SemantMe()
	{

		if (var != null){
			System.out.println("In class inserting " + var.name);
			TYPE t = var.SemantMe();
			Context cn = SYMBOL_TABLE.getInstance().getContext(var.name);
			return new TYPE_CLASS_VAR_DEC(t, var.name, false, cn);
		}
		if (func != null){
			System.out.println("In class inserting " + func.func);
			return new TYPE_CLASS_VAR_DEC(func.SemantMe(), func.func, true, null);
		}
		return null;
	}

	public boolean isDataMember(){
		if (var != null)
			return true;
		return false;

	}
	public TEMP IRme() {
		if(func != null)
			return func.IRme();

		return null;
	}


}

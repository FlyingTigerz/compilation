package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;
import CONTEXT.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	int line_number;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
		this.line_number = line_number;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException{
		TYPE t1 = null;
		TYPE t2 = null;

		t1 = var.SemantMe();

		if(t1 == null){
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		if(!t1.isClass()){
			throw new RuntimeException(String.valueOf(SYMBOL_TABLE.getInstance().lineNumber));
		}

		t2 = ((TYPE_CLASS)t1).findMembers(fieldName, false);
		if(t2 == null){//if fieldName isnt a dta memeber of our class
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		this.context = ((TYPE_CLASS_VAR_DEC) t2).context;

		//this.context = SYMBOL_TABLE.getInstance().getContext(fieldName);;
		return ((TYPE_CLASS_VAR_DEC)t2).t;
	}

	public TEMP IRme()
	{
		System.out.println("IRME IN AST_VAR_FIELD");
		TEMP varAddress = this.var.IRme();
		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();
		System.out.println("IRME IN AST_VAR_FIELD");
		IR.getInstance().Add_IRcommand(new IRcommand_Load(res, varAddress, 0, this.context, true));
		return res;
	}
}

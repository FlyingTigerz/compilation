package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;
import CONTEXT.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	int line_number;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.line_number = line_number;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public TYPE SemantMe() throws RuntimeException{
		TYPE t1 = null;

		t1 = SYMBOL_TABLE.getInstance().find(name);

		if(t1 == null){
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		if(t1.isArray()){
			System.out.println("Array");
		}
		if(t1.isClass()){
			System.out.println("Class");
		}
//		this.context = ((TYPE_CLASS_VAR_DEC) t1).context;
		this.context =  SYMBOL_TABLE.getInstance().getContext(name);


		return t1;
	}
	@Override
	public TEMP IRme() {
		System.out.println("IRME IN AST_VAR_SIMPLE");
		if(context == null)
			System.out.println("context in AST_VAR is null");
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Load(t,null, 0,this.context,true));
		System.out.println(" END IRME IN AST_VAR_SIMPLE");

		return t;
	}

}

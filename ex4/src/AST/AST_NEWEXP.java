package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;


import java.util.Objects;

public class AST_NEWEXP extends AST_EXP
{
	/************************/
	/* simple variable name */
	/************************/
	public AST_TYPE t;
	public AST_EXP e;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEWEXP(int LineNum, AST_TYPE t,AST_EXP e )
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== t -> e\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.LineNum = ++LineNum;
		this.t =t ;
		this.e=e;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		if(t!=null){
			t.PrintMe();
		if(e!=null){
			e.PrintMe();
			}
		}	
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (t != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);
		if (e != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);	
	}
	
	public TYPE SemantMe() throws semanticExc {
		if(e == null) {
			this.se = t.SemantMe();
			return this.se;
		}
		else{
			this.se = new TYPE_ARRAY(t.SemantMe());
			return new TYPE_ARRAY(t.SemantMe());
		}
	}
	
	public TEMP IRme(){
		TEMP dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		if(e == null) {
			IR.getInstance().Add_IRcommand(new IRcommand_New_Class(dest, se));
		}
		else{
			IR.getInstance().Add_IRcommand(new IRcommand_New_Array(dest, e.IRme()));
		}
		return dest;
	}
	
	
	
}

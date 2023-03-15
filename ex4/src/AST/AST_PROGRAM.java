package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

import java.util.ArrayList;
import java.util.List;


public class AST_PROGRAM extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_LIST dlist;
	int line_number;
	public ArrayList<AST_DEC_VAR> globals = new ArrayList<>();

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PROGRAM(AST_DEC_LIST dlist,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (dlist != null) System.out.print("====================== Program -> decs\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.dlist = dlist;
		this.line_number = line_number;
	}

	/******************************************************/
	/* The printing message for a Program AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST PROGRAM */
		/**************************************/
		System.out.print("AST NODE PROGRAM\n");

		/*************************************/
		/* RECURSIVELY PRINT DECLIST ... */
		/*************************************/
		if (dlist != null) dlist.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PROGRAM\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (dlist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dlist.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException
	{
		dlist.SemantMe();
		return null;
	}

	public TEMP IRme() {
		System.out.println("IRME IN AST_PROGRAM");
		AST_DEC_FUNC.IRPrintInt();
		AST_DEC_FUNC.IRPrintString();


		AST_DEC_LIST decPtr = dlist;

		while (decPtr != null) {
			if (decPtr.head instanceof AST_DEC_VAR)
			{
				this.globals.add((AST_DEC_VAR)decPtr.head);
			}
			else
			{
				System.out.println("IRME FOR FUNCTIONS OR CLASSES");
				decPtr.head.IRme();
			}
			decPtr = decPtr.tail;
		}

		IR.getInstance().Add_IRcommand(new IRcommand_Label("main"));

		for (AST_DEC_VAR globalVar : this.globals)
		{
			System.out.println("Add global var");
			globalVar.IRme();
		}

		IR.getInstance().Add_IRcommand(new IR_general_jump_command("user_main"));
		return null;
	}

	
}

package AST;
import IR.*;
import TEMP.TEMP;
import TYPES.*;

import TYPES.TYPE;

public class AST_PROGRAM extends AST_Node
{
	public AST_DEC d;
	public AST_PROGRAM p;
	static public boolean root = true;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PROGRAM(int LineNum,AST_DEC d, AST_PROGRAM p)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();



		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.d = d;
		this.p=p;
		this.LineNum=++LineNum;
	}

	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (d != null) d.PrintMe();
		if (p != null) p.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"PROGRAM\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (d != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,d.SerialNumber);}
		if (p != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,p.SerialNumber);}

	}
	public TYPE SemantMe() throws semanticExc
	{
		if (d != null) d.SemantMe();
		if (p != null) p.SemantMe();

		return null;
	}
	
	public TEMP IRme()
	{
		boolean imRoot = false;
		if(root) {
			imRoot = true;
			root = false;
		}
		/* IR Prefix */
		if(imRoot) {
			IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label("main"));
		}
		/* IR Body */
		if (d != null) d.IRme();
		if (p != null) p.IRme();

		/* IR Suffix */
		if(imRoot) {
			IR.getInstance().Add_IRcommand(new IRcommand_Label("main"));
			IR.getInstance().Add_IRcommand(new IRcommand_Jump_And_Link(IR.funcLabelPrefix + "main"));
		}
		return null;
	}

}

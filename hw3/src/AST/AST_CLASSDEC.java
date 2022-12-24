package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CLASSDEC extends AST_Node
{
	public String name1;
	public String name2;
	public AST_CFIELD_LIST astfieldlist;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CLASSDEC(int LineNum,String name1,String name2 ,AST_CFIELD_LIST astfieldlist)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name1 = name1;
		this.name2=name2;
		this.astfieldlist=astfieldlist;
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
		if (astfieldlist != null) astfieldlist.PrintMe();
		if(name1!=name2){
		System.out.format("AST NODE ID AST_TYPE_EXP_NEWE ( %s )\n",name1);
		System.out.format("AST NODE ID AST_TYPE_EXP_NEWE ( %s )\n",name2);
		}	
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (astfieldlist != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,astfieldlist.SerialNumber);}
			
	}
	public TYPE_CLASS SemantMe() throws semanticExc {
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		SYMBOL_TABLE_ENTRY extending = null;

		/****************************/
		/* [1] Check if class extends valid class */
		/****************************/
		if(name2 != null) {
			extending = SYMBOL_TABLE.getInstance().find(name2);
			if (extending == null)
			{
				System.out.format(">> ERROR [%d:%d] non existing class %s\n",2,2,name2);
				throw new semanticExc(this.LineNum);
			}
		}

		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name1);
		if (prevDec != null)
		{
			SYMBOL_TABLE_ENTRY scope = SYMBOL_TABLE.getInstance().getScope();
			/* print error only if declaration shadows a previous declaration in the same scope*/
			if(scope.prevtop_index < prevDec.prevtop_index) {
				System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, name1);
				throw new semanticExc(this.LineNum);
			}
		}

		TYPE_CLASS father = extending == null ? null : (TYPE_CLASS) extending.type;

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(name1, new TYPE_CLASS(null, name1, null));

		/***************************/
		/* [2] Semant Data Members */
		/***************************/

		TYPE_CLASS_VAR_DEC_LIST fields = astfieldlist == null ? null : astfieldlist.SemantMe();
		TYPE_CLASS t = new TYPE_CLASS(father, name1, fields);

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(name1,t);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}
}

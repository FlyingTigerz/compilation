package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;

public class AST_DEC_VAR extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_TYPE type;
	public String name;
	public AST_EXP initialValue;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_VAR(int LineNum,AST_TYPE type,String name,AST_EXP initialValue)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		

		this.type = type;
		this.name = name;
		this.initialValue = initialValue;
		this.LineNum=++LineNum;
	}

	/********************************************************/
	/* The printing message for a declaration list AST node */
	/********************************************************/
	public void PrintMe()
	{

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
		if(initialValue!=null)initialValue.PrintMe();
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);}
		if (initialValue != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,initialValue.SerialNumber);}




	}

	public TYPE SemantMe() throws semanticExc
	{

		TYPE t = type.SemantMe();
		if(t.name.equals("nil"))
		{
			System.out.format(">> ERROR [%d:%d] variable %s cant be declared void\n", 2, 2, name);
			throw new semanticExc(LineNum);
		}
		/**************************************/
		/* [1] Check That Name is available */
		/**************************************/
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name);
		if (prevDec != null)
		{
			SYMBOL_TABLE_ENTRY scope = SYMBOL_TABLE.getInstance().getScope();
			/* print error only if declaration shadows a previous declaration in the same scope*/
			if(scope == null || scope.prevtop_index < prevDec.prevtop_index) {
				System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, name);
				throw new semanticExc(LineNum);
			}
		}

		/***************************************************/
		/* [2] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name, t);
		if(initialValue!=null){
			if(initialValue.SemantMe().name=="null" &&t.name!="int"&&t.name!="string")
			{
				return null;
			}}
		if(initialValue != null && (initialValue.SemantMe() == null || !initialValue.SemantMe().isInstanceOf(t))) {
			if (!initialValue.SemantMe().isInstanceOf(t)) {
				if (initialValue.SemantMe().isClass() == true && ((TYPE_CLASS) initialValue.SemantMe()).father != null)
					System.out.format("!!!!!!!!!!HERE IS THE BUG3 !!!!!!!!!!!!!!!!!!!!!!!!!2 : %s is the name, %s is the father\n", initialValue.SemantMe().name, ((TYPE_CLASS) initialValue.SemantMe()).father.name);
				else
					System.out.format("!!!!!!!!!!HERE IS THE BUG3 !!!!!!!!!!!!!!!!!!!!!!!!!2 : %s is maidenless\n", initialValue.SemantMe().name);
			}
			System.out.format("!!!!!!!!!!HERE IS THE BUG !!!!!!!!!!!!!!!!!!!!!!!!!2 : %s is the name\n", initialValue.SemantMe().name);
			System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, initialValue.SemantMe().name, t.name);
			throw new semanticExc(this.LineNum);
		}
		/***************************************************/
		/* [3] check assigned expression type validity */
		/***************************************************/
		if(initialValue != null && (initialValue.SemantMe() == null || !initialValue.SemantMe().isInstanceOf(t))){
			System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, initialValue.SemantMe().name, t.name);
			throw new semanticExc(LineNum);
		}

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		this.se = t;
		return null;
	}
	
	@Override
	public TEMP IRme()
	{
		TEMP dataStorage = TEMP_FACTORY.getInstance().getFreshNamedTEMP(IR.globalVarPrefix + name);
		if(initialValue != null) {
			TEMP t = initialValue.IRme();
			if(initialValue instanceof AST_EXP_INT){
				IR.getInstance().Add_IRdata(new IRdata_Global_Var(dataStorage, ""+((AST_EXP_INT)initialValue).value));
			}
			else {
				IR.getInstance().Add_IRdata(new IRdata_Global_Var(dataStorage, t.toString()));
			}
		}
		
		
		return dataStorage;
	}
	
}

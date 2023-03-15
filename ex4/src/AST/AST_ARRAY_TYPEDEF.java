package AST;

import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_ARRAY_TYPEDEF extends AST_DEC
{
	/*****************/
	/*  ARRAY ID = ID[] */
	/*****************/
	public String name;
	public AST_TYPE type;
	public int line_number;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_ARRAY_TYPEDEF(String name,AST_TYPE type,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> ARRAY ID = ID[]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.type = type;
		this.line_number = line_number;

	}

	/*********************************************************/
	/* The printing message for an array declaration AST node */
	/*********************************************************/
	public void PrintMe()
	{

		/********************************************/
		/* AST NODE TYPE = AST ARRAY DECLARATION */
		/********************************************/
		System.out.format("AST NODE ARRAY TYPEDEF (%s)\n", type);

		/***********************************/
		/* RECURSIVELY EXP. */
		/***********************************/
		if (type != null) type.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ARRAY TYPEDEF\n( %s)\n", type));


	}

	public TYPE SemantMe() {

		System.out.println("IRME IN AST_ARRAY_TYPEDEF");

		if (SYMBOL_TABLE.getInstance().nr_scope != 0)
		{
			throw new RuntimeException(String.valueOf(this.line_number));
		}

		TYPE t1 = null;
		TYPE t2 = null;

		if (name != null) t1 = SYMBOL_TABLE.getInstance().find(name);
		if (type != null) t2 = type.SemantMe();

		if (t1 != null)
		{
			System.out.format(">> ERROR [%d:%d] type already exists := exp\n",6,6);
			throw new RuntimeException(String.valueOf(line_number));
		}

		System.out.println("ARRAY IS TYPE " + type + " And TYPE: " + t2);
		if (t2.isClass() || t2.isArray() || t2 == TYPE_INT.getInstance() || t2 == TYPE_STRING.getInstance()){
			TYPE_ARRAY ta = new TYPE_ARRAY(name, t2);
			SYMBOL_TABLE.getInstance().enter(name, ta);//new TYPE_ARRAY(name, type));
		} else {
			throw new RuntimeException(String.valueOf(line_number));
		}

		return null;
	}
}
	


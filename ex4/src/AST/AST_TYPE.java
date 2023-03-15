package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE extends AST_Node
{

    public String name;
	int line_number;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_TYPE(String name, int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.line_number = line_number;
	}
	
	/*************************************************/
	/* The printing message for a TYPE AST node */
	/*************************************************/
	public void PrintMe()
	{

		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NEW TYPE (%s) ", name));	
		
	}

	public TYPE SemantMe() throws RuntimeException
	{
		TYPE t1 = null;

		if(name != null)
		{
			if(name.equals("int")) {
				System.out.println("int");
				return TYPE_INT.getInstance();
			}
			else if(name.equals("string"))
				return TYPE_STRING.getInstance();
			else if(name.equals("void"))
				return TYPE_VOID.getInstance();

			t1 =SYMBOL_TABLE.getInstance().find(name);
		}
		if( (!t1.isClass() && !t1.isArray() )|| t1 == null){
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		return t1;
	}
}

package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_NEW_EXP extends AST_Node
{
	/*****************/
	/*  NEW ID [exp] */
	/*****************/
    public AST_TYPE type;
	public AST_EXP exp;
	public int line_number;
	private TYPE_CLASS classType;




	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEW_EXP(AST_TYPE type, AST_EXP exp,int line_number)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp != null) System.out.print("====================== new exp -> NEW ID [exp]\n");
		if (exp == null) System.out.print("====================== new exp -> NEW ID\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.exp = exp;
		this.line_number = line_number;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		
		/*************************************/
		/* AST NODE TYPE = AST NEW EXP */
		/*************************************/
		System.out.print("AST NODE NEW EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		//if(type != null) type.PrintMe();
		if (exp != null) exp.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NEW EXP"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws RuntimeException
	{
		TYPE t1 = null;
		TYPE t2 = null;
		if (type != null) t1 = type.SemantMe();
		if (exp != null) t2 = exp.SemantMe();

		if (t1 == null)
		{
			throw new RuntimeException(String.valueOf(this.line_number));
		}
		if (exp != null){
			if (!(t1.isArray() || t1.isClass() || t1 == TYPE_INT.getInstance() || t1 == TYPE_STRING.getInstance()) && t2 == TYPE_INT.getInstance())
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}
			return t1;
		}

		else{
			this.classType = (TYPE_CLASS) t1;
			if (!t1.isClass())
			{
				throw new RuntimeException(String.valueOf(this.line_number));
			}
			return t1;
		}
	}
	public TEMP IRme() {
		if(exp==null){

			AST_EXP_FUNC classObject = new AST_EXP_FUNC(null, this.classType.name + "class_init", null, line_number);
			classObject.isMethod = false;
			classObject.label = this.classType.classInitLabel;
			return classObject.IRme();
		}else
		{
			TEMP exp_t = this.exp.IRme();
			TEMP addr_t = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_MallocArray(addr_t, exp_t));
			return addr_t;
		}
	}
}

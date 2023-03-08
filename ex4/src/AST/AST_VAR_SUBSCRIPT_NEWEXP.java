package AST;
import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;
public class AST_VAR_SUBSCRIPT_NEWEXP extends AST_STMT
{
	public AST_VAR var;
	public AST_EXP e;
	public AST_NEWEXP newe;
		private int offset;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT_NEWEXP(int LineNum,AST_VAR var,AST_EXP e,AST_NEWEXP newe)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ e,newe ]\n");


		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.e=e;
		this.newe=newe;
		this.LineNum=++LineNum;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR EXP NEWEXP\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (e != null) e.PrintMe();
		if (newe != null) newe.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"SUBSCRIPT\nVAR\nEXPNEWEXP\n...[...]");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (e       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);
		if (newe != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newe.SerialNumber);
	}
	public TYPE SemantMe() throws semanticExc{
		TYPE v;

		v = var.SemantMe();

		TYPE_VAR t=(TYPE_VAR)v;

		this.offset=SYMBOL_TABLE.getInstance().find(t.varname).offset;
		if (v == null)
		{
			System.out.format(">> ERROR [%d:%d] non existent identifier\n",2,2);
			throw new semanticExc(this.LineNum);
		}
		if(e==null){
			TYPE ex = newe.SemantMe();
//			this.offset = SYMBOL_TABLE.getInstance().find(name).offset;
			if (!ex.isInstanceOf(v))
			{
				System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, ex.name, v.name);
				throw new semanticExc(this.LineNum);
			}
		}
		if (newe==null){
			TYPE ex = e.SemantMe();
			if(ex.isArray()&&v.isArray()){
				System.out.format(">> ---------------------------------  to %s\n", ex.name);
				System.out.format(">> -------------------------------- %s\n",v.name);

				if(!ex.name.equals(v.name)){
					System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, ex.name, v.name);
					throw new semanticExc(this.LineNum);
				}
			}
			if(ex.name=="int" && v.name=="string"){
				return TYPE_STRING.getInstance();
			}
			if (!ex.isInstanceOf(v))
			{
				System.out.format(">> ERROR [%d:%d] illegal type cast from %s to %s\n", 2, 2, ex.name, v.name);
				throw new semanticExc(this.LineNum);
			}
		}
		return v;
	}
	public TEMP IRme()
	{
		TEMP dst = IR.getInstance().fp;
		if(e != null) {
			TEMP src = e.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Store_Temp(src, dst, this.offset));
		}

		return null;
	}
}

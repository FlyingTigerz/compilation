package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import java.util.Objects;


public class AST_EXPLST_VAR_ID extends AST_EXP
{
	public AST_VAR var;
	public AST_EXP_LIST explist;
	String name;
	public TYPE_FUNCTION functionType;
	
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXPLST_VAR_ID(int LineNum,AST_VAR var,AST_EXP_LIST explist,String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> [obj.]f(expLst)\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.explist = explist;
		this.name=name;
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
		if(name != null) System.out.format("AST NODE EXPLS VAR ID( %s )\n",name);
		if (var != null) var.PrintMe();
		if (explist != null) explist.PrintMe();
		

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP LST VAR(%s)",name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);}

		if (explist != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,explist.SerialNumber);}
			
	}
	public TYPE SemantMe() throws semanticExc {
		TYPE func = null;
		if(var == null) {
			func = SYMBOL_TABLE.getInstance().find(name).type;
		}
		else{
		
			if(!var.SemantMe().isClass())
			{
			System.out.format(">> ERROR cant call function with premitive variable\n");
			throw new semanticExc(this.LineNum);
			}

			TYPE_CLASS parent_type = (TYPE_CLASS) var.SemantMe();
			System.out.print("we got here and we need to know the type");
			while(parent_type != null) {
				for (TYPE_CLASS_VAR_DEC_LIST field = parent_type.data_members; field != null; field = field.tail) {
					if (Objects.equals(field.head.name, name)) {
						func = field.head.t;
					}
				}
				parent_type = parent_type.father;
			}
		}
		if(!(func instanceof TYPE_FUNCTION)){
			System.out.format(">> ERROR [%d:%d] can't find function %s\n",2,2, name);
			throw new semanticExc(this.LineNum);
		}
		functionType = (TYPE_FUNCTION) func;
		TYPE returnType =  ((TYPE_FUNCTION) func).returnType;
		TYPE_LIST expectedParams = ((TYPE_FUNCTION) func).params;
		for (AST_EXP_LIST it=explist; it != null; it=it.restoflist)
		{
			if(expectedParams == null){
				System.out.format(">> ERROR [%d:%d] too many arguments for function %s\n",2,2, name);
				throw new semanticExc(this.LineNum);
			}
			String argType = it.first.SemantMe().name;
			String expected = expectedParams.head.name;
			if (!Objects.equals(argType, expected))
			{
				System.out.format(">> ERROR [%d:%d] argument type mismatch in %s: can't convert %s to %s \n",2,2, name, argType, expected);
				throw new semanticExc(this.LineNum);
			}
			expectedParams = expectedParams.tail;
		}
		if(expectedParams != null){
			System.out.format(">> ERROR [%d:%d] function %s expect more arguments\n",2,2, name);
			throw new semanticExc(this.LineNum);
		}
		this.se = returnType;
		return returnType;
	}
	
	public TEMP IRme() {
		if(var != null){
			var.IRme();
		}
		TEMP_LIST arg_temps = (explist != null ? explist.IRme() : null);
		TEMP resReg = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Func_Call(resReg, functionType, arg_temps));
		return resReg;
	}

}

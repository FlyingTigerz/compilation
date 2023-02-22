package AST;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;

import java.util.Objects;

public class AST_STMT_FUNCCALL extends AST_STMT
{
	public String fName;
	public AST_VAR objName;
	public AST_EXP_LIST el;
	public TYPE_FUNCTION functionType;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_FUNCCALL(int line, String fName, AST_VAR objName, AST_EXP_LIST el)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> [obj.]f(expLst);\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.fName = fName;
		this.objName = objName;
		this.el = el;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{

		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/

		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE FUNC CALL STMT\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (fName != null) System.out.format("FUNCTION CALL STATEMENT %s\n", fName);
		if (objName != null) objName.PrintMe();
		if (el != null) el.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FUNCCALL(%s)",fName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (el  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,el.SerialNumber);
		if (objName  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,objName.SerialNumber);
	}
	public TYPE SemantMe() throws SemanticException {
		TYPE func = null;
		if(objName == null) {
			func = SYMBOL_TABLE.getInstance().find(fName).type;
		}
		else{
			TYPE_CLASS parent_type = (TYPE_CLASS) objName.SemantMe();
			while(parent_type != null) {
				for (TYPE_CLASS_VAR_DEC_LIST field = parent_type.data_members; field != null; field = field.tail) {
					if (Objects.equals(field.head.name, fName)) {
						func = field.head.t;
					}
				}
				parent_type = parent_type.father;
			}
		}
		if(!(func instanceof TYPE_FUNCTION)){
			System.out.format(">> ERROR [%d:%d] can't find function %s\n",2,2, fName);
			throw new SemanticException(this.line);
		}
		functionType = (TYPE_FUNCTION) func;
		TYPE returnType =  ((TYPE_FUNCTION) func).returnType;
		TYPE_LIST expectedParams = ((TYPE_FUNCTION) func).params;
		for (AST_EXP_LIST it=el; it != null; it=it.tail)
		{
			if(expectedParams == null){
				System.out.format(">> ERROR [%d:%d] too many arguments for function %s\n",2,2, fName);
				throw new SemanticException(this.line);
			}
			String argType = it.head.SemantMe().name;
			String expected = expectedParams.head.name;
			if (!Objects.equals(argType, expected))
			{
				System.out.format(">> ERROR [%d:%d] argument type mismatch in %s: can't convert %s to %s \n",2,2, fName, argType, expected);
				throw new SemanticException(this.line);
			}
			expectedParams = expectedParams.tail;
		}
		if(expectedParams != null){
			System.out.format(">> ERROR [%d:%d] function %s expect more arguments\n",2,2, fName);
			throw new SemanticException(this.line);
		}
		this.semanticLabel = returnType;
		return returnType;
	}

	public TEMP IRme() {
		if(objName != null){
			objName.IRme();
		}
		TEMP_LIST arg_temps = (el != null ? el.IRme() : null);
		TEMP resReg = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Func_Call(resReg, functionType, arg_temps));
		return resReg;
	}
}

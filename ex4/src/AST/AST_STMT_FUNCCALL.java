package AST;
import TYPES.*;
import IR.*;
import TEMP.*;
import SYMBOL_TABLE.*;

import java.util.Objects;
public class AST_STMT_FUNCCALL extends AST_STMT
{
	public String fname;
	public AST_VAR v;
	public AST_EXP_LIST l;
	public TYPE_FUNCTION functionType;

	/**/
	/* CONSTRUCTOR(S) */
	/**/
	public AST_STMT_FUNCCALL(int LineNum,String fname, AST_VAR v, AST_EXP_LIST l)
	{

		SerialNumber = AST_Node_Serial_Number.getFresh();


		System.out.print("====================== exp -> [obj.]f(expLst);\n");


		this.fname = fname;
		this.v = v;
		this.l = l;
		this.LineNum=++LineNum;
	}


	public void PrintMe()
	{
		System.out.print("AST NODE FUNC CALL STMT\n");

		if (fname != null) System.out.format("function %s", fname);
		if (v != null) v.PrintMe();
		if (l != null) l.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FUNCCALL(%s)",fname));


		if (l  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
		if (v  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
	}
	public TYPE SemantMe()throws  semanticExc {
		int found=0;
		if(v!= null){
			TYPE t=v.SemantMe();
			if(t.isClass()){
				for (TYPE_CLASS tc =(TYPE_CLASS) t; tc != null; tc = tc.father){
					for (TYPE_CLASS_VAR_DEC_LIST it = tc.data_members; it  != null; it = it.tail){
						TYPE_CLASS_VAR_DEC nodehead = it.head;
						if(nodehead.t.isFunction()){
							if((nodehead.name).equals(fname)){
								return nodehead.t;
								//found=1;
								//break;
							}
						}
					}
					if(found == 1){
						//return it.head.t.type;
						break;
					}
				}

				if(found==0){
					System.out.format(">> ERROR [%d:%d] can't find function %s\n",2,2, fname);
					throw new semanticExc(this.LineNum);
				}
			}
		}
		else{
			SYMBOL_TABLE_ENTRY func = SYMBOL_TABLE.getInstance().find(fname);
			if(func == null || !(func.type instanceof TYPE_FUNCTION)){
				System.out.format(">> ERROR [%d:%d] can't find function %s\n",2,2, fname);
				throw new semanticExc(this.LineNum);}
			
			TYPE returnType =  (TYPE_FUNCTION)func.type;
			functionType = (TYPE_FUNCTION) func.type;
			TYPE_LIST expectedParams = ((TYPE_FUNCTION) func.type).params;
			for (AST_EXP_LIST it=l; it != null; it=it.restoflist) {
				if (expectedParams == null) {
					System.out.format(">> ERROR [%d:%d] too many arguments for function %s\n", 2, 2, fname);
					throw new semanticExc(this.LineNum);
				}
				if (expectedParams.head.isClass() && it.first.SemantMe().isClass()) {
					TYPE_CLASS son = (TYPE_CLASS) it.first.SemantMe();
					TYPE_CLASS father = (TYPE_CLASS) expectedParams.head;
					if (!(son.isInstanceOf(father))) {
						System.out.format(">>0 ERROR [%d:%d] argument type mismatch can't convert %s to %s \n", 2, 2, son.name, father.name);
						throw new semanticExc(this.LineNum);
					}
				}
				else{
					String astype = it.first.SemantMe().name;
					String astype_exp = expectedParams.head.name;
					if(astype!="nil") {
						if (astype != astype_exp) {
							System.out.format(">>1 ERROR [%d:%d] argument type mismatch can't convert %s to %s \n", 2, 2, astype, astype_exp);
							throw new semanticExc(this.LineNum);
						}
					}
					String argType = it.first.SemantMe().name;
					String expected = expectedParams.head.name;
					if (!(argType == "nil" && expected != "int" && expected != "string")) {
						if (!Objects.equals(argType, expected)) {
							System.out.format(">>2 ERROR [%d:%d] argument type mismatch in %s: can't convert %s to %s \n", 2, 2, fname, expected, argType);
							throw new semanticExc(this.LineNum);
						}

					}
				}

				expectedParams = expectedParams.tail;
			}
			if(expectedParams != null){
				System.out.format(">> ERROR [%d:%d] function %s expect more arguments\n",2,2, fname);
				throw new semanticExc(this.LineNum);
			}
			this.se=returnType;
			return returnType;

		}
		return null;
	}
	
	public TEMP IRme() {
		if(v != null){
			v.IRme();
		}
		TEMP_LIST arg_temps = (l != null ? l.IRme() : null);
		TEMP resReg = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Func_Call(resReg, functionType, arg_temps));
		return resReg;
	}
}

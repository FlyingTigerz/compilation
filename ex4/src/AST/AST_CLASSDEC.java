package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CLASSDEC extends AST_DEC
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
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST DEC CLASS\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (astfieldlist != null) astfieldlist.PrintMe();
		if (name1 != null) System.out.format("%s\n", name1);
		if (name2 != null) System.out.format("%s\n", name2);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("DEC\nclass %s [extends class] {cFieldList}",name1)
		);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,astfieldlist.SerialNumber);

	}
	public TYPE_CLASS SemantMe() throws semanticExc {

		
		SYMBOL_TABLE_ENTRY extending = null;
		if(name2 != null) {
			extending = SYMBOL_TABLE.getInstance().find(name2);
			if (extending == null)
			{
				System.out.format(">> ERROR [%d:%d] non existing class %s\n",2,2,name2);
				throw new semanticExc(this.LineNum);
			}


		}

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
		SYMBOL_TABLE.getInstance().enter(name1, new TYPE_CLASS(father, name1, null));
		

		SYMBOL_TABLE.getInstance().beginScope();

		TYPE_CLASS_VAR_DEC_LIST fields = astfieldlist == null ? null : astfieldlist.SemantMe();

		// fields = astfieldlist == null ? null : astfieldlist.SemantMe();
		AST_CFIELD_LIST hold=astfieldlist;
		 TYPE_CLASS t = new TYPE_CLASS(father, name1, fields);
		if(father!=null){
			TYPE_CLASS_VAR_DEC_LIST fatherfields=father.data_members;
		for (TYPE_CLASS_VAR_DEC_LIST it = fields; it  != null; it = it.tail)
			{
				for (TYPE_CLASS_VAR_DEC_LIST fatherit = fatherfields; fatherit  != null; fatherit = fatherit.tail)
				{
					TYPE_CLASS_VAR_DEC sonfunc = it.head;
					TYPE_CLASS_VAR_DEC fatherfunc = fatherit.head;
					TYPE tson=sonfunc.t;
					TYPE tfather=fatherfunc.t;
					if(sonfunc.t.isFunction()&&fatherfunc.t.isFunction()){
						if((sonfunc.name).equals(fatherfunc.name)){
							if(sonfunc.t.name!=fatherfunc.t.name){
								AST_CFIELD n=hold.first;
				System.out.format(">> ERROR [%d:%d] cant override function with different var Func %s\n", 2, 2, fatherfunc.t.name);
								throw new semanticExc(n.LineNum);
						}
					}
					}
					if(!(sonfunc.t.isFunction())&&!(fatherfunc.t.isFunction())){
						if((sonfunc.name).equals(fatherfunc.name)){
							if(sonfunc.t.name!=fatherfunc.t.name){
								AST_CFIELD n=hold.first;
				System.out.format(">> ERROR [%d:%d] cant override VAR with different var TYPE %s,%s\n", 2, 2, fatherfunc.t.name,sonfunc.t.name);
								throw new semanticExc(n.LineNum);
						}
					}
					}
				if(((sonfunc.t.isFunction())&&!(fatherfunc.t.isFunction()))||(!(sonfunc.t.isFunction())&&(fatherfunc.t.isFunction()))){
						if((sonfunc.name).equals(fatherfunc.name)){
						AST_CFIELD n=hold.first;
					System.out.format(">> ERROR [%d:%d] cant override VAR with different var TYPE %s,%s\n", 2, 2, fatherfunc.t.name,sonfunc.t.name);
								throw new semanticExc(n.LineNum);
					}
				}
				}
				hold=hold.restoflist;
			}

		}



		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().enter(name1,t);
		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(name1, t);
		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		this.se = t;
		return null;
	}
	public void ourfunc(int line)throws semanticExc{
	}
}


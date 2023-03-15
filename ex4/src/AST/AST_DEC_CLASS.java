package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;
import MIPS.MIPSGenerator;

import java.util.ArrayList;
import java.util.List;

public class AST_DEC_CLASS extends AST_DEC
{
	/*****************/
	/*  CLASS ID [EXTENDS ID] {[cField]+} */
	/*****************/
	public String name;
	public String extName;
	public AST_C_FIELD_LIST body;
	public int lineNumber;
	public AST_C_FIELD_LIST methods;

	public String virTableLabel;
	public List<String> virTable;
	public List<String> varDefaults;
	private TYPE_CLASS classType;
	public static int fieldsCounter = -1;
	public String varInitLabel;
	public String classInitLabel;



	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_CLASS(String name, String extName, AST_C_FIELD_LIST body,int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (extName != null) System.out.print("====================== dec -> CLASS ID [EXTENDS ID] {cFieldList}\n");
		if (extName == null) System.out.print("====================== dec -> CLASS ID {cFieldList}\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.extName = extName;
		this.body = body;
		this.lineNumber=lineNumber;

		this.virTable = new ArrayList<>();
		this.varDefaults = new ArrayList<>();

		extractFieldsAndMethods(body);
	}

	public Boolean isDataMember() {
		return true;
	}


	/*********************************************************/
	/* The printing message for a class declaration AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST CLASS DECLARATION */
		/********************************************/
		System.out.format("AST NODE CLASS %s DEC\n", name);

		/***********************************/
		/* RECURSIVELY PRINT BODY ... */
		/***********************************/
		if (body != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASS %s\nDEC EXTENDS %s\n", name, extName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe()
	{
		TYPE ext_type = null;

		if (SYMBOL_TABLE.getInstance().nr_scope != 0){
			System.out.format(">> ERROR [%d:%d] defining class outside global scope := exp\n",6,6);
			throw new RuntimeException(String.valueOf(this.lineNumber));
		}

		if(extName != null){
			ext_type = SYMBOL_TABLE.getInstance().find(extName);
			if (ext_type == null || !ext_type.isClass()){
				System.out.format(">> ERROR [%d:%d] non existing class type to extend %s\n",2,2,extName);
				throw new RuntimeException(String.valueOf(this.lineNumber));
			}
		}

		if (SYMBOL_TABLE.getInstance().find(name) != null){
			System.out.format(">> ERROR [%d:%d] name %s already exists in scope\n",2,2,name);
			throw new RuntimeException(String.valueOf(this.lineNumber));
		}

		this.fieldsCounter = 0;

		TYPE_CLASS parentClass = (TYPE_CLASS) ext_type;
		TYPE_CLASS grandParent = parentClass;
		List<TYPE_CLASS> parentClasses = new ArrayList<>();

		while (grandParent != null) {
			parentClasses.add(0, grandParent);
			grandParent = grandParent.father;
		}

		SYMBOL_TABLE.getInstance().class_scope = true;
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();
		this.varInitLabel = IRcommand.getFreshLabel(String.format("%s_fields", this.name));
		this.virTableLabel = IRcommand.getFreshLabel(String.format("%s_funcs", this.name));
		this.classInitLabel = IRcommand.getFreshLabel(String.format("%s", this.name));


		for (TYPE_CLASS curr : parentClasses) {

			TYPE_CLASS_VAR_DEC_LIST members = curr.data_members;
			while (members != null) {
				TYPE_CLASS_VAR_DEC member = (TYPE_CLASS_VAR_DEC) members.head;
				fieldsCounter++;
				members = members.tail;
			}

			TYPE_LIST curr_methods = curr.methods;
			while (curr_methods != null) {
				TYPE_FUNCTION curr_method = (TYPE_FUNCTION) curr_methods.head;
				System.out.println("Name of class "+curr.name);
				System.out.println(curr_method.name + " Offset is "+curr_method.virTableOffset);

				if (curr_method.virTableOffset == virTable.size()) {
					virTable.add(curr_method.startLabel);
				} else {
					virTable.set(curr_method.virTableOffset, curr_method.startLabel);
				}
				curr_methods = curr_methods.tail;
			}
		}

		TYPE_CLASS Current_Class_type = null;
		TYPE_CLASS_VAR_DEC_LIST ext_members = null;
		if (extName != null) {
			ext_members = ((TYPE_CLASS)ext_type).data_members;
		}

		Current_Class_type = new TYPE_CLASS((TYPE_CLASS) ext_type, name, null, null, virTableLabel, varInitLabel, classInitLabel, varDefaults);
		// To enable a data member of the same class
		SYMBOL_TABLE.getInstance().enter(name, Current_Class_type);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/

		Current_Class_type.data_members = ((TYPE_CLASS_VAR_DEC_LIST)body.SemantMe());
//		// add all father data members.
		System.out.println("------------------- " + Current_Class_type.data_members.head.t.name);

		TYPE_CLASS_VAR_DEC curr_member;

		System.out.println("check if inhert right-------------------");

		while (ext_members != null) {
			curr_member = ext_members.head;
			TYPE b = SYMBOL_TABLE.getInstance().findInScope(curr_member.name);
			TYPE member_type = curr_member.t;
			if(b == null){
				System.out.println("not exists- " + member_type.name);
				Current_Class_type.data_members = new TYPE_CLASS_VAR_DEC_LIST(curr_member, Current_Class_type.data_members);
				ext_members = ext_members.tail;
				continue;
			}
			if( !curr_member.isMethod && !(member_type == b || (member_type.isArray() && b.isArray() && ((TYPE_ARRAY)member_type).type == ((TYPE_ARRAY)b).type)
				|| (member_type.isClass() && b.isClass() && ((TYPE_CLASS)member_type).name == ((TYPE_CLASS)b).name))){
				throw new RuntimeException(String.valueOf(this.lineNumber));
			}
			if(curr_member.isMethod && b.isFunc()){
				System.out.println("check if is same method");
				if( ((TYPE_FUNCTION)member_type).returnType != ((TYPE_FUNCTION)b).returnType ){
					throw new RuntimeException(String.valueOf(this.lineNumber));
				}
				TYPE_LIST exp_params = ((TYPE_FUNCTION)member_type).params;
				TYPE_LIST act_params = ((TYPE_FUNCTION)b).params;

				while(act_params != null && exp_params != null){
					System.out.println("check p");
					TYPE parm = act_params.head;
					TYPE expectedType = exp_params.head;

					if(expectedType.isClass()){
						if(!(parm.isClass() && ((TYPE_CLASS)parm).name == ((TYPE_CLASS)expectedType).name)){
							throw new RuntimeException(String.valueOf(this.lineNumber));
						}
					}else if(expectedType.isArray()) {
						if(!(parm.isArray() && ((TYPE_ARRAY)parm).type == ((TYPE_ARRAY)expectedType).type)){
							throw new RuntimeException(String.valueOf(this.lineNumber));
						}
					} else{
						if(parm != expectedType){
							throw new RuntimeException(String.valueOf(this.lineNumber));
						}
					}
					act_params = act_params.tail;
					exp_params = exp_params.tail;
				}

				if (act_params != null || exp_params != null) {
					throw new RuntimeException(String.valueOf(this.lineNumber));
				}
			}

			ext_members = ext_members.tail;
		}

		TYPE_CLASS_VAR_DEC_LIST curr_mem_list = Current_Class_type.data_members;
		while (curr_mem_list != null){
			if(curr_mem_list.head.isMethod) {
				TYPE_FUNCTION func = (TYPE_FUNCTION) curr_mem_list.head.t;
				if( parentClass != null) {
					TYPE_CLASS_VAR_DEC parentFunc_var = (TYPE_CLASS_VAR_DEC) parentClass.findMembers(func.name, true);
					if (parentFunc_var != null) {
						TYPE_FUNCTION parentFunc = (TYPE_FUNCTION) parentFunc_var.t;
						func.virTableOffset = parentFunc.virTableOffset;
						virTable.set(func.virTableOffset, func.startLabel);
					}
					else{
						System.out.println(virTable.size());
						func.virTableOffset = virTable.size();
						virTable.add(func.startLabel);
					}
				}
				else{
					System.out.println(virTable.size());
					func.virTableOffset = virTable.size();
					virTable.add(func.startLabel);
				}


				Current_Class_type.AddMethod(func);
				func.belongsToClass = classType;
				System.out.println("ADDD  " + func.virTableOffset + "  "+func.name);
			}
			curr_mem_list = curr_mem_list.tail;
		}



		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();
		fieldsCounter = -1;


		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/

		SYMBOL_TABLE.getInstance().enter(name, Current_Class_type);
		MIPSGenerator.vtables.put(this.virTableLabel, virTable);

		this.classType = Current_Class_type;
		System.out.println("Class " + name + " has fields: " + Current_Class_type.data_members);
		System.out.println("Class " + name + " has fields: " + Current_Class_type.data_members.name);

		SYMBOL_TABLE.getInstance().class_scope = false;
		if (parentClass != null) {
			varDefaults.addAll(0, parentClass.varInitValues);
		}
		MIPSGenerator.classInitValues.put(varInitLabel, varDefaults);


		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}


	private void extractFieldsAndMethods(AST_C_FIELD_LIST fields){
		AST_C_FIELD_LIST fieldsTail = null;
		AST_C_FIELD_LIST methodsTail = null;
		AST_C_FIELD_LIST list = fields;

		while(list != null){
			if (list.head.isDataMember()){

//				if (fieldsTail == null){
//					fieldsTail = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
//					this.body = fieldsTail;
//				}
//				else{
//					fieldsTail.tail = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
//					fieldsTail = fieldsTail.tail;
//				}
				String initValue = "0";
				AST_EXP initExp = list.head.var.exp;
				if (initExp != null) {
					System.out.println("Class " +"sssssssssssssssssssssssssssssssssssssssssssss" );
					list.head.var.exp.PrintMe();
				 if (initExp instanceof AST_EXP_INT) {
						System.out.println("Class " +"sssssssssssssssssssssssssssssssssssssssssssss" );
						System.out.println("Cheyyyyys " +((AST_EXP_INT) initExp).value );
						initValue = String.valueOf(((AST_EXP_INT) initExp).value);
					}
				}
				varDefaults.add(initValue);

			}
			else{
				if (methodsTail == null){
					methodsTail = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
					this.methods = methodsTail;
				}
				else{
					methodsTail.tail = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
					methodsTail = methodsTail.tail;
				}
			}
			list = list.tail;
		}

	}

	public TEMP IRme()
	{
		System.out.println("IRME IN AST_DEC_CLASS");
		if (methods == null){
			System.out.println("HII");
		}

		AST_C_FIELD_LIST methods = this.methods;
		while (methods != null)
		{
			System.out.println("IRME IN AST_DEC_CLASS");
			System.out.println("IRME IN AST_DEC_CLASS");
			if(methods.head.var!=null){System.out.println("Class var" + methods.head.var.name);}
			if(methods.head.func!=null){System.out.println("Class var" + methods.head.func.func);}
			methods.head.IRme();
			methods = methods.tail;
		}

		Runnable irBody = () -> { IR.getInstance().Add_IRcommand(new IRcommand_Constructor(classInitLabel + "_end", classType)); };
		AST_DEC_FUNC.IRFuncDec(irBody, classInitLabel, 0, classInitLabel + "_end", false, false);

		return null;
	}
}

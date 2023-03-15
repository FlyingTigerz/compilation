package TYPES;
import java.util.ArrayList;
import java.util.List;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_CLASS_VAR_DEC_LIST data_members;

	public TYPE_LIST methods;
	public String vTableLabel;
	public String varInitLabel;
	public String classInitLabel;
	public List<String> varInitValues;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_CLASS_VAR_DEC_LIST data_members
	,  TYPE_LIST methods,
	  String vTableLabel,
	  String varInitLabel,
	  String classInitLabel,
	  List<String> varInitValues)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
		this.methods = methods;
		this.vTableLabel = vTableLabel;
		this.varInitLabel = varInitLabel;
		this.classInitLabel = classInitLabel;
		this.varInitValues = varInitValues;
	}
	public TYPE findMembers(String name, boolean isMethod) {
		TYPE_CLASS_VAR_DEC_LIST curr_mem_list = this.data_members;
		TYPE_CLASS_VAR_DEC curr_member;

		while (curr_mem_list != null) {
			curr_member = curr_mem_list.head;
			System.out.println(curr_member.t +"  Comparing " + curr_member.name + " to " + name);
			if (curr_member.name.equals(name) && curr_member.isMethod == isMethod)
				return curr_member;
			curr_mem_list = curr_mem_list.tail;

		}
		return null;
	}

	public boolean isAncestor(TYPE_CLASS actualType)
	{
		TYPE_CLASS tempFather = this;
		while(tempFather != null){
			System.out.println("in isAncestor "+ tempFather.name);
			if (tempFather.name.equals(actualType.name)){
				return true;
			}
			tempFather = tempFather.father;
		}
		return false;
	}

	public boolean isClass(){ return true;}

	public void AddMethod(TYPE method) {
		TYPE_LIST newTypeList = new TYPE_LIST(method, null);
		if(this.methods==null)
			this.methods = newTypeList;
		else {
			TYPE_LIST ptr = this.methods;
			while(ptr.tail!=null)
				ptr=ptr.tail;
			ptr.tail = newTypeList;
		}
	}





	public int getType() {return 2;}
}

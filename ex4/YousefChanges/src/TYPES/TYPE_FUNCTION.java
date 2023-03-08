package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;
	public boolean isSysCall = false;
	public int sysCallNum = 0;
	public int localVarCount = 0;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}
	public TYPE_FUNCTION(TYPE returnType, String name, int sysCallNum, TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.isSysCall = true;
		this.sysCallNum = sysCallNum;
	}
}

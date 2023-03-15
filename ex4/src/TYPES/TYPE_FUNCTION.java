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

	public TYPE_CLASS belongsToClass;
	public int virTableOffset = -1 ;
	public String startLabel = null;
	public String endLabel = null;
	public int numberOfLocals = 0;
	public int numOfParams = 1;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params,
						 int numOfParams, String startLabel, String endLabel) {

		this.returnType = returnType;
		this.name = name;
		this.params = params;
		this.startLabel = startLabel;
		this.endLabel = endLabel;
		this.belongsToClass = null;
		this.virTableOffset = -1;
		this.numOfParams = numOfParams;
	}

	public int getType() {return 6;}
	public boolean isFunc() {return true;}

}

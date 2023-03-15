package TYPES;

public class TYPE_ARRAY extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
    //TYPE_ARRAY arr;
	public String name;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE type;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_ARRAY(String name,TYPE type)
	{
        //this.arr = arr;
		this.name = name;
		this.type = type;
		
	}

	public boolean isArray(){
		return true;
	}
	public int getType() {return 1;}
}

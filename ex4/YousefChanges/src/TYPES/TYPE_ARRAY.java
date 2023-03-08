package TYPES;

import java.util.Objects;

public class TYPE_ARRAY extends TYPE
{
	public TYPE arrayType;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_ARRAY(TYPE arrayType)
	{
		this.name = arrayType.name + "[]";
		this.arrayType = arrayType;
	}

	public TYPE_ARRAY(TYPE_ARRAY array)
	{
		this.name = array.name;
		this.arrayType = array.arrayType;
	}
	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return true;}

	public boolean isInstanceOf(TYPE t){
		if(t instanceof TYPE_NIL){
			return true;
		}
		if(!(t instanceof TYPE_ARRAY)){
			return false;
		}
		TYPE_ARRAY at = (TYPE_ARRAY) t;
		if(!(Objects.equals(at.arrayType.name, this.arrayType.name))){
			return false;
		}
		return true;
	}
}

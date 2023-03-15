package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	public boolean isFunc() {return false;}

	public boolean isString() {return false;}

	public boolean isInt() {return false;}

	public int getType() {return 0;}

	public boolean isAncestor(){ return false;}

	public boolean isInstance(String name) {
		return !(this.name.equals(name));
	}

}

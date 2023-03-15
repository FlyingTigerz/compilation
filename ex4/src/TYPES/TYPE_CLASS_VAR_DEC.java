package TYPES;
import CONTEXT.*;

public class TYPE_CLASS_VAR_DEC extends TYPE
{
	public TYPE t;
	public String name;
	public boolean isMethod;
	public Context context;

	public TYPE_CLASS_VAR_DEC(TYPE t,String name, boolean isMethod, Context context)
	{
		this.t = t;
		this.name = name;
		this.isMethod = isMethod;
		this.context = context;
	}
	public int getType() {return 3;}
}

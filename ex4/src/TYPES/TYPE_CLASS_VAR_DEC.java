package TYPES;
import java.util.Objects;
public class TYPE_CLASS_VAR_DEC extends TYPE
{
	public TYPE t;
	public String name;
	public int LineNum;

	public TYPE_CLASS_VAR_DEC(TYPE t, String name)
	{
		this.t = t;
		this.name = name;

	}

}

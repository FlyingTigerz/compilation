package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{
	/****************/
	/* CTROR(S) ... */
	/****************/
	private int arg_offset = 1;
	private int var_offset = 0;
	private int field_offset = 0;
	public TYPE_FOR_SCOPE_BOUNDARIES(String name)
	{
		this.name = name;
	}
	public int getArgOffsetInc(){
		arg_offset +=1;
		return arg_offset;
	}
	public int getVarOffsetInc(){
		var_offset -=1;
		return var_offset;
	}
	public int getFieldOffsetInc(){
		field_offset -=1;
		return field_offset;
	}
	public int getArgCount(){
		return arg_offset;
	}
	public int getVarCount(){
		return var_offset;
	}
	public int getFieldCount(){
		return field_offset;
	}
	public void incrementOffsetsFromInnerQuotes(TYPE_FOR_SCOPE_BOUNDARIES inner){
		// arg_offset+=inner.getArgCount();
		var_offset+=inner.getVarCount();
		field_offset+=inner.getFieldCount();
	}
}

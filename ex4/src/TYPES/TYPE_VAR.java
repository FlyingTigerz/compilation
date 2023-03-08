package TYPES;

public class TYPE_VAR extends TYPE{
    /***********************************/
    /* The return type of the function */
    /***********************************/
    public String varname;
    public boolean isVar(){ return true;}

    public TYPE_VAR(String varname,String name)
    {
        this.name = name;
        this.varname = varname;
    }

}

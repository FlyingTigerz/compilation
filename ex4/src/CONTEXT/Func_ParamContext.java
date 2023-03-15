package CONTEXT;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import static MIPS.MIPSGenerator.WORD_SIZE;


public class Func_ParamContext extends Context {
	public int offset;
	boolean isMethod;
	
    public Func_ParamContext(int offset, boolean isMethod) {
        this.offset = offset;
        this.isMethod = isMethod;
    }

    @Override
    public void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp) {
        MIPSGenerator.getInstance().loadFpAddressWithOffset(dst, calcFpOffset());
    }

    protected int calcFpOffset() {
        if (this.isMethod)  
            return (offset + 2) * WORD_SIZE;              
        return (offset + 1) * WORD_SIZE;
    }
}

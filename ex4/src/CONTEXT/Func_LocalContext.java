package CONTEXT;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import static MIPS.MIPSGenerator.WORD_SIZE;


public class Func_LocalContext extends Context {
    public int offset;

    public Func_LocalContext(int offset) {
        this.offset = offset;
    }


    @Override
    public void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp) {
        MIPSGenerator.getInstance().loadFpAddressWithOffset(dst, calcFpOffset());
    }

    protected int calcFpOffset() {
        return -1*((offset + 1 + 1) * WORD_SIZE); 
    }
}

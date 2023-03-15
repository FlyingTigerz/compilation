package CONTEXT;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class Var_GlobalContext extends Context {
    private String varNameLabel;

    public Var_GlobalContext(String varNameLabel) {
        this.varNameLabel = varNameLabel;
    }

    @Override
    public void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp) {
        MIPSGenerator.getInstance().la(dst, varNameLabel);
    }
}

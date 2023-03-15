package CONTEXT;

import IR.*;
import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;

public class Var_ClassContext extends Context {
    public int offset;

    public Var_ClassContext(int offset) {
        this.offset = offset;
    }

    @Override
    public void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp) {

        TEMP pthis = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (thisTmp == null) {

            String cmd = "lw $t%d, 4($fp)";
            MIPSGenerator.getInstance().runCmd(cmd, pthis.getSerialNumber(), true);
        } else {

            MIPSGenerator.getInstance().load(pthis, thisTmp);
            MIPSGenerator.getInstance().checkPointerDereference(pthis);
        }
        MIPSGenerator.getInstance().addi(dst, pthis, (offset + 1) * 4);
    }

}

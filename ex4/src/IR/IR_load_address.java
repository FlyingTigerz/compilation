package IR;

import TEMP.TEMP;
import MIPS.*;

public class IR_load_address extends IRcommand {
    TEMP dst;
    String label;

    public IR_load_address(TEMP dst, String label) {
        this.dst = dst;
        this.label = label;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().la(dst, label);
    }
}

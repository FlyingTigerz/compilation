/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.MIPSGenerator;

import static java.util.Collections.*;
import TEMP.*;

public class IRcommand_array_access extends IRcommand {
    TEMP dst;
    TEMP addr_0;
    TEMP ind;

    public IRcommand_array_access(TEMP dst, TEMP addr_0, TEMP ind_0) {
        this.dst = dst;
        this.addr_0 = addr_0;
        this.ind = ind_0;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().addi(ind, ind, 1);
        TEMP t_4 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP alignedIndex = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().li(t_4, 4);
        MIPSGenerator.getInstance().mul(alignedIndex, ind, t_4);
        MIPSGenerator.getInstance().add(dst, alignedIndex, addr_0);
    }
}

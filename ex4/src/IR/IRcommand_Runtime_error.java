package IR;

import MIPS.MIPSGenerator;

public class IRcommand_Runtime_error extends IRcommand {

    String label;

    public IRcommand_Runtime_error(String const_label) {
        this.label = const_label;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {

        MIPSGenerator.getInstance().print_string_by_label(label);
        MIPSGenerator.getInstance().exitProgram();
    }
}

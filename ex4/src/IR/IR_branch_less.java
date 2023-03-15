package IR;
import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IR_branch_less extends IRcommand {
    TEMP oper1;
    TEMP oper2;
    String label;
    boolean equal;

    public IR_branch_less(TEMP oper1, TEMP oper2 , String label, boolean eq) {
        this.oper1 = oper1;
        this.oper2 = oper2;
        this.label = label;
        this.equal = eq;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        if (this.equal)
            MIPSGenerator.getInstance().ble(oper1, oper2, label);
        MIPSGenerator.getInstance().blt(oper1, oper2, label);
    }
}

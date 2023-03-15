package IR;
import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;

public class IRcommand_MallocArray extends IRcommand {

    TEMP size;
    TEMP dst;

    public IRcommand_MallocArray(TEMP dst, TEMP size) {
        this.size = size;
        this.dst = dst;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {

        TEMP newSize = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().addi(newSize, size, 1);
        MIPSGenerator.getInstance().malloc(dst, newSize);

        MIPSGenerator.getInstance().store(dst, size);
    }

}

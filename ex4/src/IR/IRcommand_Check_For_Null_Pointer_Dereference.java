package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;


/*
What This IR Command Do ?
Creates new label : not_Nil and check whther the pointer is nil, if it Nil the Exit the program (jump to labele Syscall)
if not Nil jump to the label Not_Nil



*/

public class IRcommand_Check_For_Null_Pointer_Dereference extends IRcommand {

    private TEMP thisPointer;

    public IRcommand_Check_For_Null_Pointer_Dereference(TEMP thisPointer) {
        this.thisPointer = thisPointer;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().checkPointerDereference(thisPointer);
    }
}

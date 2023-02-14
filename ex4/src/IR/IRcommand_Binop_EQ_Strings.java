package IR;

import TEMP.*;
import MIPS.*;

import java.util.HashSet;
import java.util.Set;

public class IRcommand_Binop_EQ_Strings extends IRcommand{
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_EQ_Strings(TEMP dst,TEMP t1,TEMP t2)
    {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    public Set<TEMP> usedRegs() {
        Set<TEMP> used_regs = new HashSet<TEMP>();
        used_regs.add(t1);
        used_regs.add(t2);
        return used_regs;
    }
    public TEMP modifiedReg() { return dst;}

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        /*******************************/
        /* [1] Allocate 2 fresh labels */
        /*******************************/
        String label_end        = getFreshLabel("EQend");
        String label_AssignOne  = getFreshLabel("EQAssignOne");
        String label_AssignZero = getFreshLabel("EQAssignZero");
        String label_StringsCmp = getFreshLabel("EQStringsCmp");

        /**********************************************************/
        /*  Allocate a fresh temporary  for the concatenation */
        /********************************************************/
        TEMP s1 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP s2 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP a1 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP a2 = TEMP_FACTORY.getInstance().getFreshTEMP();


        //load s1 into $t1
        MIPSGenerator.getInstance().la(s1, t1);
        //load s2 into $t2
        MIPSGenerator.getInstance().la(s2, t2);

        MIPSGenerator.getInstance().label(label_StringsCmp);
        /************************/
        /* label_StringsCmp: */
        /************************/
        //load next char byte from str1
        MIPSGenerator.getInstance().lb(a1, s1);
        //load next char byte from str2
        MIPSGenerator.getInstance().lb(a2, s2);
        //case not equal
        MIPSGenerator.getInstance().bne(a1,a2,label_AssignZero);
        //case EOF they are equal
        MIPSGenerator.getInstance().beqz(a1,label_AssignOne);
        //advance pointers
        MIPSGenerator.getInstance().addi(s1, s1, 1);
        MIPSGenerator.getInstance().addi(s2, s2, 1);
        MIPSGenerator.getInstance().jump(label_StringsCmp);


        /************************/
        /* [3] label_AssignOne: */
        /*                      */
        /*         t3 := 1      */
        /*         goto end;    */
        /*                      */
        /************************/
        MIPSGenerator.getInstance().label(label_AssignOne);
        MIPSGenerator.getInstance().li(dst,1);
        MIPSGenerator.getInstance().jump(label_end);

        /*************************/
        /* [4] label_AssignZero: */
        /*                       */
        /*         t3 := 1       */
        /*         goto end;     */
        /*                       */
        /*************************/
        MIPSGenerator.getInstance().label(label_AssignZero);
        MIPSGenerator.getInstance().li(dst,0);
        MIPSGenerator.getInstance().jump(label_end);

        /******************/
        /* [5] label_end: */
        /******************/
        MIPSGenerator.getInstance().label(label_end);
    }

}


/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import MIPS.*;
import TEMP.*;
import TYPES.*;



public class IRcommand_Constructor extends IRcommand {
    private String endLabel;
    private TYPE_CLASS typeClass;

    public IRcommand_Constructor(String endLabel, TYPE_CLASS typeClass) {
        this.endLabel = endLabel;
        this.typeClass = typeClass;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {

        boolean contains_functions;
        TEMP size = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP instance_ptr = TEMP_FACTORY.getInstance().getFreshTEMP();

        int vars_size = typeClass.varInitValues.size();
        MIPSGenerator.getInstance().li(size, 4*(vars_size+1));
        MIPSGenerator.getInstance().malloc(instance_ptr, size, false);

        if (typeClass.vTableLabel == null)
            System.out.println("NULLLL");
        if  (!MIPSGenerator.vtables.get(typeClass.vTableLabel).isEmpty()){
            contains_functions = true;
        }
        else{
            contains_functions= false;
        }
 
        if (contains_functions) {
            TEMP vtableAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
            MIPSGenerator.getInstance().la(vtableAddress, typeClass.vTableLabel);
            MIPSGenerator.getInstance().store(instance_ptr, vtableAddress);
        }

        if (!typeClass.varInitValues.isEmpty()) {
            TEMP memberP = TEMP_FACTORY.getInstance().getFreshTEMP();
            MIPSGenerator.getInstance().addi(memberP, instance_ptr, 4);
            TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();

            TEMP varInitLabelAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
            MIPSGenerator.getInstance().la(varInitLabelAddress, typeClass.varInitLabel);
            for (int i = 0; i < typeClass.varInitValues.size(); i++) {
                MIPSGenerator.getInstance().load(tmp, varInitLabelAddress, i * 4);

                MIPSGenerator.getInstance().store(memberP, tmp);
                MIPSGenerator.getInstance().addi(memberP, memberP, 4);
            }
        }

        MIPSGenerator.getInstance().move("$v0", instance_ptr); // return register
        MIPSGenerator.getInstance().jump(endLabel);

    }
}

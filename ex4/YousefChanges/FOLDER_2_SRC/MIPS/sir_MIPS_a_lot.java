/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.IR;
import TEMP.*;

public class sir_MIPS_a_lot
{
	public static final int WORD_SIZE=4;
	private static final int MAX_INT = 32767;
	private static final int MIN_INT = -32768;
	public static final String funcLabelPrefix = "FUNC_LABEL_";
	public static final String endProgLabel = "END_PROGRAM";
	public static final String  globalVarPrefix = "GLOBAL_VAR_";
	public static final String  strPrefix = "DATA_STR_";
	public static final String  EXIT_AccessViolation = "EXIT_ACCESS_VIOLATION";
	public static final String	ERR_AccessViolation = "ERR_ACCESS_VIOLATION";
	public static final String	MSG_AccessViolation = "\"Access Violation\"";

	public static final String  EXIT_ZeroDiv = "EXIT_ZERO_DIV";
	public static final String  ERR_ZeroDiv = "ERR_ZERO_DIV";
	public static final String  MSG_ZeroDiv = "\"Illegal Division By Zero\"";

	public static final String  EXIT_InvalidPointer = "EXIT_INVALID_POINTER";
	public static final String  ERR_InvalidPointer = "ERR_INVALID_POINTER";
	public static final String  MSG_InvalidPointer = "\"Invalid Pointer Dereference\"";
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.close();
	}
	/* TODO: there shouldn't be a dedicated print_int function.
	*   instead, we should have a generic syscall function that
	*   receives the syscall type as an argument*/
	public void print_int(TEMP t)
	{
		// li $a0, t
		if(t.toString().contains("$")) {
			fileWriter.format("\tmove %s, %s\n",IR.getInstance().a0, t);
		}
		else {
			fileWriter.format("\tla %s, %s\n", IR.getInstance().a0, t);
		}
		// li $v0, 1
		fileWriter.format("\tli %s, %s\n",IR.getInstance().v0, 1);
		// syscall
		fileWriter.format("\tsyscall\n");
	}
	public void print_string(TEMP t)
	{
		// li $a0, t
		if(t.toString().contains("$")) {
			fileWriter.format("\tmove %s, %s\n",IR.getInstance().a0, t);
		}
		else {
			fileWriter.format("\tlw %s, %s\n", IR.getInstance().a0, t);
		}
		// li $v0, 4
		fileWriter.format("\tli %s, %s\n",IR.getInstance().v0, 4);
		// syscall
		fileWriter.format("\tsyscall\n");
	}
	public void print_trace(TEMP t)
	{
	}
	public void exit()
	{
		// li $v0, 10
		fileWriter.format("\tli %s, %d\n",IR.getInstance().v0, 10);
		// syscall
		fileWriter.format("\tsyscall\n");
	}
	public void syscall(){
		fileWriter.format("\tsyscall\n");
	}
	/***********************/
	/* Sections */
	/***********************/
	public void dataSection() { fileWriter.format(".data\n"); }
	public void textSection(){
		fileWriter.format(".text\n");
	}
	public void finalizeTextSection(){
		// exit gracefully
		fileWriter.println(endProgLabel + ":\n\tli $v0, 10\n\tsyscall");
		// exit on access violation
		fileWriter.println(EXIT_AccessViolation + ":\n\tla $a0, " + ERR_AccessViolation + "\n\tli $v0, 4\n\tsyscall\n\tli $v0, 10\n\tsyscall");
		// exit on zero division
		fileWriter.println(EXIT_ZeroDiv + ":\n\tla $a0, " + ERR_ZeroDiv + "\n\tli $v0, 4\n\tsyscall\n\tli $v0, 10\n\tsyscall");
		// exit on invalid pointer
		fileWriter.println(EXIT_InvalidPointer + ":\n\tla $a0, " + ERR_InvalidPointer + "\n\tli $v0, 4\n\tsyscall\n\tli $v0, 10\n\tsyscall");
	}
	public void storeGlobalVariable(TEMP label, String word){
		fileWriter.format("\t%s: .word %s\n", label, word);
	}
	public void storeString(TEMP label, String str){
		fileWriter.format("\t%s: .asciiz \"%s\"\n", label, str);
	}
	public void allocate(String var_name)
	{
		fileWriter.format("\tglobal_%s: .word 721\n",var_name);
	}
	public void load(TEMP dst, TEMP src, int offset)
	{
		if(src.toString().contains("$")) {
			fileWriter.format("\tlw %s, %d(%s)\n", dst.toString(), offset * WORD_SIZE, src.toString());
		}
		else{
			if(!dst.toString().contains("$")) {
				fileWriter.format("\tla %s, %s\n", IR.getInstance().s0, dst.toString());
				dst = IR.getInstance().s0;
			}
			fileWriter.format("\tlw %s, %s+%d\n", dst.toString(), src.toString(), offset * WORD_SIZE);
		}
	}
	public void load(String dst, TEMP src, int offset)
	{
		fileWriter.format("\tlw %s, %d(%s)\n", dst, offset * WORD_SIZE, src.toString());
	}
	public void store(TEMP src, TEMP dst, int offset)
	{
		if(dst.toString().contains("$")) {
			fileWriter.format("\tsw %s, %d(%s)\n", src.toString(), offset * WORD_SIZE, dst.toString());
		}
		else{
			if(!src.toString().contains("$")) {
				fileWriter.format("\tla %s, %s\n", IR.getInstance().s0, src.toString());
				src = IR.getInstance().s0;
			}
			fileWriter.format("\tsw %s, %s+%d\n", src.toString(), dst.toString(), offset * WORD_SIZE);
		}
	}
	public void store(String src, TEMP dst, int offset)
	{
		fileWriter.format("\tsw %s, %d(%s)\n", src, offset * WORD_SIZE, dst.toString());
	}
	public void li(TEMP t,int value)
	{
		fileWriter.format("\tli %s,%d\n",t.toString(),value);
	}
	public void la(TEMP dst, TEMP src)
	{
		fileWriter.format("\tla %s,0(%s)\n",dst.toString(),src.toString());
	}
	public void move(TEMP dst, TEMP src)
	{
		fileWriter.format("\tmove %s, %s\n",dst.toString(), src.toString());
	}
	public void lb(TEMP dst,TEMP src)
	{
		fileWriter.format("\tlb %s,0(%s)\n",dst.toString(),src.toString());
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tadd %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tsub %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tmul %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}
	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tdiv %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}

	public void addi(TEMP dst,TEMP oprnd1,int value)
	{
		fileWriter.format("\taddi %s, %s, %d\n",dst.toString(),oprnd1.toString(),value);
	}

	public void label(String inlabel)
	{
		fileWriter.format("%s:\n",inlabel);
	}	
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}
	public void jal(String inlabel)
	{
		fileWriter.format("\tjal %s\n",inlabel);
	}
	public void slt(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tslt %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		fileWriter.format("\tblt %s,%s,%s\n",oprnd1.toString(),oprnd2.toString(),label);
	}
	public void blt(TEMP oprnd1,int immed,String label)
	{
		fileWriter.format("\tblt %s,%s,%s\n",oprnd1.toString(),immed,label);
	}
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		fileWriter.format("\tbgt %s,%s,%s\n",oprnd1.toString(),oprnd2.toString(),label);
	}
	public void bgt(TEMP oprnd1,int immed,String label)
	{
		fileWriter.format("\tbgt %s,%s,%s\n",oprnd1.toString(),immed,label);
	}
	public void bltz(TEMP oprnd1,String label)
	{
		fileWriter.format("\tbltz %s, %s\n",oprnd1.toString(),label);
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		fileWriter.format("\tbge %s,%s,%s\n",oprnd1.toString(),oprnd2.toString(),label);
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		fileWriter.format("\tbne %s,%s,%s\n",oprnd1.toString(),oprnd2.toString(),label);
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		fileWriter.format("\tbeq %s,%s,%s\n",oprnd1.toString(),oprnd2.toString(),label);
	}
	public void ble(TEMP oprnd1, TEMP oprnd2, String label)
	{
		fileWriter.format("\tble %s,%s,%s\n",oprnd1.toString(),oprnd2.toString(),label);
	}
	public void beqz(TEMP oprnd1,String label)
	{
		fileWriter.format("\tbeq %s,$zero,%s\n",oprnd1.toString(),label);
	}
	public void ret(TEMP res)
	{
		// epilogue
		if(res != null) {
			fileWriter.format("\tmove $v0 %s\n", res);
		}
		fileWriter.format("\tmove $sp, $fp\n");
		fileWriter.format("\tlw $fp, 0($sp)\n");
		fileWriter.format("\tlw $ra, 4($sp)\n");
		fileWriter.format("\taddi $sp, $sp, 8\n");
		fileWriter.format("\tjr $ra\n");
	}


	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static sir_MIPS_a_lot instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected sir_MIPS_a_lot() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static sir_MIPS_a_lot getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new sir_MIPS_a_lot();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./FOLDER_5_OUTPUT/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return instance;
	}
}

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

public class MIPSGenerator
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
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}
	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 721\n",var_name);
	}
	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst,var_name);
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
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);		
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
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void addi(TEMP dst,TEMP oprnd1,int value)
	{
		fileWriter.format("\taddi %s, %s, %d\n",dst.toString(),oprnd1.toString(),value);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tdiv %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}
	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}
	public void bltz(TEMP oprnd1,String label)
	{
		fileWriter.format("\tbltz %s, %s\n",oprnd1.toString(),label);
	}
	public void move(TEMP dst, TEMP src)
	{
		fileWriter.format("\tmove %s, %s\n",dst.toString(), src.toString());
	}
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}
	public void la(TEMP dst, TEMP src)
	{
		fileWriter.format("\tla %s,0(%s)\n",dst.toString(),src.toString());
	}
	public void lb(TEMP dst,TEMP src)
	{
		fileWriter.format("\tlb %s,0(%s)\n",dst.toString(),src.toString());
	}
	public void slt(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tslt %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		fileWriter.format("\tsub %s,%s,%s\n",dst.toString(),oprnd1.toString(),oprnd2.toString());
	}
	public void exit()
	{
		// li $v0, 10
		fileWriter.format("\tli %s, %d\n",IR.getInstance().v0, 10);
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
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
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

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}

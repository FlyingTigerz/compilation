/***********/
/* PACKAGE */
/***********/
package IR;
import MIPS.sir_MIPS_a_lot;
import TEMP.*;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Map;
/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IR
{
	public IRcommand head=null;
	public IRcommandList tail=null;
	private IRdata dataHead=null;
	private IRdataList dataTail=null;
	private int labelCounter = 0;
	private static final int MAX_INT = 32767;
	private static final int MIN_INT = -32768;
	public TEMP accessViolation;
	public TEMP zeroDiv;
	public TEMP invalidPtr = TEMP_FACTORY.getInstance().getFreshNamedTEMP("CONST_WORD_SIZE");
	public TEMP maxIntTemp;
	public TEMP minIntTemp;
	public TEMP spaceTemp;
	public TEMP wordSizeTemp = new TEMP(sir_MIPS_a_lot.WORD_SIZE, true);
	public TEMP sp;
	public TEMP fp;
	public TEMP ra;
	public TEMP v0;
	public TEMP v1;
	public TEMP a0;
	public TEMP a1;
	public TEMP s0;
	public TEMP s1;
	public static final String strSpace = " ";
	public static final String funcLabelPrefix = "FUNC_LABEL_";
	public static final String  overflowLabelPrefix = "OVERFLOW_";
	public static final String  underflowLabelPrefix = "UNDERFLOW_";
	public static final String  inboundsLabelPrefix = "INBOUNDS_";
	public static final String endProgLabel = "END_PROGRAM";
	public static final String  globalVarPrefix = "GLOBAL_VAR_";
	public static final String  strPrefix = "DATA_STR_";
	public static final String  exitOnAccessViolation = "EXIT_ACCESS_VIOLATION";
	public static final String  exitOnZeroDiv = "EXIT_ZERO_DIV";
	public static final String  exitOnInvalidPointer = "EXIT_INVALID_POINTER";
	public static PrintWriter fileWriter;
//	public static PrintStream fileWriter = System.out;
	private static int line_index=1;
	public Map<TEMP, Integer> regAlloc;

	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRcommand(IRcommand cmd)
	{
		if ((head == null) && (tail == null))
		{
			this.head = cmd;
		}
		else if ((head != null) && (tail == null))
		{
			this.tail = new IRcommandList(cmd,null);
		}
		else
		{
			IRcommandList it = tail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IRcommandList(cmd,null);
		}
	}

	public void finalizeFile()
	{
		fileWriter.close();
	}

	public void fileNewLine() {fileNewLine(true); fileWriter.print("\t"); }
	public void fileNewLine(boolean notab) { if(notab) {fileWriter.print(line_index++ + ".");} else fileNewLine(); }
	public void filePrintln(String line) { fileWriter.println(line); }

	/******************************************/
	/* Generate the register allocation table */
	/******************************************/
	public void genRegAlloc() {
		IR_Graph graph = new IR_Graph();
		graph.analyzeLiveness();
		graph.genInterference();
		regAlloc = graph.paintInterference(10);
		TEMP.tempToReg = regAlloc;
	}

	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRdata(IRdata data)
	{
		if ((dataHead == null) && (dataTail == null))
		{
			this.dataHead = data;
		}
		else if ((dataHead != null) && (dataTail == null))
		{
			this.dataTail = new IRdataList(data,null);
		}
		else
		{
			IRdataList it = dataTail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IRdataList(data,null);
		}
	}

	/*
	* Returns the first command after a label
	*/
	public IRcommand findCmdAtLabel(String label) {
		IRcommand curr = head;
		IRcommandList next = tail;
		while ((curr != null) && (next != null))
		{
			if (curr instanceof IRcommand_Label) {
				if (((IRcommand_Label) curr).label_name.equals(label)) {
					return next.head;
				}
			}
			curr = next.head;
			next = next.tail;
		}
		return null;
	}

	public void truncate_int(TEMP dst){
		/*
		Typical bounds check (on $t0):
		  ...
		  bgt $t0, 32767, OVERFLOW_1
		  blt $t0, -32768, UNDERFLOW_1
		  jmp INBOUNDS_1
		UNDERFLOW_1:
		  li $t0, -32768
		  jmp INBOUNDS_1
		OVERFLOW_1:
		  li $t0, 32767
		INBOUNDS_1:
  			...
		*/
		int index = IR.getInstance().getLabelIndex();
		String overflowLabel = overflowLabelPrefix + index;
		String underflowLabel = underflowLabelPrefix + index;
		String inboundsLabel = inboundsLabelPrefix + index;
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_LT(dst, MIN_INT, underflowLabel));
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_GT(dst, MAX_INT, overflowLabel));
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(inboundsLabel));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(underflowLabel));
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, MIN_INT));
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(inboundsLabel));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(overflowLabel));
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, MAX_INT));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(inboundsLabel));
	}

	public int getLabelIndex(){
		labelCounter++;
		return labelCounter;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().dataSection();
		if (dataHead != null) dataHead.MIPSme();
		if (dataTail != null) dataTail.MIPSme();
		sir_MIPS_a_lot.getInstance().textSection();
		if (head != null) head.MIPSme();
		if (tail != null) tail.MIPSme();
		sir_MIPS_a_lot.getInstance().finalizeTextSection();
	}
	public void printMe(){
		if (dataHead != null) dataHead.printMe();
		if (dataTail != null) dataTail.printMe();
		if (head != null) head.printMe();
		if (tail != null) tail.printMe();
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
			instance.maxIntTemp = TEMP_FACTORY.getInstance().getFreshNamedTEMP("CONST_MAX_INT");
			instance.minIntTemp = TEMP_FACTORY.getInstance().getFreshNamedTEMP("CONST_MIN_INT");
			instance.Add_IRdata(new IRdata_Global_Var(instance.maxIntTemp, "" + MAX_INT));
			instance.Add_IRdata(new IRdata_Global_Var(instance.minIntTemp, "" + MIN_INT));
			// string constants
			TEMP spaceData = TEMP_FACTORY.getInstance().getFreshNamedTEMP(strPrefix + "CONST_SPACE");
			TEMP accessViolationData = TEMP_FACTORY.getInstance().getFreshNamedTEMP(strPrefix + "ERR_ACCESS_VIOLATION");
			TEMP zeroDivData = TEMP_FACTORY.getInstance().getFreshNamedTEMP(strPrefix + "ERR_ZERO_DIV");
			TEMP invalidPtrData = TEMP_FACTORY.getInstance().getFreshNamedTEMP(strPrefix + "ERR_INVALID_POINTER");
			instance.Add_IRdata(new IRdata_Constant_String(spaceData, strSpace));
			instance.Add_IRdata(new IRdata_Constant_String(accessViolationData, "Access Violation"));
			instance.Add_IRdata(new IRdata_Constant_String(zeroDivData, "Illegal Division By Zero"));
			instance.Add_IRdata(new IRdata_Constant_String(invalidPtrData, "Invalid Pointer Dereference"));
			instance.spaceTemp = TEMP_FACTORY.getInstance().getFreshNamedTEMP("CONST_SPACE");
			instance.accessViolation = TEMP_FACTORY.getInstance().getFreshNamedTEMP("ERR_ACCESS_VIOLATION");
			instance.zeroDiv = TEMP_FACTORY.getInstance().getFreshNamedTEMP("ERR_ZERO_DIV");
			instance.invalidPtr = TEMP_FACTORY.getInstance().getFreshNamedTEMP("ERR_INVALID_POINTER");
			IR.getInstance().Add_IRdata(new IRdata_Global_Var(instance.spaceTemp, spaceData.toString()));
			IR.getInstance().Add_IRdata(new IRdata_Global_Var(instance.accessViolation, accessViolationData.toString()));
			IR.getInstance().Add_IRdata(new IRdata_Global_Var(instance.zeroDiv, zeroDivData.toString()));
			IR.getInstance().Add_IRdata(new IRdata_Global_Var(instance.invalidPtr, invalidPtrData.toString()));

			// unique registers
			instance.sp = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$sp");
			instance.fp = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$fp");
			instance.ra = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$ra");
			instance.v0 = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$v0");
			instance.v1 = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$v1");
			instance.a0 = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$a0");
			instance.a1 = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$a1");
			instance.s0 = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$s0");
			instance.s1 = TEMP_FACTORY.getInstance().getFreshNamedTEMP("$s1");

			// output setup
			String dirname="./FOLDER_5_OUTPUT/";
			String filename=String.format("IR.txt");
			try { instance.fileWriter = new PrintWriter(dirname+filename); }
			catch (Exception e) { e.printStackTrace(); }

		}
		return instance;
	}
}

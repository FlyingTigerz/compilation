package AST;
import MIPS.sir_MIPS_a_lot;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public int offset;
	public boolean isGlobal;
	public TEMP base;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(int line, String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = ++line;
		this.name = name;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public TYPE SemantMe() throws SemanticException {
		/**************************************/
		/* [2] Check That Identifier Exists */
		/**************************************/
		SYMBOL_TABLE_ENTRY prevDec = SYMBOL_TABLE.getInstance().find(name);
		if (prevDec == null)
		{
			/* print error only if declaration shadows a previous declaration in the same scope*/
			System.out.format(">> ERROR [%d:%d] variable %s does not exist in scope\n", 2, 2, name);
			throw new SemanticException(this.line);
		}
		this.semanticLabel = prevDec.type;
		this.offset = prevDec.offset;
		this.isGlobal = prevDec.isGlobal;
		return this.semanticLabel;
	}
	public TEMP IRme(){
		return this.IRme(true);
	}
	public TEMP IRme(boolean storeInTemp) {
		TEMP dest = null;
		if(storeInTemp) {
			dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		}
		TEMP src;
		int offset = 0;
		if(isGlobal){
			src = TEMP_FACTORY.getInstance().getFreshNamedTEMP(IR.globalVarPrefix + name);
		}
		else{
			src = IR.getInstance().fp;
			offset = this.offset;
		}
		this.base = src;
		if(storeInTemp) {
			IR.getInstance().Add_IRcommand(new IRcommand_Load(dest, src, offset));
			return dest;
		}
		return null;
	}
}

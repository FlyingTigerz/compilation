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

public class IRdataList
{
	public IRdata head;
	public IRdataList tail;

	IRdataList(IRdata head, IRdataList tail)
	{
		this.head = head;
		this.tail = tail;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (head != null) head.MIPSme();
		if (tail != null) tail.MIPSme();
	}

	public void printMe(){
		if (head != null) head.printMe();
		if (tail != null) tail.printMe();
	}
}

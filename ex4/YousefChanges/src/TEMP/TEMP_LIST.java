/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import IR.IRcommandList;

/*******************/

public class TEMP_LIST
{
    public TEMP head;
    public TEMP_LIST tail;

    public TEMP_LIST()
    {
        this.head = null;
        this.tail = null;
    }

    public TEMP_LIST(TEMP head, TEMP_LIST tail)
    {
        this.head = head;
        this.tail = tail;
    }
    public void append(TEMP t){
        if ((head == null) && (tail == null))
        {
            this.head = t;
        }
        else if ((head != null) && (tail == null))
        {
            this.tail = new TEMP_LIST(t,null);
        }
        else
        {
            TEMP_LIST it = tail;
            while ((it != null) && (it.tail != null))
            {
                it = it.tail;
            }
            it.tail = new TEMP_LIST(t,null);
        }
    }

    public String toString() {
        String items = "";
        TEMP_LIST curr = this;
        while(curr != null && curr.head != null) {
            items = items.concat(curr.head.toString() + ", ");
            curr = curr.tail;
        }
        return items.substring(0, items.length()-2);
    }
}

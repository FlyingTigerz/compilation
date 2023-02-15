package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import TYPES.TYPE;

public class AST_DEC_NEW extends AST_DEC{
        public AST_DEC_VAR vd;
        public AST_DEC_FUNC fd;
        public AST_CLASSDEC cd;
        public AST_ARRAYTYPEDEF atd;

        /******************/
        /* CONSTRUCTOR(S) */
        /******************/
        public AST_DEC_NEW(int LineNum, AST_DEC_VAR vd,AST_DEC_FUNC fd ,AST_CLASSDEC cd, AST_ARRAYTYPEDEF atd)
        {
            /******************************/
            /* SET A UNIQUE SERIAL NUMBER */
            /******************************/
            SerialNumber = AST_Node_Serial_Number.getFresh();


            /*******************************/
            /* COPY INPUT DATA NENBERS ... */
            /*******************************/
	    this.LineNum = ++LineNum;
            this.vd = vd;
            this.fd=fd;
            this.cd=cd;
            this.atd=atd;
        }

        /***********************************************/
        /* The default message for an exp var AST node */
        /***********************************************/
        public void PrintMe()
        {

            /*****************************/
            /* RECURSIVELY PRINT var ... */
            /*****************************/
            if (vd != null) vd.PrintMe();
            if (fd != null) fd.PrintMe();
            if (cd != null) cd.PrintMe();
            if (atd != null) atd.PrintMe();
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("DECNEW")
            );
            /****************************************/
            /* PRINT Edges to AST GRAPHVIZ DOT file */
            /****************************************/
            if (vd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vd.SerialNumber);}
            if (fd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fd.SerialNumber);}
            if (cd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cd.SerialNumber);}
            if (atd != null){ AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,atd.SerialNumber);}

        }
        public TYPE SemantMe() throws semanticExc {
            if (vd != null)return vd.SemantMe();
            if (fd != null)return fd.SemantMe();
            if (cd != null) return cd.SemantMe();
            return atd.SemantMe();
        }
        
        public TEMP IRMe() {
            if (vd != null)return vd.IRMe();
            if (fd != null)return fd.IRMe();
            if (cd != null) return cd.IRMe();
            return atd.IRMe();
            
        }

}



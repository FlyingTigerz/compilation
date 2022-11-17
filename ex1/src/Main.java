
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;

public class Main
{
    static public void main(String argv[])
    {
        Lexer l;
        Symbol s;
        FileReader file_reader;
        PrintWriter file_writer;
        String inputFilename = argv[0];
        String outputFilename = argv[1];

        try
        {
            /********************************/
            /* [1] Initialize a file reader */
            /********************************/
            file_reader = new FileReader(inputFilename);

            /********************************/
            /* [2] Initialize a file writer */
            /********************************/
            file_writer = new PrintWriter(outputFilename);

            /******************************/
            /* [3] Initialize a new lexer */
            /******************************/
            l = new Lexer(file_reader);

            /***********************/
            /* [4] Read next token */
            /***********************/
            s = l.next_token();

            /********************************/
            /* [5] Main reading tokens loop */
            /********************************/
            while (s.sym != TokenNames.EOF)
            {
                /************************/
                /* [6] Print to console */
                /************************/
		if(s.sym == TokenNames.ERROR){
			file_writer = new PrintWriter(outputFilename);
			file_writer.print("ERROR");
			break;
		}
                if(s.value == null){
                    file_writer.print(GetTokenName(s));
                    file_writer.print("[");
                    file_writer.print(l.getLine());
                    file_writer.print(",");
                    file_writer.print(l.getTokenStartPosition());
                    file_writer.print("]");

                }
                else{

                    file_writer.print(GetTokenName(s));
                    file_writer.print("(");
                    file_writer.print(s.value);
                    file_writer.print(")");
                    file_writer.print("[");
                    file_writer.print(l.getLine());
                    file_writer.print(",");
                    file_writer.print(l.getTokenStartPosition());
                    file_writer.print("]");
                    

                }

                /***********************/
                /* [8] Read next token */
                /***********************/
                s = l.next_token();
		if(s.sym != TokenNames.EOF){
		file_writer.print("\n");
		}
            }

            /******************************/
            /* [9] Close lexer input file */
            /******************************/
            l.yyclose();

            /**************************/
            /* [10] Close output file */
            /**************************/
            file_writer.close();
        }


        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static String GetTokenName(Symbol s) {
        int state = s.sym;
        String str = "";
        switch (state) {

            case 0:
                str = "LPAREN";
                break;
            case 1:
                str = "RPAREN";
                break;
            case 2:
                str = "LBRACK";
                break;
            case 3:
                str = "RBRACK";
                break;
            case 4:
                str = "LBRACE";
                break;
            case 5:
                str = "RBRACE";
                break;
            case 6:
                str = "NIL";
                break;
            case 7:
                str = "PLUS";
                break;
            case 8:
                str = "MINUS";
                break;
            case 9:
                str = "TIMES";
                break;
            case 10:
                str = "DIVIDE";
                break;
            case 11:
                str = "COMMA";
                break;
            case 12:
                str = "DOT";
                break;
            case 13:
                str = "SEMICOLON";
                break;
            case 14:
                str = "TYPE_INT";
                break;
            case 15:
                str = "TYPE_VOID";
                break;
            case 16:
                str = "ASSIGN";
                break;
            case 17:
                str = "EQ";
                break;
            case 18:
                str = "LT";
                break;
            case 19:
                str = "GT";
                break;
            case 20:
                str = "ARRAY";
                break;
            case 21:
                str = "CLASS";
                break;
            case 22:
                str = "EXTENDS";
                break;
            case 23:
                str = "RETURN";
                break;
            case 24:
                str = "WHILE";
                break;
            case 25:
                str = "IF";
                break;
            case 26:
                str = "NEW";
                break;
            case 27:
                str = "INT";
                break;
            case 28:
                str = "STRING";
                break;
            case 29:
                str = "ID";
                break;
            case 30:
                str = "TYPE_STRING";
                break;
	    case 31:
		str="ERROR";
            default:
                break;
        }

        return str;
    }
}


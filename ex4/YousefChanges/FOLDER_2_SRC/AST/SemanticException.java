package AST;
public class SemanticException extends Exception { 
		public int line;  /* the erronous line number in the source file */
    public SemanticException(int line) {
        super(String.format("Semantic error at line %d", line));
        this.line = line;
    }
}

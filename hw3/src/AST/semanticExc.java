package AST;
public class semanticExc extends Exception {
		public int line;  /* the erronous line number in the source file */
    public semanticExc(int line) {
        super(String.format("Semantic error at line %d", line));
        this.line = line;
    }
}

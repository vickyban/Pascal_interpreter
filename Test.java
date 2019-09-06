import Interpreter.Interpreter;
import Lexer.Lexer;
import Parser.Parser;

public class Test {
    public static void main(String[] args){
        String text =
                "BEGIN " +
                        "BEGIN " +
                        "   num := 2;" +
                        "   a := num;" +
                        "   b := 10 * a + 10;" +
                        "   c := a --b;" +
                        "END; " +
                        "x := 11; " +
                "END.";
        Lexer l = new Lexer(text);
        Parser p = new Parser(l);
        try {
            Interpreter i = new Interpreter(p);
            i.interpreter();
            System.out.println(i.GLOBAL_SCOPE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

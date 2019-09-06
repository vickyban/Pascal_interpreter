import Interpreter.Interpreter;
import Lexer.Lexer;
import Parser.Parser;

public class Test {
    public static void main(String[] args){
        String text =
                "PROGRAM part10; " +
                "VAR " +
                        "num : INTEGER;" +
                        "a,b,c,x : INTEGER; " +
                        "y : REAL; "+
                "BEGIN {part 10}" +
                        "BEGIN " +
                        "   num := 2;" +
                        "   a := num;" +
                        "   b := 10 * a + 10 * num DIV 4;" +
                        "   c := a --b;" +
                        "END; " +
                        "x := 11; " +
                        "y := 20 / 7 + 3.14 " +
                        "{ comment a } " +
                        "{ comment b } " +
                        "{ comment c } " +
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

import Interpreter.Interpreter;
import Lexer.Lexer;
import Parser.*;
import sourceToSourceCompiler.SourceToSourceCompiler;
import symbol.BuiltinTypeSymbol;
import symbol.VarSymbol;

public class Test {
    public static void main(String[] args){
        String text =
                "PROGRAM part10; \n" +
                    "VAR \n" +
                            "x,y : REAL;\n" +
                            "z : INTEGER;\n " +
                    "PROCEDURE AlphaA(a:INTEGER) "+
                        "VAR y : INTEGER; "+
                        "BEGIN {AlphaA} " +
                                "x := a + x + y; "+
                        "END; "+
                    "PROCEDURE AlphaB(a:INTEGER); "+
                        "VAR b : INTEGER; "+
                        "BEGIN {AlphaB} " +
                        "END; "+
                "BEGIN " +

                "END. ";

        Lexer l = new Lexer(text);
        try {
            Parser p = new Parser(l);
            Node n = p.parse();
            //Interpreter i = new Interpreter(p);
            //i.interpreter();
            //System.out.println(i.GLOBAL_SCOPE);
            SourceToSourceCompiler compiler = new SourceToSourceCompiler();
            compiler.visit(n);
            System.out.println(compiler.output);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        BuiltinTypeSymbol intType = new BuiltinTypeSymbol("INTEGER");
//        System.out.println(intType);
//        VarSymbol x = new VarSymbol("x",intType);
//        System.out.println(x);
    }
}

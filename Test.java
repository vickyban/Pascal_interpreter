import Interpreter.Interpreter;
import Lexer.Lexer;
import Parser.*;
import logger.Logger;
import sourceToSourceCompiler.SourceToSourceCompiler;
import symbol.BuiltinTypeSymbol;
import symbol.SemanticAnalyzer;
import symbol.VarSymbol;

public class Test {
    public static void main(String[] args){
        String text =
                "PROGRAM part10; \n" +
                    "VAR \n" +
                            "x,y : REAL;\n" +
                            "z : INTEGER;\n " +
                    "PROCEDURE AlphaA(a:INTEGER; b: INTEGER);\n "+
                        "VAR y : INTEGER;\n "+
                        "BEGIN {AlphaA}\n " +
                                "x := a + x + y + b;\n "+
                        "END;\n "+
                    "PROCEDURE AlphaB(a:INTEGER);\n "+
                        "VAR b : INTEGER;\n "+
                        "BEGIN {AlphaB}\n " +
                        "END;\n "+
                "BEGIN\n " +
                    "AlphaA(1 + 2,3);\n " +
                "END. ";

        Lexer l = new Lexer(text);
        Logger.shouldLog = true;
        try {
            Parser p = new Parser(l);
            Node n = p.parse();
            SemanticAnalyzer s = new SemanticAnalyzer();
            s.visit(n);
            //Interpreter i = new Interpreter(p);
            //i.interpreter();
            //System.out.println(i.GLOBAL_SCOPE);
            //SourceToSourceCompiler compiler = new SourceToSourceCompiler();
            //compiler.visit(n);
            //System.out.println(compiler.output);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        BuiltinTypeSymbol intType = new BuiltinTypeSymbol("INTEGER");
//        System.out.println(intType);
//        VarSymbol x = new VarSymbol("x",intType);
//        System.out.println(x);
    }
}

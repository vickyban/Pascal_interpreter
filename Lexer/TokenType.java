package Lexer;

public enum TokenType {
    NONE(null),
    EOF("EOF"),

    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    FLOAT_DIV("/"),
    OPEN_BRACKET("("),
    CLOSE_BRACKET(")"),
    SEMI(";"),
    DOT("."),
    COLON(":"),
    COMMA(","),

    PROGRAM("PROGRAM"),
    VAR("VAR"),
    INTEGER("INTEGER"),
    REAL("REAL"),
    PROCEDURE("PROCEDURE"),
    BEGIN("BEGIN"),
    INTEGER_DIV("DIV"),
    END("END"),

    ID("ID"),
    ASSIGN(":="),
    INTEGER_CONST("INTEGER CONST"),
    REAL_CONST("REAL CONST");

    public String value;
    TokenType(String value){
        this.value = value;
    }

}

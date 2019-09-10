package Lexer;

public class Token {
    public String value;
    public TokenType type;
    public int lineno;
    public int column;

    public Token(TokenType type, String value){
        this.value = value;
        this.type = type;
    }
    public Token(TokenType type, String value, int lineno, int column){
        this.value = value;
        this.type = type;
        this.lineno = lineno;
        this.column = column;
    }

    public int getIntValue(){
        return Integer.parseInt(value);
    }
    public double getDoubleValue(){
        return Double.parseDouble(value);
    }

    public String toString(){
        return "<Token: '" + value + "' line : " + lineno + " column : " + column +">";
    }
}

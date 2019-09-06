package Lexer;

public class Token {
    public String value;
    public TokenType type;

    public Token(TokenType type, String value){
        this.value = value;
        this.type = type;
    }

    public int getIntValue(){
        return Integer.parseInt(value);
    }
    public double getDoubleValue(){
        return Double.parseDouble(value);
    }
}

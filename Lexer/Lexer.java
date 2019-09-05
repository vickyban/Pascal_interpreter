package Lexer;

/**
 * break the text into tokens
 */
public class Lexer {
    private String text;
    private char curChar;
    private int index;

    public Lexer(String text){
        this.text = text;
        index = 0;
        curChar = text.charAt(0);
    }

    public Token getNextToken(){
        if(index < text.length()){
            // skip whitespaces
            if(curChar == ' ')
                skipWhitespace();
            if(curChar == '+'){
                advance();
                return new Token(TokenType.PLUS,"+");
            }else if(curChar == '-'){
                advance();
                return new Token(TokenType.MINUS,"-");
            }else if(curChar == '*'){
                advance();
                return new Token(TokenType.MULTIPLY,"*");
            }else if(curChar == '/'){
                advance();
                return new Token(TokenType.DIVIDE,"/");
            }else if(curChar == '('){
                advance();
                return new Token(TokenType.OPEN_BRACKET,"(");
            }else if(curChar == ')'){
                advance();
                return new Token(TokenType.CLOSE_BRACKET,")");
            }else if(curChar >= '0' && curChar <= '9'){
                return new Token(TokenType.INTEGER, integer());
            }
        }
        return new Token(TokenType.EOF, null);
    }

    private void skipWhitespace(){
        while(curChar == ' '){
            advance();
        }
    }

    private void advance(){
        index++;
        if(index < text.length()){
            curChar = text.charAt(index);
        }
    }

    public String integer(){
        String value = "";
        while(curChar >= '0' && curChar <= '9'){
            value += curChar;
            advance();
        }
        return value;
    }
}

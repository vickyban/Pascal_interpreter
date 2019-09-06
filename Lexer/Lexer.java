package Lexer;

import java.util.HashMap;

/**
 * break the text into tokens
 */
public class Lexer {
    private HashMap<String,Token> RESERVE_KEYWORD = new HashMap<String,Token>(){{
        put("PROGRAM", new Token(TokenType.PROGRAM, "PROGRAM"));
        put("BEGIN", new Token(TokenType.BEGIN, "BEGIN"));
        put("END", new Token(TokenType.END, "END"));
        put("VAR", new Token(TokenType.VAR, "VAR"));
        put("DIV", new Token(TokenType.INTEGER_DIV, "DIV"));
        put("INTEGER", new Token(TokenType.INTEGER, "INTEGER"));
        put("REAL", new Token(TokenType.REAL, "REAL"));
    }};
    private String text;
    private char curChar;
    private int index;

    public Lexer(String text){
        this.text = text;
        index = 0;
        curChar = text.charAt(0);
    }

    public Token getNextToken(){
        while(index < text.length()){
            // skip whitespaces
            if(curChar == ' ')
                skipWhitespace();
            if(curChar == '{'){
                advance();
                skipComment();
                continue;
            }
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
                return new Token(TokenType.FLOAT_DIV,"/");
            }else if(curChar == '('){
                advance();
                return new Token(TokenType.OPEN_BRACKET,"(");
            }else if(curChar == ')'){
                advance();
                return new Token(TokenType.CLOSE_BRACKET,")");
            }else if(curChar >= '0' && curChar <= '9'){
                return number();
            }else if(Character.isLetter(curChar)){
                return identifier();
            }else if(curChar == ':' && peekNext() == '='){
                advance();
                advance();
                return new Token(TokenType.ASSIGN, ":=");
            }else if(curChar == ':'){
                advance();
                return new Token(TokenType.COLON, ":");
            }else if(curChar == ';'){
                advance();
                return new Token(TokenType.SEMI, ";");
            }else if(curChar == '.'){
                advance();
                return new Token(TokenType.DOT, ".");
            }else if(curChar == ','){
                advance();
                return new Token(TokenType.COMMA, ",");
            }
        }
        return new Token(TokenType.EOF, null);
    }

    private void skipWhitespace(){
        while(curChar == ' '){
            advance();
        }
    }

    /**
     * skip comment in {}
     */
    private void skipComment(){
        while(curChar != '}'){
            advance();
        }
        advance(); // the closing curly brace
    }

    private void advance(){
        index++;
        if(index < text.length()){
            curChar = text.charAt(index);
        }
    }

    /**
     * return integer or float
     * @return
     */
    public Token number(){
        String value = "";
        while(curChar >= '0' && curChar <= '9'){
            value += curChar;
            advance();
        }
        if(curChar == '.'){
            value += curChar;
            advance();
            while(curChar >= '0' && curChar <= '9'){
                value += curChar;
                advance();
            }
            return new Token(TokenType.REAL_CONST, value);
        }
        return new Token(TokenType.INTEGER_CONST, value);
    }

    /**
     * peek into the next char without consuming the current char
     */
    public char peekNext(){
        int next = index + 1;
        if(next < text.length()){
            return text.charAt(next);
        }else{
            return '\u0000';
        }
    }

    /**
     * handle identifiers and reserved keywords
     * @return
     */
    public Token identifier(){
        String value = "";
        while(Character.isLetterOrDigit(curChar)){
            value += curChar;
            advance();
        }
        Token token = RESERVE_KEYWORD.getOrDefault(value, new Token(TokenType.ID, value));
        return token;
    }
}

package Lexer;

import exceptions.ErrorCode;
import exceptions.LexerError;

import java.util.HashMap;

/**
 * break the text into tokens
 */
public class Lexer {
    private HashMap<String,Token> RESERVE_KEYWORD;
    private String text;
    private char curChar;
    private int index;
    private int lineno;
    private int column;

    public Lexer(String text){
        buildReservedKeyword();
        this.text = text;
        index = 0;
        curChar = text.charAt(0);
        this.lineno = 1;
        this.column = 1;
    }

    public void buildReservedKeyword(){
        RESERVE_KEYWORD = new HashMap<>();
        for(TokenType type : TokenType.values()){
            if(type.ordinal() >= TokenType.PROGRAM.ordinal() && type.ordinal() <= TokenType.END.ordinal()){
                RESERVE_KEYWORD.put(type.name(), new Token(type, type.value));
            }
        }
    }

    public Token getNextToken() throws LexerError {
        int curLine;
        int curCol;
        while(index < text.length()){
            // skip whitespaces
            if(curChar == ' ')
                skipWhitespace();
            if(curChar == '{'){
                advance();
                skipComment();
                continue;
            }
            if(curChar == '\n'){
                advance();
                continue;
            }

            curLine = lineno;
            curCol = column;
            if(curChar == '+'){
                advance();
                return new Token(TokenType.PLUS,"+", curLine,curCol);
            }else if(curChar == '-'){
                advance();
                return new Token(TokenType.MINUS,"-", curLine, curCol);
            }else if(curChar == '*'){
                advance();
                return new Token(TokenType.MULTIPLY,"*",curLine,curLine);
            }else if(curChar == '/'){
                advance();
                return new Token(TokenType.FLOAT_DIV,"/", curLine, curCol);
            }else if(curChar == '('){
                advance();
                return new Token(TokenType.OPEN_BRACKET,"(", curLine,curCol);
            }else if(curChar == ')'){
                advance();
                return new Token(TokenType.CLOSE_BRACKET,")", curLine, curCol);
            }else if(curChar >= '0' && curChar <= '9'){
                return number();
            }else if(Character.isLetter(curChar)){
                return identifier();
            }else if(curChar == ':' && peekNext() == '='){
                advance();
                advance();
                return new Token(TokenType.ASSIGN, ":=", curLine,curCol);
            }else if(curChar == ':'){
                advance();
                return new Token(TokenType.COLON, ":", curLine,curCol);
            }else if(curChar == ';'){
                advance();
                return new Token(TokenType.SEMI, ";", curLine,curCol);
            }else if(curChar == '.'){
                advance();
                return new Token(TokenType.DOT, ".", curLine,curCol);
            }else if(curChar == ','){
                advance();
                return new Token(TokenType.COMMA, ",", curLine,curCol);
            }
            //error();
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
        if(curChar == '\n'){
            lineno++;
            column = 0;
        }
        if(index < text.length()){
            curChar = text.charAt(index);
            column++;
        }else{
            curChar = '\u0000';
        }
    }

    /**
     * return integer or float
     * @return
     */
    public Token number(){
        String value = "";
        int curLine = lineno;
        int curCol = column;
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
            return new Token(TokenType.REAL_CONST, value, curLine,curCol);
        }
        return new Token(TokenType.INTEGER_CONST, value,curLine,curCol);
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
        int curLine = lineno;
        int curCol = column;
        while(Character.isLetterOrDigit(curChar)){
            value += curChar;
            advance();
        }
        Token token = RESERVE_KEYWORD.getOrDefault(value, new Token(TokenType.ID, value));
        token.lineno = curLine;
        token.column = curCol;
        return token;
    }

    public LexerError error() throws LexerError {
        String msg = "Lexer error on " + curChar + " line: " + lineno + " column: " + column;
        throw new LexerError(ErrorCode.NONE, null, msg);
    }
}

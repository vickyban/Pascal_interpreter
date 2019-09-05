package Parser;

import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;

/**
 * parse tokens into a tree structure
 */
public class Parser {
    private Lexer lexer;
    private Token curToken;

    public Parser(Lexer lexer){
        this.lexer = lexer;
        curToken = lexer.getNextToken();
    }

    public Node factor() throws Exception {
        // return NumNode
        Token token = curToken;
        if(curToken.type == TokenType.INTEGER){
            eat(TokenType.INTEGER);
            return new NumNode(token);
        }else if(curToken.type == TokenType.OPEN_BRACKET){
            eat(TokenType.OPEN_BRACKET);
            Node node = expr();
            eat(TokenType.CLOSE_BRACKET);
            return node;
        }else if(curToken.type == TokenType.EOF){
            eat(TokenType.PLUS);
            return new UnaryNode(token, factor());
        }else if(curToken.type == TokenType.MINUS){
            eat(TokenType.MINUS);
            return new UnaryNode(token, factor());
        }
        throw new Exception("Invalid factor value: " + curToken.value);
    }

    public void eat(TokenType type) throws Exception {
        if(curToken.type == type){
            curToken = lexer.getNextToken();
        }else{
            throw new Exception("Expected " + type + " token but receive " + curToken.type );
        }
    }

    public Node expr() throws Exception {
        Node term = term();
        while(curToken.type == TokenType.PLUS || curToken.type == TokenType.MINUS){
            Token op = curToken;
           if(op.type == TokenType.PLUS)
               eat(TokenType.PLUS);
           else if (op.type == TokenType.MINUS)
               eat(TokenType.MINUS);

           term =  new BinOpNode(op, term, term());
        }
        return term;
    }

    public Node term() throws Exception{
        Node factor = factor();
        while(curToken.type == TokenType.MULTIPLY || curToken.type == TokenType.DIVIDE){
            Token op = curToken;
            if(op.type == TokenType.DIVIDE)
                eat(TokenType.DIVIDE);
            else if (op.type == TokenType.MULTIPLY)
                eat(TokenType.MULTIPLY);
            factor = new BinOpNode(op,factor,factor());

        }
        return factor;
    }

    public Node parse() throws Exception {
        return expr();
    }
}

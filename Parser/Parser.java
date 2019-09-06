package Parser;

import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;

import java.util.ArrayList;

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

    /**
     * factor:
     * PLUS factor  |
     * MINUS factor |
     * INTEGER |
     * LPAREN expr RPAREN |
     * variable
     * @return
     * @throws Exception
     */
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
        }else{
            return variable();
        }
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
        Node node = program();
        if(curToken.type != TokenType.EOF)
            throw new Exception("Should be EOF but not");
        return node;
    }

    /**
     * program: compound_statement DOT
     * @return
     * @throws Exception
     */
    public Node program() throws Exception {
        Node node = compound_statement();
        eat(TokenType.DOT);
        return node;
    }

    /**
     * compount_statement: BEGIN statement_list END
     * @return
     */
    public Node compound_statement() throws Exception {
        eat(TokenType.BEGIN);
        ArrayList<Node> nodes = statement_list();
        eat(TokenType.END);

        CompoundNode root = new CompoundNode();
        for(Node child : nodes){
             root.children.add(child);
        }
        return root;
    }

    /**
     * statement_list: statement | statement SEMI statement_list
     * @return
     */
    public ArrayList<Node> statement_list() throws Exception {
        Node node = statement();
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(node);
        while(curToken.type == TokenType.SEMI){
            eat(TokenType.SEMI);
            nodes.add(statement());
        }
        if(curToken.type == TokenType.ID)
            throw new Exception("Invalid statement");
        return nodes;
    }

    /**
     * statement: compound_statement | assignment_statement | empty
     * @return
     */
    public Node statement() throws Exception {
        if(curToken.type == TokenType.BEGIN){
            return compound_statement();
        }else if(curToken.type == TokenType.ID){
            return assignment_statement();
        }else
            return empty();
    }

    public Node empty() {
        return new NoOp();
    }

    /**
     * assignment statement: variable Assign expr
     * @return
     */
    public Node assignment_statement() throws Exception {
        Node left = variable();
        Token op = curToken;
        eat(TokenType.ASSIGN);
        Node right = expr();
        return new AssignNode(left, op, right);
    }

    /**
     * varibale : ID
     * @return
     */
    private Node variable() throws Exception {
        Node var = new VariableNode(curToken);
        eat(TokenType.ID);
        return var;
    }


}

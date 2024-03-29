package Parser;

import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;
import exceptions.ErrorCode;
import exceptions.LexerError;
import exceptions.ParserError;

import java.util.ArrayList;
import java.util.List;

/**
 * parse tokens into a tree structure
 */
public class Parser {
    private Lexer lexer;
    private Token curToken;

    public Parser(Lexer lexer) throws LexerError {
        this.lexer = lexer;
        curToken = lexer.getNextToken();
    }

    /**
     * factor:
     * PLUS factor  |
     * MINUS factor |
     * INTEGER_CONST |
     * REAL_CONST |
     * LPAREN expr RPAREN |
     * variable
     * @return
     * @throws Exception
     */
    public Node factor() throws Exception {
        // return NumNode
        Token token = curToken;
        if(curToken.type == TokenType.INTEGER_CONST){
            eat(TokenType.INTEGER_CONST);
            return new NumNode(token);
        }else if(curToken.type == TokenType.REAL_CONST){
            eat(TokenType.REAL_CONST);
            return new NumNode(token);
        }else if(curToken.type == TokenType.OPEN_BRACKET){
            eat(TokenType.OPEN_BRACKET);
            Node node = expr();
            eat(TokenType.CLOSE_BRACKET);
            return node;
        }else if(curToken.type == TokenType.PLUS){
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
            throw new ParserError(ErrorCode.UNEXPECTED_TOKEN, curToken,"Expected " + type + " token but receive " + curToken.type );
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

    /**
     * term: factor ((MUL | INTEGER_DIV | FLOAT_DIV) factor) *
     * @return
     * @throws Exception
     */
    public Node term() throws Exception{
        Node factor = factor();
        while(curToken.type == TokenType.MULTIPLY || curToken.type == TokenType.INTEGER_DIV || curToken.type == TokenType.FLOAT_DIV){
            Token op = curToken;
            if(op.type == TokenType.FLOAT_DIV)
                eat(TokenType.FLOAT_DIV);
            else if (op.type == TokenType.MULTIPLY)
                eat(TokenType.MULTIPLY);
            else if (op.type == TokenType.INTEGER_DIV)
                eat(TokenType.INTEGER_DIV);
            factor = new BinOpNode(op,factor,factor());

        }
        return factor;
    }

    public Node parse() throws Exception {
        Node node = program();
        if(curToken.type != TokenType.EOF)
            throw new ParserError(ErrorCode.UNEXPECTED_TOKEN, curToken,"Should be EOF but not");
        return node;
    }

    /**
     * compount_statement: BEGIN statement_list END
     * @return
     */
    public CompoundNode compound_statement() throws Exception {
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
            throw new ParserError(ErrorCode.UNEXPECTED_TOKEN, curToken, "");
        return nodes;
    }

    /**
     * statement:
     * compound_statement |
     * assignment_statement |
     * procall_statement |
     * empty
     * @return
     */
    public Node statement() throws Exception {
        if(curToken.type == TokenType.BEGIN){
            return compound_statement();
        }else if(curToken.type == TokenType.ID && lexer.curChar == '('){
            return procedureCall_statement();
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
    private VariableNode variable() throws Exception {
        VariableNode var = new VariableNode(curToken);
        eat(TokenType.ID);
        return var;
    }

    /**
     * block : declarations compound_statement
     */
    public BlockNode block() throws Exception {
        List<Node> declarations = declarations();
        CompoundNode compoundStatement = compound_statement();
        BlockNode node = new BlockNode(declarations,compoundStatement);
        return node;
    }

    /**
     * declarations : VAR(var_declaration SEMI)+ | (PROCEDURE ID (LPAREN formal_param_list RPAREN )? SEMI block SEMI)* | empty
     * @return
     */
    private List<Node> declarations() throws Exception {
        List<Node> declarations = new ArrayList<>();
        if(curToken.type == TokenType.VAR){
            eat(TokenType.VAR);
            while(curToken.type == TokenType.ID){
                List<VarDeclNode> var = variableDeclaration();
                declarations.addAll(var);
                eat(TokenType.SEMI);
            }
        }
        while(curToken.type == TokenType.PROCEDURE){
            declarations.add(procedureDeclaration());
        }
        return declarations;
    }
    public ProcedureDeclNode procedureDeclaration() throws Exception {
        eat(TokenType.PROCEDURE);
        String procName = curToken.value;
        eat(TokenType.ID);
        List<ParamNode> params = formalParameters();
        eat(TokenType.SEMI);
        BlockNode block = block();
        eat(TokenType.SEMI);
        return new ProcedureDeclNode(procName,params,block);
    }

    /**
     * formal_params : ID (COMMA ID)* COLON type_spec
     * @return
     */
    public List<ParamNode> formalParameters() throws Exception {
        List<ParamNode> paramNodes = new ArrayList<>();
        if(curToken.type == TokenType.OPEN_BRACKET){
            eat(TokenType.OPEN_BRACKET);
            List<VarDeclNode>results = new ArrayList<>();
            results.addAll(variableDeclaration());
            while(curToken.type == TokenType.SEMI){
                eat(TokenType.SEMI);
                results.addAll(variableDeclaration());
            }
            for(VarDeclNode d : results){
                paramNodes.add(new ParamNode(d.var,d.varType));
            }
            eat(TokenType.CLOSE_BRACKET);
        }
        return paramNodes;
    }

    /**
     * variable_declaration: ID (COMMA ID)* COLON type_spec
     * @return
     */
    private  List<VarDeclNode> variableDeclaration() throws Exception {
        List<VariableNode> nodes = new ArrayList<>();
        nodes.add(new VariableNode(curToken));
        eat(TokenType.ID);
        while(curToken.type == TokenType.COMMA){
            eat(TokenType.COMMA);
            nodes.add(new VariableNode(curToken));
            eat(TokenType.ID);
        }
         eat(TokenType.COLON);

        TypeNode type = typeSpec();
        List<VarDeclNode> declarations = new ArrayList<>();
        for(VariableNode child : nodes){
            declarations.add(new VarDeclNode(child,type));
        }
        return declarations;
    }


    /**
     * type_spec : INTEGER | REAL
     * @return
     */
    private TypeNode typeSpec() throws Exception {
        Token token = curToken;
        if(curToken.type == TokenType.INTEGER)
            eat(TokenType.INTEGER);
        else
            eat(TokenType.REAL);
        return new TypeNode(token);
    }

    /**
     * program: PROGRAM variable SEMI block DOT
     * @return
     */
    private ProgramNode program() throws Exception {
        eat(TokenType.PROGRAM);
        VariableNode progName = variable();
        eat(TokenType.SEMI);
        BlockNode blockNode = block();
        eat(TokenType.DOT);
        return new ProgramNode(progName,blockNode);
    }

    /**
     *  procall_statement: ID OPEN_BRACKET empty | expr ( COMMA expr)* CLOSE_BRACKET
     * @return
     */
    private ProcedureCall procedureCall_statement() throws Exception {
        Token token = curToken;
        String procName = curToken.value;
        eat(TokenType.ID);
        eat(TokenType.OPEN_BRACKET);

        List<Node> params = new ArrayList<>();
        if(curToken.type != TokenType.CLOSE_BRACKET)
            params.add(expr());
        while(curToken.type == TokenType.COMMA){
            eat(TokenType.COMMA);
            params.add(expr());
        }
        eat(TokenType.CLOSE_BRACKET);

        return new ProcedureCall(procName,params,token);
    }

}

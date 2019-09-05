package Interpreter;

import Lexer.TokenType;
import Parser.*;

/**
 * translate/execute AST
 *
 * will use Visitor pattern to visit/traverse AST node
 */
public class Interpreter {
    private Parser parser;
    private Node root;
    public Interpreter(Parser parser) throws Exception {
        this.parser = parser;
        root= parser.parse();
    }

    public int interpreter(){
        return visit(root);
    }

    public int visit(Node node){
        if(node instanceof NumNode)
            return visit_NumNode((NumNode) node);
        else if(node instanceof BinOpNode)
            return visit_BinOpNode((BinOpNode) node);
        else if(node instanceof UnaryNode)
            return visit_Unary((UnaryNode) node);
        return 0;
    }

    private int visit_Unary(UnaryNode node) {
        if(node.op.type == TokenType.PLUS)
            return +visit(node.expr);
        else
            return -visit(node.expr);
    }

    public int visit_NumNode(NumNode node){
        return node.value;
    }

    public int visit_BinOpNode(BinOpNode node){
        if(node.op.type == TokenType.PLUS)
            return visit(node.left) + visit(node.right);
        else if(node.op.type == TokenType.MINUS)
            return visit(node.left) - visit(node.right);
        else if(node.op.type == TokenType.MULTIPLY)
            return visit(node.left) * visit(node.right);
        else if(node.op.type == TokenType.DIVIDE)
            return visit(node.left) / visit(node.right);
        return 0;
    }
}

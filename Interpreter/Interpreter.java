package Interpreter;

import Lexer.TokenType;
import Parser.*;

import java.util.HashMap;

/**
 * translate/execute AST
 *
 * will use Visitor pattern to visit/traverse AST node
 */
public class Interpreter {
    public HashMap<String,Integer> GLOBAL_SCOPE = new HashMap<>();
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
        else if(node instanceof VariableNode)
            return visit_Var((VariableNode) node);
        else if(node instanceof CompoundNode)
            visit_Compound((CompoundNode) node);
        else if(node instanceof AssignNode )
            visit_Assign((AssignNode) node);
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
    public void visit_Compound(CompoundNode node){
        for(Node child : node.children){
            visit(child);
        }
    }

    public void visit_Assign(AssignNode node){
        String var = node.left.token.value;
        GLOBAL_SCOPE.put(var, visit(node.right));
    }

    public int visit_Var(VariableNode node){
        return GLOBAL_SCOPE.get(node.token.value);
    }

    public void visit_NoOp(){

    }
}

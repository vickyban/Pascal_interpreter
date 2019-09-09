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
    public HashMap<String,Double> GLOBAL_SCOPE = new HashMap<>();
    private Parser parser;
    private Node root;
    public Interpreter(Parser parser) throws Exception {
        this.parser = parser;
        root= parser.parse();
    }

    public double interpreter(){
        return visit(root);
    }

    public double visit(Node node){
        if(node instanceof NumNode){
            return visit_Float((NumNode) node);
        }
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
        else if(node instanceof VarDeclNode )
            visit_VarDecl((VarDeclNode) node);
        else if(node instanceof TypeNode )
            visit_Type((TypeNode) node);
        else if(node instanceof ProgramNode )
            visit_Program((ProgramNode) node);
        else if(node instanceof BlockNode )
            visit_Block((BlockNode) node);
        else if(node instanceof ProcedureDeclNode )
            visit_ProdcedureDecl((ProcedureDeclNode) node);
        return 0;
    }

    private double visit_Unary(UnaryNode node) {
        if(node.op.type == TokenType.PLUS)
            return +visit(node.expr);
        else
            return -visit(node.expr);
    }

    public double visit_Float(NumNode node){
        return node.token.getDoubleValue();
    }

    public double visit_BinOpNode(BinOpNode node){
        if(node.op.type == TokenType.PLUS)
            return visit(node.left) + visit(node.right);
        else if(node.op.type == TokenType.MINUS)
            return visit(node.left) - visit(node.right);
        else if(node.op.type == TokenType.MULTIPLY)
            return visit(node.left) * visit(node.right);
        else if(node.op.type == TokenType.INTEGER_DIV)
            return (int)visit(node.left) / (int)visit(node.right);
        else if(node.op.type == TokenType.FLOAT_DIV)
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

    public double visit_Var(VariableNode node){
        return GLOBAL_SCOPE.get(node.token.value);
    }

    public void visit_NoOp(){
    }
    public void visit_Program(ProgramNode node){
        visit(node.block);
    }
    public void visit_Block(BlockNode node){
        for(Node dec : node.declarations)
            visit(dec);
        visit(node.compoundNode);
    }
    public void visit_VarDecl(VarDeclNode node){

    }
    public void visit_Type(TypeNode node){

    }
    public void visit_ProdcedureDecl(ProcedureDeclNode node){

    }
}

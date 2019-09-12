package Interpreter;

import Lexer.TokenType;
import Parser.*;
import logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * translate/execute AST
 *
 * will use Visitor pattern to visit/traverse AST node
 */
public class Interpreter {

    public CallStack stack;
    private Node root;
    private Logger log;

    public Interpreter(Node root) throws Exception {
        this.root= root;
        this.stack = new CallStack();

        log = new Logger();
        log.shouldLogStack = true;
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
        else if(node instanceof ProcedureCall )
            visit_ProdcedureCall((ProcedureCall) node);
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
        stack.peek().setItem(var, visit(node.right));
    }

    public double visit_Var(VariableNode node){
        return stack.peek().getItem(node.token.value);
    }

    public void visit_NoOp(){
    }

    public void visit_Program(ProgramNode node){
        String progName = node.progName.name;
        ActivationRecord ar = new ActivationRecord(progName,ARType.PROGRAM,1);
        stack.push(ar);

        log.logStack("ENTER: PROGRAM " + progName);
        log.logStack(stack.toString());

        visit(node.block);

        log.logStack("LEAVE: PROGRAM " + progName);
        log.logStack(stack.toString());

         stack.pop();
    }
    public void visit_Block(BlockNode node){
        for(Node dec : node.declarations)
            visit(dec);
        visit(node.compoundNode);
    }
    public void visit_VarDecl(VarDeclNode node){
        stack.peek().setItem(node.var.name, 0.0);
    }
    public void visit_Type(TypeNode node){

    }
    public void visit_ProdcedureDecl(ProcedureDeclNode node){

    }
    public void visit_ProdcedureCall(ProcedureCall node){
        List<Double> paramValues = new ArrayList<>();
        for(Node param : node.params){
            paramValues.add(visit(param));
        }

        ActivationRecord ar = new ActivationRecord(node.procName, ARType.PROCEDURE, stack.peek().nestingLevel + 1);
        stack.push(ar);


        stack.pop();
    }
}

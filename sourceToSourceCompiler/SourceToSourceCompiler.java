package sourceToSourceCompiler;

import Parser.*;
import symbol.ScopeSymbolTable;
import symbol.Symbol;
import symbol.VarSymbol;

import java.awt.*;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 * a node visiter
 */
public class SourceToSourceCompiler {
    public ScopeSymbolTable curScope;
    public String output;

    public SourceToSourceCompiler(){
        this.output = "";
    }

    /**
     * dispatch node to approriate visit method that match it type;
     * @param node
     * @return
     */
    public String visit(Node node) throws Exception {
        if(node instanceof BinOpNode)
            return visit((BinOpNode) node);
        else if(node instanceof UnaryNode)
            return visit((UnaryNode) node);
        else if(node instanceof VariableNode)
            return visit((VariableNode) node);
        else if(node instanceof CompoundNode)
            return visit((CompoundNode) node);
        else if(node instanceof AssignNode )
            return visit((AssignNode) node);
        else if(node instanceof VarDeclNode )
            return visit((VarDeclNode) node);
        else if(node instanceof ProgramNode )
            return visit((ProgramNode) node);
        else if(node instanceof BlockNode )
            return visit((BlockNode) node);
        else if(node instanceof NumNode )
            return visit((NumNode) node);
        else if(node instanceof ProcedureDeclNode)
            return visit((ProcedureDeclNode)node);
        return "";
    }

    public String visit(ProgramNode node) throws Exception {
        String progName = node.progName.name;
        String resultStr = "program " + progName + ";\n";
        ScopeSymbolTable globalScope = new ScopeSymbolTable("global",1, null);
        // init builtin Symbols;
        globalScope.init();

        curScope = globalScope;

        // visit subtree
        resultStr += visit(node.block);
        resultStr += ".";
        resultStr += "{END OF " + progName + "}";
        output = resultStr;

        curScope = curScope.enclosingScope;
        return output;
    }

    public String visit(BlockNode node) throws Exception {
        String resultStr = "";
        for(Node declaration : node.declarations){
            resultStr += visit(declaration);
        }
        resultStr += visit(node.compoundNode);
        return resultStr;
    }

    public String visit(CompoundNode node) throws Exception {
        String resultStr = "begin\n";
        for(Node statement : node.children){
            String result = visit(statement);
            if(result == null)
                continue;
            resultStr += result;
        }
        resultStr += "end;\n";
        return resultStr;
    }

    public String visit(VarDeclNode node) throws Exception {
        String varName = node.var.name;
        String type = node.varType.value;
        if(curScope.lookup(varName,true)!= null)
            throw new Exception("ERROR: Duplicate identifier " + varName + " found");

        Symbol symbolType = curScope.lookup(type);
        VarSymbol vaSymbol = new VarSymbol(varName, symbolType);

        // at varSymbol to scope
        curScope.define(vaSymbol);

        return "var " + varName +curScope.scopeLevel + " : " + type + ";\n";
    }

    public String visit(ProcedureDeclNode node) throws Exception {
        String procName = node.procName;
        int scopeLeve = curScope.scopeLevel + 1;
        ScopeSymbolTable procedureScope = new ScopeSymbolTable(procName, scopeLeve, curScope);
        curScope = procedureScope;

        String resultStr = "procedure "+ procName;
        List<String> results = new ArrayList<>();
        for(ParamNode param  : node.params){
            String varName = param.varNode.name;
            String type = param.typeNode.value;
            Symbol typeSymbol = curScope.lookup(type);
            VarSymbol varSymbol = new VarSymbol(varName, typeSymbol);
            curScope.define(varSymbol);
            results.add(varName + scopeLeve + " : " + type);
        }

        if(results.size() != 0){
            resultStr += " (" + String.join(",", results) + ")";
        }
        resultStr += "\n";

        resultStr += visit(node.block);

        resultStr += " {END OF " + procName + "}\n";
        curScope = curScope.enclosingScope;
        return resultStr;
    }

    public String visit(AssignNode node) throws Exception {
        String resultStr = visit(node.left);
        resultStr += " := ";
        resultStr += visit(node.right);
        return resultStr + ";\n";
    }

    public String visit(VariableNode node) throws Exception {
        Symbol symbol = curScope.lookup(node.name);
        if(symbol != null)
            return symbol.toString();
        else
            throw new Exception("ERROR: " + node.name + " is not found");
    }

    public String visit(BinOpNode node) throws Exception {
         return visit(node.left) + " " + node.op.value + " " + visit(node.right);
    }

    public String visit(UnaryNode node) throws Exception {
        return node.op.value + visit(node.expr);
    }

    public String visit(NumNode node){
        return node.token.value;
    }

}

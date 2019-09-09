package symbol;

import Parser.*;

/**
 * AST node visitor to build symbol table
 * AKA SemanticAnalyzer
 */
public class SemanticAnalyzer {
    public ScopeSymbolTable curScope ;
    public SemanticAnalyzer(){
    }


    public void visit(Node node) throws Exception {
        if(node instanceof BinOpNode)
             visit_BinOpNode((BinOpNode) node);
        else if(node instanceof UnaryNode)
             visit_Unary((UnaryNode) node);
        else if(node instanceof VariableNode)
             visit_Var((VariableNode) node);
        else if(node instanceof CompoundNode)
            visit_Compound((CompoundNode) node);
        else if(node instanceof AssignNode )
            visit_Assign((AssignNode) node);
        else if(node instanceof VarDeclNode )
            visit_VarDecl((VarDeclNode) node);
        else if(node instanceof ProgramNode )
            visit_Program((ProgramNode) node);
        else if(node instanceof BlockNode )
            visit_Block((BlockNode) node);
    }

    public void visit_Block(BlockNode node) throws Exception {
        for(Node child : node.declarations){
            visit(child);
        }
        visit(node.compoundNode);
    }
    public void visit_Program(ProgramNode node) throws Exception {
        System.out.println("ENTER scope: global");
        ScopeSymbolTable globalScope = new ScopeSymbolTable("global",1, curScope);
        curScope = globalScope;

        // visit subtree
        visit(node.block);

        System.out.println(globalScope);
        System.out.println("LEAVE scope: global");
    }
    public void visit_ProcedureDecl(ProcedureDeclNode node) throws Exception {
        String procName = node.procName;
        ProcedureSymbol procSymbol = new ProcedureSymbol(procName);
        curScope.define(procSymbol);

        System.out.println("ENTER scope " + procName);

        // scope for parameters and local variable
        // use current_scope to find out the scope level of the nesting scope
        ScopeSymbolTable procedureScope = new ScopeSymbolTable(procName,curScope.scopeLevel + 1, curScope);
        curScope = procedureScope;
        for(ParamNode param : node.params){
            Symbol paramType = curScope.lookup(param.typeNode.value);
            String paramName = param.varNode.name;
            VarSymbol varSymbol = new VarSymbol(paramName,paramType);
            curScope.define(varSymbol);
            procSymbol.params.add(varSymbol);
        }
        visit(node.block);

        System.out.println(procedureScope);
        // return back to outer scope;
        curScope = curScope.enclosingScope;

        System.out.println("LEAVE scope " + procName);
    }

    public void visit_BinOpNode(BinOpNode node) throws Exception {
        visit(node.left);
        visit(node.right);
    }
    public void visit_Unary(UnaryNode node) throws Exception {
        visit(node.expr);
    }

    public void visit_Compound(CompoundNode node) throws Exception {
        for(Node child: node.children){
            visit(child);
        }
    }

    public void visit_VarDecl(VarDeclNode node) throws Exception {
        String typeName = node.varType.value;
        Symbol typeSymbol = curScope.lookup(typeName);

        // we have all the info we need to create a variable symbol
        String varName = node.var.token.value;
        VarSymbol varSymbol = new VarSymbol(varName,typeSymbol);

        // Check of the var is declared twice or more
        if(curScope.lookup(varName) != null)
            throw new Exception("Error: Duplicate identifier " + varName + " found");
        curScope.define(varSymbol);
    }

    public void visit_Var(VariableNode node) throws Exception {
        String varName = node.token.value;
        Symbol varSymbol =  curScope.lookup(varName);
        if(varSymbol == null){
            throw new Exception("Error: " + varName + " variable is not declared");
        }

    }

    public void visit_Assign(AssignNode node) throws Exception {
        visit(node.left);
        visit(node.right);
    }
}

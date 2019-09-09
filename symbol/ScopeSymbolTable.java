package symbol;

import java.util.HashMap;

/**
 * an Abstract Data type for tracking various symbols in source code
 */
public class ScopeSymbolTable {
    public String scopeName;
    public int scopeLevel;
    public ScopeSymbolTable enclosingScope;
    public HashMap<String, Symbol> symbols = new HashMap<>();

    public ScopeSymbolTable(){
        init();
    }

    public ScopeSymbolTable(String scopeName, int scopeLevel,ScopeSymbolTable enclosingScope){
        this();
        this.scopeName = scopeName;
        this.scopeLevel = scopeLevel;
        this.enclosingScope = enclosingScope;
    }

    public void init(){
        define(new BuiltinTypeSymbol("INTEGER"));
        define(new BuiltinTypeSymbol("REAL"));
    }

    public void define(Symbol symbol){
        System.out.println("Define "+ symbol);
        symbols.put(symbol.name, symbol);
    }

    /**
     * do name resolution - mapping a variable reference to its declaration
     * @param name
     * @return
     */
    public Symbol lookup(String name){
        System.out.println("Lookup " + name);
        Symbol symbol = symbols.get(name);
        return symbol;
    }

    public String toString(){
        return scopeName + "_" +scopeLevel + " : " + symbols;
    }
}

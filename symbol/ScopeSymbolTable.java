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
    }

    public ScopeSymbolTable(String scopeName, int scopeLevel,ScopeSymbolTable enclosingScope){
        this.scopeName = scopeName;
        this.scopeLevel = scopeLevel;
        this.enclosingScope = enclosingScope;
    }

    public void init(){
        symbols.put("INTEGER",new BuiltinTypeSymbol("INTEGER"));
        symbols.put("REAL",new BuiltinTypeSymbol("REAL"));
    }

    public void define(Symbol symbol){
        String name = symbol.name;
        symbol.name = name + scopeLevel;
        System.out.println("Define "+ symbol);
        symbols.put(name, symbol);
    }

    /**
     * do name resolution - mapping a variable reference to its declaration
     * @param name
     * @return
     */
    public Symbol lookup(String name){
        return lookup(name, false);
    }

    public Symbol lookup(String name, boolean curScopeOnly){
        System.out.println("Lookup " + name);
        Symbol symbol = symbols.get(name);
        if(symbol != null)
            return symbol;
        if(curScopeOnly){
            return null;
        }
        // recursively go up the chain and lookup the name
        if(enclosingScope != null)
            return enclosingScope.lookup(name);
        return null;
    }

    public String toString(){
        return scopeName + "_" +scopeLevel + " : " + symbols;
    }
}

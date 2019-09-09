package symbol;

public class VarSymbol extends Symbol {
    public VarSymbol(String name, Symbol type){
        super(name,type);
    }

    public String toString(){
        return "<"+name+":"+type+">";
    }
}

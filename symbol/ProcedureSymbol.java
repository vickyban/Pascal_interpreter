package symbol;

import Parser.ParamNode;

import java.util.ArrayList;
import java.util.List;

public class ProcedureSymbol extends Symbol {
    public List<VarSymbol> params;
    public ProcedureSymbol(String name){
        super(name);
        this.params = new ArrayList<>() {
        };
    }

    public String toString(){
        return super.toString() + ", params = " + params;
    }
}

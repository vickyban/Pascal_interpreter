package symbol;

/**
 *
 */
public class Symbol {
    public String name;
    public Symbol type;

    public Symbol(String name){
        this(name, null);
    }

    public Symbol(String name, Symbol type){
        this.name = name;
        this.type = type;
        // category is encode into the class name
    }

    public String toString(){
        return name;
    }
}

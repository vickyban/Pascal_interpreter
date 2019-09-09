package Parser;

/**
 * Node store procedure's paramenter
 */
public class ParamNode extends Node {
    public VariableNode varNode;
    public TypeNode typeNode;
    public ParamNode(VariableNode varNode, TypeNode typeNode){
        this.varNode = varNode;
        this.typeNode= typeNode;
    }
}

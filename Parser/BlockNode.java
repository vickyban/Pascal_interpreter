package Parser;

import java.util.List;

/**
 * Block AST holds delcarations and a compount statment
 */
public class BlockNode extends Node {
    public List<Node> declarations;
    public CompoundNode compoundNode;
    public BlockNode(List<Node> declarations, CompoundNode compoundNode){
        this.declarations = declarations;
        this.compoundNode = compoundNode;
    }
}

package Parser;

public class ProgramNode extends Node{
    public VariableNode progName;
    public BlockNode block;
    public ProgramNode(VariableNode progName, BlockNode block){
        this.progName = progName;
        this.block = block;
    }
}

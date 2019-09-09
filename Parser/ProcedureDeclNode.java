package Parser;

import java.util.List;

/**
 *
 */
public class ProcedureDeclNode extends Node {
    public String procName;
    public BlockNode block;
    public List<ParamNode> params;

    public ProcedureDeclNode(String proc_name, List<ParamNode> params, BlockNode block){
        procName = proc_name;
        this.params = params;
        this.block = block;
    }
}

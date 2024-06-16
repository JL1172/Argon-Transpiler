package nodes.functionNodes;

import java.util.List;

public class FuncDefNode {
    private String identifier;
    private List<String> funcReturnType;
    private List<String> funcParamType;

    public FuncDefNode(String id, List<String> frt, List<String> fpt) {
        this.identifier = id;
        this.funcParamType = fpt;
        this.funcReturnType = frt;
    }
    public String getId() {
        return this.identifier;
    }
    public List<String> getReturnType() {
        return this.funcReturnType;
    }
    public List<String> getParamType() {
        return this.funcParamType;
    }
}

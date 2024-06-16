package nodes;

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
    public String getReturnType() {
        return this.funcReturnType.toString();
    }
    public String getParamType() {
        return this.funcParamType.toString();
    }
}

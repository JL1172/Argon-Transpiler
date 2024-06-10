package nodes;

public class FuncDefNode {
    private String identifier;
    private String funcReturnType;
    private String funcParamType;

    public FuncDefNode(String id, String frt, String fpt) {
        this.identifier = id;
        this.funcParamType = fpt;
        this.funcReturnType = frt;
    }
    public String getId() {
        return this.identifier;
    }
    public String getReturnType() {
        return this.funcReturnType;
    }
    public String getParamType() {
        return this.funcParamType;
    }
}

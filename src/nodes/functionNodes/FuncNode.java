package nodes.functionNodes;

public class FuncNode {
    public final String funcName;
    public final FuncBodyNode funcBody;
    public FuncNode(String funcName, FuncBodyNode funcBody) {
        this.funcName = funcName;
        this.funcBody = funcBody;
    }
}

package nodes.functionNodes;

import java.util.List;

public class FuncBodyNode {
    //log statements
    //return statements
    //>variable declaration
    public final List<FuncLogStatementNode> logStatements;
    public final List<FuncVariableNode> variables;
    public final FuncReturnStatementNode returnStatement;
    public FuncBodyNode(List<FuncLogStatementNode> logStatementNodes, List<FuncVariableNode> variableNodes, FuncReturnStatementNode funcReturnStatementNode) {
        this.logStatements = logStatementNodes;
        this.variables = variableNodes;
        this.returnStatement = funcReturnStatementNode;
    }
}

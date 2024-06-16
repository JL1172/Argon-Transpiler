package nodes.functionNodes;

import java.util.List;

public class FuncVariableNode {
    //variable name 
    //constant status
    //variable type
    //variable value
    public final String varName;
    public final Boolean constant;
    public final List<FuncVariableTypeNode> varTypes;
    public String varValue;
    public FuncVariableNode(String fString, Boolean cBoolean, List<FuncVariableTypeNode> variableTypeNodes, String vaString) {
        this.varName = fString;
        this.constant = cBoolean;
        this.varTypes = variableTypeNodes;
        this.varValue = vaString;
    }
}

package parser;

public class FuncDefToken {
    private final FuncDefTokenType tokenType;
    private final String tokenValue;

    public FuncDefToken(FuncDefTokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.tokenValue = value;
    }

    public FuncDefTokenType getTokenType() {
        return this.tokenType;
    }

    public String getTokenValue() {
        return this.tokenValue;
    }

    @Override
    public String toString() {
        return "\n" + "Token{" +
                "type=" + this.tokenType +
                ", value='" + this.tokenValue + '\'' + '}';
    }
}

package lexer;

public class Token {
    private final TokenType tokenType;
    private final String tokenValue;

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.tokenValue = value;
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public String getTokenValue() {
        return this.tokenValue;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + this.tokenType +
                ", value='" + this.tokenValue + '\'' + '}';
    }
}

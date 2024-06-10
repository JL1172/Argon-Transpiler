package parser;

import java.util.HashMap;
import java.util.List;
import lexer.Token;
import lexer.TokenType;

public class Parser {
    // meta info
    private Stack stack = new Stack();
    private HashMap<String, String> matchingSymbols = new HashMap<>();
    private List<Token> tokens;
    private int currentIdx = 0;

    public Parser(List<Token> tokens) {
        matchingSymbols.put("}", "{");
        matchingSymbols.put(")", "(");
        this.tokens = tokens;
    }

    private void reportError(String message) {
        try {
            throw new RuntimeException(String.format("Compile Error: %s", message));
        } catch (RuntimeException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
    // meta info

    private void validatePunctuation(String character) {
        if (character.equals("{") || character.equals("(")) {
            this.stack.push(character);
        } else {
            String deletedCharacterToCompareTo = this.stack.pop();
            if (!(matchingSymbols.get(character).equals(deletedCharacterToCompareTo))) {
                this.reportError(String.format("Error Parsing Punctuation. Expected Adjacent Closing Punctuation: ",
                        matchingSymbols.get(character)));
            }
        }
    }

    // main entry point
    public void parseCode() {
        this.parseStart();
    }

    private void parseStart() {
        System.out.println(this.tokens.get(currentIdx));
    }

    private Token peek() {
        if (this.tokens.size() > 0) {
            return this.tokens.get(this.currentIdx);
        } else {
            return null;
        }
    }

    private boolean expect(TokenType input) {
        Token currentToken = this.peek();
        if (currentToken.getTokenType() != input) {
            this.reportError(String.format("Expected Token Type %s, Received %s", currentToken.getTokenType(), input));
            return false;
        } else {
            return true;
        }
    }
}

package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Style;

import lexer.Token;
import lexer.TokenType;

public class Parser {
    // meta info
    private Stack stack = new Stack();
    private HashMap<String, String> functionReferences = new HashMap<>();
    private HashMap<String, String> matchingSymbols = new HashMap<>();
    private List<Token> tokens;
    private int currentIdx = 0;

    public Parser(List<Token> tokens) {
        matchingSymbols.put("}", "{");
        matchingSymbols.put(")", "(");
        matchingSymbols.put(">", "<");
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
        if (character.equals("{") || character.equals("(") || character.equals("<")) {
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
        // todo this will eventually change, this is forcing everything to be functional
        boolean expectFunctionDefinition = this.expect(TokenType.FUNCTION_DEFINITION);
        if (expectFunctionDefinition == true) {
            this.parseFunctionDefinition();
        }
    }

    private void parseFunctionDefinition() {
        // first grab current token
        Token token = this.peek();
        // then initialize funcName
        StringBuilder funcName = new StringBuilder();
        StringBuilder funcReturnType = new StringBuilder();
        StringBuilder funcParamTypes = new StringBuilder();
        int len = token.getTokenValue().length();
        String val = token.getTokenValue();
        int trackIdx = 0;
        int step = 0;
        while (trackIdx < len) {
            // start if that character at start is == @ then increment and if it is but the
            // idx != 0 then throw error because it should only exist once
            if (val.charAt(trackIdx) == '@') {
                if (trackIdx != 0) {
                    this.reportError("Syntax Error For Function Declaration. Unexpected Token: '@' ");
                }
                while (val.charAt(trackIdx) != '<') {
                    funcName.append(val.charAt(trackIdx));
                    trackIdx++;
                }
            }
            // now if the current char is < start parsing the function return type
            if (val.charAt(trackIdx) == '<') {
                if (step != 0) {
                    this.reportError("Syntax Error For Function Declaration. Unexpected Token: '<' ");
                }
                // now increment step to next step
                step++;
                trackIdx++;
                while (val.charAt(trackIdx) != '>') {
                    Pattern typePattern = Pattern.compile("^[a-z]*$");
                    Matcher match = typePattern.matcher(String.valueOf(val.charAt(trackIdx)));
                    if (match.find()) {
                        funcReturnType.append(String.valueOf(val.charAt(trackIdx)));
                    } else {
                        this.reportError("Unexpected Token. Expected Proper Return Type");
                    }
                    trackIdx++;
                }
            }
            if (val.charAt(trackIdx) == '>') {
                if (step != 1) {
                    this.reportError("Syntax Error For Function Declaration. Unexpected Token: '>' ");
                }
                step++;
                trackIdx++;
            }
            if (val.charAt(trackIdx) == '(') {
                if (step != 2) {
                    this.reportError("Syntax Error For Function Declaration. Unexpected Token: '(' ");
                }
                trackIdx++;
                step++;
                while (val.charAt(trackIdx) != ')') {
                    // todo need to eventually account for multiple param types
                    Pattern typePattern = Pattern.compile("^[a-z]*$");
                    Matcher match = typePattern.matcher(String.valueOf(val.charAt(trackIdx)));
                    if (match.find()) {
                        funcParamTypes.append(String.valueOf(val.charAt(trackIdx)));
                    } else {
                        this.reportError("Unexpected Token. Expected Proper Type");
                    }
                    trackIdx++;
                }
            }
            if (step == 3) {
               if (val.charAt(trackIdx) != ')') {
                this.reportError("Syntax Error For Function Declaration. Unexpected Token, Expected ')' ");
               }
            }
            trackIdx++;
        }
        System.out.println(String.valueOf(funcName));
        System.out.println(String.valueOf(funcReturnType));
        System.out.println(String.valueOf(funcParamTypes));
        
        this.currentIdx++;
        this.expect(TokenType.SEMICOLON);
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

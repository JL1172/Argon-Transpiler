package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lexer.Token;
import lexer.TokenType;
import nodes.FuncDefNode;

public class Parser {
    // meta info
    private Stack stack = new Stack();
    private List<FuncDefNode> functionReferences = new ArrayList<>();
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
        // this forces everything to start as function.
        boolean expectFunctionDefinition = this.expect(TokenType.FUNCTION_DEFINITION);
        if (expectFunctionDefinition == true) {
            this.parseFunctionDefinition();
        }
    }

    private void parseFunctionDefinition() {
        // first grab current token
        Token token = this.peek();
        // then initialize funcName
        String funcName;
        List<String> funcReturnType = new ArrayList<>();
        StringBuilder funcParamTypes = new StringBuilder();

        Pattern[] functionDefPatterns = {
                Pattern.compile("@"),
                // todo this below pattern only supports primitive types, need to eventually
                // change
                Pattern.compile("\\<"),
                Pattern.compile("str"),
                Pattern.compile("int"),
                Pattern.compile("double"),
                Pattern.compile("bool"),
                Pattern.compile("null"),
                Pattern.compile("void"),
                Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*"), // Identifier,
                Pattern.compile("\\>"),
                Pattern.compile("\\("),
                Pattern.compile("\\)"),
        };

        // first grab val
        String functionDefinition = token.getTokenValue();
        // initialize tokenization
        int currentPosition = 0;
        int funcDefLength = functionDefinition.length();
        int patternLength = functionDefPatterns.length;
        List<FuncDefToken> funcDefTokens = new ArrayList<>();
        while (currentPosition < funcDefLength) {
            boolean match = false;
            for (int i = 0; i < patternLength; i++) {
                Pattern currPattern = functionDefPatterns[i];
                Matcher currMatch = currPattern.matcher(functionDefinition.substring(currentPosition));
                if (currMatch.lookingAt()) {
                    match = true;
                    String val = currMatch.group();
                    FuncDefTokenType type = FuncDefTokenType.values()[i];
                    funcDefTokens.add(new FuncDefToken(type, val));
                    currentPosition += val.length();
                }
            }
            if (match == false) {
                currentPosition++;
            }
        }
        int functionDefinitionIndex = 0;
        int functionDefinitionTokenListLength = funcDefTokens.size();
        String firstToken = funcDefTokens.get(functionDefinitionIndex).getTokenValue();
       
        if (!firstToken.equals("@")) {
            this.reportError(String.format("Syntax Error. Expected Token '@' Recieved Token %s.", firstToken));
        }
        functionDefinitionIndex++;
        FuncDefToken secondToken = funcDefTokens.get(functionDefinitionIndex);
        if (secondToken.getTokenType() != FuncDefTokenType.FUNC_NAME) {
            this.reportError(String.format("Syntax Error. Expected Token 'FUNC_NAME' Recieved Token %s",
                    secondToken.getTokenType()));
        }
        funcName = funcDefTokens.get(functionDefinitionIndex).getTokenValue();
        functionDefinitionIndex++;
        FuncDefToken thirdToken = funcDefTokens.get(functionDefinitionIndex);
        if (thirdToken.getTokenType() != FuncDefTokenType.OPEN_CARROT) {
            this.reportError(
                    String.format("Syntax Error. Expected Token '<' Recieved Token %s", thirdToken.getTokenType()));
        }
        functionDefinitionIndex++;
        // todo eventually need to parse "|" for union types
        FuncDefToken currToken = funcDefTokens.get(functionDefinitionIndex);
        while ((currToken.getTokenType() == FuncDefTokenType.BOOL || currToken.getTokenType() == FuncDefTokenType.INT
                || currToken.getTokenType() == FuncDefTokenType.DOUBLE
                || currToken.getTokenType() == FuncDefTokenType.STR || currToken.getTokenType() == FuncDefTokenType.NULL
                || currToken.getTokenType() == FuncDefTokenType.VOID)
                && functionDefinitionIndex < functionDefinitionTokenListLength) {
            funcReturnType.add(currToken.getTokenValue());
            functionDefinitionIndex++;
            currToken = funcDefTokens.get(functionDefinitionIndex);
        }
        System.out.print(funcDefTokens.get(functionDefinitionIndex));

        // int len = token.getTokenValue().length();
        // String val = token.getTokenValue();
        // int trackIdx = 0;
        // int step = 0;
        // while (trackIdx < len) {
        // // start if that character at start is == @ then increment and if it is but
        // the
        // // idx != 0 then throw error because it should only exist once
        // if (val.charAt(trackIdx) == '@') {
        // if (trackIdx != 0) {
        // this.reportError("Syntax Error For Function Declaration. Unexpected Token:
        // '@' ");
        // }
        // while (val.charAt(trackIdx) != '<') {
        // funcName.append(val.charAt(trackIdx));
        // trackIdx++;
        // }
        // }
        // // now if the current char is < start parsing the function return type
        // if (val.charAt(trackIdx) == '<') {
        // if (step != 0) {
        // this.reportError("Syntax Error For Function Declaration. Unexpected Token:
        // '<' ");
        // }
        // // now increment step to next step
        // step++;
        // trackIdx++;
        // while (val.charAt(trackIdx) != '>') {
        // Pattern typePattern = Pattern.compile("^[a-z]*$");
        // Matcher match = typePattern.matcher(String.valueOf(val.charAt(trackIdx)));
        // if (match.find()) {
        // funcReturnType.append(String.valueOf(val.charAt(trackIdx)));
        // } else {
        // this.reportError("Unexpected Token. Expected Proper Return Type");
        // }
        // trackIdx++;
        // }
        // }
        // if (val.charAt(trackIdx) == '>') {
        // if (step != 1) {
        // this.reportError("Syntax Error For Function Declaration. Unexpected Token:
        // '>' ");
        // }
        // step++;
        // trackIdx++;
        // }
        // if (val.charAt(trackIdx) == '(') {
        // if (step != 2) {
        // this.reportError("Syntax Error For Function Declaration. Unexpected Token:
        // '(' ");
        // }
        // trackIdx++;
        // step++;
        // while (val.charAt(trackIdx) != ')') {
        // // todo need to eventually account for multiple param types
        // Pattern typePattern = Pattern.compile("^[a-z]*$");
        // Matcher match = typePattern.matcher(String.valueOf(val.charAt(trackIdx)));
        // if (match.find()) {
        // funcParamTypes.append(String.valueOf(val.charAt(trackIdx)));
        // } else {
        // this.reportError("Unexpected Token. Expected Proper Type");
        // }
        // trackIdx++;
        // }
        // }
        // if (step == 3) {
        // if (val.charAt(trackIdx) != ')') {
        // this.reportError("Syntax Error For Function Declaration. Unexpected Token,
        // Expected ')' ");
        // }
        // }
        // trackIdx++;
        // }
        // this.functionReferences.add(new FuncDefNode(String.valueOf(funcName),
        // String.valueOf(funcReturnType), String.valueOf(funcParamTypes)));
        // this.currentIdx++;
        // this.expect(TokenType.SEMICOLON);

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

//todo the way i am going to deal with validating the function definitions is by using a stack, but just implementing with an arraylist
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
            currentIdx++;
        } 
        this.parseFunction();
    }

    private void parseFunctionDefinition() {
        Token token = this.peek();
        String funcName;
        List<String> funcReturnTypes = new ArrayList<>();
        List<String> funcParamTypes = new ArrayList<>();

        Pattern[] functionDefPatterns = {
                Pattern.compile("@"),
                // todo this below pattern only supports primitive types, need to change
                Pattern.compile("\\<"),
                Pattern.compile("str"),
                Pattern.compile("int"),
                Pattern.compile("double"),
                Pattern.compile("bool"),
                Pattern.compile("null"),
                Pattern.compile("void"),
                Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*"),
                Pattern.compile("\\>"),
                Pattern.compile("\\("),
                Pattern.compile("\\)"),
                Pattern.compile("\\;"),
        };
        String functionDefinition = token.getTokenValue();
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
            funcReturnTypes.add(currToken.getTokenValue());
            functionDefinitionIndex++;
            currToken = funcDefTokens.get(functionDefinitionIndex);
        }
        FuncDefToken falseFourthToken = funcDefTokens.get(functionDefinitionIndex);
        if (falseFourthToken.getTokenType() != FuncDefTokenType.CLOSED_CARROT) {
            this.reportError(String.format("Syntax Error. Expected Token '>' Recieved Token %s",
                    falseFourthToken.getTokenType()));
        }
        functionDefinitionIndex++;
        FuncDefToken falseFifthToken = funcDefTokens.get(functionDefinitionIndex);
        if (falseFifthToken.getTokenType() != FuncDefTokenType.LPAREN) {
            this.reportError(String.format("Syntax Error. Expected Token '(' Recieved Token %s",
            falseFourthToken.getTokenType()));
        }
        functionDefinitionIndex++;
        currToken = funcDefTokens.get(functionDefinitionIndex);
        while ((currToken.getTokenType() == FuncDefTokenType.BOOL || currToken.getTokenType() == FuncDefTokenType.INT
        || currToken.getTokenType() == FuncDefTokenType.DOUBLE
        || currToken.getTokenType() == FuncDefTokenType.STR || currToken.getTokenType() == FuncDefTokenType.NULL
        || currToken.getTokenType() == FuncDefTokenType.VOID)
        && functionDefinitionIndex < functionDefinitionTokenListLength) {
            funcParamTypes.add(currToken.getTokenValue());
            functionDefinitionIndex++;
            currToken = funcDefTokens.get(functionDefinitionIndex);
        }
        FuncDefToken falseSixthToken = funcDefTokens.get(functionDefinitionIndex);
        if (falseSixthToken.getTokenType() != FuncDefTokenType.RPAREN) {
            this.reportError(String.format("Syntax Error. Expected Token '(' Recieved Token %s", falseSixthToken.getTokenType()));
        }
        functionReferences.add(new FuncDefNode(funcName, funcReturnTypes, funcParamTypes));

        
        FuncDefToken falseSeventhToken = funcDefTokens.get(functionDefinitionIndex);
        if (falseSeventhToken.getTokenType() != FuncDefTokenType.RPAREN) {
            this.reportError(String.format("Syntax Error. Expected Token ';' Recieved Token %s", falseSeventhToken.getTokenType()));
        }
        functionReferences.add(new FuncDefNode(funcName, funcReturnTypes, funcParamTypes));
    }
    private void parseFunction() {
        Token currToken = this.peek();
        System.out.println(currToken);
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

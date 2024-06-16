package lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Parser;

public class Lexer {
    public static final Pattern[] PATTERNS = {
            Pattern.compile("const"),
            Pattern.compile("let"),

            // function definition
            Pattern.compile("@([a-zA-Z_][a-zA-Z0-9_]*)<([^>]+)>\\(([^,)]*(?:,[^,)]*)*)\\)\\;"),

            Pattern.compile("function"), 

            Pattern.compile("null"),
            Pattern.compile("int"),
            Pattern.compile("double"),
            Pattern.compile("bool"),
            
            Pattern.compile("str"),

            Pattern.compile("console.log\\((.*?)\\)"),

            Pattern.compile("-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?"), // number identifier
            Pattern.compile("\"(?:\\\\\"|[^\"])*\"|'(?:\\\\'|[^'])*'"), // String
            Pattern.compile("true|false"), // Boolean

            Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*"), // Identifier,

            Pattern.compile("="), // Assignment
            Pattern.compile("/="), // Divided equals sign
            Pattern.compile("\\*="), // Multiplied equals sign
            Pattern.compile("\\*\\*="), // Exponentiate equals sign
            Pattern.compile("%="), // Modulus equals sign
            Pattern.compile("\\+="), // Plus equals sign
            Pattern.compile("-="), // Minus equals sign
            Pattern.compile("\\++="), // Increment equals sign
            Pattern.compile("--="), // Decrement equals sign

            Pattern.compile("\\+"), // Plus
            Pattern.compile("\\+\\+"), // Increment
            Pattern.compile("-"), // Minus
            Pattern.compile("--"), // Decrement
            Pattern.compile("%"), // Modulus
            Pattern.compile("/"), // Division
            Pattern.compile("\\*"), // Multiplication
            Pattern.compile("\\*\\*"), // Exponentiate
            Pattern.compile("\\/\\*\\*"), // Square root

            Pattern.compile("==="), // Strictly equals
            Pattern.compile("!=="), // Strictly not equals
            Pattern.compile("&&"), // AND
            Pattern.compile("\\|\\|"), // OR
            Pattern.compile("!"), // NOT
            Pattern.compile(">"), // Greater than
            Pattern.compile("<"), // Less than
            Pattern.compile(">="), // Greater than or equal to
            Pattern.compile("<="), // Less than or equal to

            Pattern.compile("\\;"), // Semicolon
            Pattern.compile(":"), // Colon
            Pattern.compile("\\("), // Left parenthesis
            Pattern.compile("\\)"), // Right parenthesis
            Pattern.compile("\\{"), // Left brace
            Pattern.compile("\\}"), // Right brace
            Pattern.compile("\\["), // Left bracket
            Pattern.compile("\\]"), // Right bracket
            Pattern.compile(","), // Comma
            Pattern.compile("\\$"), // Dollar sign
            Pattern.compile("`"), // Back tick
            Pattern.compile("\""), // Quote
            Pattern.compile("'"), // Single quote
            Pattern.compile("\\."),
    };

    public List<Token> tokenizer(String input) {
        int currentPosition = 0;
        int inputLength = input.length();
        int patternLength = Lexer.PATTERNS.length;
        List<Token> tokens = new ArrayList<>();

        while (currentPosition < inputLength) {
            boolean match = false;
            for (int i = 0; i < patternLength; i++) {
                Pattern currentPattern = Lexer.PATTERNS[i];
                Matcher currenMatcher = currentPattern.matcher(input.substring(currentPosition));
                if (currenMatcher.lookingAt()) {
                    String value = currenMatcher.group();
                    TokenType type = TokenType.values()[i];
                    tokens.add(new Token(type, value));
                    currentPosition += value.length();
                    match = true;
                    break;
                }
            }
            if (match == false) {
                currentPosition++;
            }
        }
        return tokens;
    }

    private static String readSourceCodeFromFile(String filename) {
        StringBuilder sourceCodeBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sourceCodeBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceCodeBuilder.toString();
    }

    public static void main(String args[]) {
        String sourceCode = readSourceCodeFromFile(
                "/media/jacoblang11/technical/langs/Argon-Transpiler/source-code/source.ar");
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenizer(sourceCode);
        System.out.println(tokens);
        Parser parser = new Parser(tokens);
        parser.parseCode();
    }
}
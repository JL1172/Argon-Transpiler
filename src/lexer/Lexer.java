package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Lexer {
    public static final Pattern[] PATTERNS = {
        Pattern.compile("const"),
        Pattern.compile("let"),

        Pattern.compile("null"),
        Pattern.compile("int"),
        Pattern.compile("double"),
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

        Pattern.compile(";"), // Semicolon
        Pattern.compile("::"), // Double colon
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

    public List<TokenType> tokenizer(String input) {
        //init variable
        ArrayList<Token> tokens = new ArrayList<Token>();
        int currentPosition = 0;
        int inputLength = input.length();
        int patterLength = Lexer.PATTERNS.length;

        while (currentPosition < inputLength) {
            boolean match = false;

            // for (int i )
        }
    }

    public static void main(String args[]) {
        System.out.println("hello world");
    }
}
package lexer;

public enum TokenType {
    //VAR DECLARATION
    CONST,
    LET,

    // type names
    NULL_IDENTIFIER,
    INTEGER_IDENTIFIER,
    DOUBLE_IDENTIFIER,
    STRING_IDENTIFIER,

    // output statement
    CONSOLE_LOG,

    // primitive types
    NUMERIC_TYPE,
    STRING_TYPE, // anything surrounded by single or double quotes
    BOOLEAN_TYPE, // true or false,

    // identifier for name of method or class or property
    IDENTIFIER,

    // assignment operators
    ASSIGNMENT, // =
    DIVIDED_EQUALS_SIGN, // /=
    MULTIPLIED_EQUALS_SIGN, // *=
    EXPONENTIATE_EQUALS_SIGN, // **=
    MODULUS_EQUALS_SIGN, // %=
    PLUS_EQUALS_SIGN, // +=
    MINUS_EQUALS_SIGN, // -=
    INCREMENT_EQUALS_SIGN, // ++=
    DECREMENT_EQUALS_SIGN, // --=

    // operators
    PLUS, // +
    INCREMENT, // ++
    MINUS, // -
    DECREMENT, // --
    MODULUS, // %
    DIVISION, // /
    MULTIPLICATION, // *
    EXPONENTIATE, // **
    SQUARE_ROOT, // /**

    // equality assignment
    STRICTLY_EQUALS, // ===
    STRICTLY_NOT_EQUALS, // !==
    AND, // &&
    OR, // ||
    NOT, // !
    GREATER_THAN, // >
    LESS_THAN, // <
    GREATER_THAN_OR_EQUAL_TO, // >=
    LESS_THAN_OR_EQUAL_TO, // <=,

    // punctuation
    SEMICOLON, // ;
    DOUBLE_COLON, // ::
    COLON, // :
    LPAREN, // (
    RPAREN, // )
    LBRACE, // {
    RBRACE, // }
    LBRACKET, // [
    RBRACKET, // ]
    COMMA, // ,
    DOLLAR_SIGN, // $
    BACK_TICK, // `
    QUOTE, // "
    SINGLE_QUOTE, // '
    DOT, // .
}
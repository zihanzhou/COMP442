package Compiler;

import java.io.*;
import java.util.regex.Pattern;

public class Lexer {
    private static final String KEYWORDS = "if|then|else|integer|float|string|void|public|private|func|var|class|while|read|write|return|main|inherits|break|continue";
    private static final String LETTER = "[a-zA-Z]";
    private static final String DIGIT = "[0-9]*";
    private static final String NONZERO = "[1-9]";
    private static final String ALPHANUM = "[[a-zA-Z]|[0-9]|_]*";
    private static final String FLOATNUM = "\\d+([.]\\d*)?(e[+-]?\\d+)?";
    private static final String OPERATORS = "^(\\+|-|\\*|/|=|>|<|>=|<=|&|\\||%|!|\\^|\\(|\\))$";

    DataList<Token> token = new DataList<Token>();
    Pattern keywords = Pattern.compile(KEYWORDS);
    Pattern letter = Pattern.compile(LETTER);
    Pattern digit = Pattern.compile(DIGIT);
    Pattern nonzero = Pattern.compile(NONZERO);
    Pattern alphanum = Pattern.compile(ALPHANUM);
    Pattern floatnum = Pattern.compile(FLOATNUM);
    Pattern operators = Pattern.compile(OPERATORS);
    private BufferedReader reader;
    private BufferedWriter out_token;
    private BufferedWriter out_error;
    private int linetracker;
    private boolean EOF;

    public Lexer(){ }
    //init
    public Lexer(File filename){
        try{
            String outErrorName = "outlexerrors/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outlexerrors";
            String outTokenName = "outlextokens/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outlextokens";
            reader = new BufferedReader(new FileReader(filename));
            out_token = new BufferedWriter(new FileWriter(outTokenName));
            out_error = new BufferedWriter(new FileWriter(outErrorName));
            linetracker = 1;
            EOF = false;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no such files!");
            System.exit(0);
        }
    }

    public boolean isEOF(){
        return EOF;
    }

    public static String[] singleChars(String s) {
        return s.split("(?!^)");
    }

    public boolean isKeywords(String input){
        return keywords.matcher(input).matches();
    }

    public boolean isLetter(String input){
        return letter.matcher(input).matches();
    }

    public boolean isDigit(String input){
        return digit.matcher(input).matches();
    }

    public boolean isNonzero(String input){
        return nonzero.matcher(input).matches();
    }

    public boolean isAlphanum(String input){
        String[] ch = singleChars(input);
        for (int i = 0; i < ch.length; i++){
            if (!alphanum.matcher(ch[i]).matches())
                return false;
        }
        return true;
    }

    public boolean isID(String input){
        String[] ch = singleChars(input);
        if (isLetter(ch[0])){
            if (input.length() > 1) {
                if (!isAlphanum(input.substring(1))) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

    public boolean isInt(String input){
        String[] ch = singleChars(input);
        if ((ch.length == 1) && (ch[0].equals("0"))){
            return true;
        }else{
            if (isNonzero(ch[0])) {
                for (int i = 1; i < ch.length; i++) {
                    if (!isDigit(ch[i])) return false;
                }
                return true;
            }else{
                return false;
            }
        }
    }

    public boolean isFraction(String input){
        String[] ch = singleChars(input);
        if (ch[0].equals(".")){
            if ((ch.length == 2) && (ch[1].equals("0"))){
                return true;
            }else{
                for(int i = 1; i < ch.length - 1; i++){
                    if (!isDigit(ch[i])) return false;
                }
                if (ch[ch.length - 1].equals("0")){
                    return false;
                } else {
                    return true;
                }
            }
        }else{
            return false;
        }
    }

    public boolean isFloatNum(String input) {
        return floatnum.matcher(input).matches();
    }

    public boolean isFloat(String input){
        if (input.contains(".") && (input.length() >= 3)){
            String float_int = input.split("\\.")[0];
            if (isInt(float_int)){
                if (input.contains("e")){
                    String float_fraction = input.split("\\.")[1].split("e")[0];
                    String float_e = input.split("\\.")[1].split("e")[1];
                    float_fraction = "." + float_fraction;
                    if (isFraction(float_fraction)){
                        Boolean is_integer = false;
                        String[] ch = singleChars(float_e);
                        switch (ch[0]){
                            case "+":
                                float_e = float_e.substring(1);
                                break;
                            case "-":
                                float_e = float_e.substring(1);
                                break;
                            default:
                                break;
                        }
                        if (isInt(float_e)){
                            return true;
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                } else {
                    String float_fraction = "." + input.split("\\.")[1];
                    if (isFraction(float_fraction)){
                        return true;
                    } else {
                        return false;
                    }

                }
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    private boolean isOperator(String input){
        return operators.matcher(input).matches();
    }

    private String Operator_name(String lexeme){

        String opName = "";
        switch (lexeme){
            case "==":
                opName = "eq";
                break;
            case "+":
                opName = "plus";
                break;
            case "|":
                opName = "or";
                break;
            case "(":
                opName = "openpar";
                break;
            case ";":
                opName = "semi";
                break;
            case "<>":
                opName = "noteq";
                break;
            case "-":
                opName = "minus";
                break;
            case "&":
                opName = "and";
                break;
            case ")":
                opName = "closepar";
                break;
            case ",":
                opName = "comma";
                break;
            case "<":
                opName = "lt";
                break;
            case "*":
                opName = "mult";
                break;
            case "!":
                opName = "not";
                break;
            case "{":
                opName = "opencubr";
                break;
            case ".":
                opName = "dot";
                break;
            case ">":
                opName = "gt";
                break;
            case "/":
                opName = "div";
                break;
            case "?":
                opName = "qmark";
                break;
            case "}":
                opName = "closecubr";
                break;
            case ":":
                opName = "colon";
                break;
            case "<=":
                opName = "leq";
                break;
            case "=":
                opName = "assign";
                break;
            case "[":
                opName = "opensqbr";
                break;
            case "::":
                opName = "coloncolon";
                break;
            case ">=":
                opName = "geq";
                break;
            case "]":
                opName = "closesqbr";
                break;
            default:
                opName = null;
                break;
        }
        return opName;
    }

    public String Determine_Type(String lexeme){
        if (isKeywords(lexeme)){
            return lexeme;
        } else if (isOperator(lexeme) || ((Operator_name(lexeme) != null))){
            if (Operator_name(lexeme) != null){
                return Operator_name(lexeme);
            } else {
                return "invalidchar";
            }
        }else if (isDigit(lexeme)){
            if (isInt(lexeme)){
                return "intnum";
            } else {
                return "invalidnum";
            }
        } else if (isFloatNum(lexeme)){
            if (isFloat(lexeme)){
                return "floatnum";
            } else {
                return "invalidnum";
            }
        }else if (lexeme.length() >= 1){
            if (isID(lexeme)){
                return "id";
            } else if (lexeme.length() > 1){
                return "invalidid";
            } else {
                return "invalidchar";
            }
        }else {
            return "invalidchar";
        }
    }

    public Token validation(Token token){
        token.setType(Determine_Type(token.getLexeme()));
        switch (token.getType()){
            case "invalidchar":
                token.setValid("character");
                break;
            case "invalidid":
                token.setValid("identifier");
                break;
            case "invalidnum":
                token.setValid("number");
                break;
            default:
                token.setValid("");
                break;
        }
        return token;
    }

    private void token_writer(Token token) throws IOException {
        out_token.write(token.toString());
        out_token.flush();
        out_token.write(" ");
        out_token.flush();
    }

    private void error_writer(Token token) throws IOException {
        out_error.write(token.error_message());
        out_error.flush();
        out_error.newLine();
    }

    public Token Tokenize(){
        String str = "";
        Token output_lexeme = new Token();
        try{
            int cbuf = reader.read();
            //make sure that space, tab, return and line feed are not mixed into the token
            while ((((char)cbuf) == ' ') || (((char)cbuf) == '\t') || (((char)cbuf) == '\n') || (((char)cbuf) == '\r')){
                if (((char)cbuf) == '\n'){
                    //keep track of the location
                    linetracker += 1;
                    out_token.newLine();
                }
                cbuf = reader.read();
            }

            if (((char)cbuf == ';') || ((char)cbuf == '{') || ((char)cbuf == '}') || ((char)cbuf == '(') || ((char)cbuf == ')') || ((char)cbuf == '[') || ((char)cbuf == ']') || ((char)cbuf == ',') || ((char)cbuf == '?') || ((char)cbuf == '!') || ((char)cbuf == '+') || ((char)cbuf == '-')){
                str = "" + (char)cbuf;
                output_lexeme = token_validation(str);
                return output_lexeme;
            }

            if (((char)cbuf == '.')) {
                str = "" + (char)cbuf;
                output_lexeme = token_validation(str);
                return output_lexeme;
            }

            while(cbuf != -1){
                if ((((char)cbuf) == ' ') || (((char)cbuf) == '\t') || (((char)cbuf) == '\n')){
                    break;
                }else{
                    if (((char)cbuf) != '\r') {
                        str += (char)cbuf;
                    }
                }
                cbuf = reader.read();
                if (((char)cbuf == ';') || ((char)cbuf == '{') || ((char)cbuf == '}') || ((char)cbuf == '(') || ((char)cbuf == ')') || ((char)cbuf == '[') || ((char)cbuf == ']') || ((char)cbuf == ',') || ((char)cbuf == '?') || ((char)cbuf == '!') || ((char)cbuf == '+') || ((char)cbuf == '-')){
                    if (str.contains("::") && str.length() > 2) {
                        int index_start = str.indexOf("::");
                        Token token = token_validation(str.substring(0, index_start));
                        this.token.add(token);
                        token = token_validation(str.substring(index_start, index_start + 2));
                        this.token.add(token);
                        token = token_validation(str.substring(index_start + 2));
                        this.token.add(token);
                    } else if (str.contains("<") || str.contains(">")) {
                        if (str.contains("<>") && (str.length() >= 4)) {
                            int index_start = str.indexOf("<>");
                            Token token = token_validation(str.substring(0, index_start));
                            this.token.add(token);
                            token = token_validation(str.substring(index_start, index_start + 2));
                            this.token.add(token);
                            output_lexeme = token_validation(str.substring(index_start + 2));
                            return output_lexeme;
                        } else if (str.contains("<") && (str.length() >= 3)) {
                            int index_start = str.indexOf("<");
                            Token token = token_validation(str.substring(0, index_start));
                            this.token.add(token);
                            token = token_validation(str.substring(index_start, index_start + 1));
                            this.token.add(token);
                            token = token_validation(str.substring(index_start + 1));
                            this.token.add(token);
                        } else if (str.contains(">") && (str.length() >= 3)) {
                            int index_start = str.indexOf(">");
                            Token token = token_validation(str.substring(0, index_start));
                            this.token.add(token);
                            token = token_validation(str.substring(index_start, index_start + 1));
                            this.token.add(token);
                            token = token_validation(str.substring(index_start + 1));
                            this.token.add(token);
                        } else {
                            output_lexeme = token_validation(str);
                            token.add(output_lexeme);
                        }
                    } else {
                        output_lexeme = token_validation(str);
                        token.add(output_lexeme);
                    }
                    str = "" + (char)cbuf;
                    break;
                }
                if (((char)cbuf == '.')) {
                    String type = Determine_Type(str);
                    if (type.equals("id")) {
                        output_lexeme = token_validation(str);
                        token.add(output_lexeme);
                        str = "" + (char)cbuf;
                        break;
                    }
                }
            }

            if (cbuf != -1){
                if (str.contains("<") || str.contains(">")) {
                    if (str.contains("<>") && (str.length() >= 4)) {
                        int index_start = str.indexOf("<>");
                        Token token = token_validation(str.substring(0, index_start));
                        this.token.add(token);
                        token = token_validation(str.substring(index_start, index_start + 2));
                        this.token.add(token);
                        output_lexeme = token_validation(str.substring(index_start + 2));
                        return output_lexeme;
                    } else if (str.contains("<") && (str.length() >= 3)) {
                        int index_start = str.indexOf("<");
                        Token token = token_validation(str.substring(0, index_start));
                        this.token.add(token);
                        token = token_validation(str.substring(index_start, index_start + 1));
                        this.token.add(token);
                        output_lexeme = token_validation(str.substring(index_start + 1));
                        return output_lexeme;
                    } else if (str.contains(">") && (str.length() >= 3)) {
                        int index_start = str.indexOf(">");
                        Token token = token_validation(str.substring(0, index_start));
                        this.token.add(token);
                        token = token_validation(str.substring(index_start, index_start + 1));
                        this.token.add(token);
                        output_lexeme = token_validation(str.substring(index_start + 1));
                        return output_lexeme;
                    } else {
                        output_lexeme = token_validation(str);
                    }
                } else
                    output_lexeme = token_validation(str);

            } else {
                EOF = true;
                String sub_string_2 = "";
                if (str.length() >= 2){
                    sub_string_2 = str.substring(0,2);
                }
                if (str.equals("//")) {
                    Token token = new Token(str, linetracker);
                    token.setType("inlinecmt");
                    token_writer(token);
                    output_lexeme = token;
                } else if (sub_string_2.equals("//")) {
                    Token token = new Token(sub_string_2, linetracker);
                    token.setType("inlinecmt");
                    token_writer(token);
                    output_lexeme = token;
                    this.token.add(output_lexeme);

                    String sub_string = str.substring(2);
                    token = new Token(sub_string, linetracker);
                    token.setType("inlinecmt");
                    token_writer(token);
                    output_lexeme = token;
                }
            }
            if (((char)cbuf) == '\n'){
                linetracker += 1;
                out_token.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output_lexeme;
    }

    public DataList<Token> Lex(){
        Token test = new Token();
        while (isEOF() == false){
            test = Tokenize();
            token.add(test);
        }
        return token;
    }

    private Token token_validation(String str) throws IOException {
        Token output_lexeme = new Token();
        String sub_string_2  = "";
        String sub_string = "";
        if (str.length() >= 2 ){
            sub_string_2 = str.substring(0, 2);
            sub_string = str.substring(2);
        }
        if (str.equals("/*") || sub_string_2.equals("/*")){
            String str_new = sub_string_2 + " " + sub_string;
            output_lexeme = blkcmt_handle(str_new);
        }else if (str.equals("//") || sub_string_2.equals("//")) {
            String str_new = sub_string_2 + " " + sub_string + " ";
            output_lexeme = inlinecmt_handle(str_new);
        }else if ((str.substring(0,1).equals("\"")) && (str.length() > 1)){
            if (!(str.substring(str.length() - 1)).equals("\""))
                str += " ";
            output_lexeme = stringlit_handle(str);
        }else {
            Token token = new Token(str, linetracker);
            token = validation(token);
            token_writer(token);
            if (token.checkValid() == false){
                error_writer(token);
            }
            output_lexeme = token;
        }
        return output_lexeme;
    }

    //handing string literals
    private Token stringlit_handle(String str) {
        Token token = new Token();
        if (str.substring(str.length() - 1).equals("\"")){
            token = new Token(str, linetracker);
            token.setType("stringlit");
            try{
                token_writer(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return token;
        }
        try {
            int start_line = linetracker;
            //keep track of the quote, if it never appears again, it is invalid
            boolean endofstr = true;
            if ((!isAlphanum(str.substring(1))) && (!str.substring(1).contains(" "))){
                endofstr = false;
            }
            int cbuf = reader.read();
            while (cbuf != -1){
                String ch = String.valueOf((char)cbuf);
                if (isAlphanum(ch) || ch.equals(" ")){
                    str += ch;
                } else if (ch.equals("\"")) {
                    str += ch;
                    break;
                }else {
                    endofstr = false;
                }
                cbuf = reader.read();
            }
            if (endofstr) {
                token = new Token(str, start_line);
                token.setType("stringlit");
                token_writer(token);
            } else {
                token = new Token(str, start_line);
                token.setType("invalidstringlit");
                token.setValid("String");
                token_writer(token);
                error_writer(token);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
    //inline comment handing, this part is fairly short because there isnt any chance of invalid inline comment that could happen
    private Token inlinecmt_handle(String str) {
        Token token = new Token();
        try {
            int cbuf = reader.read();
            while ((char)cbuf != '\n'){
                str += (char)cbuf;
                cbuf = reader.read();
            }
            token = new Token(str, linetracker);
            token.setType("inlinecmt");
            token_writer(token);
            linetracker += 1;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    //block comment handling, similar to the string literal but need to make sure "*/" appears
    private Token blkcmt_handle(String str){
        Token token = new Token();
        boolean endofcmt = false;
        int start_line = linetracker;
        if (str.substring(str.length() - 2, str.length()).equals("*/")){
            token = new Token(str, start_line);
            token.setType("blockcmt");
            try{
                token_writer(token);
            }catch (IOException e){
                e.printStackTrace();
            }
            return token;
        }
        try {
            int cbuf = reader.read();
            while (cbuf != -1){
                if ((char)cbuf == '*'){
                    str += (char)cbuf;
                    cbuf = reader.read();
                    if ((char)cbuf == '/'){
                        str += (char)cbuf;
                        cbuf = reader.read();
                        endofcmt = true;
                        linetracker += 1;
                        break;
                    }
                }
                if ((char)cbuf == '\n'){
                    str += "\n";
                    linetracker += 1;
                } else {
                    if ((char)cbuf != '\r') {
                        str += (char)cbuf;
                    }
                }
                cbuf = reader.read();

            }
            if ((cbuf == -1) && (endofcmt == false)){
                token = new Token(str, start_line);
                token.setType("invalidblockcmt");
                token.setValid("Block Comment");
                token_writer(token);
                error_writer(token);
            } else {
                token = new Token(str, start_line);
                token.setType("blockcmt");
                token_writer(token);
                out_token.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
    //stream close
    public void close() throws IOException {
        out_error.close();
        reader.close();
        out_token.close();
    }
}

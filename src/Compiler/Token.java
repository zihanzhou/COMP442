package Compiler;

public class Token{
    private String lexeme;
    private String Type;
    private int location;
    private String valid;

    public Token() {
        this.lexeme = "";
        this.location = -1;
        this.valid = "";
    }

    public Token(String lexeme, int location){
        this.lexeme = lexeme;
        this.location = location;
        this.valid = "";
    }

    public String toString(){
        return "[" + this.Type + ", " + this.lexeme + ", " + this.location + "]";
    }

    public void setType(String Type){
        this.Type = Type;
    }

    public void setLexeme(String lexeme) { this.lexeme = lexeme;}

    public void setLocation(int location) { this.location = location; }

    public String getLexeme() { return this.lexeme; }

    public String getType() {return this.Type; }

    public int getLocation() {return this.location;}

    public void setValid(String valid){
        this.valid = valid;
    }

    public boolean checkValid(){
        return (this.valid == "");
    }

    public String error_message() {
        return "Lexical error: Invalid " + this.valid + ": \"" + this.lexeme + "\": line" + this.location + ".";
    }
}
package Compiler;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import AST.Node;

public class Parse2 {
    DataList<Token> token = new DataList<Token>();
    private ArrayList<String> Derivation = new ArrayList<>();
    private boolean EOF;
    private boolean isSuccess = true;
    private int linetracker;
    private Token lookahead = new Token();
    private BufferedWriter out_Ast;
    private BufferedWriter out_Derivation;
    private BufferedWriter out_Error;
    private int vpointer = 0;
    private Stack<Node> stack = new Stack<>();
    public Parse2(DataList<Token> token) {
        this.token = token;
    }

    private Token nextToken() {
        if (vpointer == this.token.size())
            System.exit(0);
        Token token = this.token.get(vpointer);
        vpointer++;
        return token;
    }

    private boolean skipErrors(List<String> First, List<String> Follow) {
        if (First.contains(lookahead.getLexeme()) || First.contains(lookahead.getType()) || (First.contains("EPSILON") && (Follow.contains(lookahead.getLexeme()) || Follow.contains(lookahead.getType())))) {
            return true;
        } else {
            System.out.println("Syntex error " + lookahead.getLexeme() + " at :" + lookahead.getLocation());
            List<String> newList = new ArrayList<String>(First);
            newList.addAll(Follow);
            while (!newList.contains(lookahead.getLexeme()) && !newList.contains(lookahead.getType())) {
                lookahead = nextToken();
                if (First.contains("EPSILON") && Follow.contains(lookahead.getLexeme())) {
                    //checkSuccess(false);
                    return false;
                }
            }
            return true;
        }
    }

    private boolean match(String token) {
        if (lookahead.getLexeme().equals(token)) {
            lookahead = nextToken();
            return true;
        } else {
            //System.out.println("syntex error at line:" + lookahead.getLocation() + ". expected " + token + ". existed " + lookahead.getLexeme());
            lookahead = nextToken();
            //checkSuccess(false);
            return false;
        }
    }
    private boolean match_type(String token) {
        if (lookahead.getType().equals(token)) {
            lookahead = nextToken();
            return true;
        } else {
            System.out.println("syntex error at line:" + lookahead.getLocation() + ". expected " + token + ". existed " + lookahead.getLexeme());
            lookahead = nextToken();
            //checkSuccess(false);
            return false;
        }
    }

    public void parse() {
        lookahead = nextToken();
        Node Es = new Node();
        if (E(Es)) {
            System.out.println(Es.print());
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    public boolean E(Node Es){
        Node Ts = new Node();
        Node E_s = new Node();

        if (Arrays.asList("0","1","(", "id").contains(lookahead.getLexeme()) || Arrays.asList("0","1","(", "id").contains(lookahead.getType())){
            boolean match1 = T(Ts);
            boolean match2 = E_(Ts, E_s);
            if (match1 && match2){
                System.out.println("E->TE_");
                Es.copy(E_s);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }



    public boolean T(Node Ts){
        Node Fs = new Node();
        Node T_s = new Node();
        if (Arrays.asList("0","1","(", "id").contains(lookahead.getLexeme()) || Arrays.asList("0","1","(", "id").contains(lookahead.getType())){
            boolean match1 = F(Fs);
            boolean match2 = T_(Fs, T_s);
            if (match1 && match2) {
                System.out.println("T->FT_");
                Ts.copy(T_s);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean F(Node Fs){
        Node Es = new Node();
        if (Arrays.asList("id").contains(lookahead.getType())) {
            Node child = new Node();
            child.setData(lookahead.getLexeme());
            boolean match1 = match_type("id");
            if (match1 ) {
                System.out.println("F->id");
                Fs.setData("id");
                Fs.adoptChildren(child);
                return true;
            } else {
                return false;
            }
        } else if (Arrays.asList("(").contains(lookahead.getLexeme())){

            boolean match1 = match("(");
            boolean match2 = E(Es);
            boolean match3 = match(")");
            if (match1 && match2 && match3) {
                System.out.println("T_->EPSILON");
                Fs.copy(Es);
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    public boolean T_(Node Fi, Node T_s){
        Node Fs = new Node();
        Node T_2s = new Node();
        if (Arrays.asList("*").contains(lookahead.getLexeme())) {

            boolean match1 = match("*");
            boolean match2 = F(Fs);
            boolean match3 = T_(Fs, T_2s);
            if (match1 && match2 && match3) {
                System.out.println("T_->*FT_");
                T_s.copy(T_s.makeFamily("*",Fi,T_2s));
                return true;
            } else {
                return false;
            }
        } else if (Arrays.asList("+", ")").contains(lookahead.getLexeme()) || lookahead.getLocation() == -1){
            System.out.println("T_->EPSILON");
            T_s.copy(Fi);
            return true;
        }else {
            return false;
        }
    }

    public boolean E_(Node Ti, Node E_s) {
        Node Ts = new Node();
        Node E_2s = new Node();
        if (Arrays.asList("+").contains(lookahead.getLexeme())) {
            boolean match1 = match("+");
            boolean match2 = T(Ts);
            boolean match3 = E_(Ts, E_2s);
            if (match1 && match2 && match3) {
                System.out.println("E_->TE_");
                E_s.copy(E_s.makeFamily("+",Ti,E_2s));
                return true;
            } else {
                return false;
            }
        } else if (Arrays.asList(")").contains(lookahead.getLexeme()) || lookahead.getLocation() == -1) {
            System.out.println("E_->EPSILON");
            E_s.copy(Ti);
            return true;
        } else {
            return false;
        }
    }

    public boolean E(){
        if (Arrays.asList("0","1","(", "id").contains(lookahead.getLexeme()) || Arrays.asList("0","1","(", "id").contains(lookahead.getType())){
            boolean match1 = T();
            boolean match2 = E_();
            if (match1 && match2){
                System.out.println("E->TE_");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean T(){
        if (Arrays.asList("0","1","(", "id").contains(lookahead.getLexeme()) || Arrays.asList("0","1","(", "id").contains(lookahead.getType())){
            boolean match1 = F();
            boolean match2 = T_();
            if (match1 && match2) {
                System.out.println("T->FT_");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean E_() {
        if (Arrays.asList("+").contains(lookahead.getLexeme())) {
            boolean match1 = match("+");
            boolean match2 = T();
            boolean match3 = E_();
            if (match1 && match2 && match3) {
                System.out.println("E_->TE_");
                return true;
            } else {
                return false;
            }
        } else if (Arrays.asList(")").contains(lookahead.getLexeme()) || lookahead.getLocation() == -1) {
            System.out.println("E_->EPSILON");
            return true;
        } else {
            return false;
        }
    }

    public boolean T_(){

        if (Arrays.asList("*").contains(lookahead.getLexeme())) {

            boolean match1 = match("*");
            boolean match2 = F();
            boolean match3 = T_();
            if (match1 && match2 && match3) {
                System.out.println("T_->*FT_");
                return true;
            } else {
                return false;
            }
        } else if (Arrays.asList("+", ")").contains(lookahead.getLexeme()) || lookahead.getLocation() == -1){
            System.out.println("T_->EPSILON");
            return true;
        }else {
            return false;
        }
    }

    public boolean F(){

        if (Arrays.asList("id").contains(lookahead.getType())) {
            boolean match1 = match_type("id");
            if (match1 ) {
                System.out.println("F->id");
                return true;
            } else {
                return false;
            }
        } else if (Arrays.asList("(").contains(lookahead.getLexeme())){

            boolean match1 = match("(");
            boolean match2 = E();
            boolean match3 = match(")");
            if (match1 && match2 && match3) {
                System.out.println("T_->EPSILON");
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

}



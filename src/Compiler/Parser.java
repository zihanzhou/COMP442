package Compiler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AST.*;

public class Parser {

    /*===================================*/
    private final List<String> First_Type = Arrays.asList("float", "integer", "string", "id");
    private final List<String> First_ArraySizeRept = Arrays.asList("[", "EPSILON");
    private final List<String> First_VarDecl = Arrays.asList("string", "integer", "float", "id");
    private final List<String> First_VarDeclRep = Arrays.asList("string", "integer", "float", "id", "EPSILON");
    private final List<String> First_MethodBodyVar = Arrays.asList("var", "EPSILON");
    private final List<String> First_IntNum = Arrays.asList("intnum", "EPSILON");
    private final List<String> First_NestedId = Arrays.asList(",", "EPSILON");
    private final List<String> First_Inherit = Arrays.asList("inherits", "EPSILON");
    private final List<String> First_FParamsTail = Arrays.asList(",", "EPSILON");
    private final List<String> First_FParams = Arrays.asList("id", "EPSILON", "string", "integer", "float");
    private final List<String> First_FuncDeclTail = Arrays.asList("id", "string", "integer", "float", "void");
    private final List<String> First_FuncDecl = Arrays.asList("func");
    private final List<String> First_MemberDecl = Arrays.asList("func", "id", "string", "integer", "float");
    private final List<String> First_Visibility = Arrays.asList("public", "private", "EPSILON");
    private final List<String> First_ClassDeclBody = Arrays.asList("public", "id", "func", "EPSILON", "private", "string", "integer", "float");
    private final List<String> First_ClassDecl = Arrays.asList("EPSILON", "class");
    private final List<String> First_FuncHead = Arrays.asList("func");
    private final List<String> First_ClassMethod = Arrays.asList("EPSILON", "::");
    private final List<String> First_FuncBody = Arrays.asList("{");
    private final List<String> First_FuncOrVarIdnestTail = Arrays.asList("EPSILON", ".");
    private final List<String> First_FuncOrVarIdnest = Arrays.asList("(", "[", ".", "EPSILON");
    private final List<String> First_IndiceRep = Arrays.asList("[", "EPSILON");
    private final List<String> First_Expr = Arrays.asList("floatnum", "?", "(", "id", "+", "stringlit", "-", "intnum", "!");
    private final List<String> First_ArithExpr = Arrays.asList("floatnum", "?", "(", "id", "+", "stringlit", "-", "intnum", "!");
    private final List<String> First_Term = Arrays.asList("floatnum", "?", "(", "id", "+", "stringlit", "-", "intnum", "!");
    private final List<String> First_Factor = Arrays.asList("floatnum", "?", "(", "id", "+", "stringlit", "-", "intnum", "!");
    private final List<String> First_Sign = Arrays.asList("+", "-");
    private final List<String> First_FuncOrVar = Arrays.asList("id");
    private final List<String> First_AParams = Arrays.asList("floatnum", "?", "(", "id", "+", "stringlit", "EPSILON", "-", "intnum", "!");
    private final List<String> First_AParamsTail = Arrays.asList(",", "EPSILON");
    private final List<String> First_TermTail = Arrays.asList("&", "*", "/", "EPSILON");
    private final List<String> First_MultOp = Arrays.asList("&", "*", "/");
    private final List<String> First_ArithExprTail = Arrays.asList("|", "+", "-", "EPSILON");
    private final List<String> First_AddOp = Arrays.asList("|", "+", "-");
    private final List<String> First_ExprTail = Arrays.asList("<=", ">", "<>", "<", "EPSILON", "==", ">=");
    private final List<String> First_RelOp = Arrays.asList("<=", ">", "<>", "<", "==", ">=");
    private final List<String> First_VariableIdnestTail = Arrays.asList(".", "EPSILON");
    private final List<String> First_VariableIdnest = Arrays.asList("[", "EPSILON", ".");
    private final List<String> First_Variable = Arrays.asList("id");
    private final List<String> First_StatementList = Arrays.asList("write", "return", "id", "if", "EPSILON", "read", "while", "break", "continue");
    private final List<String> First_StatBlock = Arrays.asList("{", "write", "return", "id", "if", "EPSILON", "read", "while", "break", "continue");
    private final List<String> First_Statement = Arrays.asList("if", "while", "read", "write", "return", "break", "continue", "id");
    private final List<String> First_FuncOrAssignStat = Arrays.asList("id");
    private final List<String> First_FuncOrAssignStatIdnestVarTail = Arrays.asList(".", "=");
    private final List<String> First_AssignStatTail = Arrays.asList("=");
    private final List<String> First_AssignOp = Arrays.asList("=");
    private final List<String> First_FuncOrAssignStatIdnest = Arrays.asList("(", "[", "=", ".");
    private final List<String> First_FuncOrAssignStatIdnestFuncTail = Arrays.asList(".", "EPSILON");
    private final List<String> First_FuncStatTail = Arrays.asList("(", "[", ".");
    private final List<String> First_FuncStatTailIdnest = Arrays.asList(".", "EPSILON");
    private final List<String> First_Function = Arrays.asList("func");
    private final List<String> First_FuncDef = Arrays.asList("func", "EPSILON");
    private final List<String> First_Prog = Arrays.asList("main", "func", "class");
    /*===================================*/
    private final List<String> Follow_Type = Arrays.asList("{", ";", "id");
    private final List<String> Follow_ArraySizeRept = Arrays.asList(")", ",", ";");
    private final List<String> Follow_VarDecl = Arrays.asList("public", "private", "func", "integer", "float", "string", "id", "}");
    private final List<String> Follow_IntNum = Arrays.asList("]");
    private final List<String> Follow_VarDeclRep = Arrays.asList("}");
    private final List<String> Follow_MethodBodyVar = Arrays.asList("if", "while", "read", "write", "return", "break", "continue", "id", "}");
    private final List<String> Follow_NestedId = Arrays.asList("{");
    private final List<String> Follow_Inherit = Arrays.asList("{");
    private final List<String> Follow_FParamsTail = Arrays.asList(")");
    private final List<String> Follow_FParams = Arrays.asList(")");
    private final List<String> Follow_FuncDeclTail = Arrays.asList("{", ";");
    private final List<String> Follow_FuncDecl = Arrays.asList("public", "private", "func", "integer", "float", "string", "id", "}");
    private final List<String> Follow_MemberDecl = Arrays.asList("public", "private", "func", "integer", "flaot", "string", "id", "}");
    private final List<String> Follow_Visibility = Arrays.asList("func", "id", "string", "integer", "float");
    private final List<String> Follow_ClassDeclBody = Arrays.asList("}");
    private final List<String> Follow_ClassDecl = Arrays.asList("func", "main");
    private final List<String> Follow_FuncHead = Arrays.asList("{", ";");
    private final List<String> Follow_ClassMethod = Arrays.asList("(");
    private final List<String> Follow_FuncBody = Arrays.asList("main", "func");
    private final List<String> Follow_FuncOrVarIdnestTail = Arrays.asList("*", "/", "&", ";", "==", "<>", "<", ">", "<=", ">=", "+", "-", "|", ",", ":", "]", ")");
    private final List<String> Follow_FuncOrVarIdnest = Arrays.asList("*", "/", "&", ";", "==", "<>", "<", ">", "<=", ">=", "+", "-", "|", ",", ":", "]", ")");
    private final List<String> Follow_IndiceRep = Arrays.asList("*", "/", "&", ";", "=", ".", "==", "<>", "<", ">", "<=", ">=", "+", "-", "|", ",", ":", "]", ")");
    private final List<String> Follow_Expr = Arrays.asList(";", ",", ":", "]", ")");
    private final List<String> Follow_ArithExpr = Arrays.asList(";", "==", "<>", "<", ">", "<=", ">=", ",", ":", "]", ")");
    private final List<String> Follow_Term = Arrays.asList(";", "==", "<>", "<", ">", "<=", ">=", "+", "-", "|", ",", ":", "]", ")");
    private final List<String> Follow_Factor = Arrays.asList("*", "/", "&", ";", "==", "<>", "<", ">", "<=", ">=", "+", "-", "|", ",", ":", "]", ")");
    private final List<String> Follow_Sign = Arrays.asList("intnum", "floatnum", "stringlit", "(", "!", "?", "id", "+", "-");
    private final List<String> Follow_FuncOrVar = Arrays.asList("*", "/", "&", ";", "==", "<>", "<", ">", "<=", ">=", "+", "-", "|", ",", ":", "]", ")");
    private final List<String> Follow_AParams = Arrays.asList(")");
    private final List<String> Follow_AParamsTail = Arrays.asList(")");
    private final List<String> Follow_TermTail = Arrays.asList(";", "==", "<>", "<", ">", "<=", ">=", "+", "-", "|", ",", ":", "]", ")");
    private final List<String> Follow_MultOp = Arrays.asList("intnum", "floatnum", "stringlit", "(", "!", "?", "id", "+", "-");
    private final List<String> Follow_ArithExprTail = Arrays.asList(";", "==", "<>", "<", ">", "<=", ">=", ",", ":", "]", ")");
    private final List<String> Follow_AddOp = Arrays.asList("floatnum", "?", "(", "id", "+", "stringlit", "-", "intnum", "!");
    private final List<String> Follow_ExprTail = Arrays.asList(";", ",", ":", "]", ")");
    private final List<String> Follow_RelOp = Arrays.asList("intnum", "floatnum", "stringlit", "(", "?", "id", "+", "-");
    private final List<String> Follow_VariableIdnestTail = Arrays.asList(")");
    private final List<String> Follow_VariableIdnest = Arrays.asList(")");
    private final List<String> Follow_Variable = Arrays.asList(")");
    private final List<String> Follow_StatementList = Arrays.asList("}");
    private final List<String> Follow_StatBlock = Arrays.asList("else", ";");
    private final List<String> Follow_Statement = Arrays.asList("if", "while", "read", "write", "return", "break", "continue", "id", "else", ";", "}");
    private final List<String> Follow_FuncOrAssignStat = Arrays.asList(";");
    private final List<String> Follow_FuncOrAssignStatIdnestVarTail = Arrays.asList(";");
    private final List<String> Follow_AssignStatTail = Arrays.asList(";");
    private final List<String> Follow_AssignOp = Arrays.asList("intnum", "floatnum", "stringlit", "*", "!", "?", "id", "+", "-");
    private final List<String> Follow_FuncOrAssignStatIdnest = Arrays.asList(";");
    private final List<String> Follow_FuncOrAssignStatIdnestFuncTail = Arrays.asList(";");
    private final List<String> Follow_FuncStatTail = Arrays.asList(";");
    private final List<String> Follow_FuncStatTailIdnest = Arrays.asList(";");
    private final List<String> Follow_Function = Arrays.asList("main", "func");
    private final List<String> Follow_FuncDef = Arrays.asList("main");
    private final List<String> Follow_Prog = Arrays.asList("$");

    public Node AST = new ProgNode();
    DataList<Token> token = new DataList<Token>();
    private ArrayList<String> Derivation = new ArrayList<>();
    private boolean isSuccess = true;
    private Token lookahead = new Token();
    private BufferedWriter out_Ast;
    private BufferedWriter out_Derivation;
    private BufferedWriter out_Error;
    private int vpointer = 0;


    public Parser(DataList<Token> token) {
        this.token = token;
        removeComment();
    }

    public void set_path(File filename) {
        try {
            String outDerivation = "outderivation/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outderivation";
            String outAst = "outast/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outast";
            String outError = "outsyntaxerrors/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outsyntaxerrors";
            out_Derivation = new BufferedWriter(new FileWriter(outDerivation));
            out_Ast = new BufferedWriter(new FileWriter(outAst));
            out_Error = new BufferedWriter(new FileWriter(outError));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void parse() {
        lookahead = nextToken();
        Node node = new ProgNode();
        Derivation.add("<Prog>");
        if (Prog(node)) {
            System.out.println("Success");
            String str = node.print();
            AST = node;
            if (isSuccess) {
                try {
                    out_Ast.write(str);
                    out_Ast.flush();
                    out_Ast.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Node return_Node() {
        return AST;
    }

    public void close() throws IOException {
        out_Error.close();
        out_Ast.close();
        out_Derivation.close();
    }

    private void removeComment() {
        int i = 0;
        int size = this.token.size();
        //System.out.println(size);
        while (i < size - 1) {
            if (this.token.get(i).getType().equals("inlinecmt") || this.token.get(i).getType().equals("blockcmt")) {
                this.token.remove(i);
                size = size - 1;
            } else {
                i++;
            }
        }
        size = this.token.size();
        this.token.get(size - 1).setLexeme("$");
    }

    private Token nextToken() {
        if (vpointer == this.token.size())
            System.exit(0);
        Token token = this.token.get(vpointer);
        vpointer++;
        return token;
    }

    private void checkSuccess(boolean success) {
        if (!success) {
            isSuccess = false;
            try {
                out_Derivation.write("Abort, Failed to parse!");
                out_Derivation.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void derive(String str, String[] production) {
        if (isSuccess) {
            int index = Derivation.indexOf(str);
            Derivation.remove(index);
            for (int i = 0; i < production.length; i++) {
                Derivation.add(index + i, production[i]);
            }
            derivation_write();
        }
    }

    private void derivation_write() {
        try {
            String str;
            for (int i = 0; i < Derivation.size(); i++) {
                str = Derivation.get(i) + " ";
                out_Derivation.write(str);
                out_Derivation.flush();
            }
            out_Derivation.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean skipErrors(List<String> First, List<String> Follow) {
        if (First.contains(lookahead.getLexeme()) || First.contains(lookahead.getType()) || (First.contains("EPSILON") && (Follow.contains(lookahead.getLexeme()) || Follow.contains(lookahead.getType())))) {
            return true;
        } else {
            try {
                out_Error.write("Syntex error " + lookahead.getLexeme() + " at :" + lookahead.getLocation());
                out_Error.flush();
                out_Error.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> newList = new ArrayList<String>(First);
            newList.addAll(Follow);
            while (!newList.contains(lookahead.getLexeme()) && !newList.contains(lookahead.getType())) {
                lookahead = nextToken();
                if (First.contains("EPSILON") && Follow.contains(lookahead.getLexeme())) {
                    checkSuccess(false);
                    return false;
                }
            }
            return true;
        }
    }

    private boolean match_type(String token) {
        if (lookahead.getType().equals(token)) {
            lookahead = nextToken();
            return true;
        } else {
            try {
                out_Error.write("syntex error at line:" + lookahead.getLocation() + ". expected " + token + ". existed " + lookahead.getLexeme());
                out_Error.flush();
                out_Error.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lookahead = nextToken();
            checkSuccess(false);
            return false;
        }
    }

    private boolean match(String token) {
        if (lookahead.getLexeme().equals(token)) {
            lookahead = nextToken();
            return true;
        } else {
            try {
                out_Error.write("syntex error at line:" + lookahead.getLocation() + ". expected " + token + ". existed " + lookahead.getLexeme());
                out_Error.flush();
                out_Error.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lookahead = nextToken();
            checkSuccess(false);
            return false;
        }
    }

    private Node makeId() {
        Node Id = new Node(lookahead);
        return Id;
    }

    private boolean Prog(Node Prog) {
        boolean success = true;
        Node ClassDeclList = new ClassListNode(), FuncDefList = new FuncDefListNode(), ClassDecls = new ClassNode(), FuncDef = new FuncDefNode(), FuncBody = new MainBlockNode();
        if (!skipErrors(First_Prog, Follow_Prog)) return false;
        if (First_Prog.contains(lookahead.getLexeme())) {
            derive("<Prog>", new String[]{"<ClassDecl>", "<FuncDef>", "main", "<FuncBody>"});
            boolean match1 = ClassDecl(ClassDecls);
            boolean match2 = FuncDef(FuncDef);
            boolean match3 = match("main");
            boolean match4 = FuncBody(FuncBody);
            if (match1 && match2 && match3 && match4) {
                if (!ClassDecls.isEmpty()) {
                    ClassDeclList.Family("Class List", ClassDecls);
                } else {
                    ClassDeclList.Family("Class List", "EPSILON");
                }

                FuncBody.setData("Stat Block");
                Prog.Family("Prog", ClassDeclList, FuncDefList, FuncBody);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean ClassDecl(Node ClassDecl) {
        Node id = new IdNode(), InherList = new InheritListNode(), MembList = new MemberListNode(), MembDecl = new MembDeclNode(), ClassDecls = new ClassNode();
        boolean success = true;
        if (!skipErrors(First_ClassDecl, Follow_ClassDecl)) return false;
        if (First_ClassDecl.contains(lookahead.getLexeme())) {
            derive("<ClassDecl>", new String[]{"class", "id", "<Inherit>", "{", "<ClassDeclBody>", "}", ";", "<ClassDecl>"});
            boolean match1 = match("class");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = Inherit(InherList);
            boolean match4 = match("{");
            boolean match5 = ClassDeclBody(MembDecl);
            boolean match6 = match("}");
            boolean match7 = match(";");
            boolean match8 = ClassDecl(ClassDecls);
            if (match1 && match2 && match3 && match4 && match5 && match6 && match7 && match8) {
                if (!MembDecl.isEmpty()) {
                    MembList.Family("Memb List", MembDecl);
                } else {
                    MembList.Family("Memb List", "EPSILON");
                }
                ClassDecl.Family("Class Decl", id, InherList, MembList);
                if (!ClassDecls.isEmpty()) {
                    ClassDecl.makeSiblings(ClassDecls);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ClassDecl.contains(lookahead.getLexeme())) {
            derive("<ClassDecl>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Inherit(Node InherList) {
        Node id = new IdNode(), NestedId = new IdNode();
        boolean success = true;
        if (!skipErrors(First_Inherit, Follow_Inherit)) return false;
        if (First_Inherit.contains(lookahead.getLexeme())) {
            derive("<Inherit>", new String[]{"inherits", "id", "<NestedId>"});
            boolean match1 = match(First_Inherit.get(0));
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = NestedId(NestedId);
            if (match1 && match2 && match3) {
                if (!NestedId.isEmpty()) {
                    InherList.Family("Inher List", id, NestedId);
                } else {
                    InherList.Family("Inher List", id);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_Inherit.contains(lookahead.getLexeme())) {
            InherList.Family("Inher List", new IdNode("EPSILON"));
            derive("<Inherit>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean ClassDeclBody(Node MembDecl) {
        Node MembDecls = new MembDeclNode(), FuncOrVarDecl, Visibility = new VisibilityNode();
        boolean success = true;
        if (!skipErrors(First_ClassDeclBody, Follow_ClassDeclBody)) return false;
        if (First_ClassDeclBody.contains(lookahead.getLexeme()) || First_ClassDeclBody.contains(lookahead.getType())) {
            derive("<ClassDeclBody>", new String[]{"<Visibility>", "<MemberDecl>", "<ClassDeclBody>"});
            boolean match1 = Visibility(Visibility);
            boolean match2;
            if (First_FuncDecl.contains(lookahead.getLexeme())) {
                FuncOrVarDecl = new FuncDeclNode();
                match2 = MemberDecl(FuncOrVarDecl);
            } else if (First_VarDecl.contains(lookahead.getLexeme()) || First_VarDecl.contains(lookahead.getType())) {
                FuncOrVarDecl = new VarDeclNode();
                match2 = MemberDecl(FuncOrVarDecl);
            } else {
                success = false;
                FuncOrVarDecl = new Node();
                match2 = MemberDecl(FuncOrVarDecl);
            }
            boolean match3 = ClassDeclBody(MembDecls);
            if (match1 && match2 && match3) {
                FuncOrVarDecl.addChild(Visibility);
                MembDecl.Family("Memb Decl", FuncOrVarDecl);
                if (!MembDecls.isEmpty()) {
                    MembDecl.makeSiblings(MembDecls);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ClassDeclBody.contains(lookahead.getLexeme())) {
            derive("<ClassDeclBody>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Visibility(Node Visibility) {
        boolean success = true;
        if (!skipErrors(First_Visibility, Follow_Visibility)) return false;
        if (First_Visibility.contains(lookahead.getLexeme())) {
            if (First_Visibility.get(0).equals(lookahead.getLexeme())) {
                Visibility.setData(lookahead.getLexeme());
                Visibility.setType("Visibility");
                derive("<Visibility>", new String[]{First_Visibility.get(0)});
                if (match_type(First_Visibility.get(0))) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Visibility.get(1).equals(lookahead.getLexeme())) {
                Visibility.setData(lookahead.getLexeme());
                Visibility.setType("Visibility");
                derive("<Visibility>", new String[]{First_Visibility.get(1)});
                if (match_type(First_Visibility.get(1))) {
                    success = true;
                } else {
                    success = false;
                }
            }
        } else if (Follow_Visibility.contains(lookahead.getLexeme()) || Follow_Visibility.contains(lookahead.getType())) {
            Visibility.setData("EPSILON");
            Visibility.setType("Visibility");
            derive("<Visibility>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean MemberDecl(Node MembDecl) {
        boolean success = true;
        if (!skipErrors(First_MemberDecl, Follow_MemberDecl)) return false;
        if (First_MemberDecl.contains(lookahead.getLexeme()) || First_MemberDecl.contains(lookahead.getType())) {
            if (First_MemberDecl.get(0).equals(lookahead.getLexeme())) {
                derive("<MemberDecl>", new String[]{"<FuncDecl>"});
                boolean match = FuncDecl(MembDecl);
                if (match) {
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<MemberDecl>", new String[]{"<VarDecl>"});
                boolean match = VarDecl(MembDecl);
                if (match) {
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncDecl(Node FuncDecl) {
        Node type = new TypeNode(), id = new IdNode(), FParmList = new ParamListNode(), FParam = new ParamNode();
        boolean success = true;
        if (!skipErrors(First_FuncDecl, Follow_FuncDecl)) return false;
        if (First_FuncDecl.contains(lookahead.getLexeme())) {
            derive("<FuncDecl>", new String[]{"func", "id", "(", "<FParams>", ")", ":", "<FuncDeclTail>", ";"});
            boolean match1 = match("func");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = match("(");
            boolean match4 = FParams(FParam);
            boolean match5 = match(")");
            boolean match6 = match(":");
            boolean match7 = FuncDeclTail(type);
            boolean match8 = match(";");
            if (match1 && match2 && match3 && match4 && match5 && match6 && match7 && match8) {
                if (!FParam.isEmpty()) {
                    FParmList.Family("FParm List", FParam);
                } else {
                    FParmList.Family("FParm List", "EPSILON");
                }
                FuncDecl.Family("Func Decl", type, id, FParmList);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FParams(Node FParam) {
        Node type = new TypeNode(), id = new IdNode(), dim = new DimNode(), DimList = new DimListNode(), FParams = new ParamNode();
        boolean success = true;
        if (!skipErrors(First_FParams, Follow_FParamsTail)) return false;
        if (First_FParams.contains(lookahead.getLexeme()) || First_FParams.contains(lookahead.getType())) {
            derive("<FParams>", new String[]{"<Type>", "id", "<ArraySizeRept>", "<FParamsTail>"});
            boolean match1 = Type(type);
            id = makeId();
            boolean match2 = match_type("id");
            boolean match3 = ArraySizeRept(dim);
            boolean match4 = FParamsTail(FParams);
            if (match1 && match2 && match3 && match4) {
                if (!dim.isEmpty()) {
                    DimList.Family("Dim List", dim);
                } else {
                    DimList.Family("Dim List", "EPSILON");
                }
                FParam.Family("FParam", type, id, DimList);
                if (!FParams.isEmpty()) {
                    FParam.makeSiblings(FParams);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FParams.contains(lookahead.getLexeme())) {
            derive("<FParams>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean FParamsTail(Node FParam) {
        Node type = new TypeNode(), id = new IdNode(), dim = new DimNode(), DimList = new DimListNode(), FParams = new ParamNode();
        boolean success = true;
        if (!skipErrors(First_FParamsTail, Follow_FParamsTail)) return false;
        if (First_FParamsTail.contains(lookahead.getLexeme())) {
            derive("<FParamsTail>", new String[]{",", "<Type>", "id", "<ArraySizeRept>", "<FParamsTail>"});
            boolean match1 = match(First_FParamsTail.get(0));
            boolean match2 = Type(type);
            id = new IdNode(lookahead);
            boolean match3 = match_type("id");
            boolean match4 = ArraySizeRept(dim);
            boolean match5 = FParamsTail(FParams);
            if (match1 && match2 && match3 && match4 && match5) {
                if (!dim.isEmpty()) {
                    DimList.Family("Dim List", dim);
                } else {
                    DimList.Family("Dim List", "EPSILON");
                }
                FParam.Family("FParam", type, id, DimList);
                if (!FParams.isEmpty()) {
                    FParam.makeSiblings(FParams);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FParamsTail.contains(lookahead.getLexeme())) {
            derive("<FParamsTail>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean ArraySizeRept(Node num) {
        Node nums = new DimNode();
        boolean success = true;
        if (!skipErrors(First_ArraySizeRept, Follow_ArraySizeRept)) return false;
        if (First_ArraySizeRept.contains(lookahead.getLexeme())) {
            derive("<ArraySizeRept>", new String[]{"[", "<IntNum>", "]", "<ArraySizeRept>"});
            boolean match1 = match("[");
            boolean match2 = IntNum(num);
            boolean match3 = match("]");
            boolean match4 = ArraySizeRept(nums);
            if (match1 && match2 && match3 && match4) {
                if (num.isEmpty()) {
                    num.setData("EPSILON");
                }
                if (!nums.isEmpty()) {
                    num.makeSiblings(nums);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ArraySizeRept.contains(lookahead.getLexeme())) {
            derive("<ArraySizeRept>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean IntNum(Node num) {
        boolean success = true;
        if (!skipErrors(First_IntNum, Follow_IntNum)) return false;
        if (First_IntNum.contains(lookahead.getType())) {
            num.setData(lookahead.getLexeme());
            derive("<IntNum>", new String[]{"intnum"});
            if (match_type("intnum")) {
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_IntNum.contains(lookahead.getLexeme())) {
            derive("<IntNum>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean IntNum() {
        boolean success = true;
        if (!skipErrors(First_IntNum, Follow_IntNum)) return false;
        if (Follow_IntNum.contains(lookahead.getLexeme())) {
            derive("<IntNum>", new String[]{});
            success = true;
        } else if (match_type("intnum")) {
            derive("<IntNum>", new String[]{"intnum"});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Type(Node Type) {
        boolean success = true;
        if (!skipErrors(First_Type, Follow_Type)) return false;
        if (First_Type.get(0).equals(lookahead.getLexeme())) {
            Type.copy(new Node(lookahead));
            Type.setType("Type");
            derive("<Type>", new String[]{First_Type.get(0)});
            if (match(First_Type.get(0))) {
                success = true;
            } else {
                success = false;
            }
        } else if (First_Type.get(1).equals(lookahead.getLexeme())) {
            Type.copy(new Node(lookahead));
            Type.setType("Type");
            derive("<Type>", new String[]{First_Type.get(1)});
            if (match(First_Type.get(1))) {
                success = true;
            } else {
                success = false;
            }
        } else if (First_Type.get(2).equals(lookahead.getLexeme())) {
            Type.copy(new Node(lookahead));
            Type.setType("Type");
            derive("<Type>", new String[]{First_Type.get(2)});
            if (match(First_Type.get(2))) {
                success = true;
            } else {
                success = false;
            }
        } else if (First_Type.get(3).equals(lookahead.getType())) {
            Type.copy(new Node(lookahead));
            Type.setType("Type");
            derive("<Type>", new String[]{First_Type.get(3)});
            if (match_type(First_Type.get(3))) {
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean NestedId(Node NestedId) {
        Node idnest = new NestedIdNode();
        boolean success = true;
        if (!skipErrors(First_NestedId, Follow_NestedId)) return false;
        if (First_NestedId.contains(lookahead.getLexeme())) {
            derive("<NestedId>", new String[]{",", "id", "<NestedId>"});
            boolean match1 = match(",");
            NestedId.copy(makeId());
            boolean match2 = match_type("id");
            boolean match3 = NestedId(idnest);
            if (match1 && match2 && match3) {
                if (!idnest.isEmpty()) {
                    NestedId.makeSiblings(idnest);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_NestedId.contains(lookahead.getLexeme())) {
            derive("<NestedId>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncDeclTail(Node Type) {
        boolean success = true;
        if (First_FuncDeclTail.get(4).equals(lookahead.getLexeme())) {
            derive("<FuncDeclTail>", new String[]{"void"});
            Type.copy(new Node(lookahead));
            Type.setType("Type");
            if (match("void")) {
                success = true;
            } else {
                success = false;
            }
        } else {
            derive("<FuncDeclTail>", new String[]{"<Type>"});
            if (Type(Type)) {
                success = true;
            } else {
                success = false;
            }
        }
        return success;
    }

    private boolean VarDecl(Node VarDecl) {
        Node type = new TypeNode(), id = new IdNode(), DimList = new DimListNode(), dim = new DimNode();
        boolean success = true;
        if (!skipErrors(First_VarDecl, Follow_VarDecl)) return false;
        if (First_VarDecl.contains(lookahead.getLexeme()) || First_VarDecl.contains(lookahead.getType())) {
            derive("<VarDecl>", new String[]{"<Type>", "id", "<ArraySizeRept>", ";"});
            boolean match1 = Type(type);
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = ArraySizeRept(dim);
            boolean match4 = match(";");
            if (match1 && match2 && match2 && match3 && match4) {
                if (!dim.isEmpty()) {
                    DimList.Family("Dim List", dim);
                } else {
                    DimList.Family("Dim List", "EPSILON");
                }
                VarDecl.Family("Var Decl", type, id, DimList);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncDef(Node FuncDef) {
        Node FuncDefs = new FuncDefNode();
        boolean success = true;
        if (!skipErrors(First_FuncDef, Follow_FuncDef)) return false;
        if (First_FuncDef.contains(lookahead.getLexeme())) {
            derive("<FuncDef>", new String[]{"<Function>", "<FuncDef>"});
            boolean match1 = Function(FuncDef);
            boolean match2 = FuncDef(FuncDefs);
            if (match1 && match2) {
                FuncDef.setData("Func Def");
                if (!FuncDefs.isEmpty()) {
                    FuncDef.makeSiblings(FuncDefs);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FuncDef.contains(lookahead.getLexeme())) {
            derive("<FuncDef>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Function(Node FuncDef) {
        Node FuncBody = new ProgramBlockNode();
        boolean success = true;
        if (!skipErrors(First_Function, Follow_Function)) return false;
        if (First_Function.contains(lookahead.getLexeme())) {
            derive("<Function>", new String[]{"<FuncHead>", "<FuncBody>"});
            boolean match1 = FuncHead(FuncDef);
            boolean match2 = FuncBody(FuncBody);
            if (match1 && match2) {
                FuncDef.addChild(FuncBody);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncHead(Node FuncDef) {
        Node id = new IdNode(), ScopeSpec = new IdNode(), FParam = new ParamNode(), FParamList = new ParamListNode(), type = new TypeNode(), Scope_spec = new ScopeSpecNode();
        boolean success = true;
        if (!skipErrors(First_FuncHead, Follow_FuncHead)) return false;
        if (First_FuncHead.contains(lookahead.getLexeme())) {
            derive("<FuncHead>", new String[]{"func", "id", "<ClassMethod>", "(", "<FParams>", ")", ":", "<FuncDeclTail>"});
            boolean match1 = match("func");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = ClassMethod(ScopeSpec);
            boolean match4 = match("(");
            boolean match5 = FParams(FParam);
            boolean match6 = match(")");
            boolean match7 = match(":");
            boolean match8 = FuncDeclTail(type);
            if (match1 && match2 && match3 && match4 && match5 && match6 && match7 && match8) {
                if (!ScopeSpec.isEmpty()) {
                    Scope_spec.Family("Scope Spec", id);
                    id = ScopeSpec;
                } else {
                    Scope_spec.Family("Scope Spec", "EPSILON");

                }
                if (!FParam.isEmpty()) {
                    FParamList.Family("FParm List", FParam);
                } else {
                    FParamList.Family("FParm List", "EPSILON");
                }
                FuncDef.Family("Func Def", type, Scope_spec, id, FParamList);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean ClassMethod(Node ScopeSpec) {
        boolean success = true;
        if (!skipErrors(First_ClassMethod, Follow_ClassMethod)) return false;
        if (First_ClassMethod.contains(lookahead.getLexeme())) {
            derive("<ClassMethod>", new String[]{"::", "id"});
            boolean match1 = match("::");
            ScopeSpec.copy(new IdNode(lookahead));
            boolean match2 = match_type("id");
            if (match1 && match2) {
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ClassMethod.contains(lookahead.getLexeme())) {
            derive("<ClassMethod>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncBody(Node ProgramBlock) {
        Node VarDecl = new VarDeclListNode(), Statements = new StatementNode(), StatmentList = new StatBlockNode(), FuncBody = new ProgramBlockNode();
        boolean success = true;
        if (!skipErrors(First_FuncBody, Follow_FuncBody)) return false;
        if (First_FuncBody.contains(lookahead.getLexeme())) {
            derive("<FuncBody>", new String[]{"{", "<MethodBodyVar>", "<StatementList>", "}"});
            boolean match1 = match("{");
            boolean match2 = MethodBodyVar(VarDecl);
            boolean match3 = StatementList(Statements);
            boolean match4 = match("}");
            if (match1 && match2 && match3 && match4) {
                if (VarDecl.haveChildren()) {
                    VarDecl.setData("Var Decl List");
                } else {
                    VarDecl.Family("Var Decl List", "EPSILON");
                }
                if (!Statements.isEmpty()) {
                    StatmentList.Family("Statement List", Statements);
                } else {
                    StatmentList.Family("StatmentList", "EPISLON");
                }
                ProgramBlock.Family("StatBlock", VarDecl, StatmentList);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean MethodBodyVar(Node StatBlock) {
        Node VarDeclRep = new Node();
        boolean success = true;
        if (!skipErrors(First_MethodBodyVar, Follow_MethodBodyVar)) return false;
        if (First_MethodBodyVar.contains(lookahead.getLexeme())) {
            derive("<MethodBodyVar>", new String[]{"var", "{", "<VarDeclRep>", "}"});
            boolean match1 = match("var");
            boolean match2 = match("{");
            boolean match3 = VarDeclRep(VarDeclRep);
            boolean match4 = match("}");
            if (match1 && match2 && match3 && match4) {
                if (!VarDeclRep.isEmpty()) {
                    StatBlock.adoptChildren(VarDeclRep);
                } else {
                    Node Epsilon = new Node();
                    Epsilon.setData("EPSILON");
                    StatBlock.adoptChildren(Epsilon);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_MethodBodyVar.contains(lookahead.getLexeme()) || Follow_MethodBodyVar.contains(lookahead.getType())) {
            derive("<MethodBodyVar>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean VarDeclRep(Node VarDecl) {
        Node VarDecls = new Node();
        boolean success = true;
        if (!skipErrors(First_VarDeclRep, Follow_VarDeclRep)) return false;
        if (First_VarDeclRep.contains(lookahead.getLexeme()) || First_VarDeclRep.contains(lookahead.getType())) {
            derive("<VarDeclRep>", new String[]{"<VarDecl>", "<VarDeclRep>"});
            boolean match1 = VarDecl(VarDecl);
            boolean match2 = VarDeclRep(VarDecls);
            if (match1 && match2) {
                if (!VarDecls.isEmpty()) {
                    VarDecl.makeSiblings(VarDecls);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_VarDeclRep.contains(lookahead.getLexeme())) {
            derive("<VarDeclRep>", new String[]{});
            return true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean StatementList(Node Statement) {
        Node Statements = new StatementNode(), Stat;
        boolean success = true;
        if (!skipErrors(First_StatementList, Follow_StatementList)) return false;
        if (First_StatementList.contains(lookahead.getLexeme()) || First_StatementList.contains(lookahead.getType())) {
            derive("<StatementList>", new String[]{"<Statement>", "<StatementList>"});
            switch (lookahead.getLexeme()) {
                case "if":
                    Stat = new IfStatNode();
                    break;
                case "while":
                    Stat = new WhileStatNode();
                    break;
                case "read":
                    Stat = new ReadStatNode();
                    break;
                case "write":
                    Stat = new WriteStatNode();
                    break;
                case "return":
                    Stat = new ReturnStatNode();
                    break;
                case "break":
                case "continue":
                    Stat = new StatementNode();
                    break;
                default:
                    Stat = new FuncOrAssignStatNode();
                    break;
            }
            boolean match1 = Statement(Stat);
            boolean match2 = StatementList(Statements);
            if (match1 && match2) {
                Statement.Family("Stat", Stat);
                if (!Statements.isEmpty()) {
                    Statement.makeSiblings(Statements);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_StatementList.contains(lookahead.getLexeme())) {
            derive("<StatementList>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean StatBlock(Node StatBlock) {
        Node Statments = new Node(), StatementList = new Node();
        boolean success = true;
        if (!skipErrors(First_StatBlock, Follow_StatBlock)) return false;
        if (First_StatBlock.contains(lookahead.getLexeme()) || First_StatBlock.contains(lookahead.getType())) {
            if (First_StatBlock.get(0).equals(lookahead.getLexeme())) {
                derive("<StatBlock>", new String[]{"{", "<StatementList>", "}"});
                boolean match1 = match("{");
                boolean match2 = StatementList(Statments);
                boolean match3 = match("}");
                if (match1 && match2 && match3) {
                    StatementList.setData("Statement List");
                    StatementList.adoptChildren(Statments);
                    StatBlock.copy(StatementList);
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<StatBlock>", new String[]{"<Statement>"});
                boolean match1 = Statement(Statments);
                if (match1) {
                    StatBlock.copy(Statments);
                    success = true;
                } else {
                    success = false;
                }
            }
        } else if (Follow_StatBlock.contains(lookahead.getLexeme())) {
            Node Epsilon = new Node();
            Epsilon.setData("EPSILON");
            StatBlock.setData("Statement List");
            StatBlock.adoptChildren(Epsilon);
            derive("<StatBlock>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Statement(Node Statement) {
        Node Expr = new ExprNode(), StatBlock1 = new StatBlockNode(), StatBlock2 = new StatBlockNode(), Variable = new VarNode(), FuncOrVarAssign = new StatementNode();
        boolean success = true;
        if (!skipErrors(First_Statement, Follow_Statement)) return false;
        if (First_Statement.contains(lookahead.getLexeme()) || First_Statement.contains(lookahead.getType())) {
            if (First_Statement.get(0).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"if", "(", "<Expr>", ")", "then", "<StatBlock>", "else", "<StatBlock>", ";"});
                boolean match1 = match("if");
                boolean match2 = match("(");
                boolean match3 = Expr(Expr);
                boolean match4 = match(")");
                boolean match5 = match("then");
                boolean match6 = StatBlock(StatBlock1);
                boolean match7 = match("else");
                boolean match8 = StatBlock(StatBlock2);
                boolean match9 = match(";");
                if (match1 && match2 && match3 && match4 && match5 && match6 && match7 && match8 && match9) {
                    Expr.setData("Expr");
                    Statement.Family("if Stat", Expr, StatBlock1, StatBlock2);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(1).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"while", "(", "<Expr>", ")", "<StatBlock>", ";"});
                boolean match1 = match("while");
                boolean match2 = match("(");
                boolean match3 = Expr(Expr);
                boolean match4 = match(")");
                boolean match5 = StatBlock(StatBlock1);
                boolean match6 = match(";");
                if (match1 && match2 && match3 && match4 && match5 && match6) {
                    Statement.setData("While Stat");
                    Statement.Family("While Stat", Expr, StatBlock1);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(2).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"read", "(", "<Variable>", ")", ";"});
                boolean match1 = match("read");
                boolean match2 = match("(");
                boolean match3 = Variable(Variable);
                boolean match4 = match(")");
                boolean match5 = match(";");
                if (match1 && match2 && match3 && match4 && match5) {
                    Statement.setData("Read");
                    Node Variables = new Node();
                    Variables.Family("Variable", Variable);
                    Statement.Family("Read", Variables);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(3).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"write", "(", "<Expr>", ")", ";"});
                boolean match1 = match("write");
                boolean match2 = match("(");
                boolean match3 = Expr(Expr);
                boolean match4 = match(")");
                boolean match5 = match(";");
                if (match1 && match2 && match3 && match4 && match5) {
                    Expr.setData("Expr");
                    Statement.Family("Write", Expr);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(4).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"return", "(", "<Expr>", ")", ";"});
                boolean match1 = match("return");
                boolean match2 = match("(");
                boolean match3 = Expr(Expr);
                boolean match4 = match(")");
                boolean match5 = match(";");
                if (match1 && match2 && match3 && match4 && match5) {
                    Expr.setData("Expr");
                    Statement.Family("Return", Expr);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(5).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"break", ";"});
                boolean match1 = match("break");
                boolean match2 = match(";");
                if (match1 && match2) {
                    Statement.setData("break");
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(6).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"continue", ";"});
                boolean match1 = match("continue");
                boolean match2 = match(";");
                if (match1 && match2) {
                    Statement.setData("continue");
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(7).equals(lookahead.getType())) {
                derive("<Statement>", new String[]{"<FuncOrAssignStat>", ";"});
                boolean match1 = FuncOrAssignStat(Statement);
                boolean match2 = match(";");
                if (match1 && match2) {
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean Variable(Node Variable) {
        Node Id = new IdNode(), VariableIdnest = new VarNode();
        boolean success = true;
        if (!skipErrors(First_Variable, Follow_Variable)) return false;
        if (First_Variable.contains(lookahead.getType())) {
            derive("<Variable>", new String[]{"id", "<VariableIdnest>"});
            Id = new IdNode(lookahead);
            boolean match1 = match_type("id");
            boolean match2 = VariableIdnest(Id, VariableIdnest);
            if (match1 && match2) {
                Variable.copy(VariableIdnest);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean VariableIdnest(Node Id, Node VariableIdnest) {
        Node Index = new DataMemberNode(), VariableIdnestTail = new VarNode();
        boolean success = true;
        if (!skipErrors(First_VariableIdnest, Follow_VariableIdnest)) return false;
        if (First_VariableIdnest.contains(lookahead.getLexeme())) {
            derive("<VariableIdnest>", new String[]{"<IndiceRep>", "<VariableIdnestTail>"});
            boolean match1 = IndiceRep(Id, Index);
            boolean match2 = VariableIdnestTail(Index, VariableIdnestTail);
            if (match1 && match2) {
                VariableIdnest.copy(VariableIdnestTail);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_VariableIdnest.contains(lookahead.getLexeme())) {
            derive("<VariableIdnest>", new String[]{"<IndiceRep>", "<VariableIdnestTail>"});
            VariableIdnest.Family("DataMember", Id, new IndiceListNode("EPSILON"));
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean VariableIdnestTail(Node VariableIdnest, Node VariableIdnestTail) {
        Node Id = new IdNode(), VariableIdnests = new VarNode();
        boolean success = true;
        if (!skipErrors(First_VariableIdnestTail, Follow_VariableIdnestTail)) return false;
        if (First_VariableIdnestTail.contains(lookahead.getLexeme())) {
            derive("<VariableIdnestTail>", new String[]{".", "id", "<VariableIdnest>"});
            boolean match1 = match(".");
            Id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = VariableIdnest(Id, VariableIdnests);
            if (match1 && match2 && match3) {
                VariableIdnestTail.Family(".", VariableIdnest, VariableIdnests);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_VariableIdnestTail.contains(lookahead.getLexeme())) {
            derive("<VariableIdnestTail>", new String[]{});
            VariableIdnestTail.copy(VariableIdnest);
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncOrAssignStat(Node Statement) {
        Node id = new IdNode(), Statements = new StatementNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStat, Follow_FuncOrAssignStat)) return false;
        if (First_FuncOrAssignStat.contains(lookahead.getType())) {
            derive("<FuncOrAssignStat>", new String[]{"id", "<FuncOrAssignStatIdnest>"});
            id = new IdNode(lookahead);
            boolean match1 = match_type("id");
            boolean match2 = FuncOrAssignStatIdnest(id, Statements);
            if (match1 && match2) {
                Statement.copy(Statements);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncOrAssignStatIdnestVarTail(Node Id, Node Statement) {
        Node Ids = new IdNode(), Stat = new StatementNode(), AssignStat = new AssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStatIdnestVarTail, Follow_FuncOrAssignStatIdnestVarTail)) return false;
        if (First_FuncOrAssignStatIdnestVarTail.contains(lookahead.getLexeme())) {
            if (First_FuncOrAssignStatIdnestVarTail.get(0).equals(lookahead.getLexeme())) {
                derive("<FuncOrAssignStatIdnestVarTail>", new String[]{".", "id", "<FuncOrAssignStatIdnest>"});
                boolean match1 = match(".");
                Ids.Family(".", Id, new IdNode(lookahead));
                boolean match2 = match_type("id");
                boolean match3 = FuncOrAssignStatIdnest(Ids, Stat);
                if (match1 && match2 && match3) {
                    Statement.copy(Stat);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_FuncOrAssignStatIdnestVarTail.get(1).equals(lookahead.getLexeme())) {
                derive("<FuncOrAssignStatIdnestVarTail>", new String[]{"<AssignStatTail>"});
                boolean match1 = AssignStatTail(Id, Stat);
                if (match1) {
                    AssignStat.Family("Assign Stat", Stat);
                    Statement.copy(AssignStat);
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncOrAssignStatIdnest(Node Id, Node Stat) {
        Node AParams = new FuncCallNode(), FuncOrAssignStatIdnestFuncTail = new FuncOrAssignStatNode(), Index = new DataMemberNode(), FuncOrAssignStatIdnestVarTail = new FuncOrAssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStatIdnest, Follow_FuncOrAssignStatIdnest)) return false;
        if (First_FuncOrAssignStatIdnest.contains(lookahead.getLexeme())) {
            if (First_FuncOrAssignStatIdnest.get(0).equals(lookahead.getLexeme())) {
                derive("<FuncOrAssignStatIdnest>", new String[]{"(", "<AParams>", ")", "<FuncOrAssignStatIdnestFuncTail>"});
                boolean match1 = match("(");
                boolean match2 = AParams(Id, AParams);
                boolean match3 = match(")");
                boolean match4 = FuncOrAssignStatIdnestFuncTail(AParams, FuncOrAssignStatIdnestFuncTail);
                if (match1 && match2 && match3 && match4) {
                    Stat.copy(FuncOrAssignStatIdnestFuncTail);
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<FuncOrAssignStatIdnest>", new String[]{"<IndiceRep>", "<FuncOrAssignStatIdnestVarTail>"});
                boolean match1 = IndiceRep(Id, Index);
                boolean match2 = FuncOrAssignStatIdnestVarTail(Index, FuncOrAssignStatIdnestVarTail);
                if (match1 && match2) {
                    Stat.copy(FuncOrAssignStatIdnestVarTail);
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncOrAssignStatIdnestFuncTail(Node AParams, Node FuncOrAssignStatIdnestFuncTail) {
        Node Id = new IdNode(), FuncStatTail = new FuncOrAssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStatIdnestFuncTail, Follow_FuncOrAssignStatIdnestFuncTail)) return false;
        if (First_FuncOrAssignStatIdnestFuncTail.contains(lookahead.getLexeme())) {
            derive("<FuncOrAssignStatIdnestFuncTail>", new String[]{".", "id", "<FuncStatTail>"});
            boolean match1 = match(".");
            Id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = FuncStatTail(Id, FuncStatTail);
            if (match1 && match2 && match3) {
                FuncOrAssignStatIdnestFuncTail.Family(".", AParams, FuncStatTail);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FuncOrAssignStatIdnestFuncTail.contains(lookahead.getLexeme())) {
            derive("<FuncOrAssignStatIdnestFuncTail>", new String[]{});
            FuncOrAssignStatIdnestFuncTail.copy(AParams);
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncStatTail(Node Id, Node FuncStatTail) {
        Node AParams = new FuncCallNode(), FuncStatTailIdnest = new Node(), Index = new DataMemberNode(), Ids = new IdNode(), FuncStatTails = new FuncOrAssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncStatTail, Follow_FuncStatTail)) return false;
        if (First_FuncStatTail.contains(lookahead.getLexeme())) {
            if (First_FuncStatTail.get(0).equals(lookahead.getLexeme())) {
                derive("<FuncStatTail>", new String[]{"(", "<AParams>", ")", "<FuncStatTailIdnest>"});
                boolean match1 = match("(");
                boolean match2 = AParams(Id, AParams);
                boolean match3 = match(")");
                boolean match4 = FuncStatTailIdnest(AParams, FuncStatTailIdnest);
                if (match1 && match2 && match3 && match4) {
                    FuncStatTail.copy(FuncStatTailIdnest);
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<FuncStatTail>", new String[]{"<IndiceRep>", ".", "id", "<FuncStatTail>"});
                boolean match1 = IndiceRep(Id, Index);
                boolean match2 = match(".");
                Ids = new IdNode(lookahead);
                boolean match3 = match_type("id");
                boolean match4 = FuncStatTail(Ids, FuncStatTails);
                if (match1 && match2 && match3 && match4) {
                    Node Stat = new Node();
                    Stat = Stat.makeFamily(".", Index, FuncStatTails);
                    FuncStatTail.copy(Stat);
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncStatTailIdnest(Node AParams, Node FuncStatTailIdnest) {
        Node Id = new IdNode(), FuncStatTail = new FuncOrAssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncStatTailIdnest, Follow_FuncStatTailIdnest)) return false;
        if (First_FuncStatTailIdnest.contains(lookahead.getLexeme())) {
            derive("<FuncStatTailIdnest>", new String[]{".", "id", "<FuncStatTail>"});
            boolean match1 = match(".");
            Id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = FuncStatTail(Id, FuncStatTail);
            if (match1 && match2 && match3) {
                FuncStatTailIdnest.Family(".", AParams, FuncStatTail);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FuncStatTailIdnest.contains(lookahead.getLexeme())) {
            derive("<FuncStatTailIdnest>", new String[]{});
            FuncStatTailIdnest.copy(AParams);
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean AssignStatTail(Node id, Node Statement) {
        Node Expr = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_AssignStatTail, Follow_AssignStatTail)) return false;
        if (First_AssignStatTail.contains(lookahead.getLexeme())) {
            derive("<AssignStatTail>", new String[]{"<AssignOp>", "<Expr>"});
            Node expr = new Node("expr");
            boolean match1 = AssignOp();
            boolean match2 = Expr(Expr);
            if (match1 && match2) {
                Expr.setData("Expr");
                Statement.Family("=", id, Expr);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean Expr(Node Expr) {
        Node ArithExpr = new ExprNode(), ExprTail = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_Expr, Follow_Expr)) return false;
        if (First_Expr.contains(lookahead.getLexeme()) || First_Expr.contains(lookahead.getType())) {
            derive("<Expr>", new String[]{"<ArithExpr>", "<ExprTail>"});
            boolean match1 = ArithExpr(ArithExpr);
            boolean match2 = ExprTail(ArithExpr, ExprTail);
            if (match1 && match2) {
                Expr.adoptChildren(ExprTail);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }


    private boolean ArithExpr(Node ArithExpr) {
        Node Term = new ExprNode(), ArithExprTail = new AddOpNode();
        boolean success = true;
        if (!skipErrors(First_ArithExpr, Follow_ArithExpr)) return false;
        if (First_ArithExpr.contains(lookahead.getLexeme()) || First_ArithExpr.contains(lookahead.getType())) {
            derive("<ArithExpr>", new String[]{"<Term>", "<ArithExprTail>"});
            boolean match1 = Term(Term);
            boolean match2 = ArithExprTail(Term, ArithExprTail);
            //System.out.println(ArithExprTail.print());
            if (match1 && match2) {
                ArithExpr.copy(ArithExprTail);
                //ArithExpr = new AddOpNode();
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean ArithExprTail(Node Term, Node ArithExprTail) {
        Node Terms = new ExprNode(), ArithExprTails = new AddOpNode(), Add = new AddOpNode(), AddOp = new AddOpNode();
        boolean success = true;
        if (!skipErrors(First_ArithExprTail, Follow_ArithExprTail)) return false;
        if (First_ArithExprTail.contains(lookahead.getLexeme())) {
            derive("<ArithExprTail>", new String[]{"<AddOp>", "<Term>", "<ArithExprTail>"});
            AddOp = new AddOpNode(lookahead.getLexeme());
            boolean match1 = AddOp();
            boolean match2 = Term(Terms);
            Add.Family(AddOp, Term, Terms);

            //System.out.println("Add Node: \r\n" + Add.print());
            boolean match3 = ArithExprTail(Add, ArithExprTails);
            System.out.println("ArithTail:\r\n" + ArithExprTails.print());
            if (match1 && match2 && match3) {
                if (!ArithExprTails.isEmpty()) {
                    ArithExprTail.copy(ArithExprTails);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ArithExprTail.contains(lookahead.getLexeme())) {
            derive("<ArithExprTail>", new String[]{});
            ArithExprTail.copy(Term);
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean ExprTail(Node ArithExpr, Node ExprTail) {
        Node ArithExprs = new ExprNode();
        Token RelOp = new Token();
        boolean success = true;
        if (!skipErrors(First_ExprTail, Follow_ExprTail)) return false;
        if (First_ExprTail.contains(lookahead.getLexeme())) {
            derive("<ExprTail>", new String[]{"<RelOp>", "<ArithExpr>"});
            RelOp = lookahead;
            boolean match1 = RelOp();
            boolean match2 = ArithExpr(ArithExprs);
            if (match1 && match2) {
                ExprTail.Family(RelOp, ArithExpr, ArithExprs);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ExprTail.contains(lookahead.getLexeme())) {
            ExprTail.copy(ArithExpr);
            derive("<ExprTail>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Term(Node Term) {
        Node Factor = new ExprNode(), TermTail = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_Term, Follow_Term)) return false;
        if (First_Term.contains(lookahead.getLexeme()) || First_Term.contains(lookahead.getType())) {
            derive("<Term>", new String[]{"<Factor>", "<TermTail>"});
            boolean match1 = Factor(Factor);
            boolean match2 = TermTail(Factor, TermTail);
            if (match1 && match2) {
                Term.copy(TermTail);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean TermTail(Node Factor, Node Termtail) {
        Node Factors = new ExprNode(), Termtails = new ExprNode();
        Token MultOp = new Token();
        boolean success = true;
        if (!skipErrors(First_TermTail, Follow_TermTail)) return false;
        if (First_TermTail.contains(lookahead.getLexeme())) {
            derive("<TermTail>", new String[]{"<MultOp>", "<Factor>", "<TermTail>"});
            MultOp = lookahead;
            boolean match1 = MultOp();
            boolean match2 = Factor(Factors);
            Factors = Factors.makeFamily(MultOp, Factor, Factors);
            boolean match3 = TermTail(Factors, Termtails);
            if (match1 && match2 && match3) {
                if (!Termtails.isEmpty()) {
                    Termtail.copy(Termtails);
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_TermTail.contains(lookahead.getLexeme())) {
            derive("<TermTail>", new String[]{});
            Termtail.copy(Factor);
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Factor(Node Factor) {
        Node Expr = new ExprNode(), Factors = new ExprNode(), FuncOrVar = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_Factor, Follow_Factor)) return false;
        if (First_Factor.contains(lookahead.getLexeme()) || First_Factor.contains(lookahead.getType())) {
            if (lookahead.getType().equals("intnum")) {
                derive("<Factor>", new String[]{"intnum"});
                Factor.copy(new Node(lookahead));
                boolean match1 = match_type("intnum");
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getType().equals("floatnum")) {
                derive("<Factor>", new String[]{"floatnum"});
                Factor.copy(new Node(lookahead));
                boolean match1 = match_type("floatnum");
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getType().equals("stringlit")) {
                derive("<Factor>", new String[]{"stringlit"});
                Factor.copy(new Node(lookahead));
                boolean match1 = match_type("stringlit");
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getLexeme().equals("(")) {
                derive("<Factor>", new String[]{"(", "<Expr>", ")"});
                boolean match1 = match("(");
                boolean match2 = Expr(Expr);
                boolean match3 = match(")");
                if (match1 && match2 && match3) {
                    Expr.setData("Expr");
                    Factor.copy(Expr);
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getLexeme().equals("!")) {
                derive("<Factor>", new String[]{"!", "<Factor>"});
                boolean match1 = match("!");
                boolean match2 = Factor(Factors);
                if (match1 && match2) {
                    Factor.setData("not");
                    Factor.adoptChildren(Factors);
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getLexeme().equals("?")) {
                Node Expr1 = new Node();
                Node Expr2 = new Node();
                Node Expr3 = new Node();
                derive("<Factor>", new String[]{"?", "[", "<Expr>", ":", "<Expr>", ":", "<Expr>", "]"});
                boolean match1 = match("?");
                boolean match2 = match("[");
                boolean match3 = Expr(Expr1);
                boolean match4 = match(":");
                boolean match5 = Expr(Expr2);
                boolean match6 = match(":");
                boolean match7 = Expr(Expr3);
                boolean match8 = match("]");
                if (match1 && match2 && match3 && match4 && match5 && match6 && match7 && match8) {
                    Factor.setData("qr");
                    Expr1.setData("Expr");
                    Expr2.setData("Expr");
                    Expr3.setData("Expr");
                    Factor.adoptChildren(Expr1);
                    Factor.adoptChildren(Expr2);
                    Factor.adoptChildren(Expr3);
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getLexeme().equals("+") || lookahead.getLexeme().equals("-")) {
                derive("<Factor>", new String[]{"<Sign>", "<Factor>"});
                Node Sign = new Node(lookahead);
                boolean match1 = Sign();
                boolean match2 = Factor(Factors);
                if (match1 && match2) {
                    Factor.copy(Sign);
                    Factor.adoptChildren(Factors);
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<Factor>", new String[]{"<FuncOrVar>"});
                boolean match1 = FuncOrVar(FuncOrVar);
                if (match1) {
                    Factor.copy(FuncOrVar);
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncOrVar(Node FuncOrVar) {
        Node id = new IdNode(), FuncOrVarIdnest = new Node();
        boolean success = true;
        if (!skipErrors(First_FuncOrVar, Follow_FuncOrVar)) return false;
        if (First_FuncOrVar.contains(lookahead.getType())) {
            derive("<FuncOrVar>", new String[]{"id", "<FuncOrVarIdnest>"});
            id = new IdNode(lookahead);
            boolean match1 = match_type("id");
            boolean match2 = FuncOrVarIdnest(id, FuncOrVarIdnest);
            if (match1 && match2) {
                FuncOrVar.copy(FuncOrVarIdnest);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncOrVarIdnestTail(Node FuncOrVarIdnest, Node FuncOrVarIdnestTail) {
        Node id = new IdNode(), FuncOrVarIdnests = new FuncOrVarNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrVarIdnestTail, Follow_FuncOrVarIdnestTail)) return false;
        if (First_FuncOrVarIdnestTail.contains(lookahead.getLexeme())) {
            derive("<FuncOrVarIdnestTail>", new String[]{".", "id", "<FuncOrVarIdnest>"});
            boolean match1 = match(".");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = FuncOrVarIdnest(id, FuncOrVarIdnests);
            if (match1 && match2 && match3) {
                FuncOrVarIdnestTail.copy(FuncOrVarIdnestTail.makeFamily(".", FuncOrVarIdnest, FuncOrVarIdnests));
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FuncOrVarIdnestTail.contains(lookahead.getLexeme())) {
            FuncOrVarIdnestTail.copy(FuncOrVarIdnest);
            derive("<FuncOrVarIdnestTail>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean FuncOrVarIdnest(Node id, Node FuncOrVarIdnest) {
        Node Aparams = new FuncCallNode(), Index = new DataMemberNode(), FuncOrVarIdnestTail = new FuncOrVarNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrVarIdnest, Follow_FuncOrVarIdnest)) return false;
        if (First_FuncOrVarIdnest.contains(lookahead.getLexeme())) {
            if (lookahead.getLexeme().equals("(")) {
                derive("<FuncOrVarIdnest>", new String[]{"(", "<AParams>", ")", "<FuncOrVarIdnestTail>"});
                boolean match1 = match("(");
                boolean match2 = AParams(id, Aparams);
                boolean match3 = match(")");
                boolean match4 = FuncOrVarIdnestTail(Aparams, FuncOrVarIdnestTail);
                if (match1 && match2 && match3 && match4) {
                    FuncOrVarIdnest.copy(FuncOrVarIdnestTail);
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<FuncOrVarIdnest>", new String[]{"<IndiceRep>", "<FuncOrVarIdnestTail>"});
                boolean match1 = IndiceRep(id, Index);
                boolean match2 = FuncOrVarIdnestTail(Index, FuncOrVarIdnestTail);
                if (match1 && match2) {
                    FuncOrVarIdnest.copy(FuncOrVarIdnestTail);
                    success = true;
                } else {
                    success = false;
                }
            }
        } else if (Follow_FuncOrVarIdnest.contains(lookahead.getLexeme())) {
            FuncOrVarIdnest.copy(id);
            derive("<FuncOrVarIdnest>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean IndiceRep(Node id, Node Index) {
        Node Expr = new ExprNode(), Exprs = new ExprNode(), IndiceList = new IndiceListNode();
        boolean success = true;
        if (!skipErrors(First_IndiceRep, Follow_IndiceRep)) return false;
        if (First_IndiceRep.contains(lookahead.getLexeme())) {
            derive("<IndiceRep>", new String[]{"[", "<Expr>", "]", "<IndiceRep>"});
            boolean match1 = match("[");
            boolean match2 = Expr(Expr);
            boolean match3 = match("]");
            boolean match4 = IndiceRep(Exprs);
            if (match1 && match2 && match3 && match4) {
                if (!Exprs.isEmpty()) {
                    Expr.makeSiblings(Exprs);
                }
                Expr.setData("Arith Expr");
                IndiceList.Family("Index List", Expr);
                Index.Family("Data Member", id, IndiceList);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_IndiceRep.contains(lookahead.getLexeme())) {
            derive("<IndiceRep>", new String[]{});
            if (!id.isEmpty()) {
                IndiceList.setData("EPSILON");
                Index.Family("Data Member", id, IndiceList);
            }
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean IndiceRep(Node Index) {
        Node Expr = new ExprNode(), Exprs = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_IndiceRep, Follow_IndiceRep)) return false;
        if (First_IndiceRep.contains(lookahead.getLexeme())) {
            derive("<IndiceRep>", new String[]{"[", "<Expr>", "]", "<IndiceRep>"});
            boolean match1 = match("[");
            boolean match2 = Expr(Expr);
            boolean match3 = match("]");
            boolean match4 = IndiceRep(Exprs);
            if (match1 && match2 && match3 && match4) {
                if (!Exprs.isEmpty()) {
                    Expr.makeSiblings(Exprs);
                }
                Expr.setData("Arith Expr");
                Index.copy(Expr);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_IndiceRep.contains(lookahead.getLexeme())) {
            derive("<IndiceRep>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean AParams(Node id, Node Aparams) {
        Node Expr = new ExprNode(), Exprs = new ExprNode(), Aparam = new AParamNode();
        boolean success = true;
        if (!skipErrors(First_AParams, Follow_AParams)) return false;
        if (First_AParams.contains(lookahead.getLexeme()) || First_AParams.contains(lookahead.getType())) {
            derive("<AParams>", new String[]{"<Expr>", "<AParamsTail>"});
            boolean match1 = Expr(Expr);
            boolean match2 = AParamsTail(Exprs);
            if (match1 && match2) {
                Expr.setData("Expr");
                if (!Exprs.isEmpty()) {
                    Expr.makeSiblings(Exprs);
                }
                Aparam.Family("AParams", Expr);
                Node temp = new Node();
                Aparams.Family("Fcall", id, Aparam);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_AParams.contains(lookahead.getLexeme())) {
            derive("<AParams>", new String[]{});
            Aparam.Family("AParams", "EPSILON");
            Aparams.Family("Fcall", id, Aparam);
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean AParamsTail(Node Expr) {
        Node Exprs = new Node();
        boolean success = true;
        if (!skipErrors(First_AParamsTail, Follow_AParamsTail)) return false;
        if (First_AParamsTail.contains(lookahead.getLexeme())) {
            derive("<AParamsTail>", new String[]{",", "<Expr>", "<AParamsTail>"});
            boolean match1 = match(",");
            boolean match2 = Expr(Expr);
            boolean match3 = AParamsTail(Exprs);
            if (match1 && match2 && match3) {
                if (!Exprs.isEmpty()) {
                    Expr.makeSiblings(Exprs);
                }
                Expr.setData("Expr");
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_AParamsTail.contains(lookahead.getLexeme())) {
            derive("<AParamsTail>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    private boolean Sign() {
        boolean success = true;
        if (!skipErrors(First_Sign, Follow_Sign)) return false;
        if (First_Sign.contains(lookahead.getLexeme())) {
            if (First_Sign.get(0).equals(lookahead.getLexeme())) {
                derive("<Sign>", new String[]{First_Sign.get(0)});
                boolean match1 = match(First_Sign.get(0));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Sign.get(1).equals(lookahead.getLexeme())) {
                derive("<Sign>", new String[]{First_Sign.get(1)});
                boolean match1 = match(First_Sign.get(1));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else {
                success = false;
            }
        }
        return success;
    }

    private boolean AddOp() {
        boolean success = true;
        if (!skipErrors(First_AddOp, Follow_AddOp)) return false;
        if (First_AddOp.contains(lookahead.getLexeme())) {
            if (First_AddOp.get(0).equals(lookahead.getLexeme())) {
                derive("<AddOp>", new String[]{First_RelOp.get(0)});
                boolean match1 = match(First_AddOp.get(0));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_AddOp.get(1).equals(lookahead.getLexeme())) {
                derive("<AddOp>", new String[]{First_RelOp.get(1)});
                boolean match1 = match(First_AddOp.get(1));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_AddOp.get(2).equals(lookahead.getLexeme())) {
                derive("<AddOp>", new String[]{First_RelOp.get(2)});
                boolean match1 = match(First_AddOp.get(2));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean AssignOp() {
        boolean success = true;
        if (!skipErrors(First_AssignOp, Follow_AssignOp)) return false;
        if (First_AssignOp.contains(lookahead.getLexeme())) {
            derive("<AssignOp>", new String[]{"="});
            boolean match1 = match("=");
            if (match1) {
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }

    private boolean MultOp() {
        boolean success = true;
        if (!skipErrors(First_MultOp, Follow_MultOp)) return false;
        if (First_MultOp.contains(lookahead.getLexeme())) {
            if (First_MultOp.get(0).equals(lookahead.getLexeme())) {
                derive("<MultOp>", new String[]{First_MultOp.get(0)});
                boolean match1 = match(First_MultOp.get(0));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_MultOp.get(1).equals(lookahead.getLexeme())) {
                derive("<MultOp>", new String[]{First_MultOp.get(1)});
                boolean match1 = match(First_MultOp.get(1));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_MultOp.get(2).equals(lookahead.getLexeme())) {
                derive("<MultOp>", new String[]{First_MultOp.get(2)});
                boolean match1 = match(First_MultOp.get(2));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }


    private boolean RelOp() {
        boolean success = true;
        if (!skipErrors(First_RelOp, Follow_RelOp)) return false;
        if (First_RelOp.contains(lookahead.getLexeme())) {
            if (First_RelOp.get(0).equals(lookahead.getLexeme())) {
                derive("<RelOp>", new String[]{First_RelOp.get(0)});
                boolean match1 = match(First_RelOp.get(0));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_RelOp.get(1).equals(lookahead.getLexeme())) {
                derive("<RelOp>", new String[]{First_RelOp.get(1)});
                boolean match1 = match(First_RelOp.get(1));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_RelOp.get(2).equals(lookahead.getLexeme())) {
                derive("<RelOp>", new String[]{First_RelOp.get(2)});
                boolean match1 = match(First_RelOp.get(2));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_RelOp.get(3).equals(lookahead.getLexeme())) {
                derive("<RelOp>", new String[]{First_RelOp.get(3)});
                boolean match1 = match(First_RelOp.get(3));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_RelOp.get(4).equals(lookahead.getLexeme())) {
                derive("<RelOp>", new String[]{First_RelOp.get(4)});
                boolean match1 = match(First_RelOp.get(4));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_RelOp.get(5).equals(lookahead.getLexeme())) {
                derive("<RelOp>", new String[]{First_RelOp.get(5)});
                boolean match1 = match(First_RelOp.get(5));
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        return success;
    }
}

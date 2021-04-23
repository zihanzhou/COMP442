package Compiler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AST.*;

public class Parse {

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
    private boolean indice_check = false;
    public Node ProgNode = new ProgNode();

    public Parse(DataList<Token> token) {
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

        Derivation.add("<Prog>");
        ProgNode = Prog(new ProgNode());
        if (ProgNode.getSuccess()) {
            System.out.println("Success");
            String str = ProgNode.print();
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

    public void close() throws IOException {
        out_Error.close();
        out_Ast.close();
        out_Derivation.close();
    }

    private void removeComment() {
        int i = 0;
        int size = this.token.size();
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

    private Node Prog(Node Prog) {
        boolean success = true;
        Node ClassDeclList = new ClassListNode(), FuncDefList = new FuncDefListNode(), ClassDecls = new ClassNode(), FuncDef = new FuncDefNode(), FuncBody = new MainBlockNode();
        if (!skipErrors(First_Prog, Follow_Prog)) {
            Prog.setSuccess(false);
            return Prog;
        }
        if (First_Prog.contains(lookahead.getLexeme())) {
            derive("<Prog>", new String[]{"<ClassDecl>", "<FuncDef>", "main", "<FuncBody>"});
            ClassDecls = ClassDecl(ClassDecls);
            boolean match1 = ClassDecls.getSuccess();
            FuncDef = FuncDef(FuncDef);
            boolean match2 = FuncDef.getSuccess();
            boolean match3 = match("main");
            FuncBody = FuncBody(FuncBody);
            boolean match4 = FuncBody.getSuccess();
            if (match1 && match2 && match3 && match4) {
                if (!ClassDecls.isEmpty()) {
                    ClassDeclList.Family("Class List", ClassDecls);
                } else {
                    ClassDeclList.Family("Class List", "EPSILON");
                }

                if (!FuncDef.isEmpty()){
                    FuncDefList.Family("Func Def List", FuncDef);
                } else {
                    FuncDefList.Family("Func Def List", "EPSILON");
                }
                FuncBody.setData("Main");
                Prog.Family("Prog", ClassDeclList, FuncDefList, FuncBody);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        Prog.setSuccess(success);
        return Prog;
    }

    private Node ClassDecl(Node ClassDecl) {
        Node id, InherList = new InheritListNode(), MembList = new MemberListNode(), MembDecl = new MembDeclNode(), ClassDecls = new ClassNode();
        boolean success = true;
        if (!skipErrors(First_ClassDecl, Follow_ClassDecl)) {
            ClassDecl.setSuccess(false);
            return ClassDecl;
        }
        if (First_ClassDecl.contains(lookahead.getLexeme())) {
            derive("<ClassDecl>", new String[]{"class", "id", "<Inherit>", "{", "<ClassDeclBody>", "}", ";", "<ClassDecl>"});
            boolean match1 = match("class");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            InherList = Inherit(InherList);
            boolean match3 = InherList.getSuccess();
            boolean match4 = match("{");
            MembDecl = ClassDeclBody(MembDecl);
            boolean match5 = MembDecl.getSuccess();
            boolean match6 = match("}");
            boolean match7 = match(";");
            ClassDecls = ClassDecl(ClassDecls);
            boolean match8 = ClassDecls.getSuccess();
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
        ClassDecl.setSuccess(success);
        return ClassDecl;
    }

    private Node Inherit(Node InherList) {
        Node id, NestedId = new IdNode();
        boolean success = true;
        if (!skipErrors(First_Inherit, Follow_Inherit)) {
            InherList.setSuccess(false);
            return InherList;
        }
        if (First_Inherit.contains(lookahead.getLexeme())) {
            derive("<Inherit>", new String[]{"inherits", "id", "<NestedId>"});
            boolean match1 = match(First_Inherit.get(0));
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            NestedId = NestedId(NestedId);
            boolean match3 = NestedId.getSuccess();
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
            InherList.Family("Inher List", new Node("EPSILON"));
            derive("<Inherit>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        InherList.setSuccess(success);
        return InherList;
    }

    private Node ClassDeclBody(Node MembDecl) {
        Node MembDecls = new MembDeclNode(), Visibility = new VisibilityNode();
        boolean success = true;
        if (!skipErrors(First_ClassDeclBody, Follow_ClassDeclBody)) {
            MembDecl.setSuccess(false);
            return MembDecl;
        }
        if (First_ClassDeclBody.contains(lookahead.getLexeme()) || First_ClassDeclBody.contains(lookahead.getType())) {
            derive("<ClassDeclBody>", new String[]{"<Visibility>", "<MemberDecl>", "<ClassDeclBody>"});
            Visibility = Visibility(Visibility);
            boolean match1 = Visibility.getSuccess();
            MembDecl = MemberDecl(MembDecl);
            boolean match2 = MembDecl.getSuccess();
            MembDecls = ClassDeclBody(MembDecls);
            boolean match3 = MembDecls.getSuccess();
            if (match1 && match2 && match3) {
                MembDecl.adoptChildren(Visibility);
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
        MembDecl.setSuccess(success);
        return MembDecl;
    }

    private Node Visibility(Node Visibility) {
        boolean success = true;
        if (!skipErrors(First_Visibility, Follow_Visibility)) {
            Visibility.setSuccess(false);
            return Visibility;
        }
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
        Visibility.setSuccess(success);
        return Visibility;
    }

    private Node MemberDecl(Node MembDecl) {
        boolean success = true;
        if (!skipErrors(First_MemberDecl, Follow_MemberDecl)){
            MembDecl.setSuccess(false);
            return MembDecl;
        }
        if (First_MemberDecl.contains(lookahead.getLexeme()) || First_MemberDecl.contains(lookahead.getType())) {
            if (First_MemberDecl.get(0).equals(lookahead.getLexeme())) {
                derive("<MemberDecl>", new String[]{"<FuncDecl>"});
                MembDecl = FuncDecl(new FuncDeclNode());
                boolean match = MembDecl.getSuccess();
                if (match) {
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<MemberDecl>", new String[]{"<VarDecl>"});
                MembDecl = VarDecl(new VarDeclNode());
                boolean match = MembDecl.getSuccess();
                if (match) {
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        MembDecl.setSuccess(success);
        return MembDecl;
    }

    private Node FuncDecl(Node FuncDecl) {
        Node type = new TypeNode(), id = new IdNode(), FParmList = new ParamListNode(), FParam = new ParamNode();
        boolean success = true;
        if (!skipErrors(First_FuncDecl, Follow_FuncDecl)){
            FuncDecl.setSuccess(false);
            return FuncDecl;
        }
        if (First_FuncDecl.contains(lookahead.getLexeme())) {
            derive("<FuncDecl>", new String[]{"func", "id", "(", "<FParams>", ")", ":", "<FuncDeclTail>", ";"});
            boolean match1 = match("func");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            boolean match3 = match("(");
            FParam = FParams(FParam);
            boolean match4 = FParam.getSuccess();
            boolean match5 = match(")");
            boolean match6 = match(":");
            type = FuncDeclTail(type);
            boolean match7 = type.getSuccess();
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
        FuncDecl.setSuccess(success);
        return FuncDecl;
    }

    private Node FuncDeclTail(Node Type) {
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
            Type = Type(Type);
            if (Type.getSuccess()) {
                success = true;
            } else {
                success = false;
            }
        }
        Type.setSuccess(success);
        return Type;
    }

    private Node FParams(Node FParam) {
        Node type = new TypeNode(), id = new IdNode(), dim = new DimNode(), DimList = new DimListNode(), FParams = new ParamNode();
        boolean success = true;
        if (!skipErrors(First_FParams, Follow_FParamsTail)) {
            FParam.setSuccess(false);
            return FParam;
        }
        if (First_FParams.contains(lookahead.getLexeme()) || First_FParams.contains(lookahead.getType())) {
            derive("<FParams>", new String[]{"<Type>", "id", "<ArraySizeRept>", "<FParamsTail>"});
            type = Type(type);
            boolean match1 = type.getSuccess();
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            dim = ArraySizeRept(dim);
            boolean match3 = dim.getSuccess();
            FParams = FParamsTail(FParams);
            boolean match4 = FParams.getSuccess();
            if (match1 && match2 && match3 && match4) {
                if (!dim.isEmpty()) {
                    DimList.Family("Dim List", dim);
                } else {
                    DimList.Family("Dim List", "EPSILON");
                }
                id.setType(type.getData());
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
        FParam.setSuccess(success);
        return FParam;
    }

    private Node FParamsTail(Node FParam) {
        Node type = new TypeNode(), id = new IdNode(), dim = new DimNode(), DimList = new DimListNode(), FParams = new ParamNode();
        boolean success = true;
        if (!skipErrors(First_FParamsTail, Follow_FParamsTail)) {
            FParam.setSuccess(false);
            return FParam;
        }
        if (First_FParamsTail.contains(lookahead.getLexeme())) {
            derive("<FParamsTail>", new String[]{",", "<Type>", "id", "<ArraySizeRept>", "<FParamsTail>"});
            boolean match1 = match(First_FParamsTail.get(0));
            type = Type(type);
            boolean match2 = type.getSuccess();
            id = new IdNode(lookahead);
            boolean match3 = match_type("id");
            dim = ArraySizeRept(dim);
            boolean match4 = dim.getSuccess();
            FParams = FParamsTail(FParams);
            boolean match5 = FParams.getSuccess();
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
        FParam.setSuccess(success);
        return FParam;
    }

    private Node ArraySizeRept(Node num) {
        Node nums = new DimNode();
        boolean success = true;
        if (!skipErrors(First_ArraySizeRept, Follow_ArraySizeRept)) {
            num.setSuccess(false);
            return num;
        }
        if (First_ArraySizeRept.contains(lookahead.getLexeme())) {
            derive("<ArraySizeRept>", new String[]{"[", "<IntNum>", "]", "<ArraySizeRept>"});
            boolean match1 = match("[");
            num = IntNum(num);
            boolean match2 = num.getSuccess();
            boolean match3 = match("]");
            nums = ArraySizeRept(nums);
            boolean match4 = nums.getSuccess();
            if (match1 && match2 && match3 && match4) {
                if (num.isEmpty()) {
                    num.setData("empty");
                } else if (!nums.isEmpty()) {
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
        num.setSuccess(success);
        return num;
    }

    private Node IntNum(Node num) {
        boolean success = true;
        if (!skipErrors(First_IntNum, Follow_IntNum)) {
            num.setSuccess(false);
            return num;
        }
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
        num.setSuccess(success);
        return num;
    }

    private Node Type(Node Type) {
        boolean success = true;
        if (!skipErrors(First_Type, Follow_Type)) {
            Type.setSuccess(false);
            return Type;
        }
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
        Type.setSuccess(success);
        return Type;
    }

    private Node NestedId(Node NestedId) {
        Node idnest = new IdNode();
        boolean success = true;
        if (!skipErrors(First_NestedId, Follow_NestedId)){
            NestedId.setSuccess(false);
            return NestedId;
        }
        if (First_NestedId.contains(lookahead.getLexeme())) {
            derive("<NestedId>", new String[]{",", "id", "<NestedId>"});
            boolean match1 = match(",");
            NestedId = new IdNode(lookahead);
            boolean match2 = match_type("id");
            idnest = NestedId(idnest);
            boolean match3 = idnest.getSuccess();
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
        NestedId.setSuccess(success);
        return NestedId;
    }


    private Node VarDecl(Node VarDecl) {
        Node type = new TypeNode(), id = new IdNode(), DimList = new DimListNode(), dim = new DimNode();
        boolean success = true;
        if (!skipErrors(First_VarDecl, Follow_VarDecl)) {
            VarDecl.setSuccess(false);
            return VarDecl;
        }
        if (First_VarDecl.contains(lookahead.getLexeme()) || First_VarDecl.contains(lookahead.getType())) {
            derive("<VarDecl>", new String[]{"<Type>", "id", "<ArraySizeRept>", ";"});
            type = Type(type);
            boolean match1 = type.getSuccess();
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            dim = ArraySizeRept(dim);
            boolean match3 = dim.getSuccess();
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
        VarDecl.setSuccess(success);
        return VarDecl;
    }

    private Node FuncDef(Node FuncDef) {
        Node FuncDefs = new FuncDefNode();
        boolean success = true;
        if (!skipErrors(First_FuncDef, Follow_FuncDef)){
            FuncDef.setSuccess(false);
            return FuncDef;
        }
        if (First_FuncDef.contains(lookahead.getLexeme())) {
            derive("<FuncDef>", new String[]{"<Function>", "<FuncDef>"});
            FuncDef = Function(FuncDef);
            boolean match1 = FuncDef.getSuccess();
            FuncDefs = FuncDef(FuncDefs);
            boolean match2 = FuncDefs.getSuccess();
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
        FuncDef.setSuccess(success);
        return FuncDef;
    }

    private Node Function(Node FuncDef) {
        Node FuncBody = new ProgramBlockNode();
        boolean success = true;
        if (!skipErrors(First_Function, Follow_Function)){
            FuncDef.setSuccess(false);
            return FuncDef;
        }
        if (First_Function.contains(lookahead.getLexeme())) {
            derive("<Function>", new String[]{"<FuncHead>", "<FuncBody>"});
            FuncDef = FuncHead(FuncDef);
            boolean match1 = FuncDef.getSuccess();
            FuncBody = FuncBody(FuncBody);
            boolean match2 = FuncBody.getSuccess();
            if (match1 && match2) {
                FuncDef.addChild(FuncBody);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        FuncDef.setSuccess(success);
        return FuncDef;
    }

    private Node FuncHead(Node FuncDef) {
        Node id = new IdNode(), ScopeSpec = new IdNode(), FParam = new ParamNode(), FParamList = new ParamListNode(), type = new TypeNode(), Scope_spec = new ScopeSpecNode();
        boolean success = true;
        if (!skipErrors(First_FuncHead, Follow_FuncHead)) {
            FuncDef.setSuccess(false);
            return FuncDef;
        }
        if (First_FuncHead.contains(lookahead.getLexeme())) {
            derive("<FuncHead>", new String[]{"func", "id", "<ClassMethod>", "(", "<FParams>", ")", ":", "<FuncDeclTail>"});
            boolean match1 = match("func");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            ScopeSpec = ClassMethod(ScopeSpec);
            boolean match3 = ScopeSpec.getSuccess();
            boolean match4 = match("(");
            FParam = FParams(FParam);
            boolean match5 = FParam.getSuccess();
            boolean match6 = match(")");
            boolean match7 = match(":");
            type =  FuncDeclTail(type);
            boolean match8 = type.getSuccess();
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
        FuncDef.setSuccess(success);
        return FuncDef;
    }

    private Node ClassMethod(Node ScopeSpec) {
        boolean success = true;
        if (!skipErrors(First_ClassMethod, Follow_ClassMethod)) {
            ScopeSpec.setSuccess(false);
            return ScopeSpec;
        }
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
        ScopeSpec.setSuccess(success);
        return ScopeSpec;
    }

    private Node FuncBody(Node ProgramBlock) {
        Node VarDecl = new VarDeclListNode(), Statements = new StatementNode(), StatmentList = new StatBlockNode(), FuncBody = new ProgramBlockNode();
        boolean success = true;
        if (!skipErrors(First_FuncBody, Follow_FuncBody)) {
            ProgramBlock.setSuccess(false);
            return ProgramBlock;
        }
        if (First_FuncBody.contains(lookahead.getLexeme())) {
            derive("<FuncBody>", new String[]{"{", "<MethodBodyVar>", "<StatementList>", "}"});
            boolean match1 = match("{");
            VarDecl = MethodBodyVar(VarDecl);
            boolean match2 = VarDecl.getSuccess();
            Statements = StatementList(Statements);
            boolean match3 = Statements.getSuccess();
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
        ProgramBlock.setSuccess(success);
        return ProgramBlock;
    }

    private Node MethodBodyVar(Node StatBlock) {
        Node VarDeclRep = new VarDeclNode();
        boolean success = true;
        if (!skipErrors(First_MethodBodyVar, Follow_MethodBodyVar)) {
            StatBlock.setSuccess(false);
            return StatBlock;
        }
        if (First_MethodBodyVar.contains(lookahead.getLexeme())) {
            derive("<MethodBodyVar>", new String[]{"var", "{", "<VarDeclRep>", "}"});
            boolean match1 = match("var");
            boolean match2 = match("{");
            VarDeclRep = VarDeclRep(VarDeclRep);
            boolean match3 = VarDeclRep.getSuccess();
            boolean match4 = match("}");
            if (match1 && match2 && match3 && match4) {
                if (!VarDeclRep.isEmpty()) {
                    StatBlock.adoptChildren(VarDeclRep);
                } else {
                    StatBlock.adoptChildren(new Node("EPSILON"));
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
        StatBlock.setSuccess(success);
        return StatBlock;
    }

    private Node VarDeclRep(Node VarDecl) {
        Node VarDecls = new VarDeclNode();
        boolean success = true;
        if (!skipErrors(First_VarDeclRep, Follow_VarDeclRep)) {
            VarDecl.setSuccess(false);
            return VarDecl;
        }
        if (First_VarDeclRep.contains(lookahead.getLexeme()) || First_VarDeclRep.contains(lookahead.getType())) {
            derive("<VarDeclRep>", new String[]{"<VarDecl>", "<VarDeclRep>"});
            VarDecl = VarDecl(VarDecl);
            boolean match1 = VarDecl.getSuccess();
            VarDecls = VarDeclRep(VarDecls);
            boolean match2 = VarDecls.getSuccess();
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
            success = true;
        } else {
            success = false;
        }
        VarDecl.setSuccess(success);
        return VarDecl;
    }

    private Node StatementList(Node Statement) {
        Node Statements = new StatementNode();
        boolean success = true;
        if (!skipErrors(First_StatementList, Follow_StatementList)){
            Statement.setSuccess(false);
            return Statement;
        }
        if (First_StatementList.contains(lookahead.getLexeme()) || First_StatementList.contains(lookahead.getType())) {
            derive("<StatementList>", new String[]{"<Statement>", "<StatementList>"});
            Statement = Statement(Statement);
            boolean match1 = Statement.getSuccess();
            Statements = StatementList(Statements);
            boolean match2 = Statements.getSuccess();
            if (match1 && match2) {
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
        Statement.setSuccess(success);
        return Statement;
    }

    private Node StatBlock(Node StatBlock) {
        Node Statments = new Node(), StatementList = new Node();
        boolean success = true;
        if (!skipErrors(First_StatBlock, Follow_StatBlock)) {
            StatBlock.setSuccess(false);
            return StatBlock;
        }
        if (First_StatBlock.contains(lookahead.getLexeme()) || First_StatBlock.contains(lookahead.getType())) {
            if (First_StatBlock.get(0).equals(lookahead.getLexeme())) {
                derive("<StatBlock>", new String[]{"{", "<StatementList>", "}"});
                boolean match1 = match("{");
                Statments = StatementList(Statments);
                boolean match2 = Statments.getSuccess();
                boolean match3 = match("}");
                if (match1 && match2 && match3) {
                    StatementList.setData("Statement List");
                    StatementList.adoptChildren(Statments);
                    StatBlock = StatementList;
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<StatBlock>", new String[]{"<Statement>"});
                Statments = Statement(Statments);
                boolean match1 = Statments.getSuccess();
                if (match1) {
                    StatBlock = Statments;
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
        StatBlock.setSuccess(success);
        return StatBlock;
    }

    private Node Statement(Node Statement) {
        Node Expr = new ExprNode(), StatBlock1 = new StatBlockNode(), StatBlock2 = new StatBlockNode(), Variable = new VarNode(), FuncOrVarAssign = new StatementNode();
        boolean success = true;
        if (!skipErrors(First_Statement, Follow_Statement)) {
            Statement.setSuccess(false);
            return Statement;
        }
        if (First_Statement.contains(lookahead.getLexeme()) || First_Statement.contains(lookahead.getType())) {
            if (First_Statement.get(0).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"if", "(", "<Expr>", ")", "then", "<StatBlock>", "else", "<StatBlock>", ";"});
                boolean match1 = match("if");
                boolean match2 = match("(");
                Expr = Expr(Expr);
                boolean match3 = Expr.getSuccess();
                boolean match4 = match(")");
                boolean match5 = match("then");
                StatBlock1 = StatBlock(StatBlock1);
                boolean match6 = StatBlock1.getSuccess();
                boolean match7 = match("else");
                StatBlock2 = StatBlock(StatBlock2);
                boolean match8 = StatBlock2.getSuccess();
                boolean match9 = match(";");
                if (match1 && match2 && match3 && match4 && match5 && match6 && match7 && match8 && match9) {
                    Expr.setData("Expr");
                    Statement = new IfStatNode();
                    Statement.Family("if Stat", Expr, StatBlock1, StatBlock2);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(1).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"while", "(", "<Expr>", ")", "<StatBlock>", ";"});
                boolean match1 = match("while");
                boolean match2 = match("(");
                Expr = Expr(Expr);
                boolean match3 = Expr.getSuccess();
                boolean match4 = match(")");
                StatBlock1 = StatBlock(StatBlock1);
                boolean match5 = StatBlock1.getSuccess();
                boolean match6 = match(";");
                if (match1 && match2 && match3 && match4 && match5 && match6) {
                    Statement = new WhileStatNode();
                    Statement.setData("While Stat");
                    Expr.setData("Expr");
                    Statement.Family("While Stat", Expr, StatBlock1);
                    success = true;
                } else {
                    success = false;
                }
            } else if (First_Statement.get(2).equals(lookahead.getLexeme())) {
                derive("<Statement>", new String[]{"read", "(", "<Variable>", ")", ";"});
                boolean match1 = match("read");
                boolean match2 = match("(");
                Variable = Variable(Variable);
                boolean match3 = Variable.getSuccess();
                boolean match4 = match(")");
                boolean match5 = match(";");
                if (match1 && match2 && match3 && match4 && match5) {
                    Statement = new ReadStatNode();
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
                Expr = Expr(Expr);
                boolean match3 = Expr.getSuccess();
                boolean match4 = match(")");
                boolean match5 = match(";");
                if (match1 && match2 && match3 && match4 && match5) {
                    Statement = new WriteStatNode();
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
                Expr = Expr(Expr);
                boolean match3 = Expr.getSuccess();
                boolean match4 = match(")");
                boolean match5 = match(";");
                if (match1 && match2 && match3 && match4 && match5) {
                    Statement = new ReturnStatNode();
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
                Statement = FuncOrAssignStat(Statement);
                boolean match1 = Statement.getSuccess();
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
        Statement.setSuccess(success);
        return Statement;
    }

    private Node Variable(Node Variable) {
        Node Id = new IdNode(), VariableIdnest = new VarNode();
        boolean success = true;
        if (!skipErrors(First_Variable, Follow_Variable)) {
            Variable.setSuccess(false);
            return Variable;
        }
        if (First_Variable.contains(lookahead.getType())) {
            derive("<Variable>", new String[]{"id", "<VariableIdnest>"});
            Id = new IdNode(lookahead);
            boolean match1 = match_type("id");
            VariableIdnest = VariableIdnest(Id, VariableIdnest);
            boolean match2 = VariableIdnest.getSuccess();
            if (match1 && match2) {
                Variable = VariableIdnest;
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        Variable.setSuccess(success);
        return Variable;
    }

    private Node VariableIdnest(Node Id, Node VariableIdnest) {
        Node Index = new DataMemberNode(), VariableIdnestTail = new VarNode();
        boolean success = true;
        if (!skipErrors(First_VariableIdnest, Follow_VariableIdnest)) {
            VariableIdnest.setSuccess(false);
            return VariableIdnest;
        }
        if (First_VariableIdnest.contains(lookahead.getLexeme())) {
            derive("<VariableIdnest>", new String[]{"<IndiceRep>", "<VariableIdnestTail>"});
            Index = IndiceRep(Id, Index);
            boolean match1 = Index.getSuccess();
            VariableIdnestTail = VariableIdnestTail(Index, VariableIdnestTail);
            boolean match2 = VariableIdnestTail.getSuccess();
            if (match1 && match2) {
                VariableIdnest = VariableIdnestTail;
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_VariableIdnest.contains(lookahead.getLexeme())) {
            derive("<VariableIdnest>", new String[]{"<IndiceRep>", "<VariableIdnestTail>"});
            VariableIdnest = new DataMemberNode();
            VariableIdnest.Family("Var", Id, new IndiceListNode("EPSILON"));
            success = true;
        } else {
            success = false;
        }
        VariableIdnest.setSuccess(success);
        return VariableIdnest;
    }

    private Node VariableIdnestTail(Node VariableIdnest, Node VariableIdnestTail) {
        Node Id = new IdNode(), VariableIdnests = new VarNode();
        boolean success = true;
        if (!skipErrors(First_VariableIdnestTail, Follow_VariableIdnestTail)) {
            VariableIdnestTail.setSuccess(false);
            return VariableIdnestTail;
        }
        if (First_VariableIdnestTail.contains(lookahead.getLexeme())) {
            derive("<VariableIdnestTail>", new String[]{".", "id", "<VariableIdnest>"});
            boolean match1 = match(".");
            Id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            VariableIdnests = VariableIdnest(Id, VariableIdnests);
            boolean match3 = VariableIdnests.getSuccess();
            if (match1 && match2 && match3) {
                VariableIdnestTail = new DotNode();
                VariableIdnestTail.Family(".", VariableIdnest, VariableIdnests);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_VariableIdnestTail.contains(lookahead.getLexeme())) {
            derive("<VariableIdnestTail>", new String[]{});
            VariableIdnestTail = VariableIdnest;
            success = true;
        } else {
            success = false;
        }
        VariableIdnestTail.setSuccess(success);
        return VariableIdnestTail;
    }

    private Node FuncOrAssignStat(Node Statement){
        Node id = new IdNode(), Statements = new StatementNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStat, Follow_FuncOrAssignStat)) {
            Statement.setSuccess(false);
            return Statement;
        }
        if (First_FuncOrAssignStat.contains(lookahead.getType())) {
            derive("<FuncOrAssignStat>", new String[]{"id", "<FuncOrAssignStatIdnest>"});
            id = new IdNode(lookahead);
            boolean match1 = match_type("id");
            Statements = FuncOrAssignStatIdnest(id, Statements);
            boolean match2 = Statements.getSuccess();
            if (match1 && match2) {
                Statement = Statements;
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        Statement.setSuccess(success);
        return Statement;
    }

    private Node FuncOrAssignStatIdnest(Node Id, Node Statement) {
        Node AParams = new FuncCallNode(), FuncOrAssignStatIdnestFuncTail = new FuncOrAssignStatNode(), Index = new DataMemberNode(), FuncOrAssignStatIdnestVarTail = new FuncOrAssignStatNode(), IndiceList = new IndiceListNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStatIdnest, Follow_FuncOrAssignStatIdnest)) {
            Statement.setSuccess(false);
            return Statement;
        }
        if (First_FuncOrAssignStatIdnest.contains(lookahead.getLexeme())) {
            if (First_FuncOrAssignStatIdnest.get(0).equals(lookahead.getLexeme())) {
                derive("<FuncOrAssignStatIdnest>", new String[]{"(", "<AParams>", ")", "<FuncOrAssignStatIdnestFuncTail>"});
                boolean match1 = match("(");
                AParams = AParams(Id, AParams);
                boolean match2 = AParams.getSuccess();
                boolean match3 = match(")");
                FuncOrAssignStatIdnestFuncTail = FuncOrAssignStatIdnestFuncTail(AParams, FuncOrAssignStatIdnestFuncTail);
                boolean match4 = FuncOrAssignStatIdnestFuncTail.getSuccess();
                if (match1 && match2 && match3 && match4) {
                    Statement = FuncOrAssignStatIdnestFuncTail;
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<FuncOrAssignStatIdnest>", new String[]{"<IndiceRep>", "<FuncOrAssignStatIdnestVarTail>"});
                Index = IndiceRep(Id, Index);
                boolean match1 = Index.getSuccess();
                FuncOrAssignStatIdnestVarTail = FuncOrAssignStatIdnestVarTail(Index, FuncOrAssignStatIdnestVarTail);
                boolean match2 = FuncOrAssignStatIdnestVarTail.getSuccess();
                if (match1 && match2) {
                    Statement = FuncOrAssignStatIdnestVarTail;
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        Statement.setSuccess(success);
        return Statement;
    }

    private Node FuncOrAssignStatIdnestFuncTail(Node AParams, Node FuncOrAssignStatIdnestFuncTail) {
        Node Id = new IdNode(), FuncStatTail = new FuncOrAssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStatIdnestFuncTail, Follow_FuncOrAssignStatIdnestFuncTail)) {
            FuncOrAssignStatIdnestFuncTail.setSuccess(false);
            return FuncOrAssignStatIdnestFuncTail;
        }
        if (First_FuncOrAssignStatIdnestFuncTail.contains(lookahead.getLexeme())) {
            derive("<FuncOrAssignStatIdnestFuncTail>", new String[]{".", "id", "<FuncStatTail>"});
            boolean match1 = match(".");
            Id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            FuncStatTail = FuncStatTail(Id, FuncStatTail);
            boolean match3 = FuncStatTail.getSuccess();
            if (match1 && match2 && match3) {
                FuncOrAssignStatIdnestFuncTail = new DotNode();
                FuncOrAssignStatIdnestFuncTail.Family(".", AParams, FuncStatTail);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FuncOrAssignStatIdnestFuncTail.contains(lookahead.getLexeme())) {
            derive("<FuncOrAssignStatIdnestFuncTail>", new String[]{});
            FuncOrAssignStatIdnestFuncTail = AParams;
            success = true;
        } else {
            success = false;
        }
        FuncOrAssignStatIdnestFuncTail.setSuccess(success);
        return FuncOrAssignStatIdnestFuncTail;
    }

    private Node FuncStatTail(Node Id, Node FuncStatTail) {
        Node AParams = new FuncCallNode(), FuncStatTailIdnest = new Node(), Index = new DataMemberNode(), Ids = new IdNode(), FuncStatTails = new FuncOrAssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncStatTail, Follow_FuncStatTail)) {
            FuncStatTail.setSuccess(false);
            return FuncStatTail;
        }
        if (First_FuncStatTail.contains(lookahead.getLexeme())) {
            if (First_FuncStatTail.get(0).equals(lookahead.getLexeme())) {
                derive("<FuncStatTail>", new String[]{"(", "<AParams>", ")", "<FuncStatTailIdnest>"});
                boolean match1 = match("(");
                AParams = AParams(Id, AParams);
                boolean match2 = AParams.getSuccess();
                boolean match3 = match(")");
                FuncStatTailIdnest = FuncStatTailIdnest(AParams, FuncStatTailIdnest);
                boolean match4 = FuncStatTailIdnest.getSuccess();
                if (match1 && match2 && match3 && match4) {
                    FuncStatTail = FuncStatTailIdnest;
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<FuncStatTail>", new String[]{"<IndiceRep>", ".", "id", "<FuncStatTail>"});
                Index = IndiceRep(Id, Index);
                boolean match1 = Index.getSuccess();
                boolean match2 = match(".");
                Ids = new IdNode(lookahead);
                boolean match3 = match_type("id");
                FuncStatTails = FuncStatTail(Ids, FuncStatTails);
                boolean match4 = FuncStatTails.getSuccess();
                if (match1 && match2 && match3 && match4) {
                    Node Stat = new DotNode();
                    Stat.Family(".", Index, FuncStatTails);
                    FuncStatTail = Stat;
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        FuncStatTail.setSuccess(success);
        return FuncStatTail;
    }

    private Node FuncStatTailIdnest(Node AParams, Node FuncStatTailIdnest) {
        Node Id = new IdNode(), FuncStatTail = new FuncOrAssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncStatTailIdnest, Follow_FuncStatTailIdnest)) {
            FuncStatTailIdnest.setSuccess(false);
            return FuncStatTail;
        }
        if (First_FuncStatTailIdnest.contains(lookahead.getLexeme())) {
            derive("<FuncStatTailIdnest>", new String[]{".", "id", "<FuncStatTail>"});
            boolean match1 = match(".");
            Id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            FuncStatTail = FuncStatTail(Id, FuncStatTail);
            boolean match3 = FuncStatTail.getSuccess();
            if (match1 && match2 && match3) {
                FuncStatTailIdnest = new DotNode();
                FuncStatTailIdnest.Family(".", AParams, FuncStatTail);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FuncStatTailIdnest.contains(lookahead.getLexeme())) {
            derive("<FuncStatTailIdnest>", new String[]{});
            FuncStatTailIdnest = AParams;
            success = true;
        } else {
            success = false;
        }
        FuncStatTailIdnest.setSuccess(success);
        return FuncStatTailIdnest;
    }

    private Node FuncOrAssignStatIdnestVarTail(Node Id, Node Statement) {
        Node Ids = new IdNode(), Stat = new StatementNode(), AssignStat = new AssignStatNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrAssignStatIdnestVarTail, Follow_FuncOrAssignStatIdnestVarTail)) {
            Statement.setSuccess(false);
            return Statement;
        }
        if (First_FuncOrAssignStatIdnestVarTail.contains(lookahead.getLexeme())) {
            if (First_FuncOrAssignStatIdnestVarTail.get(0).equals(lookahead.getLexeme())) {
                boolean match1 = match(".");
                Ids = new IdNode(lookahead);
                boolean match2 = match_type("id");
                if (lookahead.getLexeme().equals("[")){
                    derive("<FuncOrAssignStatIdnestVarTail>", new String[]{".", "id", "<IndiceRep>", "<FuncOrAssignStatIdnestVarTail>"});
                    Node Index = new IndiceListNode();
                    Index = IndiceRep(Ids, Index);
                    boolean match3 = Index.getSuccess();
                    Ids = new DotNode();
                    Ids.Family(".", Id, Index);
                    Stat = FuncOrAssignStatIdnestVarTail(Ids, Stat);
                    boolean match4 = Stat.getSuccess();
                    if (match1 && match2 && match3 && match4) {
                        Statement = Stat;
                        success = true;
                    } else {
                        success = false;
                    }
                } else if (lookahead.getLexeme().equals("(")){
                    derive("<FuncOrAssignStatIdnestVarTail>", new String[]{".", "id", "(", "<AParams>", ")", "<FuncOrAssignStatIdnestFuncTail>"});
                    Node AParams = new AParamNode();
                    AParams = AParams(Ids, AParams);
                    boolean match3 = AParams.getSuccess();
                    Ids = new DotNode();
                    Ids.Family(".", Id, AParams);
                    Stat = FuncOrAssignStatIdnestFuncTail(Ids, Stat);
                    boolean match4 = Stat.getSuccess();
                    if (match1 && match2 && match3 && match4) {
                        Statement = Stat;
                        success = true;
                    } else {
                        success = false;
                    }
                } else if (lookahead.getLexeme().equals("=")){
                    derive("<FuncOrAssignStatIdnestVarTail>", new String[]{".", "id", "<AssignStatTail>"});
                    Node Assign = new AssignStatNode();
                    Node Var = new VarNode();
                    Var.Family("Var", Ids, new IndiceListNode("EPSILON"));
                    Ids = new DotNode();
                    Ids.Family(".", Id, Var);
                    Stat = AssignStatTail(Ids, Stat);
                    boolean match3 = Stat.getSuccess();
                    if (match1 && match2 && match3) {
                        Statement = Stat;
                        success = true;
                    } else {
                        success = false;
                    }
                } else {
                    success = false;
                }
            } else if (First_FuncOrAssignStatIdnestVarTail.get(1).equals(lookahead.getLexeme())) {
                derive("<FuncOrAssignStatIdnestVarTail>", new String[]{"<AssignStatTail>"});
                Stat = AssignStatTail(Id, Stat);
                boolean match1 = Stat.getSuccess();
                if (match1) {
                    AssignStat.Family("Assign Stat", Stat);
                    Statement = AssignStat;
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        Statement.setSuccess(success);
        return Statement;
    }

    private Node AssignStatTail(Node id, Node Statement) {
        Node Expr = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_AssignStatTail, Follow_AssignStatTail)) {
            Statement.setSuccess(false);
            return Statement;
        }
        if (First_AssignStatTail.contains(lookahead.getLexeme())) {
            derive("<AssignStatTail>", new String[]{"<AssignOp>", "<Expr>"});
            int location = lookahead.getLocation();
            boolean match1 = AssignOp();
            Expr = Expr(Expr);
            boolean match2 = Expr.getSuccess();
            if (match1 && match2) {
                Expr.setData("Expr");
                Statement = new AssignNode();
                Statement.Family("=", id, Expr);
                Statement.setType("Assign");
                Statement.setLocation(location);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        Statement.setSuccess(success);
        return Statement;
    }

    private Node Expr(Node Expr) {
        Node ArithExpr = new ExprNode(), ExprTail = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_Expr, Follow_Expr)) {
            Expr.setSuccess(false);
            return Expr;
        }
        if (First_Expr.contains(lookahead.getLexeme()) || First_Expr.contains(lookahead.getType())) {
            derive("<Expr>", new String[]{"<ArithExpr>", "<ExprTail>"});
            ArithExpr = ArithExpr(ArithExpr);
            boolean match1 = ArithExpr.getSuccess();
            ExprTail = ExprTail(ArithExpr, ExprTail);
            boolean match2 = ExprTail.getSuccess();
            if (match1 && match2) {
                Expr.adoptChildren(ExprTail);
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        Expr.setSuccess(success);
        return Expr;
    }


    private Node ArithExpr(Node ArithExpr) {
        Node Term = new ExprNode(), ArithExprTail = new AddOpNode();
        boolean success = true;
        if (!skipErrors(First_ArithExpr, Follow_ArithExpr)) {
            ArithExpr.setSuccess(false);
            return ArithExpr;
        }
        if (First_ArithExpr.contains(lookahead.getLexeme()) || First_ArithExpr.contains(lookahead.getType())) {
            derive("<ArithExpr>", new String[]{"<Term>", "<ArithExprTail>"});
            Term = Term(Term);
            boolean match1 = Term.getSuccess();
            ArithExprTail = ArithExprTail(Term, ArithExprTail);
            boolean match2 = ArithExprTail.getSuccess();
            if (match1 && match2) {
                ArithExpr = ArithExprTail;
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        ArithExpr.setSuccess(success);
        return ArithExpr;
    }

    private Node ArithExprTail(Node Term, Node ArithExprTail) {
        Node Terms = new ExprNode(), ArithExprTails = new AddOpNode(), Add = new AddOpNode(), AddOp = new AddOpNode();
        boolean success = true;
        if (!skipErrors(First_ArithExprTail, Follow_ArithExprTail)) {
            ArithExprTail.setSuccess(false);
            return ArithExprTail;
        }
        if (First_ArithExprTail.contains(lookahead.getLexeme())) {
            derive("<ArithExprTail>", new String[]{"<AddOp>", "<Term>", "<ArithExprTail>"});
            AddOp = new AddOpNode(lookahead.getLexeme());
            boolean match1 = AddOp();
            Terms = Term(Terms);
            boolean match2 = Terms.getSuccess();
            Add.Family(AddOp, Term, Terms);
            ArithExprTails = ArithExprTail(Add, ArithExprTails);
            boolean match3 = ArithExprTails.getSuccess();
            if (match1 && match2 && match3) {
                if (!ArithExprTails.isEmpty()) {
                    ArithExprTail = ArithExprTails;
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ArithExprTail.contains(lookahead.getLexeme())) {
            derive("<ArithExprTail>", new String[]{});
            ArithExprTail = Term;
            success = true;
        } else {
            success = false;
        }
        ArithExprTail.setSuccess(success);
        return ArithExprTail;
    }

    private Node ExprTail(Node ArithExpr, Node ExprTail) {
        Node ArithExprs = new ExprNode();
        Token RelOp = new Token();
        boolean success = true;
        if (!skipErrors(First_ExprTail, Follow_ExprTail)) {
            ExprTail.setSuccess(false);
            return ExprTail;
        }
        if (First_ExprTail.contains(lookahead.getLexeme())) {
            derive("<ExprTail>", new String[]{"<RelOp>", "<ArithExpr>"});
            RelOp = lookahead;
            boolean match1 = RelOp();
            ArithExprs = ArithExpr(ArithExprs);
            boolean match2 = ArithExprs.getSuccess();
            if (match1 && match2) {
                ExprTail = new RelOpNode();
                ExprTail.Family(RelOp, ArithExpr, ArithExprs);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_ExprTail.contains(lookahead.getLexeme())) {
            ExprTail = ArithExpr;
            derive("<ExprTail>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        ExprTail.setSuccess(success);
        return ExprTail;
    }

    private Node Term(Node Term) {
        Node Factor = new ExprNode(), TermTail = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_Term, Follow_Term)) {
            Term.setSuccess(false);
            return Term;
        }
        if (First_Term.contains(lookahead.getLexeme()) || First_Term.contains(lookahead.getType())) {
            derive("<Term>", new String[]{"<Factor>", "<TermTail>"});
            Factor = Factor(Factor);
            boolean match1 = Factor.getSuccess();
            TermTail = TermTail(Factor, TermTail);
            boolean match2 = TermTail.getSuccess();
            if (match1 && match2) {
                Term = TermTail;
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        Term.setSuccess(success);
        return Term;
    }

    private Node TermTail(Node Factor, Node Termtail) {
        Node Factors = new ExprNode(), Termtails = new MultOpNode(), MultOp = new MultOpNode(), Mult = new MultOpNode();;
        boolean success = true;
        if (!skipErrors(First_TermTail, Follow_TermTail)) {
            Termtail.setSuccess(false);
            return Termtail;
        }
        if (First_TermTail.contains(lookahead.getLexeme())) {
            derive("<TermTail>", new String[]{"<MultOp>", "<Factor>", "<TermTail>"});
            MultOp = new MultOpNode(lookahead.getLexeme());
            boolean match1 = MultOp();
            Factors = Factor(Factors);
            boolean match2 = Factor.getSuccess();
            Mult.Family(MultOp, Factor, Factors);
            Termtails = TermTail(Mult, Termtails);
            boolean match3 = Termtails.getSuccess();
            if (match1 && match2 && match3) {
                if (!Termtails.isEmpty()) {
                    Termtail = Termtails;
                }
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_TermTail.contains(lookahead.getLexeme())) {
            derive("<TermTail>", new String[]{});
            Termtail = Factor;
            success = true;
        } else {
            success = false;
        }
        Termtail.setSuccess(success);
        return Termtail;
    }

    private Node Factor(Node Factor) {
        Node Expr = new ExprNode(), Factors = new ExprNode(), FuncOrVar = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_Factor, Follow_Factor)) {
            Factor.setSuccess(false);
            return Factor;
        }
        if (First_Factor.contains(lookahead.getLexeme()) || First_Factor.contains(lookahead.getType())) {
            if (lookahead.getType().equals("intnum")) {
                derive("<Factor>", new String[]{"intnum"});
                Factor = new NumNode(lookahead);
                boolean match1 = match_type("intnum");
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getType().equals("floatnum")) {
                derive("<Factor>", new String[]{"floatnum"});
                Factor = new NumNode(lookahead);
                boolean match1 = match_type("floatnum");
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getType().equals("stringlit")) {
                derive("<Factor>", new String[]{"stringlit"});
                Factor = new Node(lookahead);
                boolean match1 = match_type("stringlit");
                if (match1) {
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getLexeme().equals("(")) {
                derive("<Factor>", new String[]{"(", "<Expr>", ")"});
                boolean match1 = match("(");
                Expr = Expr(Expr);
                boolean match2 = Expr.getSuccess();
                boolean match3 = match(")");
                if (match1 && match2 && match3) {
                    Expr.setData("Expr");
                    Factor = Expr;
                    success = true;
                } else {
                    success = false;
                }
            } else if (lookahead.getLexeme().equals("!")) {
                derive("<Factor>", new String[]{"!", "<Factor>"});
                boolean match1 = match("!");
                Factors = Factor(Factors);
                boolean match2 = Factors.getSuccess();
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
                Expr1 = Expr(Expr1);
                boolean match3 = Expr1.getSuccess();
                boolean match4 = match(":");
                Expr2 = Expr(Expr2);
                boolean match5 = Expr2.getSuccess();
                boolean match6 = match(":");
                Expr3 = Expr(Expr3);
                boolean match7 = Expr3.getSuccess();
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
                Node Sign = new SignNode(lookahead);
                boolean match1 = Sign();
                Factors = Factor(Factors);
                boolean match2 = Factors.getSuccess();
                if (match1 && match2) {
                    Factor = Sign;
                    Factor.adoptChildren(Factors);
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<Factor>", new String[]{"<FuncOrVar>"});
                FuncOrVar = FuncOrVar(FuncOrVar);
                boolean match1 = FuncOrVar.getSuccess();
                if (match1) {
                    Factor = FuncOrVar;
                    success = true;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        Factor.setSuccess(success);
        return Factor;
    }

    private Node FuncOrVar(Node FuncOrVar) {
        Node id = new IdNode(), FuncOrVarIdnest = new Node();
        boolean success = true;
        if (!skipErrors(First_FuncOrVar, Follow_FuncOrVar)) {
            FuncOrVar.setSuccess(false);
            return FuncOrVar;
        }
        if (First_FuncOrVar.contains(lookahead.getType())) {
            derive("<FuncOrVar>", new String[]{"id", "<FuncOrVarIdnest>"});
            id = new IdNode(lookahead);
            boolean match1 = match_type("id");
            FuncOrVarIdnest = FuncOrVarIdnest(id, FuncOrVarIdnest);
            boolean match2 = FuncOrVarIdnest.getSuccess();
            if (match1 && match2) {
                FuncOrVar = FuncOrVarIdnest;
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }
        FuncOrVar.setSuccess(success);
        return FuncOrVar;
    }

    private Node FuncOrVarIdnestTail(Node FuncOrVarIdnest, Node FuncOrVarIdnestTail) {
        Node id = new IdNode(), FuncOrVarIdnests = new FuncOrVarNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrVarIdnestTail, Follow_FuncOrVarIdnestTail)) {
            FuncOrVarIdnestTail.setSuccess(false);
            return FuncOrVarIdnestTail;
        }
        if (First_FuncOrVarIdnestTail.contains(lookahead.getLexeme())) {
            derive("<FuncOrVarIdnestTail>", new String[]{".", "id", "<FuncOrVarIdnest>"});
            boolean match1 = match(".");
            id = new IdNode(lookahead);
            boolean match2 = match_type("id");
            FuncOrVarIdnests = FuncOrVarIdnest(id, FuncOrVarIdnests);
            boolean match3 = FuncOrVarIdnest.getSuccess();
            if (match1 && match2 && match3) {
                FuncOrVarIdnestTail = new DotNode();
                FuncOrVarIdnestTail.Family(".", FuncOrVarIdnest, FuncOrVarIdnests);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_FuncOrVarIdnestTail.contains(lookahead.getLexeme())) {
            FuncOrVarIdnestTail = FuncOrVarIdnest;
            derive("<FuncOrVarIdnestTail>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        FuncOrVarIdnestTail.setSuccess(success);
        return FuncOrVarIdnestTail;
    }

    private Node FuncOrVarIdnest(Node id, Node FuncOrVarIdnest) {
        Node Aparams = new FuncCallNode(), Index = new DataMemberNode(), FuncOrVarIdnestTail = new FuncOrVarNode();
        boolean success = true;
        if (!skipErrors(First_FuncOrVarIdnest, Follow_FuncOrVarIdnest)) {
            FuncOrVarIdnest.setSuccess(false);
            return FuncOrVarIdnest;
        }
        if (First_FuncOrVarIdnest.contains(lookahead.getLexeme())) {
            if (lookahead.getLexeme().equals("(")) {
                derive("<FuncOrVarIdnest>", new String[]{"(", "<AParams>", ")", "<FuncOrVarIdnestTail>"});
                boolean match1 = match("(");
                Aparams = AParams(id, Aparams);
                boolean match2 = Aparams.getSuccess();
                boolean match3 = match(")");
                FuncOrVarIdnestTail = FuncOrVarIdnestTail(Aparams, FuncOrVarIdnestTail);
                boolean match4 = FuncOrVarIdnestTail.getSuccess();
                if (match1 && match2 && match3 && match4) {
                    FuncOrVarIdnest = FuncOrVarIdnestTail;
                    success = true;
                } else {
                    success = false;
                }
            } else {
                derive("<FuncOrVarIdnest>", new String[]{"<IndiceRep>", "<FuncOrVarIdnestTail>"});
                Index = IndiceRep(id, Index);
                boolean match1 = Index.getSuccess();
                FuncOrVarIdnestTail = FuncOrVarIdnestTail(Index, FuncOrVarIdnestTail);
                boolean match2 = FuncOrVarIdnestTail.getSuccess();
                if (match1 && match2) {
                    FuncOrVarIdnest = FuncOrVarIdnestTail;
                    success = true;
                } else {
                    success = false;
                }
            }
        } else if (Follow_FuncOrVarIdnest.contains(lookahead.getLexeme())) {
            Node IndiceList = new IndiceListNode();
            IndiceList.setData("EPSILON");
            FuncOrVarIdnest = new DataMemberNode();
            FuncOrVarIdnest.Family("Var", id, IndiceList);
            derive("<FuncOrVarIdnest>", new String[]{});
            success = true;
        } else {
            success = false;
        }
        FuncOrVarIdnest.setSuccess(success);
        return FuncOrVarIdnest;
    }

    private Node IndiceRep(Node id, Node Index) {
        Node Expr = new ExprNode(), Exprs = new ExprNode(), IndiceList = new IndiceListNode();
        boolean success = true;
        if (!skipErrors(First_IndiceRep, Follow_IndiceRep)) {
            Index.setSuccess(false);
            return Index;
        }
        if (First_IndiceRep.contains(lookahead.getLexeme())) {
            derive("<IndiceRep>", new String[]{"[", "<Expr>", "]", "<IndiceRep>"});
            boolean match1 = match("[");
            Expr = Expr(Expr);
            boolean match2 = Expr.getSuccess();
            boolean match3 = match("]");
            Exprs = IndiceRep(Exprs);
            boolean match4 = Exprs.getSuccess();
            if (match1 && match2 && match3 && match4) {
                if (!Exprs.isEmpty()) {
                    Expr.makeSiblings(Exprs);
                }
                Expr.setData("Expr");
                IndiceList.Family("Index List", Expr);
                Index.Family("Var", id, IndiceList);
                success = true;
            } else {
                success = false;
            }
        } else if (Follow_IndiceRep.contains(lookahead.getLexeme())) {
            derive("<IndiceRep>", new String[]{});
            if (!id.isEmpty()) {
                IndiceList.setData("EPSILON");
                Index.Family("Var", id, IndiceList);
            }
            success = true;
        } else {
            success = false;
        }
        Index.setSuccess(success);
        return Index;
    }

    private Node IndiceRep(Node Index) {
        Node Expr = new ExprNode(), Exprs = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_IndiceRep, Follow_IndiceRep)) {
            Index.setSuccess(false);
            return Index;
        }
        if (First_IndiceRep.contains(lookahead.getLexeme())) {
            derive("<IndiceRep>", new String[]{"[", "<Expr>", "]", "<IndiceRep>"});
            boolean match1 = match("[");
            Expr = Expr(Expr);
            boolean match2 = Expr.getSuccess();
            boolean match3 = match("]");
            Exprs = IndiceRep(Exprs);
            boolean match4 = Exprs.getSuccess();
            if (match1 && match2 && match3 && match4) {
                if (!Exprs.isEmpty()) {
                    Expr.makeSiblings(Exprs);
                }
                Expr.setData("Expr");
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
        Index.setSuccess(success);
        return Index;
    }

    private Node AParams(Node id, Node Aparams) {
        Node Expr = new ExprNode(), Exprs = new ExprNode(), Aparam = new AParamNode();
        boolean success = true;
        if (!skipErrors(First_AParams, Follow_AParams)) {
            Aparams.setSuccess(false);
            return Aparam;
        }
        if (First_AParams.contains(lookahead.getLexeme()) || First_AParams.contains(lookahead.getType())) {
            derive("<AParams>", new String[]{"<Expr>", "<AParamsTail>"});
            Expr = Expr(Expr);
            boolean match1 = Expr.getSuccess();
            Exprs = AParamsTail(Exprs);
            boolean match2 = Exprs.getSuccess();
            if (match1 && match2) {
                Expr.setData("Expr");
                if (!Exprs.isEmpty()) {
                    Expr.makeSiblings(Exprs);
                }
                Aparam.Family("AParams", Expr);
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
        Aparams.setSuccess(success);
        return Aparams;
    }

    private Node AParamsTail(Node Expr) {
        Node Exprs = new ExprNode();
        boolean success = true;
        if (!skipErrors(First_AParamsTail, Follow_AParamsTail)) {
            Expr.setSuccess(false);
            return Expr;
        }
        if (First_AParamsTail.contains(lookahead.getLexeme())) {
            derive("<AParamsTail>", new String[]{",", "<Expr>", "<AParamsTail>"});
            boolean match1 = match(",");
            Expr = Expr(Expr);
            boolean match2 = Expr.getSuccess();
            Exprs = AParamsTail(Exprs);
            boolean match3 = Exprs.getSuccess();
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
        Expr.setSuccess(success);
        return Expr;
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

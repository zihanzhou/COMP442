package Visitors.SymbolTable;

import AST.*;
import SymbolTable.*;
import Visitors.Visitor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymTabCreationVisitor extends Visitor {

    public String m_outputfilename = new String();
    public Integer m_tempVarNum    = 0;
    public String outSymbolTable   = "";
    public String outSemanticerrors = "";
    public List<String> FuncDeclList   = new ArrayList<String>();
    public List<String> FuncDeclClass = new ArrayList<String>();
    public List<SymTab> FuncDeclTable = new ArrayList<SymTab>();
    public List<String> FuncDefFree = new ArrayList<String>();
    public BufferedWriter out_Semanticerrors;
    public List<SymTab> MembFunc = new ArrayList<>();
    public ArrayList<String> ClassList = new ArrayList<String>(){{add("float"); add("integer"); add("string");}};
    public SymTabCreationVisitor() {
    }

    public SymTabCreationVisitor(File filename) throws IOException {
        outSymbolTable = "outsymboltables/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outsymboltables";
        outSemanticerrors = "outsemanticerrors/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outsemanticerrors";
        out_Semanticerrors = new BufferedWriter(new FileWriter(outSemanticerrors));
    }

    public void setOutErrors(BufferedWriter out) {
        out_Semanticerrors = out;
    }

    public String getNewTempVarName(){
        m_tempVarNum++;
        return "t" + m_tempVarNum.toString();
    }

    public void visit(ProgNode p_node) {
        p_node.m_symtab = new SymTab(0,"global", null);
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        try {
            BufferedWriter out_SymbolTable = new BufferedWriter(new FileWriter(outSymbolTable));
            String str = p_node.m_symtab.toString();
            out_SymbolTable.write(str);
            out_SymbolTable.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void visit(AddOpNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(AParamNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(AssignStatNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ClassListNode p_node) {
        for (Node child : p_node.getChildren()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }


    public void visit(ClassNode p_node) {
        String classname = p_node.getChildren().get(0).getData();
        ClassList.add(classname);
        SymTab localtable = new SymTab(1, classname, p_node.m_symtab);
        ClassEntry classEntry = new ClassEntry(classname, localtable);
        Node InheritList = p_node.getChildren().get(1);
        List<String> shadowedVar = new ArrayList<>();
        for (Node child : InheritList.getChildren()){
            if (!child.getData().equals("EPSILON")){
                SymTabEntry tab = p_node.m_symtab.lookupName(child.getData());
                classEntry.addInherits(tab.m_subtable);
                if (tab.m_subtable != null) {
                    for (int i = 0; i < tab.m_subtable.m_symlist.size(); i++) {
                        if (tab.m_subtable.m_symlist.get(i).m_kind.equals("data")) {
                            shadowedVar.add(tab.m_subtable.m_symlist.get(i).m_name);
                        }
                    }
                }
            }
        }
        p_node.m_symtabentry = classEntry;
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        for (Node child : p_node.getChildren()){
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        Node MembList = p_node.getChildren().get(2);
        List<String> VarName = new ArrayList<>();
        List<String> FuncName = new ArrayList<>();
        List<List<String>> FuncParamList = new ArrayList<>();
        for (Node child : MembList.getChildren()){
            if (child.getData().equals("EPSILON")){
                break;
            }
            String DeclName = child.getChildren().get(1).getData();
            String DeclType = child.getData();
            if (DeclType.equals("Var Decl")) {
                if (VarName.contains(DeclName)) {
                    try {
                        out_Semanticerrors.write("multiply declared identifier in class " + classname + " variable " + DeclName + " at line : " + child.getChildren().get(1).getLocation());
                        out_Semanticerrors.flush();
                        out_Semanticerrors.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (shadowedVar.contains(DeclName)){
                    try {
                        out_Semanticerrors.write("shadowed inherited data member in class " + classname + " variable " + DeclName + " at line : " + child.getChildren().get(1).getLocation());
                        out_Semanticerrors.flush();
                        out_Semanticerrors.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    VarName.add(DeclName);
                }
            } else {
                List<String> FuncParam = new ArrayList<>();
                for (Node Param : child.getChildren().get(2).getChildren()){
                    FuncParam.add(Param.getChildren().get(0).getData());
                }
                if (FuncName.contains(DeclName)) {
                    if (!FuncParam.equals(FuncParamList.get(FuncName.indexOf(DeclName)))) {
                        try {
                            out_Semanticerrors.write("Overloaded member function in " + classname + "::" + DeclName + " at line : " + child.getChildren().get(1).getLocation());
                            out_Semanticerrors.flush();
                            out_Semanticerrors.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FuncParamList.add(FuncParam);
                        FuncName.add(DeclName);

                    } else {
                        try {
                            out_Semanticerrors.write("multiply declared member function in " + classname + "::" + DeclName + " at line : " + child.getChildren().get(1).getLocation());
                            out_Semanticerrors.flush();
                            out_Semanticerrors.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    FuncParamList.add(FuncParam);
                    FuncName.add(DeclName);

                }
            }
        }

    }

    public void visit(DataMemberNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(DimListNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(DimNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    public void visit(ExprNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(FuncCallNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(FuncDeclNode p_node) {
        String type = p_node.getChildren().get(0).getData();
        String id = p_node.getChildren().get(1).getData();
        String classname = p_node.m_symtab.m_name;
        FuncDeclList.add(id);
        FuncDeclClass.add(classname);
        SymTab localtable = new SymTab(2,classname + "::" + id, p_node.m_symtab);
        MembFunc.add(localtable);
        ArrayList<VarEntry> ParamList = new ArrayList<VarEntry>();
        for (Node child : p_node.getChildren().get(2).getChildren()){
            if (child.getData().equals("EPSILON")){
                break;
            } else {
                ArrayList<Integer> DimList = new ArrayList<>();
                if (!child.getChildren().get(2).getChildren().get(0).getData().equals("EPSILON")){
                    for (Node dim : child.getChildren().get(2).getChildren()){
                        DimList.add(Integer.parseInt( dim.getData()));
                    }
                }
                if (DimList.isEmpty()) {
                    DimList = null;
                }
                VarEntry Param = new VarEntry("param", child.getChildren().get(0).getData(), child.getChildren().get(1).getData(), DimList, "");
                ParamList.add(Param);
            }
        }
        String visibility = p_node.getChildren().get(3).getData();
        p_node.m_symtabentry = new FuncEntry(type, id, ParamList, visibility, localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        for (Node child : p_node.getChildren()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }


    public void visit(FuncDefListNode p_node) {
        List<List<String>> FuncFreeParam = new ArrayList<>();

        for (Node child : p_node.getChildren() ) {

            if (child.getData().equals("EPSILON")){
                break;
            }
            String classname = child.getChildren().get(1).getChildren().get(0).getData();
            String funcname = child.getChildren().get(2).getData();
            if (classname.equals("EPSILON")){
                List<String> FuncParam = new ArrayList<>();

                for (Node Param : child.getChildren().get(3).getChildren()) {
                    if (Param.getData().equals("EPSILON")) {
                        break;
                    }
                    FuncParam.add(Param.getChildren().get(0).getData());
                }
                if (!FuncDefFree.contains(funcname)){
                    FuncDefFree.add(funcname);
                    FuncFreeParam.add(FuncParam);
                    child.m_symtab = p_node.m_symtab;
                    child.accept(this);
                } else {
                    int index = FuncDefFree.indexOf(funcname);
                    if (FuncParam.equals(FuncFreeParam.get(index))){
                        try {
                            out_Semanticerrors.write("multiply defined free function: " + funcname + " at line : " + child.getChildren().get(2).getLocation());
                            out_Semanticerrors.flush();
                            out_Semanticerrors.newLine();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            out_Semanticerrors.write("Overloaded free function: " + funcname + " at line : " + child.getChildren().get(2).getLocation());
                            out_Semanticerrors.flush();
                            out_Semanticerrors.newLine();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                        FuncFreeParam.add(FuncParam);
                        FuncDefFree.add(funcname);
                    }
                }
            } else {
                boolean isSeen = false;
                for (int i = 0; i < FuncDeclList.size(); i ++){
                    if (FuncDeclList.get(i).equals(funcname) && FuncDeclClass.get(i).equals(classname)){
                        isSeen = true;
                        FuncDeclList.remove(i);
                        FuncDeclClass.remove(i);
                        break;
                    }
                }
                if (!isSeen){
                    try {
                        out_Semanticerrors.write("undeclared member function definition: " + classname + "::" + funcname + " at line : " + child.getChildren().get(2).getLocation());
                        out_Semanticerrors.flush();
                        out_Semanticerrors.newLine();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!FuncDeclList.isEmpty()){
            for (int i = 0; i < FuncDeclList.size(); i ++){
                try {
                    out_Semanticerrors.write("undefined member function definition: " + FuncDeclClass.get(i) + "::" + FuncDeclList.get(i));
                    out_Semanticerrors.flush();
                    out_Semanticerrors.newLine();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        for (Node child : p_node.getChildren()){
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(FuncOrAssignStatNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(FuncOrVarDeclNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(FuncOrVarNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(IdNode p_node) {
        for (Node child : p_node.getChildren()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    public void visit(IfStatNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(IndiceListNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(InheritListNode p_node) {
        for (Node child : p_node.getChildren()) {
            p_node.m_symtabentry = new InheritEntry(child.getData());
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(MembDeclNode p_node) {
        for (Node child : p_node.getChildren()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(MemberListNode p_node) {
        for (Node child : p_node.getChildren()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(MultOpNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(NestedIdNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ParamListNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ParamNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }


    public void visit(ProgramBlockNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ReadStatNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ReturnStatNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ScopeSpecNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(StatBlockNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(StatementNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(TypeNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(VarDeclNode p_node) {
        String type = p_node.getChildren().get(0).getData();
        String id = p_node.getChildren().get(1).getData();
        SymTab localtable = new SymTab(2,id, p_node.m_symtab);
        ArrayList<Integer> dimList = new ArrayList<>();
        for (Node Dim : p_node.getChildren().get(2).getChildren()){
            if (Dim.getData().equals("EPSILON")){
                break;
            }else {
                if (Dim.getData().equals("empty")){
                    dimList.add(0);
                } else {
                    Integer dimval = Integer.parseInt(Dim.getData());
                    dimList.add(dimval);
                }
            }
        }
        String kind = "";
        if (p_node.m_symtab.m_tablelevel == 1) {
            kind = "data";
        } else {
            kind = "local";
        }
        if (dimList.isEmpty()) {
            dimList = null;
        }
        String visibility = new String();
        if (p_node.getChildren().size() == 4) {
            visibility = p_node.getChildren().get(3).getData();
        }
        p_node.m_symtabentry = new VarEntry(kind, type, id, dimList, visibility);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        for (Node child : p_node.getChildren()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(VarNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(VisibilityNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(WhileStatNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(WriteStatNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(FuncDefNode p_node) {
        String type = p_node.getChildren().get(0).getData();
        String scopespec = p_node.getChildren().get(1).getChildren().get(0).getData();
        String scopeFunc = "";
        if (scopespec.equals("EPSILON")) {
            scopeFunc = "";
        } else {
             scopeFunc = scopespec;
        }
        String id = p_node.getChildren().get(2).getData();
        ArrayList<VarEntry> ParamList = new ArrayList<VarEntry>();
        SymTab uppertable = null;
        if (!scopeFunc.equals("")) {
            for (SymTab table : MembFunc){
                if (table.m_name.equals(scopeFunc + "::" + id)){
                    uppertable = table;
                    MembFunc.remove(MembFunc.indexOf(table));
                    break;
                }
            }
        } else {
            uppertable = p_node.m_symtab;
        }
        SymTab localtable = new SymTab(2, scopeFunc + "::" + id, uppertable);
        for (Node child : p_node.getChildren().get(3).getChildren()){
            if (child.getData().equals("EPSILON")){
                break;
            } else {
                String Param_type = child.getChildren().get(0).getData();
                String Param_id = child.getChildren().get(1).getData();
                ArrayList<Integer> DimList = new ArrayList<>();
                if (!child.getChildren().get(2).getChildren().get(0).getData().equals("EPSILON")){
                    for (Node dim : child.getChildren().get(2).getChildren()){

                        if (dim.getData().equals("empty")){
                            DimList.add(0);
                        }else {
                            DimList.add(Integer.parseInt(dim.getData()));
                        }
                    }
                }
                if (DimList.isEmpty()) {
                    DimList = null;
                }
                VarEntry Param = new VarEntry("param", Param_type, Param_id, DimList, "");
                localtable.addEntry(Param);
                ParamList.add(Param);
            }
        }
        p_node.m_symtabentry = new FuncEntry(type, id, ParamList, "", localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(Node p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(VarDeclListNode p_node) {
        List<String> VarList = new ArrayList<>();
        for (Node child : p_node.getChildren()){
            if (child.getData().equals("EPSILON")){
                break;
            }
            String VarName = child.getChildren().get(1).getData();
            String VarClass = child.getChildren().get(0).getData();
            if (VarList.contains(VarName)) {
                try {
                    out_Semanticerrors.write("multiply declared identifier " + VarName +  " in function at line : " + child.getChildren().get(1).getLocation());
                    out_Semanticerrors.flush();
                    out_Semanticerrors.newLine();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                VarList.add(VarName);
            }
            boolean isContains = false;
            for (String Classes : ClassList){
                if (Classes.equalsIgnoreCase(VarClass)){
                    isContains = true;
                    break;
                }
            }
            if (!isContains){

                try {
                    out_Semanticerrors.write("Undeclared class " + VarClass +  " in function at line : " + child.getChildren().get(1).getLocation());
                    out_Semanticerrors.flush();
                    out_Semanticerrors.newLine();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Node child : p_node.getChildren()){
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(MainBlockNode p_node) {
        SymTab localtable = new SymTab(2,  "::main", p_node.m_symtab);
        p_node.m_symtabentry = new FuncEntry("", "main", new ArrayList<>(), "", localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(AssignNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(SignNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(RelOpNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(DotNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(NumNode p_node) {
        for (Node child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }
}

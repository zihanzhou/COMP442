package Visitors.SymbolTable;

import AST.*;
import SymbolTable.FuncEntry;
import SymbolTable.SymTab;
import SymbolTable.SymTabEntry;
import SymbolTable.VarEntry;
import Visitors.Visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeCheckingVisitor extends Visitor {

    public String outErrors = new String();
    public String outErrorMessage    = "";
    public BufferedWriter out_Semanticerrors;
    public TypeCheckingVisitor() {

    }

    public boolean typeCompare(String Left, String Right){

        if (Left.equalsIgnoreCase(Right)){
            return true;
        } else if ((Left.equalsIgnoreCase("intnum")) && (Right.equalsIgnoreCase("integer"))){
            return true;
        } else if ((Left.equalsIgnoreCase("integer")) && (Right.equalsIgnoreCase("intnum"))){
            return true;
        } else if ((Left.equalsIgnoreCase("floatnum")) && (Right.equalsIgnoreCase("float"))){
            return true;
        } else if ((Left.equalsIgnoreCase("float")) && (Right.equalsIgnoreCase("floatnum"))){
            return true;
        } else  if ((Left.equalsIgnoreCase("string")) && (Right.equalsIgnoreCase("stringlit"))){
            return true;
        } else  if ((Left.equalsIgnoreCase("stringlit")) && (Right.equalsIgnoreCase("string"))){
            return true;
        } else {
            return false;
        }
    }
    public TypeCheckingVisitor(File filename) throws IOException {
        outErrors = "outsemanticerrors/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outsemanticerrors";
        out_Semanticerrors = new BufferedWriter(new FileWriter(outErrors));
    }

    public void setOutErrors(BufferedWriter out) {
        out_Semanticerrors = out;
    }

    public void visit(AddOpNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);

        String Left = p_node.getChildren().get(0).getType();
        String Right = p_node.getChildren().get(1).getType();
        if (!typeCompare(Left, Right)){
            try {
                out_Semanticerrors.write("Type error in expression in line " + p_node.getChildren().get(0).getLocation());
                out_Semanticerrors.flush();
                out_Semanticerrors.newLine();
            } catch (IOException e){
                e.printStackTrace();
            }
            p_node.setType("Type Error");
        } else {
            p_node.setType(Left);
        }
    }

    public void visit(AParamNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(AssignStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ClassListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ClassNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(DataMemberNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.setType(p_node.getChildren().get(0).getType());
        int arraySize = 0;
        for (Node child : p_node.getChildren().get(1).getChildren()){
            if (!child.getData().equals("EPSILON")){
                arraySize++;
            }
        }
        String lookupResult = new String();
        int dimension = 0;
        if (p_node.m_symtab.lookupName(p_node.getChildren().get(0).getData()).m_dims == null){
            dimension = 0;
        } else {
            lookupResult = p_node.m_symtab.lookupName(p_node.getChildren().get(0).getData()).m_name;
            if (lookupResult == null){
                if (p_node.parent.getData().equals(".") && (p_node.parent.getChildren().get(0) != p_node)){
                    Node parent = p_node.getParent();
                    String classname = parent.getChildren().get(0).getType();
                    SymTabEntry classToLookUp = p_node.m_symtab.lookupName(classname);
                    if (classToLookUp.isEmpty){
                        dimension = -1;
                    } else {
                        dimension = p_node.m_symtab.lookupName(classToLookUp.m_subtable, p_node.getChildren().get(0).getData()).m_dims.size();
                    }
                } else
                    dimension = -1;
            } else
                dimension = p_node.m_symtab.lookupName(p_node.getChildren().get(0).getData()).m_dims.size();
        }
        if (arraySize == 0){
            arraySize = dimension;
        }
        if (arraySize != dimension){

            try {
                out_Semanticerrors.write("Use of array with wrong number of dimensions : " + p_node.getChildren().get(0).getData() + " at line " + p_node.getChildren().get(0).getLocation());
                out_Semanticerrors.flush();
                out_Semanticerrors.newLine();
            } catch (IOException e){
                e.printStackTrace();
            }
        }


    }

    public void visit(DimListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(DimNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);

    }

    public void visit(ExprNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.setType(p_node.getChildren().get(0).getType());
    }

    public void visit(FuncCallNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String FuncName = p_node.getChildren().get(0).getData();
        int paramSize = 0;
        List<String> paramType = new ArrayList<>();
        for (Node child : p_node.getChildren().get(1).getChildren()){
            if (!child.getData().equals("EPSILON")){
                paramSize++;
                paramType.add(child.getChildren().get(0).getType());
            } else {
                break;
            }
        }
        SymTabEntry entry = p_node.m_symtab.lookupName(FuncName, paramSize);
        if (entry.isEmpty){
            SymTabEntry entry_FuncName = p_node.m_symtab.lookupName(FuncName);
            if (entry_FuncName.isEmpty){
                p_node.setType("Undeclared");
            } else {
                p_node.setType("Wrong number");
            }
        } else {
            FuncEntry funcEntry = (FuncEntry)entry;
            boolean isSameType = true;
            for (VarEntry var : entry.m_params){
                if (!typeCompare(var.m_type, paramType.get(entry.m_params.indexOf(var)))){
                    isSameType = false;
                    break;
                }
            }
            if (isSameType == false){
                p_node.setType("Wrong type");
                try {
                    out_Semanticerrors.write("Function call with wrong type of parameters in line " + p_node.getChildren().get(0).getLocation());
                    out_Semanticerrors.flush();
                    out_Semanticerrors.newLine();
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                List<Integer> ParamDim = new ArrayList<>();
                int i = 0;
                for (Node child : p_node.getChildren().get(1).getChildren()){
                    if (child.getData().equals("EPSILON")){
                        break;
                    }
                    if (child.getChildren().get(0).getData().equals("Var")){
                        int arraySize = 0;
                        if (child.getChildren().get(0).getChildren().get(1).haveChildren()) {
                            for (Node dim : child.getChildren().get(0).getChildren().get(1).getChildren()) {
                                if (!dim.getData().equals("EPSILON")){
                                    arraySize ++;
                                }
                            }
                        } else {
                            arraySize = 0;
                        }
                        if (arraySize == 0){
                            List<Integer> var = p_node.m_symtab.lookupName(child.getChildren().get(0).getChildren().get(0).getData()).m_dims;
                            if (var != null){
                                arraySize = var.size();
                            } else {
                                arraySize = -1;
                            }
                        }
                        if (!entry.m_params.isEmpty()) {
                            if ((entry.m_params.get(i).m_dims != null)) {
                                if (arraySize != entry.m_params.get(i).m_dims.size()) {
                                    try {
                                        out_Semanticerrors.write("Array parameter using wrong number of dimensions " + p_node.getChildren().get(0).getLocation());
                                        out_Semanticerrors.flush();
                                        out_Semanticerrors.newLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (arraySize != -1){
                                try {
                                    out_Semanticerrors.write("Array parameter using wrong number of dimensions " + p_node.getChildren().get(0).getLocation());
                                    out_Semanticerrors.flush();
                                    out_Semanticerrors.newLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        i++;
                    }
                }
                p_node.setType(entry.m_type);
            }
        }
    }

    public void visit(FuncDeclNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(FuncDefListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(FuncOrAssignStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(FuncOrVarDeclNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(FuncOrVarNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.setType(p_node.getChildren().get(0).getType());
    }

    public void visit(IdNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        if (!p_node.m_symtab.lookupName(p_node.getData()).isEmpty) {
            p_node.setType(p_node.m_symtab.lookupName(p_node.getData()).m_type);
        } else if (p_node.getType().equals("") || p_node.getType().equals("id")){
            p_node.setType("Undeclared");
        }
    }

    public void visit(IfStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(IndiceListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        for (Node child : p_node.getChildren() ){
            if (!(child.getType().equals("intnum") || child.getType().equals("integer"))){
                try {
                    out_Semanticerrors.write("Array index is not an integer in line " + child.getChildren().get(0).getLocation());
                    out_Semanticerrors.flush();
                    out_Semanticerrors.newLine();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public void visit(InheritListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(MembDeclNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(MemberListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(MultOpNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String Left = p_node.getChildren().get(0).getType();
        String Right = p_node.getChildren().get(1).getType();
        if (!typeCompare(Left, Right)){
            try {
                out_Semanticerrors.write("Type error in expression in line " + p_node.getChildren().get(0).getLocation());
                out_Semanticerrors.flush();
                out_Semanticerrors.newLine();
            } catch (IOException e){
                e.printStackTrace();
            }
            p_node.setType("Type Error");
        } else {
            p_node.setType(Left);
        }

    }

    public void visit(NestedIdNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ParamListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ParamNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ProgNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ProgramBlockNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ReadStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ReturnStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(ScopeSpecNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(StatBlockNode p_node) {

        for (Node child : p_node.getChildren() )
            child.accept(this);
        for (Node child : p_node.getChildren() ) {
            if (child.getData().equals("Fcall") && child.getType().equals("Undeclared")){
                try {
                    out_Semanticerrors.write("Undeclared free function " + child.getChildren().get(0).getData() + " in line " + child.getChildren().get(0).getLocation());
                    out_Semanticerrors.flush();
                    out_Semanticerrors.newLine();
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else if (child.getData().equals("Fcall") && child.getType().equals("Wrong number")){
                try {
                    out_Semanticerrors.write("Function call with wrong number of parameters " + child.getChildren().get(0).getData() + " in line " + child.getChildren().get(0).getLocation());
                    out_Semanticerrors.flush();
                    out_Semanticerrors.newLine();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void visit(StatementNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(TypeNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(VarDeclNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.setType(p_node.getChildren().get(0).getData());
    }

    public void visit(VarNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.setType(p_node.getChildren().get(0).getType());
    }

    public void visit(VisibilityNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(WhileStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(WriteStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(FuncDefNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(Node p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(VarDeclListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(MainBlockNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(AssignNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String Left = p_node.getChildren().get(0).getType();
        String Right = p_node.getChildren().get(1).getType();
        if (Left.equals("Undeclared")){
            try {
                out_Semanticerrors.write("Undeclared local variable " + p_node.getLocation());
                out_Semanticerrors.flush();
                out_Semanticerrors.newLine();
            } catch (IOException e){
                e.printStackTrace();
            }
        } else if (!typeCompare(Left, Right)){
            try {
                out_Semanticerrors.write("Type error in assignment statement in line " + p_node.getLocation());
                out_Semanticerrors.flush();
                out_Semanticerrors.newLine();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void visit(SignNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.setType(p_node.getChildren().get(0).getType());
    }

    public void visit(RelOpNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String Left = p_node.getChildren().get(0).getType();
        String Right = p_node.getChildren().get(1).getType();
        if (!typeCompare(Left, Right)){
            try {
                out_Semanticerrors.write("Type error in expression in line " + p_node.getChildren().get(0).getLocation());
                out_Semanticerrors.flush();
                out_Semanticerrors.newLine();
            } catch (IOException e){
                e.printStackTrace();
            }
            p_node.setType("Type Error");
        } else {
            p_node.setType(Left);
        }
    }

    public void visit(DotNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);

        String Left = p_node.getChildren().get(0).getType();
        String Right = p_node.getChildren().get(1).getType();
        if (Right.equals("id")){

            SymTabEntry entry = p_node.m_symtab.lookupName(Left);
            if (!entry.isEmpty) {
                SymTabEntry toLookup = p_node.m_symtab.lookupName(entry.m_subtable, p_node.getChildren().get(1).getChildren().get(0).getData());
                if (toLookup.isEmpty) {
                    try {
                        out_Semanticerrors.write("Undeclared data member " + p_node.getChildren().get(1).getChildren().get(0).getData() + " in line " + p_node.getChildren().get(1).getChildren().get(0).getLocation());
                        out_Semanticerrors.flush();
                        out_Semanticerrors.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p_node.setType("Undeclared data member");
                    p_node.getChildren().get(1).setType("Undeclared data member");
                } else {
                    p_node.getChildren().get(1).setType(toLookup.m_type);
                    if (p_node.getChildren().get(1).getData().equals("Var")) {
                        p_node.getChildren().get(1).getChildren().get(0).setType(toLookup.m_type);
                    }
                }
            }
        }
        if (Left.equalsIgnoreCase("integer") || Left.equalsIgnoreCase("float") || Left.equalsIgnoreCase("string")){
            try {
                out_Semanticerrors.write("\".\" operator used on non-class type in line " + p_node.getChildren().get(0).getChildren().get(0).getData() + " in line " + p_node.getChildren().get(0).getChildren().get(0).getLocation());
                out_Semanticerrors.flush();
                out_Semanticerrors.newLine();
                p_node.setType("dot operator used on non-class type in line");
            } catch (IOException e){
                e.printStackTrace();
            }
        } else if (Right.equals("Wrong number")){
            SymTab Left_table = p_node.m_symtab.lookupName(Left).m_subtable;
            if (Left_table != null){
                SymTabEntry entry = Left_table.lookupName(Left_table, Right);
                if (entry.isEmpty){
                    try {
                        out_Semanticerrors.write("Undeclared member function " +  p_node.getChildren().get(1).getChildren().get(0).getData() + " in line " + p_node.getChildren().get(1).getChildren().get(0).getLocation());
                        out_Semanticerrors.flush();
                        out_Semanticerrors.newLine();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    p_node.setType("Undeclared member function");
                }
            }
        } else {
            SymTab Left_table = p_node.m_symtab.lookupName(Left).m_subtable;
            if (Left_table != null){
                if (p_node.getChildren().get(1).getData().equals("Var")) {
                    String Right_Var = p_node.getChildren().get(1).getChildren().get(0).getData();
                    SymTabEntry entry = Left_table.lookupName(Left_table, Right_Var);
                    if (entry.isEmpty) {
                        try {
                            out_Semanticerrors.write("Undeclared data member " + p_node.getChildren().get(1).getChildren().get(0).getData() + " in line " + p_node.getChildren().get(1).getChildren().get(0).getLocation());
                            out_Semanticerrors.flush();
                            out_Semanticerrors.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p_node.setType("Undeclared data membe");
                    } else {
                        p_node.setType(p_node.getChildren().get(1).getType());
                    }
                } else {
                    p_node.setType(p_node.getChildren().get(1).getType());
                }
            } else {
                p_node.setType(p_node.getChildren().get(1).getType());
            }
        }

    }

    public void visit(NumNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }
}

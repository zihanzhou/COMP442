package Visitors;

import AST.*;
import SymbolTable.SymTabEntry;
import SymbolTable.VarEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ComputeMemSizeVisitor extends Visitor{

    public String outMemSize   = "";
    public String  m_outputfilename = new String();
    public Integer m_tempVarNum    = 0;

    public ComputeMemSizeVisitor() {
    }

    public String getNewTempVarName(){
        m_tempVarNum++;
        return "t" + m_tempVarNum.toString();
    }

    public ComputeMemSizeVisitor(File filename) {
        outMemSize = "outmemsize/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outmemsize";
    }

    public int sizeOfEntry(Node p_node) {
        int size = 0;
        if(p_node.m_symtabentry.m_type.equals("integer") || p_node.m_symtabentry.m_type.equals("intnum"))
            size = 4;
        else if(p_node.m_symtabentry.m_type.equals("float") || p_node.m_symtabentry.m_type.equals("floatnum"))
            size = 8;
        else {
            size = - p_node.m_symtab.lookupName(p_node.getType()).m_subtable.m_size;
        }
        // if it is an array, multiply by all dimension sizes
        VarEntry ve = (VarEntry) p_node.m_symtabentry;
        if(ve.m_dims != null)
            for(Integer dim : ve.m_dims)
                size *= dim;
        return size;
    }

    public int sizeOfTypeNode(Node p_node) {
        int size = 0;
        if(p_node.data.equals("integer") || p_node.data.equals("intnum"))
            size = 4;
        else if(p_node.data.equals("float") || p_node.data.equals("floatnum"))
            size = 8;
        else if (p_node.data.equals("void")) {
            size = 0;
        } else {
            size = - p_node.m_symtab.lookupName(p_node.getData()).m_subtable.m_size;
        }
        return size;
    }

    public void visit(AddOpNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);

        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.m_symtabentry = new VarEntry("tempvar", p_node.getType(), p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims, "");
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
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

        for (SymTabEntry entry : p_node.m_symtab.m_symlist){
            entry.m_offset = p_node.m_symtab.m_size - entry.m_size;
            p_node.m_symtab.m_size -= entry.m_size;
        }
        if (p_node.m_symtab.m_size == 0){
            p_node.m_symtab.m_size -= 4;
        }
    }

    public void visit(DataMemberNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.m_moonVarName = p_node.getChildren().get(0).getData();
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
        p_node.m_moonVarName = p_node.getChildren().get(0).m_moonVarName;
    }

    public void visit(FuncCallNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        String vartype = p_node.getType();
        p_node.m_symtabentry = new VarEntry("retval", vartype, p_node.m_moonVarName, new ArrayList<>(), "");
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
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
    }

    public void visit(IdNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        p_node.m_moonVarName = p_node.getData();
    }

    public void visit(IfStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(IndiceListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(InheritListNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        for (Node child : p_node.getChildren()){
            if (!child.getData().equals("EPSILON")) {
                p_node.m_symtabentry.m_size -= p_node.m_symtab.lookupName(child.getData()).m_subtable.m_size;
            }
        }
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

        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        String vartype = p_node.getType();
        p_node.m_symtabentry = new VarEntry("tempvar", vartype, p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims, "");
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
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
        for (SymTabEntry entry : p_node.m_symtab.m_symlist){
            entry.m_offset     = p_node.m_symtab.m_size - entry.m_size;
            p_node.m_symtab.m_size -= entry.m_size;
        }
        try {
            BufferedWriter out_SymbolTable = new BufferedWriter(new FileWriter(outMemSize));
            String str = p_node.m_symtab.toString();
            out_SymbolTable.write(str);
            out_SymbolTable.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
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
        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
    }

    public void visit(VarNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
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
        p_node.m_symtab.m_size = -(this.sizeOfTypeNode(p_node.getChildren().get(0)));
        p_node.m_symtab.m_size -= 4;
        for (SymTabEntry entry : p_node.m_symtab.m_symlist){
            if (entry.m_kind.equals("param")){
                int size = 0;
                if(entry.m_type.equals("integer") || entry.m_type.equals("intnum"))
                    size = 4;
                else if(entry.m_type.equals("float") || entry.m_type.equals("floatnum"))
                    size = 8;
                VarEntry ve = (VarEntry) entry;
                if(ve.m_dims != null){
                    for(Integer dim : ve.m_dims)
                        size *= dim;
                }
                entry.m_size = size;
            }
            entry.m_offset = p_node.m_symtab.m_size - entry.m_size;
            p_node.m_symtab.m_size -= entry.m_size;
        }
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

        p_node.m_symtab.m_size = 0;
        for (SymTabEntry entry : p_node.m_symtab.m_symlist){
            if (entry.m_kind.equals("param")){
                int size = 0;
                if(entry.m_type.equals("integer") || entry.m_type.equals("intnum"))
                    size = 4;
                else if(entry.m_type.equals("float") || entry.m_type.equals("floatnum"))
                    size = 8;
                VarEntry ve = (VarEntry) entry;
                if(ve.m_dims != null){
                    for(Integer dim : ve.m_dims)
                        size *= dim;
                }
                entry.m_size = size;
            }
            entry.m_offset = p_node.m_symtab.m_size - entry.m_size;
            p_node.m_symtab.m_size -= entry.m_size;
        }

    }

    public void visit(AssignNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(SignNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(RelOpNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.m_symtabentry = new VarEntry("tempvar", p_node.getType(), p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims, "");
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
    }

    public void visit(DotNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(NumNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        String vartype = p_node.getType();
        p_node.m_symtabentry = new VarEntry("litval", vartype, p_node.m_moonVarName, new ArrayList<Integer>(), "");
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);

    }
}

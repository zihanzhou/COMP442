package Visitors;

import AST.*;
import SymbolTable.SymTabEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TagsBasedCodeGenerationVisitor extends Visitor{

    public Stack<String> m_registerPool   = new Stack<String>();
    public Integer       m_tempVarNum     = 0;
    public String        m_moonExecCode   = new String();               // moon code instructions part
    public String        m_moonDataCode   = new String();               // moon code data part
    public String        m_mooncodeindent = new String("           ");
    public String        outMoon = new String();
    public BufferedWriter out_Moon;
    public TagsBasedCodeGenerationVisitor() {
        // create a pool of registers as a stack of Strings
        // assuming only r1, ..., r12 are available
        for (Integer i = 12; i>=1; i--)
            m_registerPool.push("r" + i.toString());
    }



    public TagsBasedCodeGenerationVisitor(File filename) throws IOException {
        outMoon = "outmoon/" + filename.getName().substring(0, filename.getName().length() - 4) + ".moon";

        for (Integer i = 14; i>=1; i--)
            m_registerPool.push("r" + i.toString());
    }

    public int sizeOfTypeNode(Node p_node) {
        int size = 0;
        if(p_node.type.equals("integer") || p_node.type.equals("intnum"))
            size = 4;
        else if(p_node.type.equals("float") || p_node.type.equals("floatnum"))
            size = 8;
        else if (p_node.type.equals("void")) {
            size = 0;
        } else {
            size = - p_node.m_symtab.lookupName(p_node.getType()).m_subtable.m_size;
        }
        return size;
    }

    public void visit(AddOpNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String localRegister      = this.m_registerPool.pop();
        String leftChildRegister  = this.m_registerPool.pop();
        String rightChildRegister = this.m_registerPool.pop();
        String op = "";
        if (p_node.getData().equals("+")){
            op = "add ";
        } else if (p_node.getData().equals("-")){
            op = "sub ";
        } else {
            op = "or ";
        }
        int size = p_node.m_symtabentry.m_size;
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + p_node.getChildren().get(0).m_moonVarName + " "+ p_node.getData() +" " + p_node.getChildren().get(1).m_moonVarName + "\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister +  "," + p_node.getChildren().get(0).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + p_node.getChildren().get(1).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + op + localRegister +      "," + leftChildRegister + "," + rightChildRegister + "\n";
        m_moonDataCode += m_mooncodeindent + "% space for " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(1).m_moonVarName + "\n";
        m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res " + size + "\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";

        this.m_registerPool.push(leftChildRegister);
        this.m_registerPool.push(rightChildRegister);
        this.m_registerPool.push(localRegister);
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
        String localRegister = this.m_registerPool.pop();

        for (Node child : p_node.getChildren() )
            child.accept(this);

        this.m_registerPool.push(localRegister);
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
    }

    public void visit(FuncCallNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);

        String localregister1 = this.m_registerPool.pop();
        SymTabEntry tableentryofcalledfunction = p_node.m_symtab.lookupName(p_node.getChildren().get(0).getData());
        int indexofparam = 0;
        m_moonExecCode += m_mooncodeindent + "% processing: function call to "  + p_node.getChildren().get(0).m_moonVarName + " \n";
        for(Node param : p_node.getChildren().get(1).getChildren()){
            m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + param.m_moonVarName + "(r0)\n";
            String nameofparam = tableentryofcalledfunction.m_subtable.m_symlist.get(indexofparam).m_name;
            m_moonExecCode += m_mooncodeindent + "sw " + nameofparam + "(r0)," + localregister1 + "\n";
            indexofparam++;
        }

        m_moonExecCode += m_mooncodeindent + "jl r15," + p_node.getData() + "\n";

        m_moonDataCode += m_mooncodeindent + "% space for function call expression factor\n";
        m_moonDataCode += String.format("%-11s", p_node.m_moonVarName) + "res 4\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.getData() + "return(r0)\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localregister1 + "\n";
        this.m_registerPool.push(localregister1);
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
    }

    public void visit(IfStatNode p_node) {
        Node child = p_node.getChildren().get(0);
        child.accept(this);
        String reg = this.m_registerPool.pop();
        m_moonExecCode += m_mooncodeindent + "lw " + reg + "," + child.m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "bz " + reg + ",else1\n";
        child = p_node.getChildren().get(1);
        child.accept(this);
        m_moonExecCode += m_mooncodeindent + "j endif1";
        m_moonExecCode += "else1";
        child = p_node.getChildren().get(2);
        child.accept(this);
        m_moonExecCode += "endif1";
        this.m_registerPool.push(reg);
    }

    public void visit(IndiceListNode p_node) {
        if (!p_node.getData().equals("EPSILON")){
            List<String> RegisterList = new ArrayList<>();
            int size = sizeOfTypeNode(p_node.parent.getChildren().get(0));
            int power = p_node.getChildren().size();

            for (Node child : p_node.getChildren()){
                String Register = this.m_registerPool.pop();
                child.accept(this);
                m_moonExecCode += m_mooncodeindent + "lw "  + Register  + "," + child.getChildren().get(0).m_moonVarName + "(r0)\n";
                RegisterList.add(Register);
            }
            List<String> AddList = new ArrayList<>();
            for (String reg : RegisterList){
                String Register = this.m_registerPool.pop();
                m_moonExecCode += m_mooncodeindent + "muli "  + Register  + "," + reg + "," + Math.pow(size, power) + "\n";
                power--;
                AddList.add(Register);
            }
            List<String> AddList2 = new ArrayList<>();
            int index = 0;
            if (AddList.size() > 1) {
                for (int i = 1; i < AddList.size(); i++) {
                    String Register = this.m_registerPool.pop();
                    if (i == 1) {
                        m_moonExecCode += m_mooncodeindent + "add " + Register + "," + AddList.get(0) + "," + AddList.get(1) + "\n";
                        AddList2.add(Register);
                        index++;
                    } else {
                        m_moonExecCode += m_mooncodeindent + "add " + Register + "," + AddList.get(i) + "," + AddList2.get(index) + "\n";
                        AddList2.add(Register);
                        index++;
                    }
                }
                String regi = this.m_registerPool.pop();
                m_moonExecCode += m_mooncodeindent + "lw "  + regi  + "," + p_node.parent.getChildren().get(0).getData() + "(" + AddList2.get(index - 1) + ")\n";
                this.m_registerPool.push(regi);
            } else {
                String regi = this.m_registerPool.pop();
                m_moonExecCode += m_mooncodeindent + "lw "  + regi  + "," + p_node.parent.getChildren().get(0).getData() + "(" + AddList.get(0) + ")\n";
                this.m_registerPool.push(regi);
            }

            for (String Register : RegisterList){
                this.m_registerPool.push(Register);
            }
            for (String Register : AddList){
                this.m_registerPool.push(Register);
            }
            for (String Register : AddList2){
                this.m_registerPool.push(Register);
            }
        }

        for (Node child : p_node.getChildren()) {
            child.accept(this);
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
        for (Node child : p_node.getChildren())
            child.accept(this);

        String localRegister      = this.m_registerPool.pop();
        String leftChildRegister  = this.m_registerPool.pop();
        String rightChildRegister = this.m_registerPool.pop();

        int size = p_node.m_symtabentry.m_size;
        String op = "";
        if (p_node.getData().equals("*")){
            op = "mul ";
        } else if (p_node.getData().equals("/")){
            op = "div ";
        } else {
            op = "and ";
        }
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + p_node.getChildren().get(0).m_moonVarName + " "+ p_node.getData() +" " + p_node.getChildren().get(1).m_moonVarName + "\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister  + "," + p_node.getChildren().get(0).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + p_node.getChildren().get(1).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + op + localRegister      + "," + leftChildRegister + "," + rightChildRegister + "\n";
        m_moonDataCode += m_mooncodeindent + "% space for " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(1).m_moonVarName + "\n";
        m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res " + size + "\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";

        this.m_registerPool.push(leftChildRegister);
        this.m_registerPool.push(rightChildRegister);
        this.m_registerPool.push(localRegister);
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
        try {
            out_Moon = new BufferedWriter(new FileWriter(outMoon));
            out_Moon.write(m_moonExecCode);
            out_Moon.flush();
            out_Moon.write(m_moonDataCode);
            out_Moon.flush();

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
        String localregister1 = this.m_registerPool.pop();
        for (Node child : p_node.getChildren() )
            child.accept(this);

        m_moonExecCode += m_mooncodeindent + "% processing: return("  + p_node.getChildren().get(0).m_moonVarName + ")\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.getChildren().get(0).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "sw "   + p_node.m_symtab.m_name + "return(r0)," + localregister1 + "\n";
        this.m_registerPool.push(localregister1);
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
        int size = p_node.m_symtabentry.m_size;
        m_moonDataCode += m_mooncodeindent + "% space for variable " + p_node.getChildren().get(1).getData() + "\n";
        m_moonDataCode += String.format("%-10s" ,p_node.getChildren().get(1).getData()) + " res " + size + "\n";
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
        m_moonExecCode += "gowhile1";
        Node child = p_node.getChildren().get(0);
        child.accept(this);
        String reg = this.m_registerPool.pop();
        m_moonExecCode += m_mooncodeindent + "lw " + reg + "," + child.m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "bz " + reg + " endwhile1\n";
        child = p_node.getChildren().get(1);
        child.accept(this);
        m_moonExecCode += m_mooncodeindent + "j gowhile1\n";
        m_moonExecCode += "enndwhile1\n";

    }

    public void visit(WriteStatNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);

        Node var = p_node.getChildren().get(0);
        var = var.getChildren().get(0);

        while (true){
            if ((var.getChildren().size() > 1) && (var.getData().equals("."))){
                var = var.getChildren().get(1);
                if (!var.haveChildren()){
                    break;
                } else {
                    var.m_moonVarName = var.getChildren().get(0).m_moonVarName;
                }
            } else {
                if (var.getData().equals("Var") || var.getData().equals("Fcall")) {
                    break;
                } else {
                    var = var.getChildren().get(1);
                    if (!var.haveChildren()){
                        break;
                    } else {
                        var.m_moonVarName = var.getChildren().get(0).m_moonVarName;
                    }
                }
            }

        }
        String localRegister      = this.m_registerPool.pop();

        m_moonExecCode += m_mooncodeindent + "% processing: put("  + var.m_moonVarName + ")\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + var.m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "% put value on stack\n";
        m_moonExecCode += m_mooncodeindent + "sw -8(r14)," + localRegister + "\n";
        m_moonExecCode += m_mooncodeindent + "% link buffer to stack\n";
        m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ",r0, buf\n";
        m_moonExecCode += m_mooncodeindent + "sw -12(r14)," + localRegister + "\n";
        m_moonExecCode += m_mooncodeindent + "% convert int to string for output\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, intstr\n";
        m_moonExecCode += m_mooncodeindent + "sw -8(r14),r13\n";
        m_moonExecCode += m_mooncodeindent + "% output to console\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, putstr\n";

        this.m_registerPool.push(localRegister);

    }

    public void visit(FuncDefNode p_node) {
        String funcName = p_node.getChildren().get(2).getData();
        m_moonExecCode += m_mooncodeindent + "% processing function definition: "  + p_node.m_moonVarName + "\n";
        m_moonExecCode += String.format("%-10s",funcName);
        m_moonDataCode += String.format("%-11s", funcName + "link") + "res 4\n";
        m_moonExecCode += m_mooncodeindent + "sw " + funcName + "link(r0),r15\n";

        int size = 0;
        String type = p_node.getChildren().get(0).getData();
        if (type.equals("float")){
            size = 8;
        } else if (type.equals("integer")){
            size = 4;
        } else if (type.equals("void")){
            size = 0;
        } else {
            SymTabEntry se = p_node.m_symtab.lookupName(p_node.getChildren().get(0).getData());
            size = -se.m_subtable.m_size;
        }
        m_moonDataCode += String.format("%-11s", funcName + "return") + "res " + size + "\n";
        for (Node child : p_node.getChildren() )
            child.accept(this);
        m_moonExecCode += m_mooncodeindent + "lw r15," + p_node.getData() + "link(r0)\n";
        m_moonExecCode += m_mooncodeindent + "jr r15\n";
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
        m_moonExecCode += m_mooncodeindent + "entry\n";
        m_moonExecCode += m_mooncodeindent + "addi r14,r0,topaddr\n";
        for (Node child : p_node.getChildren() )
            child.accept(this);

        m_moonDataCode += m_mooncodeindent + "% buffer space used for console output\n";
        m_moonDataCode += String.format("%-11s", "buf") + "res 20\n";
        m_moonExecCode += m_mooncodeindent + "hlt\n";
    }

    public void visit(AssignNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
        String localRegister = this.m_registerPool.pop();

        m_moonExecCode += m_mooncodeindent + "% processing: "  + p_node.getChildren().get(0).m_moonVarName + " := " + p_node.getChildren().get(1).m_moonVarName + "\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + p_node.getChildren().get(1).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.getChildren().get(0).m_moonVarName + "(r0)," + localRegister + "\n";

        this.m_registerPool.push(localRegister);

    }

    public void visit(SignNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);
    }

    public void visit(RelOpNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);

        String localRegister      = this.m_registerPool.pop();
        String leftChildRegister  = this.m_registerPool.pop();
        String rightChildRegister = this.m_registerPool.pop();
        String op = "";
        if (p_node.getData().equals("<")){
            op = "clt ";
        } else if (p_node.getData().equals("<=")){
            op = "cle ";
        } else if (p_node.getData().equals(">=")){
            op = "cge";
        }else if(p_node.getData().equals(">")){
            op = "cgt ";
        }else{
            op = "ceq ";
        }
        int size = p_node.m_symtabentry.m_size;
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + p_node.getChildren().get(0).m_moonVarName + " "+ p_node.getData() +" " + p_node.getChildren().get(1).m_moonVarName + "\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister +  "," + p_node.getChildren().get(0).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + p_node.getChildren().get(1).m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + op + localRegister +      "," + leftChildRegister + "," + rightChildRegister + "\n";
        m_moonDataCode += m_mooncodeindent + "% space for " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(1).m_moonVarName + "\n";
        m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res " + size + "\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";

        this.m_registerPool.push(leftChildRegister);
        this.m_registerPool.push(rightChildRegister);
        this.m_registerPool.push(localRegister);

    }

    public void visit(DotNode p_node) {
        for (Node child : p_node.getChildren() )
            child.accept(this);


    }

    public void visit(NumNode p_node) {

        for (Node child : p_node.getChildren() )
            child.accept(this);


        String localRegister = this.m_registerPool.pop();

        m_moonExecCode += m_mooncodeindent + "% processing: "  + p_node.m_moonVarName + " := " + p_node.getData() + "\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + p_node.m_moonVarName + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";

        m_moonDataCode += m_mooncodeindent + "% space for " + p_node.m_moonVarName + "\n";
        m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res " + sizeOfTypeNode(p_node) + "\n";



        this.m_registerPool.push(localRegister);

    }
}

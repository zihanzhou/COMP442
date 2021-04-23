package AST;
import java.util.ArrayList;
import java.util.List;
import SymbolTable.*;
import Compiler.Token;
import Visitors.Visitor;

public class Node {
    public List<Node>      children        = new ArrayList<Node>();
    public String          data            = null;
    public int             location        = 0;
    public Node            parent          = null;
    public List<Node>      sibilings       = new ArrayList<Node>();
    public String          type            = null;
    public static int       nodelevel       = 0;
    public int              nodeId          = 0;
    public static int       curNodeId       = 0;
    public boolean          success         = true;

    public  SymTab          m_symtab        = null;
    public  SymTabEntry     m_symtabentry   = null;

    public  String      m_localRegister      = new String();
    public  String      m_leftChildRegister  = new String();
    public  String      m_rightChildRegister = new String();
    public  String      m_moonVarName        = new String();

    public Node() {

    }

    public Node(String data) {
        this.setData(data);
        this.success = true;
        this.nodeId = Node.curNodeId;
        Node.curNodeId++;
    }

    public Node(Token token) {
        this.setData(token.getLexeme());
        this.setType(token.getType());
        this.setLocation(token.getLocation());
        this.nodeId = Node.curNodeId;
        this.success = true;
        Node.curNodeId++;
    }

    public Node(String data, Node parent) {
        this.setData(data);
        this.setParent(parent);
        parent.addChild(this);
        this.nodeId = Node.curNodeId;
        this.success = true;
        Node.curNodeId++;
    }

    public Node(String data, String type) {
        this.setData(data);
        this.setType(type);
        this.nodeId = Node.curNodeId;
        this.success = true;
        Node.curNodeId++;
    }


    public boolean isEmpty() {
        return (((this.data == null) || (this.data.equals(""))) && (this.type == null) && (this.location == 0));
    }

    public String getData() {
        return this.data;
    }

    public void setData(String p_data) {
        this.data = p_data;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String p_type) {
        this.type = p_type;
    }

    public int getLocation() {
        return this.location;
    }

    public void setLocation(int p_location) {
        this.location = p_location;
    }

    public boolean getSuccess() { return this.success; }

    public void setSuccess(boolean success) { this.success = success; }

    public void makeSiblings(Node p_child) {
        this.sibilings.add(p_child);
        if (p_child.haveSiblings()) {
            List<Node> sib = p_child.getSiblings();
            for (int i = 0; i < p_child.getSiblingSize(); i++) {
                this.sibilings.add(sib.get(i));
            }
        }
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }


    public List<Node> getSiblings() {
        return this.sibilings;
    }

    public int getSiblingSize() {
        return this.sibilings.size();
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node p_parent) {
        this.parent = p_parent;
    }

    public void adoptChildren(Node p_child) {
        p_child.setParent(this);
        this.children.add(p_child);
        if (p_child.haveSiblings()) {
            List<Node> p_child_sib = p_child.getSiblings();
            for (int i = 0; i < p_child_sib.size(); i++) {
                p_child_sib.get(i).setParent(this);
                this.children.add(p_child_sib.get(i));
            }
        }
    }

    public void adoptGrandChildren(Node p_grandchild) {
        if (p_grandchild.haveChildren()) {
            List<Node> grandchild = p_grandchild.children;
            for (int i = 0; i < grandchild.size(); i++) {
                this.adoptChildren(grandchild.get(i));
            }
        }
    }

    public boolean haveChildren() {
        if (this.children.size() == 0)
            return false;
        else
            return true;
    }

    public boolean haveSiblings() {
        if (this.sibilings.size() == 0)
            return false;
        else
            return true;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if (this.children.size() == 0)
            return true;
        else
            return false;
    }

    public void Family(String op, Node kid1) {
        this.setData(op);
        this.adoptChildren(kid1);
    }

    public void Family(String op, String kid1) {
        this.setData(op);
        Node Epsilon = new Node();
        Epsilon.setData("EPSILON");
        this.adoptChildren(Epsilon);
    }

    public void Family(String op, Node kid1, Node kid2) {
        this.setData(op);
        kid1.makeSiblings(kid2);
        this.adoptChildren(kid1);
    }

    public void Family(String op, Node kid1, Node kid2, Node kid3) {
        this.setData(op);
        kid2.makeSiblings(kid3);
        kid1.makeSiblings(kid2);
        this.adoptChildren(kid1);
    }

    public void Family(String op, Node kid1, Node kid2, Node kid3, Node kid4) {
        this.setData(op);
        this.addChild(kid1);
        this.addChild(kid2);
        this.addChild(kid3);
        this.addChild(kid4);
    }
    public void Family(Token op, Node kid1, Node kid2) {
        this.setData(op.getLexeme());
        this.setType(op.getType());
        this.setLocation(op.getLocation());
        this.addChild(kid1);
        this.addChild(kid2);
    }

    public Node makeFamily(String op, Node kid1) {
        Node operator = new Node(op);
        operator.adoptChildren(kid1);
        return operator;
    }

    public void Family(Node addOp, Node kid1, Node kid2) {
        this.setData(addOp.getData());
        this.addChild(kid1);
        this.addChild(kid2);
    }

    public Node makeFamily(String op, Node kid1, Node kid2) {
        Node operator = new Node(op);
        kid1.makeSiblings(kid2);
        operator.adoptChildren(kid1);
        return operator;
    }

    public Node makeFamily(Token token, Node kid1, Node kid2) {
        Node operator = new Node(token);
        kid1.makeSiblings(kid2);
        operator.adoptChildren(kid1);
        return operator;
    }



    public Node makeFamily(Token token, List<Node> kids) {
        Node operator = new Node(token);
        Node kid1 = kids.get(0);
        for (int i = 1; i < kids.size(); i++) {
            kid1.makeSiblings(kids.get(i));
        }
        operator.adoptChildren(kid1);
        return operator;
    }

    public Node makeFamily(String str, List<Node> kids) {
        Node operator = new Node(str);
        Node kid1 = kids.get(0);
        for (int i = 1; i < kids.size(); i++) {
            kid1.makeSiblings(kids.get(i));
        }
        operator.adoptChildren(kid1);
        return operator;
    }

    public String print() {
        String str = printChildren();
        return str;
    }

    public String printChildren() {
        String str = "";
        String space = "";
        for (int i = 0; i < nodelevel; i++)
            space += "     ";
        str += space + output() + "\r\n";
        Node.nodelevel++;
        List<Node> c = this.getChildren();
        for (int i = 0; i < c.size(); i++) {
            str = str + c.get(i).printChildren();
        }
        Node.nodelevel--;
        return str;
    }

    public String output() {
        String toprint = "";// + this.getClass().getName() + " ";
        if (this.location == 0) {
            toprint += this.getData();
        } else {
            toprint += this.getType() + ": " + this.getData() + " :[" + this.getLocation() + "]";
        }
        return toprint;// + " "+ this.getClass().getName() + " ";
    }

    public void copy(Node node) {
        this.children = node.children;
        this.data = node.data;
        this.location = node.location;
        this.parent = node.parent;
        this.sibilings = node.sibilings;
        this.type = node.type;
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

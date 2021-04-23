package AST;

import Visitors.Visitor;

public class AssignStatNode extends Node{

    public AssignStatNode() {super("");}

    public AssignStatNode(String data){
        super("=");
    }

    public AssignStatNode(String data, Node parent){
        super("=", parent);
    }

    public AssignStatNode(String data, Node leftChild, Node rightChild){
        super("=");
        this.addChild(leftChild);
        this.addChild(rightChild);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

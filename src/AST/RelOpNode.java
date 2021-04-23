package AST;

import Visitors.Visitor;

public class RelOpNode extends Node{

    public RelOpNode() {
        super("");
    }
    public RelOpNode(String data){
        super(data);
    }

    public RelOpNode(String data, Node parent){
        super(data, parent);
    }

    public RelOpNode(String data, Node leftChild, Node rightChild){
        super(data);
        this.addChild(leftChild);
        this.addChild(rightChild);
    }
    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

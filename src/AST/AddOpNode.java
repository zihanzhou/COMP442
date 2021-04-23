package AST;

import Visitors.Visitor;

public class AddOpNode extends Node {
    public AddOpNode() { super(); }

    public AddOpNode(String data){
        super(data);
    }

    public AddOpNode(String data, Node parent){
        super(data, parent);
    }

    public AddOpNode(String data, Node leftChild, Node rightChild){
        super(data);
        this.addChild(leftChild);
        this.addChild(rightChild);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

package AST;

import Visitors.Visitor;

public class MultOpNode extends Node{
    public MultOpNode() {
        super("");
    }
    public MultOpNode(String data){
        super(data);
    }

    public MultOpNode(String data, Node parent){
        super(data, parent);
    }

    public MultOpNode(String data, Node leftChild, Node rightChild){
        super(data);
        this.addChild(leftChild);
        this.addChild(rightChild);
    }
    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

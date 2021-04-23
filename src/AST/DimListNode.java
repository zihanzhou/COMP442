package AST;

import Visitors.Visitor;

import java.util.List;

public class DimListNode extends Node {
    public DimListNode(){
        super("");
    }

    public DimListNode(Node parent){
        super("", parent);
    }

    public DimListNode(List<Node> listOfDimNodes){
        super("");
        for (Node child : listOfDimNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

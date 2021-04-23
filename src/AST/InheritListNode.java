package AST;

import Visitors.Visitor;

import java.util.List;

public class InheritListNode extends Node{
    public InheritListNode(){
        super("");
    }

    public InheritListNode(Node parent){
        super("", parent);
    }

    public InheritListNode(List<Node> listOfInheritNodes){
        super("");
        for (Node child : listOfInheritNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

package AST;

import Visitors.Visitor;

import java.util.List;

public class ClassListNode extends Node{

    public ClassListNode(){
        super("");
    }

    public ClassListNode(Node parent){
        super("", parent);
    }

    public ClassListNode(List<Node> listOfClassNodes){
        super("");
        for (Node child : listOfClassNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

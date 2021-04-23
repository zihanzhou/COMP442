package AST;

import Visitors.Visitor;

import java.util.List;

public class ParamListNode extends Node {
    public ParamListNode(){
        super("");
    }

    public ParamListNode(Node parent){
        super("", parent);
    }

    public ParamListNode(List<Node> listOfParamNodes){
        super("");
        for (Node child : listOfParamNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

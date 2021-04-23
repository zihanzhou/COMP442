package AST;

import Visitors.Visitor;

public class FuncCallNode extends Node {
    public FuncCallNode(){
        super("");
    }

    public FuncCallNode(Node parent){
        super("", parent);
    }

    public FuncCallNode(Node id, Node paramList){
        super("");
        this.addChild(id);
        this.addChild(paramList);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

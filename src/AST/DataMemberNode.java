package AST;

import Visitors.Visitor;

public class DataMemberNode extends Node {
    public DataMemberNode(){
        super("");
    }

    public DataMemberNode(Node parent){
        super("", parent);
    }

    public DataMemberNode(Node id, Node dimList){
        super("");
        this.addChild(id);
        this.addChild(dimList);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

package AST;

import Visitors.Visitor;

public class IndiceListNode extends Node {
    public IndiceListNode(){
        super("");
    }

    public IndiceListNode(String str) {
        super(str);
    }

    public IndiceListNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

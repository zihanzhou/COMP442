package AST;

import Visitors.Visitor;

public class ReadStatNode extends Node{
    public ReadStatNode() {
        super("");
    }

    public ReadStatNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

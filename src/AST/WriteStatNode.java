package AST;

import Visitors.Visitor;

public class WriteStatNode extends Node{
    public WriteStatNode() {
        super("");
    }

    public WriteStatNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

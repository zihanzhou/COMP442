package AST;

import Visitors.Visitor;

public class ReturnStatNode extends Node {
    public ReturnStatNode() {
        super("");
    }

    public ReturnStatNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

package AST;

import Visitors.Visitor;

public class IfStatNode extends Node {
    public IfStatNode() {
        super("");
    }

    public IfStatNode(Node parent) {
        super("", parent);
    }

    public IfStatNode(Node expr, Node statblock1, Node statblock2) {
        super("");
        this.addChild(expr);
        this.addChild(statblock1);
        this.addChild(statblock2);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

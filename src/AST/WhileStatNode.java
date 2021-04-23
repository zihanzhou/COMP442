package AST;

import Visitors.Visitor;

public class WhileStatNode extends Node {
    public WhileStatNode() {
        super("");
    }

    public WhileStatNode(Node parent) {
        super("", parent);
    }

    public WhileStatNode(Node expr, Node statblock){
        super("");
        this.addChild(expr);
        this.addChild(statblock);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

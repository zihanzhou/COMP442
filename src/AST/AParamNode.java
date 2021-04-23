package AST;

import Visitors.Visitor;

public class AParamNode extends Node{
    public AParamNode() {
        super("");
    }

    public AParamNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

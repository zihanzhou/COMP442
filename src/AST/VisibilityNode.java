package AST;

import Visitors.Visitor;

public class VisibilityNode extends Node {
    public VisibilityNode() {
        super("");
    }

    public VisibilityNode(String type) {
        super(type);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

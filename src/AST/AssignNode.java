package AST;

import Visitors.Visitor;

public class AssignNode extends Node{

    public AssignNode() {
        super("");
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

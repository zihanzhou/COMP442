package AST;

import Visitors.Visitor;

public class VarNode extends Node{
    public VarNode () {
        super("");
    }

    public VarNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

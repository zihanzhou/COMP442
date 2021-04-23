package AST;

import Visitors.Visitor;

public class FuncOrAssignStatNode extends Node{
    public FuncOrAssignStatNode() {
        super("");
    }

    public FuncOrAssignStatNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

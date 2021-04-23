package AST;

import Visitors.Visitor;

public class FuncOrVarNode extends Node{
    public FuncOrVarNode () {
        super("");
    }

    public FuncOrVarNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

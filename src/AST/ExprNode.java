package AST;

import Visitors.Visitor;

public class ExprNode extends Node {
    public ExprNode() {
        super("");
    }

    public ExprNode(Node parent){
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

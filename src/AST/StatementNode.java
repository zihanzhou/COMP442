package AST;

import Visitors.Visitor;

public class StatementNode extends Node{
    public StatementNode() {
        super("");
    }

    public StatementNode(Node parent) {
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

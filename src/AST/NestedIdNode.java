package AST;

import Visitors.Visitor;

public class NestedIdNode extends Node {
    public NestedIdNode() {
        super("");
    }

    public NestedIdNode(String op, Node id) {
        super("");
        this.addChild(new Node(op));
        this.addChild(id);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

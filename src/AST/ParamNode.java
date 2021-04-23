package AST;

import Visitors.Visitor;

public class ParamNode extends Node {

    public ParamNode() {
        super("");
    }

    public ParamNode(Node parent) {
        super("", parent);
    }

    public ParamNode(Node type, Node id, Node arraysize) {
        super("");
        this.addChild(type);
        this.addChild(id);
        this.addChild(arraysize);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

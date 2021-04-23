package AST;

import Visitors.Visitor;

public class ScopeSpecNode extends Node{
    public ScopeSpecNode() {
        super("");
    }

    public ScopeSpecNode(Node Id){
        super("");
        this.addChild(Id);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

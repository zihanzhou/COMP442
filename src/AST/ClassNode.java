package AST;

import Visitors.Visitor;

import java.util.List;

public class ClassNode extends Node{
    public ClassNode(){
        super("");
    }

    public ClassNode(Node p_parent){
        super("", p_parent);
    }

    public ClassNode(Node id, Node inheritlist, Node memberlist){
        super("");
        this.addChild(id);
        this.addChild(inheritlist);
        this.addChild(memberlist);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

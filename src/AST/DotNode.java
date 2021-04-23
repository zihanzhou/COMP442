package AST;

import Visitors.Visitor;

public class DotNode extends Node{
    public DotNode(){
        super("");
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

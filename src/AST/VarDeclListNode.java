package AST;

import Visitors.Visitor;

public class VarDeclListNode extends Node {

    public VarDeclListNode(){
        super("");
    }

    public VarDeclListNode(Node parent){
        super("", parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

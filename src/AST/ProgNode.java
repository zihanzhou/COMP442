package AST;

import Visitors.Visitor;

public class ProgNode extends Node{
    public ProgNode(){
        super("");
    }

    public ProgNode(Node parent){
        super("", parent);
    }

    public ProgNode(Node classlist, Node funcdeflist, Node programstatblock){
        super("");
        this.addChild(classlist);
        this.addChild(funcdeflist);
        this.addChild(programstatblock);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

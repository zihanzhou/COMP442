package AST;

import Visitors.Visitor;

public class FuncDeclNode extends Node{
    public FuncDeclNode(){
        super("");
    }

    public FuncDeclNode(Node parent){
        super("", parent);
    }

    public FuncDeclNode(Node type, Node id, Node paramList){
        super("");
        this.addChild(type);
        this.addChild(id);
        this.addChild(paramList);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

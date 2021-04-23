package AST;

import Visitors.Visitor;

public class VarDeclNode extends Node {
    public VarDeclNode(){
        super("");
    }

    public VarDeclNode(Node parent){
        super("", parent);
    }

    public VarDeclNode(Node type, Node id, Node dimList){
        super("");
        this.addChild(type);
        this.addChild(id);
        this.addChild(dimList);
    }

    public VarDeclNode(Node type, Node id){
        super("");
        this.addChild(type);
        this.addChild(id);
        this.addChild(new DimListNode());
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

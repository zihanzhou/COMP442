package AST;

import Visitors.Visitor;

public class MembDeclNode extends Node{
    public MembDeclNode() {
        super("");
    }

    public MembDeclNode(Node parent){
        super("", parent);
    }

    public MembDeclNode(Node visibility, Node type, Node id, Node DimOrParamList){
        super("");
        this.addChild(visibility);
        this.addChild(type);
        this.addChild(id);
        this.addChild(DimOrParamList);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

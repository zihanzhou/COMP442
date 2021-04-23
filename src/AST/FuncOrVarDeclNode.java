package AST;

import Visitors.Visitor;

public class FuncOrVarDeclNode extends Node {
    public FuncOrVarDeclNode() {
        super("");
    }

    public FuncOrVarDeclNode(Node parent){
        super("", parent);
    }

    public FuncOrVarDeclNode(Node type, Node id, Node DimOrParamList){
        super("");
        this.addChild(type);
        this.addChild(id);
        this.addChild(DimOrParamList);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

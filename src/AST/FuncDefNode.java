package AST;

import Visitors.Visitor;

public class FuncDefNode extends Node {

    public FuncDefNode() {
        super("");
    }

    public FuncDefNode(Node parent) {
        super("", parent);
    }

    public FuncDefNode(Node type, Node scopeSpec, Node id, Node paramList, Node statBlock) {
        super("");
        this.addChild(type);
        this.addChild(scopeSpec);
        this.addChild(id);
        this.addChild(paramList);
        this.addChild(statBlock);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

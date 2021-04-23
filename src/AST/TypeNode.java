package AST;

import Visitors.Visitor;

public class TypeNode extends Node {

    public TypeNode() { super(""); }

    public TypeNode(String data){
        super(data);
    }

    public TypeNode(String data, Node parent){
        super(data, parent);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

package AST;

import Compiler.Token;
import Visitors.Visitor;

public class IdNode extends Node{

    public IdNode() {
        super("");
    }

    public IdNode(Token data) { super(data); }

    public IdNode(String data) {
        super(data);
    }

    public IdNode(String data, Node parent){
        super(data, parent);
    }

    public IdNode(String data, String type){
        super(data, type);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }


}

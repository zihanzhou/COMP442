package AST;

import Visitors.Visitor;
import Compiler.*;

public class SignNode extends Node{

    public SignNode() {
        super("");
    }

    public SignNode(Token token) { super(token); }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

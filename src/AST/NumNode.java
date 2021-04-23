package AST;

import Visitors.Visitor;
import Compiler.*;
public class NumNode extends Node{
    public NumNode(){
        super("");
    }

    public NumNode(Token token){
        super(token);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

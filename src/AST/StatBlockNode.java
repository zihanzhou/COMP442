package AST;

import Visitors.Visitor;

import java.util.List;

public class StatBlockNode extends Node {
    public StatBlockNode(){
        super("");
    }

    public StatBlockNode(Node parent){
        super("", parent);
    }

    public StatBlockNode(List<Node> listOfStatOrVarDeclNodes){
        super("");
        for (Node child : listOfStatOrVarDeclNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

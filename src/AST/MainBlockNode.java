package AST;

import Visitors.Visitor;

import java.util.List;

public class MainBlockNode extends Node{
    public MainBlockNode(){
        super("");
    }

    public MainBlockNode(Node parent){
        super("", parent);
    }

    public MainBlockNode(List<Node> listOfStatOrVarDeclNodes){
        super("");
        for (Node child : listOfStatOrVarDeclNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}

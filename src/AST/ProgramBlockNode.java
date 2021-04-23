package AST;

import Visitors.Visitor;

import java.util.List;

public class ProgramBlockNode extends Node {

    public ProgramBlockNode(){
        super("");
    }

    public ProgramBlockNode(Node parent){
        super("", parent);
    }

    public ProgramBlockNode(List<Node> listOfStatOrVarDeclNodes){
        super("");
        for (Node child : listOfStatOrVarDeclNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

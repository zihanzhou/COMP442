package AST;

import Visitors.Visitor;

import java.util.List;

public class FuncDefListNode extends Node{
    public FuncDefListNode(){
        super("");
    }

    public FuncDefListNode(Node parent){
        super("", parent);
    }

    public FuncDefListNode(List<Node> listOfFuncDefNodes){
        super("");
        for (Node child : listOfFuncDefNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }


}

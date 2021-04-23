package AST;

import Visitors.Visitor;

import java.util.List;

public class MemberListNode extends Node{
    public MemberListNode(){
        super("");
    }

    public MemberListNode(Node parent){
        super("", parent);
    }

    public MemberListNode(List<Node> listOfMemberNodes){
        super("");
        for (Node child : listOfMemberNodes)
            this.addChild(child);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

package AST;

import Visitors.Visitor;

public class DimNode extends Node{

    public DimNode() {super("");}

    public DimNode(String data){
        super(data);
    }

    public DimNode(String data, Node parent){
        super(data, parent);
    }

    public DimNode(String data, String type){
        super(data, type);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}

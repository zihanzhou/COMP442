package Visitors;

import AST.*;

//45
public abstract class Visitor {
    public abstract void visit(AddOpNode p_node);

    public abstract void visit(AParamNode p_node);

    public abstract void visit(AssignStatNode p_node);

    public abstract void visit(ClassListNode p_node);

    public abstract void visit(ClassNode p_node);

    public abstract void visit(DataMemberNode p_node);

    public abstract void visit(DimListNode p_node);

    public abstract void visit(DimNode p_node);

    public abstract void visit(ExprNode p_node);

    public abstract void visit(FuncCallNode p_node);

    public abstract void visit(FuncDeclNode p_node);

    public abstract void visit(FuncDefListNode p_node);

    public abstract void visit(FuncOrAssignStatNode p_node);

    public abstract void visit(FuncOrVarDeclNode p_node);

    public abstract void visit(FuncOrVarNode p_node);

    public abstract void visit(IdNode p_node);

    public abstract void visit(IfStatNode p_node);

    public abstract void visit(IndiceListNode p_node);

    public abstract void visit(InheritListNode p_node);

    public abstract void visit(MembDeclNode p_node);

    public abstract void visit(MemberListNode p_node);

    public abstract void visit(MultOpNode p_node);

    public abstract void visit(NestedIdNode p_node);

    public abstract void visit(ParamListNode p_node);

    public abstract void visit(ParamNode p_node);

    public abstract void visit(ProgNode p_node);

    public abstract void visit(ProgramBlockNode p_node);

    public abstract void visit(ReadStatNode p_node);

    public abstract void visit(ReturnStatNode p_node);

    public abstract void visit(ScopeSpecNode p_node);

    public abstract void visit(StatBlockNode p_node);

    public abstract void visit(StatementNode p_node);

    public abstract void visit(TypeNode p_node);

    public abstract void visit(VarDeclNode p_node);

    public abstract void visit(VarNode p_node);

    public abstract void visit(VisibilityNode p_node);

    public abstract void visit(WhileStatNode p_node);

    public abstract void visit(WriteStatNode p_node);

    public abstract void visit(FuncDefNode p_node);

    public abstract void visit(Node p_node);

    public abstract void visit(VarDeclListNode p_node);

    public abstract void visit(MainBlockNode p_node);

    public abstract void visit(AssignNode p_node);

    public abstract void visit(SignNode p_node);

    public abstract void visit(RelOpNode p_node);

    public abstract void visit(DotNode p_node);

    public abstract void visit(NumNode p_node);
}

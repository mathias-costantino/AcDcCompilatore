package visitor;

import ast.*;

public interface IVisitor {
    void visit(NodeProgram node);
    void visit(NodeId node);
    void visit(NodeDecl node);
    void visit(NodeBinOp node);
    void visit(NodePrint node);
    void visit(NodeAssign node);
    void visit(NodeDeref node);
    void visit(NodeCost node);
}
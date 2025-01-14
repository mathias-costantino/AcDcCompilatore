package ast;

import visitor.IVisitor;

public class NodeAssign extends NodeStm {
	
	private NodeId id;
	private NodeExpr expr;
	
	//costruttore
	public NodeAssign(NodeId id, NodeExpr expr) {
		this.id = id;
		this.expr = expr;
	}
	
	//getter
	public NodeId getId() {
		return id;
	}
	
	public NodeExpr getExpr() {
		return expr;
	}

	@Override
	public String toString() {
		return id.toString() + "=" + expr.toString();
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

}

package ast;

import visitor.IVisitor;

public class NodeDeref extends NodeExpr {
	
	private NodeId id;
	
	//costruttore
	public NodeDeref(NodeId id) {
		this.id = id;
	}
	
	//getter
	public NodeId getId() {
		return id;
	}

	@Override
	public String toString() {
		return "(Deref: " + id.toString();
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

}

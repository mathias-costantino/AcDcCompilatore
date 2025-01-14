package ast;

import visitor.IVisitor;

public class NodePrint extends NodeStm {
	
	private NodeId id;
	
	//costruttore
	public NodePrint(NodeId id) {
		this.id = id;
	}
	
	//getter
	public NodeId getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Print(" + id + ")";
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

}

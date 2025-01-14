package ast;

import visitor.IVisitor;

public class NodeDecl extends NodeDecSt {
	
	private NodeId id;
	private LangType type;
	private NodeExpr init;
	
	//costruttore
	public NodeDecl(NodeId id, LangType type, NodeExpr init) {
		this.id = id;
		this.type = type;
		this.init = init;
		
		 System.out.println("Creazione NodeDecl: id=" + id.getName() + ", type=" + type);
    }

	
	//getter
	public NodeId getId() {
		return id;
	}
	
	public LangType getType() {
		return type;
	}
	
	public NodeExpr getInit() {
		return init;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeDecl(");
		sb.append("id: ").append(id.toString());
		sb.append(", type: ").append(type);
		if(init!=null) {
			sb.append(", init: ").append(init.toString());
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
	
}

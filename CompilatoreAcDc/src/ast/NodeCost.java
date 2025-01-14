package ast;

import visitor.IVisitor;

public class NodeCost extends NodeExpr {
	
	private String value;
	private LangType type;
	
	//costruttore
	public NodeCost(String value, LangType type) {
		this.value = value;
		this.type = type;
	}
	
	//getter
	public String getValue() {
		return value;
	}
	
	public LangType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "(Cost: " + type.toString() + "," + value + ")";
	}

	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
	
}

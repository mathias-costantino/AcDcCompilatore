package ast;

import visitor.IVisitor;

public class NodeId extends NodeAST {
	
	private String name;
	
	//costruttore
	public NodeId(String name) {
		this.name = name;
		 System.out.println("Creazione NodeId con nome: " + name);
	}
	
	//getter
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "NodeId{name='" + name + "'}";
	}

	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
	
}


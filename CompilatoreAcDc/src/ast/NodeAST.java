package ast;

import visitor.IVisitor;

public abstract class NodeAST {
	
	@Override
	public abstract String toString();
	
	public abstract void accept(IVisitor visitor);
}

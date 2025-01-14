package ast;

import visitor.IVisitor;

public abstract class NodeAST {
	
	//public abstract TypeDescriptor calcResType();
	//public abstract String calcCodice();
	
	@Override
	public abstract String toString();
	
	public abstract void accept(IVisitor visitor);
}

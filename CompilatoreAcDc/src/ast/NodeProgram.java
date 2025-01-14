package ast;

import java.util.ArrayList;

import visitor.IVisitor;

public class NodeProgram extends NodeAST {
	
	private ArrayList<NodeDecSt> decSts;
	
	//costruttore
	public NodeProgram(ArrayList<NodeDecSt> decSts) {
		this.decSts = decSts;
	}
	
	//getter
	public ArrayList<NodeDecSt> getDecSts(){
		return decSts;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeProgram{\n");
		for (NodeDecSt decSt : decSts) {
			sb.append(" ").append(decSt.toString()).append("\n");
		}
		sb.append("}");
		return sb.toString();
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

}

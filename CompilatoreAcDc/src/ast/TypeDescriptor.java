package ast;

/**
 * @author Mathias Costantino, 20043922
 */
public class TypeDescriptor {
	
	public enum TipoTD {
		INT,
		FLOAT,
		OK,
		ERROR;
	}
	
	private TipoTD tipo;
	private String msg;
	private int riga;
	
	//costruttori
	public TypeDescriptor(TipoTD tipo) {
		this.tipo = tipo;
		this.msg = "";
	}
	
	public TypeDescriptor(TipoTD tipo, String msg, int riga) {
		this.tipo = tipo;
		this.msg = msg;
		this.riga = riga;
	}
	
	//getters
	public TipoTD getTipo() {
		return tipo;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public int getRiga() {
		return riga;
	}
	
	public boolean compatibile(TypeDescriptor left, TypeDescriptor right) {
		if(left.tipo == TipoTD.ERROR || right.tipo == TipoTD.ERROR) {
			return false;
		}
		if(left.tipo == TipoTD.FLOAT && right.tipo == TipoTD.INT) {
			return true;
		}
		return left.tipo == right.tipo;
	}
	
	@Override
    public String toString() {
        return tipo + (msg.isEmpty() ? "" : ": " + msg);
    }
}

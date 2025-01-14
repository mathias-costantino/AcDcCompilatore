package token;

public class Token {

	private int riga;
	private TokenType tipo;
	private String val;
	
	public Token(TokenType tipo, int riga, String val) {
		this.tipo = tipo;
		this.riga = riga;
		this.val = val;
	}
	
	public Token(TokenType tipo, int riga) {
		this.tipo = tipo;
		this.riga = riga;
	}

    // Getters per i campi
	public int getRiga() {
		return riga;
	}

	public TokenType getTipo() {
	    return tipo;
	}
	

	public void setTipo(TokenType tipo) {
	     this.tipo = tipo;
	}

	public void setRiga(int riga) {
	       this.riga = riga;
	}

	public void setVal(String val) {
	        this.val = val;
	    }
	
	public String getVal() {
		return val;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<").append(tipo).append(",").append("r:"+riga);
		builder = val != null ? builder.append(","+val+">") : builder.append(">");
		return builder.toString();
	}

}

package symbolTable;

import ast.LangType;

/**
 * @author Mathias Costantino, 20043922
 */
public class Attributes {

	private LangType tipo;
	private char registro;

	public char getRegistro() {
		return registro;
	}

	public void setRegister(char registro) {
		this.registro = registro;
	}

	public Attributes(LangType tipo) {
		this.tipo = tipo;
	}

	public LangType getTipo() {
		return tipo;
	}

	public void setTipo(LangType tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return tipo.toString();
	}
}
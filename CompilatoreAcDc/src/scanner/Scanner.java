package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import token.*;

/**
 * @author Mathias Costantino, 20043922
 */
public class Scanner {
	private static final char EOF = (char) -1;
	private int riga = 1;
	private PushbackReader buffer;
	private String log;
	
	private Token nextTk = null;

	// skpChars: insieme caratteri di skip (include EOF) e inizializzazione
	private List<Character> skpChars;
	// letters: insieme lettere 
	private List<Character> letters;
	// digits: cifre 
	private List<Character> digits;

	// operTkType: mapping fra caratteri '+', '-', '*', '/'  e il TokenType corrispondente
	private Map<Character, TokenType> operTkType;
	private Map<Character, TokenType> opAssignTkType;
	// delimTkType: mapping fra caratteri '=', ';' e il e il TokenType corrispondente
	private Map<Character, TokenType> delimTkType;

	// keyWordsTkType: mapping fra le stringhe "print", "float", "int" e il TokenType  corrispondente
	private Map<String, TokenType> keyWordsTkType;

	public Scanner(String fileName) throws FileNotFoundException {

		this.buffer = new PushbackReader(new FileReader(fileName));
		riga = 1;
		// inizializzare campi che non hanno inizializzazione
		 skpChars = new ArrayList<>();
		 skpChars = Arrays.asList(' ', '\t', '\r', '\n', EOF);
		 
		 letters = new ArrayList<>();
		 letters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
		 
		 digits = new ArrayList<>();
		 digits = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
		 
		 operTkType = new HashMap<>();
	     operTkType.put('+', TokenType.PLUS); 
	     operTkType.put('-', TokenType.MINUS); 
	     operTkType.put('*', TokenType.TIMES);
	     operTkType.put('/', TokenType.DIVIDE); 
	     //aggiunta operatore =
	     //token OP_ASSIGN
	     opAssignTkType = new HashMap<>();
	     opAssignTkType.put('+', TokenType.OP_ASSIGN); 
	     opAssignTkType.put('-', TokenType.OP_ASSIGN); 
	     opAssignTkType.put('*', TokenType.OP_ASSIGN);
	     opAssignTkType.put('/', TokenType.OP_ASSIGN); 
	  
	     delimTkType = new HashMap<>();
	     delimTkType.put('=', TokenType.ASSIGN); 
	     delimTkType.put(';', TokenType.SEMI); 
	     
	     keyWordsTkType = new HashMap<>();
	     keyWordsTkType.put("print", TokenType.PRINT); 
	     keyWordsTkType.put("float", TokenType.TYFLOAT); 
	     keyWordsTkType.put("int", TokenType.TYINT); 
	}
	
	public Token peekToken() throws IOException, LexicalException {
		if(nextTk==null)
			nextTk = nextToken();
		return nextTk;
	}
	
	public Token nextToken() throws IOException, LexicalException {
	    // Avanza nel buffer leggendo i carattere in skipChars
	    while (skpChars.contains(peekChar())) {
	        if (peekChar() == '\n') {
	            riga++;
	        }
	        if (peekChar() == EOF) {
	            readChar();
	            nextTk = new Token(TokenType.EOF, riga);
	            return nextTk;
	        }
	        readChar();
	    }

	    char currentChar = peekChar();
	    //System.out.println("Inizio lettura token, carattere iniziale: " + currentChar);
	    
	    // Gestione numeri
	    if(digits.contains(currentChar) || currentChar == '.') {
	    	nextTk = scanNumber();
	        return nextTk;
	    }
	    
	    // Gestione identificatori e parole chiavi
	    if(letters.contains(currentChar)) {
	    	nextTk = scanId();
	        return nextTk;
	    }
	    
	    // Gestione operatori e delimitatori
	    if(operTkType.containsKey(currentChar) || delimTkType.containsKey(currentChar)) {
	    	nextTk = scanOperator();
	        return nextTk;
	    }

	    // Se arriviamo qui, il carattere è illegale
	    readChar(); // Consuma il carattere illegale
	    throw new LexicalException("Carattere illegale alla riga " + riga + ": " + currentChar);
	}

	   private Token scanId() throws IOException {
	        StringBuilder sb = new StringBuilder();

	        while (letters.contains(peekChar())) {
	            sb.append(readChar());
	        }

	        String id = sb.toString();
	        
	        // Verifica se è una parola chiave
	        if (keyWordsTkType.containsKey(id)) {
	            return new Token(keyWordsTkType.get(id), riga, id);
	        }
	        
	        return new Token(TokenType.ID, riga, id);
	    }
	
	// private Token scanOperator()
	   private Token scanOperator() throws IOException, LexicalException {
	        char currentChar = peekChar();
	        readChar(); // Consuma l'operatore

	        // Verifica se è un operatore di assegnamento composto
	        if (peekChar() == '=' && operTkType.containsKey(currentChar)) {
	            readChar(); // Consuma il '='
	            return new Token(TokenType.OP_ASSIGN, riga, String.valueOf(currentChar) + "=");
	        }

	        // Verifica se è un operatore semplice
	        if (operTkType.containsKey(currentChar)) {
	            return new Token(operTkType.get(currentChar), riga, String.valueOf(currentChar));
	        }

	        // Verifica se è un delimitatore
	        if (delimTkType.containsKey(currentChar)) {
	            return new Token(delimTkType.get(currentChar), riga, String.valueOf(currentChar));
	        }

	        throw new LexicalException("Operatore non valido alla riga " + riga);
	    }

	   private Token scanNumber() throws IOException, LexicalException {
		    StringBuilder sb = new StringBuilder();
		    boolean isFloat = false;
		    boolean hasDecimalPoint = false;
		    int decimalCount = 0;

		    while (digits.contains(peekChar())) {
		        sb.append(readChar());
		    }

		    if (peekChar() == '.') {
		        if (hasDecimalPoint) {
		            throw new LexicalException("Numero non valido: troppi punti decimali alla riga " + riga);
		        }
		        hasDecimalPoint = true;
		        isFloat = true;
		        sb.append(readChar());

		        while (digits.contains(peekChar())) {
		            sb.append(readChar());
		            decimalCount++;
		            if (decimalCount > 5) {
		                throw new LexicalException("Numero decimale non valido alla riga " + riga);
		            }
		        }

		        if (decimalCount == 0) {
		            throw new LexicalException("Numero decimale non valido alla riga " + riga);
		        }
		    }

		    if (peekChar() == '.') {
		        throw new LexicalException("Numero decimale non valido alla riga " + riga);
		    }

		    String number = sb.toString();
		    return new Token(isFloat ? TokenType.FLOAT : TokenType.INT, riga, number);
		}

	private char readChar() throws IOException {
		return ((char) this.buffer.read());
	}

	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
}

package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import scanner.LexicalException;
import parser.*;



import token.*;

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
	    System.out.println("Inizio lettura token, carattere iniziale: " + currentChar);
	    
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
	
	/*
	public Token nextToken() throws IOException, LexicalException {
	    // Avanza nel buffer leggendo i carattere in skipChars
	    // incrementando riga se leggi '\n'.
	    // Se raggiungi la fine del file ritorna il Token EOF
	    while (skpChars.contains(peekChar())) {
	        if (peekChar() == '\n') {
	            riga++;
	        }
	        if (peekChar() == EOF) {
	            readChar(); // Consuma EOF
	            nextTk = new Token(TokenType.EOF, riga);
	            return nextTk;
	        }
	        readChar(); // Consuma il carattere
	    }

	    // Continua con la lettura del token fino alla fine della riga
	    char currentChar = peekChar();
	    

	    System.out.println("Inizio lettura token, carattere iniziale: " + currentChar);
	    
	    while(peekChar() != '\n' && peekChar() != EOF) {
	    	 //currentChar = peekChar(); 
	    	  //gestione numeri
		    if(digits.contains(currentChar) || currentChar == '.') {
		    	System.out.println("Trovato numero o punto: " + currentChar);
		    	return scanNumber();
		    }
		    
		    //gestione identificatori e parole chiavi
		    if(letters.contains(currentChar)) {
		    	 System.out.println("Trovato identificatore o parola chiave: " + currentChar);
		    	return scanId();
		    }
		    
		    //gestione operatori e delimitatori
		    if(operTkType.containsKey(peekChar()) || delimTkType.containsKey(peekChar())) {
		    	System.out.println("Trovato operatore o delimitatore: " + currentChar);
		    	return scanOperator();
		    }
	
	    	readChar();
	    	
	    }
	    
	    System.out.println("Carattere illegale trovato: " + currentChar); 
	    throw new LexicalException("Carattere illegale alla riga " + riga + ": " + currentChar);
        
	}*/


	/*
	public Token nextToken() throws IOException, LexicalException {
	    try {
	        while(true) {
	            char nextChar = peekChar(); 
	            System.out.println("Carattere letto: " + nextChar);  // Stampa il carattere che sta causando l'errore
	            
	            if(nextChar == '\n') {
	                riga++;
	                readChar(); // Consuma il carattere '\n'
	                continue;
	            }
	            
	            if(skpChars.contains(nextChar)) {
	                readChar(); // Consuma il carattere
	                continue;
	            }

	            if(nextChar == EOF) {  
	            	readChar();
	            	Token nextTk = new Token(TokenType.EOF, riga, "EOF");
	            	return nextTk;
	            }

	            if(letters.contains(nextChar)) {
	                return scanId(nextChar);
	            }

	            if (operTkType.containsKey(nextChar) || delimTkType.containsKey(nextChar)) {
	                return scanOperator(nextChar); 
	            }

	            if(digits.contains(nextChar)) {
	                return scanNumber(nextChar);
	            }

	            // Aggiungi la stampa per capire quale carattere è non valido
	            System.out.println("Carattere non valido trovato: " + nextChar);
	            
	            // Consuma il carattere non valido
	            readChar(); 

	            // Lancia l'eccezione per carattere non valido
	            throw new LexicalException("Errore: carattere non valido");
	        }
	    } catch (IOException e) {
	        throw new LexicalException("\nErrore di I/O\n\n");
	    }
	}*/

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
	
	/*
	public Token scanId() throws IOException {
	    StringBuilder sb = new StringBuilder();
	    
	    // Inizia con il carattere già letto (peekChar)
	    char currentChar = peekChar();
	    System.out.println("Inizio con il carattere: " + currentChar);
	    sb.append(currentChar);

	    // Continua a leggere finché il carattere è una lettera o una cifra
	    while (Character.isLetterOrDigit(peekChar())) {
	        currentChar = peekChar();
	        System.out.println("Carattere corrente: " + currentChar);
	        
	        sb.append(readChar());
	        System.out.println("Carattere aggiunto all'identificatore: " + currentChar);
	    }

	    String idString = sb.toString();
	    System.out.println("Identificatore completo: " + idString);

	    // Controlla se la stringa corrisponde a una parola chiave
	    if (keyWordsTkType.containsKey(idString)) {
	        System.out.println("Parola chiave trovata: " + idString);
	        return new Token(keyWordsTkType.get(idString), riga, idString);
	    }

	    // Altrimenti, restituisci un identificatore generico
	    System.out.println("Restituito identificatore generico: " + idString);
	    return new Token(TokenType.ID, riga, idString);
	}*/



	/*
	public Token scanId() throws IOException {
	    StringBuilder id = new StringBuilder();
	    
	    // Non chiamare readChar() all'inizio, ma usa il carattere già letto
	  
	    
	    System.out.println("Inizializzo scanId con nextChar: " + nextChar);
	    id.append(nextChar);
	    
	  
	
	    
	    nextChar = readChar();
	    System.out.println("Dopo readChar, nextChar: " + nextChar);
	    
	    // Continua a leggere finché il carattere è una lettera o una cifra
	    while (Character.isLetterOrDigit(nextChar)) {
	        id.append(nextChar);
	        nextChar = readChar();
	        System.out.println("Dentro il ciclo, nextChar: " + nextChar);
	    }

	    // Effettua un "pushback" dell'ultimo carattere letto
	    buffer.unread(nextChar);

	    String idString = id.toString();
	    System.out.println("idString: " + idString);

	    // Controlla se la stringa corrisponde a una parola chiave
	    if (keyWordsTkType.containsKey(idString)) {
	        return new Token(keyWordsTkType.get(idString), riga, idString);
	    }

	    // Altrimenti, restituisci un identificatore generico
	    return new Token(TokenType.ID, riga, idString);
	}*/


	/*
	// private Token scanId()
	private Token scanId(char nextChar) throws IOException {
	    StringBuilder id = new StringBuilder();
	    
	    nextChar = readChar();
	   
	    

	    // Continua a leggere finché il carattere è una lettera o una cifra
	    while (Character.isLetterOrDigit(nextChar)) {
	        id.append(nextChar);
	        nextChar = readChar();
	    }

	    // Effettua un "pushback" dell'ultimo carattere letto
	    buffer.unread(nextChar);

	    String idString = id.toString();

	    // Controlla se la stringa corrisponde a una parola chiave
	    if (keyWordsTkType.containsKey(idString)) {
	        return new Token(keyWordsTkType.get(idString), riga, idString);
	    }

	    // Altrimenti, restituisci un identificatore generico
	    return new Token(TokenType.ID, riga, idString);
	}*/



	
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

		    // Gestione parte intera
		    while (digits.contains(peekChar())) {
		        sb.append(readChar());
		    }

		    // Gestione decimali
		    if (peekChar() == '.') {
		        if (hasDecimalPoint) {
		            throw new LexicalException("Numero non valido: troppi punti decimali alla riga " + riga);
		        }
		        hasDecimalPoint = true;
		        isFloat = true;
		        sb.append(readChar());

		        // Conta i caratteri dopo il punto
		        while (digits.contains(peekChar())) {
		            sb.append(readChar());
		            decimalCount++;
		            if (decimalCount > 5) {
		                throw new LexicalException("Numero decimale non valido alla riga " + riga);
		            }
		        }

		        // Controlla se ci sono numeri dopo il punto
		        if (decimalCount == 0) {
		            throw new LexicalException("Numero decimale non valido alla riga " + riga);
		        }
		    }

		    // Controllo per ulteriori punti decimali
		    if (peekChar() == '.') {
		        throw new LexicalException("Numero decimale non valido alla riga " + riga);
		    }

		    String number = sb.toString();
		    return new Token(isFloat ? TokenType.FLOAT : TokenType.INT, riga, number);
		}

	   
	  /*
	   private Token scanNumber() throws IOException, LexicalException {
		    StringBuilder sb = new StringBuilder();
		    boolean isFloat = false;
		    int decimalCount = 0;

		    // Gestione parte intera
		    while (digits.contains(peekChar())) {
		        sb.append(readChar());
		    }

		    // Gestione decimali
		    if (peekChar() == '.') {
		    	
		        isFloat = true;
		        sb.append(readChar());
		        
		        // Conta tutti i decimali disponibili per verificare se sono troppi
		        while (digits.contains(peekChar())) {
		            if (decimalCount >= 5) {
		                // Se abbiamo già 5 decimali e ce ne sono altri, è un errore
		                throw new LexicalException("Numero decimale non valido alla riga " + riga);
		            }
		            else if (peekChar() == '.') {
		            	throw new LexicalException("Numero non valido: troppi punti decimali alla riga " + riga);
		            }
		            sb.append(readChar());
		            decimalCount++;
		        }

		        if (decimalCount == 0) {
		            throw new LexicalException("Numero decimale non valido alla riga " + riga);
		        }
		    }
		    
		
		    String number = sb.toString();
		    return new Token(isFloat ? TokenType.TYFLOAT : TokenType.TYINT, riga, number);
		}
	   */













	

	private char readChar() throws IOException {
		return ((char) this.buffer.read());
	}

	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
}

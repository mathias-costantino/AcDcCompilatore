package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;

import org.junit.Test;

import scanner.LexicalException;
import scanner.Scanner;
import token.Token;
import token.TokenType;

public class TestScanner {
	
	@Test
	public void testcaratteriNonCaratteri() throws IOException {
	    String path = "CompilatoreAcDc/src/test/data/testScanner/caratteriNonCaratteri.txt";
	    Scanner scanner = new Scanner(path);

	    LexicalException thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Carattere illegale alla riga 1: ^", thrown.getMessage());
	     
	    thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Carattere illegale alla riga 1: &", thrown.getMessage());
	    
	    assertDoesNotThrow(() -> {
	        Token tokenSemi = scanner.nextToken();
	        assertEquals(TokenType.SEMI, tokenSemi.getTipo());
	        assertEquals(2, tokenSemi.getRiga());
	    });
	    
	    thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Carattere illegale alla riga 2: |", thrown.getMessage());
	    
	    assertDoesNotThrow(() -> {
	        Token tokenPlus = scanner.nextToken();
	        assertEquals(TokenType.PLUS, tokenPlus.getTipo());
	        assertEquals(3, tokenPlus.getRiga());
	    });
	    
	    assertTrue(true);
	}

	@Test
	public void erroriNumbers() throws IOException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/erroriNumbers.txt";
	    Scanner scanner = new Scanner(path);
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.INT, token.getTipo());
	    	assertEquals("0", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.INT, token.getTipo());
	    	assertEquals("33", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    LexicalException thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Numero decimale non valido alla riga 3", thrown.getMessage());
	    
	    thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Numero decimale non valido alla riga 5", thrown.getMessage());
	}
	
	@Test
	public void testEOF() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testEOF.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Token token = scanner.nextToken();
	    assertTrue(token.getTipo() == TokenType.EOF);
	    assertEquals(4, token.getRiga());
	}
	
	@Test
	public void testFloat() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testFloat.txt";
	    Scanner scanner = new Scanner(path);
	      
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.FLOAT, token.getTipo());
	    	assertEquals("098.8095", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    LexicalException thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Numero decimale non valido alla riga 2", thrown.getMessage());
	    
	    thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Numero decimale non valido alla riga 3", thrown.getMessage());
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.FLOAT, token.getTipo());
	    	assertEquals("89.99999", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	}
	
	@Test
	public void testGenerale() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testGenerale.txt";
	    Scanner scanner = new Scanner(path);
	      
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TYINT, token.getTipo());
	    	assertEquals("int", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("temp", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.SEMI, token.getTipo());
	    	assertEquals(";", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("temp", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.OP_ASSIGN, token.getTipo());
	    	assertEquals("+=", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    LexicalException thrown = assertThrows(LexicalException.class, () -> {
	        scanner.nextToken();
	    });
	    assertEquals("Numero decimale non valido alla riga 2", thrown.getMessage());
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.SEMI, token.getTipo());
	    	assertEquals(";", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TYFLOAT, token.getTipo());
	    	assertEquals("float", token.getVal());
	    	assertEquals(4, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("b", token.getVal());
	    	assertEquals(4, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.SEMI, token.getTipo());
	    	assertEquals(";", token.getVal());
	    	assertEquals(4, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("b", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ASSIGN, token.getTipo());
	    	assertEquals("=", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("temp", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.PLUS, token.getTipo());
	    	assertEquals("+", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.FLOAT, token.getTipo());
	    	assertEquals("3.2", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.SEMI, token.getTipo());
	    	assertEquals(";", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.PRINT, token.getTipo());
	    	assertEquals("print", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("b", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.SEMI, token.getTipo());
	    	assertEquals(";", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	}
	
	@Test
	public void testId() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testId.txt";
	    Scanner scanner = new Scanner(path);
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("jskjdsfhkjdshkf", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("printl", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("ffloat", token.getVal());
	    	assertEquals(4, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("hhhjj", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	}
	
	@Test
	public void testIdKeyWords() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testIdKeyWords.txt";
	    Scanner scanner = new Scanner(path);
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TYINT, token.getTipo());
	    	assertEquals("int", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("inta", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TYFLOAT, token.getTipo());
	    	assertEquals("float", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.PRINT, token.getTipo());
	    	assertEquals("print", token.getVal());
	    	assertEquals(3, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("nome", token.getVal());
	    	assertEquals(4, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("intnome", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TYINT, token.getTipo());
	    	assertEquals("int", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ID, token.getTipo());
	    	assertEquals("nome", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	}
	

	@Test
	public void testInt() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testInt.txt";
	    Scanner scanner = new Scanner(path);
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.INT, token.getTipo());
	    	assertEquals("0050", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.INT, token.getTipo());
	    	assertEquals("698", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.INT, token.getTipo());
	    	assertEquals("560099", token.getVal());
	    	assertEquals(4, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.INT, token.getTipo());
	    	assertEquals("1234", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	}

	@Test
	public void testKeywords() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testKeywords.txt";
	    Scanner scanner = new Scanner(path);
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.PRINT, token.getTipo());
	    	assertEquals("print", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TYFLOAT, token.getTipo());
	    	assertEquals("float", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TYINT, token.getTipo());
	    	assertEquals("int", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	}
	
	@Test
	public void testOpsDels() throws IOException, LexicalException {
		String path = "CompilatoreAcDc/src/test/data/testScanner/testOpsDels.txt";
	    Scanner scanner = new Scanner(path);
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.PLUS, token.getTipo());
	    	assertEquals("+", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.OP_ASSIGN, token.getTipo());
	    	assertEquals("/=", token.getVal());
	    	assertEquals(1, token.getRiga());
	    });

	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.MINUS, token.getTipo());
	    	assertEquals("-", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.TIMES, token.getTipo());
	    	assertEquals("*", token.getVal());
	    	assertEquals(2, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.DIVIDE, token.getTipo());
	    	assertEquals("/", token.getVal());
	    	assertEquals(3, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.OP_ASSIGN, token.getTipo());
	    	assertEquals("+=", token.getVal());
	    	assertEquals(5, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ASSIGN, token.getTipo());
	    	assertEquals("=", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.OP_ASSIGN, token.getTipo());
	    	assertEquals("-=", token.getVal());
	    	assertEquals(6, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.MINUS, token.getTipo());
	    	assertEquals("-", token.getVal());
	    	assertEquals(8, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.ASSIGN, token.getTipo());
	    	assertEquals("=", token.getVal());
	    	assertEquals(8, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.OP_ASSIGN, token.getTipo());
	    	assertEquals("*=", token.getVal());
	    	assertEquals(8, token.getRiga());
	    });
	    
	    assertDoesNotThrow(() -> {
	    	Token token = scanner.nextToken();
	    	assertEquals(TokenType.SEMI, token.getTipo());
	    	assertEquals(";", token.getVal());
	    	assertEquals(10, token.getRiga());
	    });
	}
}




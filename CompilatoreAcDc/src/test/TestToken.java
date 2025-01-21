package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import token.Token;
import token.TokenType;

/**
 * @author Mathias Costantino, 20043922
 */
public class TestToken {

	@Test
	public void test() {
		
		/*
		INT,
		FLOAT,
		ID,
		TYINT,
		TYFLOAT,
		PRINT,
		OP_ASSIGN,
		ASSIGN,
		PLUS,
		MINUS,
		TIMES,
		DIVIDE,
		SEMI,
		EOF;
		 */
		
		Token token0 = new Token(TokenType.INT, 1, "4");
		Token token1 = new Token(TokenType.FLOAT, 5, "5.23");
		Token token2 = new Token(TokenType.ID, 2, "parola");
		Token token3 = new Token(TokenType.TYINT, 1);
		Token token4 = new Token(TokenType.TYFLOAT, 11);
		Token token5 = new Token(TokenType.PRINT, 1);
		Token token6 = new Token(TokenType.OP_ASSIGN, 1);
		Token token7 = new Token(TokenType.ASSIGN, 2);
		Token token8 = new Token(TokenType.PLUS, 4);
		Token token9 = new Token(TokenType.MINUS, 5);
		Token token10 = new Token(TokenType.TIMES, 1);
		Token token11 = new Token(TokenType.DIVIDE, 1);
		Token token12 = new Token(TokenType.SEMI, 1);
		Token token13 = new Token(TokenType.EOF, 1);
		
		assertEquals("<INT,r:1,4>", token0.toString());
		assertEquals("<FLOAT,r:5,5.23>", token1.toString());
		assertEquals("<ID,r:2,parola>", token2.toString());
		assertEquals("<TYINT,r:1>", token3.toString());
		assertEquals("<TYFLOAT,r:11>", token4.toString());
		assertEquals("<PRINT,r:1>", token5.toString());
		assertEquals("<OP_ASSIGN,r:1>", token6.toString());
		assertEquals("<ASSIGN,r:2>", token7.toString());
		assertEquals("<PLUS,r:4>", token8.toString());
		assertEquals("<MINUS,r:5>", token9.toString());
		assertEquals("<TIMES,r:1>", token10.toString());
		assertEquals("<DIVIDE,r:1>", token11.toString());
		assertEquals("<SEMI,r:1>", token12.toString());
		assertEquals("<EOF,r:1>", token13.toString());
		
		
	}

}

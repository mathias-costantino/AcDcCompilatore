package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import ast.NodeProgram;
import ast.TypeDescriptor;
import parser.Parser;
import parser.SyntacticException;
import scanner.LexicalException;
import scanner.Scanner;
import symbolTable.SymbolTable;
import visitor.*;

public class TestTypeChecking {
	
	@Test
	public void test1_dicRipetute() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testTypeChecking/1_dicRipetute.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var visitor = new TypeCheckingVisitor();

	    System.out.println("Inizio visita del programma...");
	    nP.accept(visitor);
	    System.out.println("Fine visita del programma.");

	    // Verifica il tipo restituito
	    System.out.println("Tipo restituito dal visitatore: " + visitor.getResType().getTipo());
	    System.out.println("Messaggio restituito: " + visitor.getResType().getMsg());

	    assertEquals(TypeDescriptor.TipoTD.ERROR, visitor.getResType().getTipo());
	    assertTrue(visitor.getResType().getMsg().contains("Errore: Variabile a già dichiarata."));
	}

	
	@Test
	public void test2_idNonDec() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testTypeChecking/2_idNonDec.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var visitor = new TypeCheckingVisitor();

	    System.out.println("Inizio visita del programma...");
	    nP.accept(visitor);
	    System.out.println("Fine visita del programma.");

	    // Verifica il tipo restituito
	    System.out.println("Tipo restituito dal visitatore: " + visitor.getResType().getTipo());
	    System.out.println("Messaggio restituito: " + visitor.getResType().getMsg());

	    assertEquals(TypeDescriptor.TipoTD.ERROR, visitor.getResType().getTipo());
	    assertTrue(visitor.getResType().getMsg().contains("Errore: Variabile b non dichiarata"));
	}
	
	
	@Test
	public void test3_idNonDec() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testTypeChecking/3_idNonDec.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var visitor = new TypeCheckingVisitor();

	    System.out.println("Inizio visita del programma...");
	    nP.accept(visitor);
	    System.out.println("Fine visita del programma.");

	    // Verifica il tipo restituito
	    System.out.println("Tipo restituito dal visitatore: " + visitor.getResType().getTipo());
	    System.out.println("Messaggio restituito: " + visitor.getResType().getMsg());

	    assertEquals(TypeDescriptor.TipoTD.ERROR, visitor.getResType().getTipo());
	    assertTrue(visitor.getResType().getMsg().contains("Errore: Variabile c non dichiarata"));
	}
	
	@Test
	public void test4_tipoNonCompatibile() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testTypeChecking/4_tipoNonCompatibile.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var visitor = new TypeCheckingVisitor();

	    System.out.println("Inizio visita del programma...");
	    nP.accept(visitor);
	    System.out.println("Fine visita del programma.");

	    // Verifica il tipo restituito
	    System.out.println("Tipo restituito dal visitatore: " + visitor.getResType().getTipo());
	    System.out.println("Messaggio restituito: " + visitor.getResType().getMsg());

	    assertEquals(TypeDescriptor.TipoTD.ERROR, visitor.getResType().getTipo());
	    assertTrue(visitor.getResType().getMsg().contains("Errore: Tipo non compatibile nell'assegnazione a b"));
	}
	
	@Test
	public void test5_corretto() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testTypeChecking/5_corretto.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var visitor = new TypeCheckingVisitor();

	    System.out.println("Inizio visita del programma...");
	    nP.accept(visitor);
	    System.out.println("Fine visita del programma.");

	    // Verifica il tipo restituito
	    System.out.println("Tipo restituito dal visitatore: " + visitor.getResType().getTipo());
	    System.out.println("Messaggio restituito: " + visitor.getResType().getMsg());

	    assertEquals(TypeDescriptor.TipoTD.OK, visitor.getResType().getTipo());
	    //vuoto perchè non sono presenti errori
	    assertTrue(visitor.getResType().getMsg().contains("")); 
	}
	
	@Test
	public void test6_corretto() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testTypeChecking/6_corretto.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var visitor = new TypeCheckingVisitor();

	    System.out.println("Inizio visita del programma...");
	    nP.accept(visitor);
	    System.out.println("Fine visita del programma.");

	    // Verifica il tipo restituito
	    System.out.println("Tipo restituito dal visitatore: " + visitor.getResType().getTipo());
	    System.out.println("Messaggio restituito: " + visitor.getResType().getMsg());

	    assertEquals(TypeDescriptor.TipoTD.OK, visitor.getResType().getTipo());
	    //vuoto perchè non sono presenti errori
	    assertTrue(visitor.getResType().getMsg().contains("")); 
	}
	
	@Test
	public void test7_corretto() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testTypeChecking/7_corretto.txt";
	    Scanner scanner = new Scanner(path);
	    
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var visitor = new TypeCheckingVisitor();

	    System.out.println("Inizio visita del programma...");
	    nP.accept(visitor);
	    System.out.println("Fine visita del programma.");

	    // Verifica il tipo restituito
	    System.out.println("Tipo restituito dal visitatore: " + visitor.getResType().getTipo());
	    System.out.println("Messaggio restituito: " + visitor.getResType().getMsg());

	    assertEquals(TypeDescriptor.TipoTD.OK, visitor.getResType().getTipo());
	    //vuoto perchè non sono presenti errori
	    assertTrue(visitor.getResType().getMsg().contains("")); 
	}
}

package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ast.NodeProgram;
import ast.TypeDescriptor;
import parser.Parser;
import parser.SyntacticException;
import scanner.LexicalException;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

public class testCodeGenerator {
/*
	@Test
	public void test1_assign() throws LexicalException, SyntacticException, IOException {
	    String path = "CompilatoreAcDc/src/test/data/testCodeGenerator/1_assign.txt";
	    Scanner scanner = new Scanner(path);
	    Parser parser = new Parser(scanner);
	    NodeProgram nP = parser.parse();
	    var tcVisit = new TypeCheckingVisitor();
        nP.accept(tcVisit);
        var cgVisit = new CodeGeneratorVisitor();
        nP.accept(cgVisit);
        
        assertEquals(cgVisit.getLog(), "");

	}
	*/
	
	@Test
	public void test1_assign() throws LexicalException, SyntacticException, IOException {

		String path = "CompilatoreAcDc/src/test/data/testCodeGenerator/1_assign.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        
        // Prima il type checking
        var tcVisit = new TypeCheckingVisitor();
        nP.accept(tcVisit);
        assertEquals(TypeDescriptor.TipoTD.OK, tcVisit.getResType().getTipo());
        
        // Poi la generazione del codice
        var cgVisit = new CodeGeneratorVisitor();
        nP.accept(cgVisit);
        
        // Verifica che non ci siano errori
        assertEquals("", cgVisit.getLog());
        // Verifica il codice generato
        assertEquals("1 6 / sa la p P", cgVisit.getCodiceDc());

	}
}

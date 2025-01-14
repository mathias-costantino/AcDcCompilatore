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
	
	 @Test
	    public void test2_divisioni() throws LexicalException, SyntacticException, IOException {
	        String path = "CompilatoreAcDc/src/test/data/testCodeGenerator/2_divsioni.txt";
	        Scanner scanner = new Scanner(path);
	        Parser parser = new Parser(scanner);
	        NodeProgram nP = parser.parse();
	        
	        var tcVisit = new TypeCheckingVisitor();
	        nP.accept(tcVisit);
	        assertEquals(TypeDescriptor.TipoTD.OK, tcVisit.getResType().getTipo());
	        
	        var cgVisit = new CodeGeneratorVisitor();
	        nP.accept(cgVisit);
	        
	        assertEquals("", cgVisit.getLog());
	 
	        //int a=0; 0 sa
	        //a += 1; la 1 + sa
	        //int b=6; 6 sb
	        //float temp;
	        //temp =  1.0 / 6 + a / b;  1.0 6 / la lb / + sc
	        //print a; la p P
	        //print b; lb p P
	        //print temp; lc p P
	        assertEquals("0 sa la 1 + sa 6 sb 1.0 6 / la lb / + sc la p P lb p P lc p P", cgVisit.getCodiceDc());
	    }
	 
	   @Test
	    public void test3_generale() throws LexicalException, SyntacticException, IOException {
	        String path = "CompilatoreAcDc/src/test/data/testCodeGenerator/3_generale.txt";
	        Scanner scanner = new Scanner(path);
	        Parser parser = new Parser(scanner);
	        NodeProgram nP = parser.parse();
	        
	        var tcVisit = new TypeCheckingVisitor();
	        nP.accept(tcVisit);
	        assertEquals(TypeDescriptor.TipoTD.OK, tcVisit.getResType().getTipo());
	        
	        var cgVisit = new CodeGeneratorVisitor();
	        nP.accept(cgVisit);
	        
	        assertEquals("", cgVisit.getLog());
	        
	        //int i; 
	        //float f;
	        //i = 5+3; 5 3 + sa
	        //f = i + 0.5; la 0.5 + sb
	        //print i; la p P
	        //f /= 4; lb 4 / sb
	        //float flo;
	        //print f; lb p P
	        //flo = f - 1; lb 1 - sc
	        //flo *= f; lc lb * sc
	        //print flo; lc p P

	        assertEquals("5 3 + sa la 0.5 + sb la p P lb 4 / sb lb p P lb 1 - sc lc lb * sc lc p P", cgVisit.getCodiceDc());
	    }
	   
	   @Test
	    public void test4_registriFiniti() throws LexicalException, SyntacticException, IOException {
	        String path = "CompilatoreAcDc/src/test/data/testCodeGenerator/4_registriFiniti.txt";
	        Scanner scanner = new Scanner(path);
	        Parser parser = new Parser(scanner);
	        NodeProgram nP = parser.parse();
	        
	        var tcVisit = new TypeCheckingVisitor();
	        nP.accept(tcVisit);
	        assertEquals(TypeDescriptor.TipoTD.OK, tcVisit.getResType().getTipo());
	        
	        var cgVisit = new CodeGeneratorVisitor();
	        nP.accept(cgVisit);
	        
	        assertTrue(cgVisit.getLog().contains("Errore: registri esauriti"));
	        assertEquals("6 2 / sa la p P", cgVisit.getCodiceDc());
	    }
	 
}

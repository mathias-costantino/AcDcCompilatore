package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import parser.Parser;
import parser.SyntacticException;
import scanner.LexicalException;

import org.junit.Test;

public class TestParser {

	@Test
	public void testParserCorretto1() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserCorretto1.txt");
			Parser parser = new Parser(scanner);
			assertDoesNotThrow(() -> parser.parse());
	}
	
	@Test
	public void testParserCorretto2() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserCorretto2.txt");
			Parser parser = new Parser(scanner);
			assertDoesNotThrow(() -> parser.parse());
	}
	
	@Test
	public void testParserEcc_0() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_0.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Operatore di assegnamento non valido", thrown.getMessage());
	}
	
	@Test
	public void testParserEcc_1() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_1.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Valore non valido", thrown.getMessage());
	}
	
	@Test
	public void testParserEcc_2() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_2.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Token non valido in DSs: INT", thrown.getMessage());
	}
	
	@Test
	public void testParserEcc_3() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_3.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Operatore di assegnamento non valido", thrown.getMessage());
	}
	
	@Test
	public void testParserEcc_4() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_4.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Token atteso ID ma trovato INT alla riga 2", thrown.getMessage());
	}
	
	@Test
	public void testParserEcc_5() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_5.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Token atteso ID ma trovato INT alla riga 3", thrown.getMessage());
	}
	
	@Test
	public void testParserEcc_6() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_6.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Token atteso ID ma trovato TYFLOAT alla riga 3", thrown.getMessage());
	}
	
	@Test
	public void testParserEcc_7() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testParserEcc_7.txt");
			Parser parser = new Parser(scanner);
			SyntacticException thrown = assertThrows(SyntacticException.class, ()->{
				parser.parse();
			});
			assertEquals("Token atteso ID ma trovato ASSIGN alla riga 2", thrown.getMessage());
	}
	
	@Test
	public void testSoloDich() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testSoloDich.txt");
			Parser parser = new Parser(scanner);
			assertDoesNotThrow(() -> parser.parse());
	}
	
	@Test
	public void testSoloDichPrint() throws LexicalException, SyntacticException, IOException {
			scanner.Scanner scanner = new scanner.Scanner("CompilatoreAcDc/src/test/data/testParser/testSoloDichPrint.txt");
			Parser parser = new Parser(scanner);
			assertDoesNotThrow(() -> parser.parse());
	}
}

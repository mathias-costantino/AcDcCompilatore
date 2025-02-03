package parser;

import java.io.IOException;
import java.util.ArrayList;

import ast.*;
import token.*;
import scanner.*;

/**
 * @author Mathias Costantino, 20043922
 */
public class Parser {
    private Scanner scanner;
    private String errorMessage = "Errore durante la scansione";

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public NodeProgram parse() throws LexicalException, SyntacticException, IOException {
        return parsePrg();
    }

    private NodeProgram parsePrg() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }
        
        switch(tk.getTipo()) {
            case TYINT:
            case TYFLOAT:
            case ID:
            case PRINT:
            case EOF:
                ArrayList<NodeDecSt> decSts = parseDSs();
                match(TokenType.EOF);
                return new NodeProgram(decSts);
            default:
                throw new SyntacticException("Token non valido all'inizio del programma: " + tk.getTipo() + 
                                           " alla riga " + tk.getRiga());
        }
    }

    private ArrayList<NodeDecSt> parseDSs() throws LexicalException, SyntacticException, IOException {
  
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case TYINT:
            case TYFLOAT:
                NodeDecl decl = parseDcl();
                ArrayList<NodeDecSt> rest = parseDSs();
                rest.add(0, decl);
                return rest;
            case ID:
            case PRINT:
                NodeStm stm = parseStm();
                ArrayList<NodeDecSt> rest2 = parseDSs();
                rest2.add(0, stm);
                return rest2;
            case EOF:
                return new ArrayList<>();
            default:
                throw new SyntacticException("Token non valido in DSs: " + tk.getTipo());
        }
    }

    private NodeDecl parseDcl() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case TYINT:
                match(tk.getTipo());  // consuma il token TYINT
                Token idToken = scanner.peekToken();  // guarda il token ID ma non consumarlo ancora
                NodeId id = new NodeId(idToken.getVal());  // crea NodeId con il valore dell'ID
                match(TokenType.ID);  // ora consuma il token ID
                NodeExpr init = parseDclP();
                return new NodeDecl(id, LangType.INT, init);
                
            case TYFLOAT:
                match(tk.getTipo());  // consuma il token TYFLOAT
                Token idToken2 = scanner.peekToken();  // guarda il token ID ma non consumarlo ancora
                NodeId id2 = new NodeId(idToken2.getVal());  // crea NodeId con il valore dell'ID
                match(TokenType.ID);  // ora consuma il token ID
                NodeExpr init2 = parseDclP();
                return new NodeDecl(id2, LangType.FLOAT, init2);
                
            default:
                throw new SyntacticException("Tipo non valido nella dichiarazione");
        }
    }

    private NodeExpr parseDclP() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case SEMI:
                match(TokenType.SEMI);
                return null;
            case ASSIGN:
                match(TokenType.ASSIGN);
                NodeExpr expr = parseExp();
                match(TokenType.SEMI);
                return expr;
            default:
                throw new SyntacticException("Token non valido in DclP");
        }
    }

    private NodeStm parseStm() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case ID:
                Token idToken = scanner.peekToken();
                match(TokenType.ID);
                NodeId id = new NodeId(idToken.getVal());

                LangOper op = parseOp();
                NodeExpr expr = parseExp();
                match(TokenType.SEMI);
                
                if (op != null) {
                    // Per operatori composti (+=, -=, etc.)
                    NodeExpr left = new NodeDeref(id);
                    NodeBinOp binOp = new NodeBinOp(op, left, expr);
                    return new NodeAssign(id, binOp);
                } else {
                    // Per assegnamento semplice (=)
                    return new NodeAssign(id, expr);
                }
            case PRINT:
                match(TokenType.PRINT);
                Token idToken2 = scanner.peekToken();
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodePrint(new NodeId(idToken2.getVal()));
            default:
                throw new SyntacticException("Statement non valido");
        }
    }

    private LangOper parseOp() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case ASSIGN:
                match(TokenType.ASSIGN);
                return null;
            case OP_ASSIGN:
                match(TokenType.OP_ASSIGN);
                String operator = tk.getVal();  
                switch(operator) {
                    case "+=":
                        return LangOper.PLUS;
                    case "-=":
                        return LangOper.MINUS;
                    case "*=":
                        return LangOper.TIMES;
                    case "/=":
                        return LangOper.DIVIDE;
                    default:
                        throw new SyntacticException("Operatore di assegnamento composto non supportato: " + operator);
                }
            default:
                throw new SyntacticException("Operatore di assegnamento non valido");
        }
    }

    private NodeExpr parseExp() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }
    	
        switch (tk.getTipo()) {
		case ID, FLOAT, INT -> {
			NodeExpr tr = parseTr();
			NodeExpr expP = parseExpP(tr);
			return expP;
		}
		default -> {
			throw new SyntacticException("ID, FLOAT o INT");
		}
		}
    }

    private NodeExpr parseExpP(NodeExpr left) throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case PLUS:
                match(TokenType.PLUS);
                NodeExpr term = parseTr();
                return parseExpP(new NodeBinOp(LangOper.PLUS, left, term));
            case MINUS:
                match(TokenType.MINUS);
                term = parseTr();
                return parseExpP(new NodeBinOp(LangOper.MINUS, left, term));
            case SEMI:
                return left;
            default:
                throw new SyntacticException("Operatore non valido in espressione");
        }
    }

    private NodeExpr parseTr() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }
        
        NodeExpr val = parseVal();
        return parseTrP(val);
    }

    private NodeExpr parseTrP(NodeExpr left) throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case TIMES:
                match(TokenType.TIMES);
                NodeExpr val = parseVal();
                return parseTrP(new NodeBinOp(LangOper.TIMES, left, val));
            case DIVIDE:
                match(TokenType.DIVIDE);
                val = parseVal();
                return parseTrP(new NodeBinOp(LangOper.DIVIDE, left, val));
            case PLUS:
            case MINUS:
            case SEMI:
                return left;
            default:
                throw new SyntacticException("Operatore non valido nel termine");
        }
    }

    private NodeExpr parseVal() throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }

        switch(tk.getTipo()) {
            case INT:
                match(TokenType.INT);
                return new NodeCost(tk.getVal(), LangType.INT);
            case FLOAT:
                match(TokenType.FLOAT);
                return new NodeCost(tk.getVal(), LangType.FLOAT);
            case ID:
                match(TokenType.ID);
                return new NodeDeref(new NodeId(tk.getVal()));
            default:
                throw new SyntacticException("Valore non valido");
        }
    }

    private Token match(TokenType type) throws LexicalException, SyntacticException, IOException {
        Token tk;
        try {
            tk = scanner.peekToken();
            System.out.println("Token atteso: " + type + " , Token trovato: " + tk.getTipo());
        } catch (Exception e) {
            throw new SyntacticException(errorMessage);
        }
        if (type.equals(tk.getTipo())) {
        	   try {
        		   Token consumedToken = scanner.nextToken();
                   return consumedToken;  
               } catch (Exception e) {
                   throw new SyntacticException(errorMessage);
               }
        } else {
            throw new SyntacticException("Token atteso " + type + " ma trovato " + tk.getTipo() + 
                                       " alla riga " + tk.getRiga());
        }
    }
}
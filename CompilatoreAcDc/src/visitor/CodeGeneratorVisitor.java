package visitor;

import ast.*;
import symbolTable.SymbolTable;
import symbolTable.Attributes;

public class CodeGeneratorVisitor implements IVisitor {
    private String codiceDc;      // mantiene il codice della visita
    private String log;           // per l'eventuale errore nella generazione del codice
    private int nextRegisterIndex;    // per la gestione dei registri disponibili
    private static final String REGISTERS = "abcdefghijklmnopqrstuvwxyz";
    
    public CodeGeneratorVisitor() {
        this.codiceDc = "";
        this.log = "";
        this.nextRegisterIndex = 0;
    }
    
    public String getCodiceDc() {
        return codiceDc;
    }
    
    public String getLog() {
        return log;
    }
    
    private char newRegister() {
        if (nextRegisterIndex >= REGISTERS.length()) {
            log = "Errore: registri esauriti";
            return '\0';
        }
        return REGISTERS.charAt(nextRegisterIndex++);
    }
    
    @Override
    public void visit(NodeProgram node) {
        for (NodeDecSt decSt : node.getDecSts()) {
            if (!log.isEmpty()) return;  // Se c'è un errore, non continuare
            ((NodeAST)decSt).accept(this);
        }
    }
    
    @Override
    public void visit(NodeDecl node) {
        // Genera un nuovo registro
        char reg = newRegister();
        if (!log.isEmpty()) return;
        
        // Assegna il registro alla variabile nella symbol table
        Attributes attr = SymbolTable.lookup(node.getId().getName());
        if (attr != null) {
            attr.setRegister(reg);
        }
        
        // Se c'è un'inizializzazione, genera il codice come per un assegnamento
        if (node.getInit() != null) {
            node.getInit().accept(this);
            if (!log.isEmpty()) return;
            codiceDc += " s" + reg;  // Aggiungo spazio prima di s
        }
    }
    
    @Override
    public void visit(NodeAssign node) {
        // Genera codice per l'espressione a destra
        node.getExpr().accept(this);
        if (!log.isEmpty()) return;
        
        // Ottiene il registro dell'identificatore a sinistra
        Attributes attr = SymbolTable.lookup(node.getId().getName());
        if (attr != null) {
            codiceDc += " s" + attr.getRegistro();  // Aggiungo spazio prima di s
        }
    }
    
    @Override
    public void visit(NodeBinOp node) {
        node.getLeft().accept(this);
        if (!log.isEmpty()) return;
        String leftCode = codiceDc;
        
        node.getRight().accept(this);
        if (!log.isEmpty()) return;
        
        codiceDc = leftCode + " " + codiceDc;  // Aggiungo spazio tra operandi
        
        // Aggiunge l'operatore appropriato con spazi
        switch (node.getOp()) {
            case PLUS:
                codiceDc += " +";
                break;
            case MINUS:
                codiceDc += " -";
                break;
            case TIMES:
                codiceDc += " *";
                break;
            case DIVIDE:
                codiceDc += " /";
                break;
            case DIV_FLOAT:
                codiceDc += " 5k/0k";  // Imposta precisione 5 per div float, poi resetta
                break;
        }
    }
    
    @Override
    public void visit(NodePrint node) {
        node.getId().accept(this);
        if (!log.isEmpty()) return;
        codiceDc += " p P";  // Aggiungo spazio prima di p e tra p e P
    }
    
    @Override
    public void visit(NodeDeref node) {
        node.getId().accept(this);
    }
    
    @Override
    public void visit(NodeId node) {
        Attributes attr = SymbolTable.lookup(node.getName());
        if (attr != null) {
            if (!codiceDc.isEmpty() && !codiceDc.endsWith(" ")) {
                codiceDc += " ";
            }
            codiceDc += "l" + attr.getRegistro();  // Il load non necessita di spazio prima di l
        }
    }
    
    @Override
    public void visit(NodeCost node) {
        // Per le costanti, aggiunge spazio se necessario
        if (!codiceDc.isEmpty() && !codiceDc.endsWith(" ")) {
            codiceDc += " ";
        }
        codiceDc += node.getValue();
    }
}
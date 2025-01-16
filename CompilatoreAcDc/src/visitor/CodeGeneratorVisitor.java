package visitor;

import ast.*;
import symbolTable.SymbolTable;
import symbolTable.Attributes;

public class CodeGeneratorVisitor implements IVisitor {
    private String codiceDc;      
    private String log;           
    private int nextRegisterIndex;    
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
        
        Attributes attr = SymbolTable.lookup(node.getId().getName());
        if (attr != null) {
            attr.setRegister(reg);
        }
        
        if (node.getInit() != null) {
            node.getInit().accept(this);
            if (!log.isEmpty()) return;
            codiceDc += " s" + reg;
        }
    }
    
    @Override
    public void visit(NodeAssign node) {
        node.getExpr().accept(this);
        if (!log.isEmpty()) return;
        
        Attributes attr = SymbolTable.lookup(node.getId().getName());
        if (attr != null) {
            codiceDc += " s" + attr.getRegistro();
        }
    }
    
    @Override
    public void visit(NodeBinOp node) {
        node.getLeft().accept(this);
        if (!log.isEmpty()) return;
        String leftCode = codiceDc;

        codiceDc = "";  // Reset per evitare duplicazione
        node.getRight().accept(this);
        if (!log.isEmpty()) return;
        String rightCode = codiceDc;

        codiceDc = leftCode + " " + rightCode;
        
        // Verifica se uno degli operandi è float o se l'operazione è DIV_FLOAT
        boolean isFloatOperation = false;
        if (node.getLeft() instanceof NodeCost) {
            isFloatOperation |= ((NodeCost)node.getLeft()).getType() == LangType.FLOAT;
        } else if (node.getLeft() instanceof NodeDeref) {
            Attributes leftAttr = SymbolTable.lookup(((NodeDeref)node.getLeft()).getId().getName());
            isFloatOperation |= (leftAttr != null && leftAttr.getTipo() == LangType.FLOAT);
        }
        
        if (node.getRight() instanceof NodeCost) {
            isFloatOperation |= ((NodeCost)node.getRight()).getType() == LangType.FLOAT;
        } else if (node.getRight() instanceof NodeDeref) {
            Attributes rightAttr = SymbolTable.lookup(((NodeDeref)node.getRight()).getId().getName());
            isFloatOperation |= (rightAttr != null && rightAttr.getTipo() == LangType.FLOAT);
        }
        
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
                // Se uno degli operandi è float o l'operazione è DIV_FLOAT, usa la divisione float
                if (isFloatOperation || node.getOp() == LangOper.DIV_FLOAT) {
                    codiceDc += " 5k / 0k";  // Imposta precisione a 5, divide, resetta a 0
                } else {
                    codiceDc += " /";  
                }
                break;
            case DIV_FLOAT:
                codiceDc += " 5k / 0k";  // Set precision to 5, divide, reset to 0
                break;
        }
    }
    
    @Override
    public void visit(NodePrint node) {
        node.getId().accept(this);
        if (!log.isEmpty()) return;
        codiceDc += " p P";
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
            codiceDc += "l" + attr.getRegistro();
        }
    }
    
    @Override
    public void visit(NodeCost node) {
        if (!codiceDc.isEmpty() && !codiceDc.endsWith(" ")) {
            codiceDc += " ";
        }
        codiceDc += node.getValue();
    }
}

  

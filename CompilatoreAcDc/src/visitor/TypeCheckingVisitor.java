package visitor;

import ast.*;
import symbolTable.SymbolTable;
import symbolTable.Attributes;

/**
 * @author Mathias Costantino, 20043922
 */
public class TypeCheckingVisitor implements IVisitor {
    private TypeDescriptor resType;
    
    public TypeCheckingVisitor() {
        SymbolTable.init(); // Reinizializza la tabella dei simboli per ogni nuovo visitor
    }
    
    public TypeDescriptor getResType() {
        return resType;
    }

    /**
     * Visita il nodo radice del programma.
     * Attraversa tutti i nodi di dichiarazione e statement.
     * Se trova un errore di tipo, interrompe la visita.
     * 
     * @param node Nodo programma da visitare
     */
    @Override
    public void visit(NodeProgram node) {
        for (NodeDecSt decSt : node.getDecSts()) {
            ((NodeAST)decSt).accept(this);
            if (resType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
                return;
            }
        }
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
    }

    /**
     * Visita un nodo identificatore.
     * Verifica che l'identificatore sia stato dichiarato nella symbol table.
     * Imposta il tipo risultante in base al tipo della variabile.
     * 
     * @param node Nodo identificatore da visitare
     */
    @Override
    public void visit(NodeId node) { 
        Attributes attr = SymbolTable.lookup(node.getName());
        if (attr == null) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR, 
                "Errore: Variabile " + node.getName() + " non dichiarata", 0);
            return;
        }
        resType = new TypeDescriptor(attr.getTipo() == LangType.INT ? 
            TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT);
    }
    
    /**
     * Verifica la compatibilità dei tipi se è presente un'inizializzazione.
     * Inserisce la variabile nella symbol table se non è già presente.
     * 
     * @param node Nodo dichiarazione da visitare
     */
    @Override
    public void visit(NodeDecl node) {
        String varName = node.getId().getName();
        //System.out.println("Visita dichiarazione di variabile: " + varName + ", tipo: " + node.getType());

        if (node.getInit() != null) {
            node.getInit().accept(this);
            TypeDescriptor initType = resType;

            if (initType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
                return;
            }

            TypeDescriptor declType = new TypeDescriptor(
                node.getType() == LangType.INT ? 
                TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT);

            if (!declType.compatibile(declType, initType)) {
                resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                    "Errore: Tipo non compatibile nella dichiarazione di " + varName, 0);
                return;
            }
        }

        //System.out.println("Tentativo di inserire variabile: " + varName);
        
        if (!SymbolTable.enter(varName, new Attributes(node.getType()))) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Errore: Variabile " + varName + " già dichiarata.", 0);  
            return;
        }

        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
    }


    /**
     * Verifica la compatibilità dei tipi degli operandi.
     * Gestisce la divisione con float.
     * 
     * @param node Nodo operazione binaria da visitare
     */
    @Override
    public void visit(NodeBinOp node) {
        node.getLeft().accept(this);
        TypeDescriptor leftType = resType;
        node.getRight().accept(this);
        TypeDescriptor rightType = resType;
        
        if (leftType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            resType = leftType;
            return;
        }
        if (rightType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            resType = rightType;
            return;
        }
        
        if (node.getOp() == LangOper.DIVIDE && 
            (leftType.getTipo() == TypeDescriptor.TipoTD.FLOAT || 
             rightType.getTipo() == TypeDescriptor.TipoTD.FLOAT)) {
            node = new NodeBinOp(LangOper.DIV_FLOAT, node.getLeft(), node.getRight());
        }
        
        resType = new TypeDescriptor(
            (leftType.getTipo() == TypeDescriptor.TipoTD.FLOAT || 
             rightType.getTipo() == TypeDescriptor.TipoTD.FLOAT) ?
            TypeDescriptor.TipoTD.FLOAT : TypeDescriptor.TipoTD.INT);
    }

    /**
     * Verifica la compatibilità tra il tipo della variabile e dell'espressione.
     * 
     * @param node Nodo assegnamento da visitare
     */
    @Override
    public void visit(NodeAssign node) {
        node.getId().accept(this);
        TypeDescriptor idType = resType;
        
        if (idType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            return;
        }
        
        node.getExpr().accept(this);
        TypeDescriptor exprType = resType;
        
        if (exprType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            return;
        }
        
        if (!idType.compatibile(idType, exprType)) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Errore: Tipo non compatibile nell'assegnazione a " + node.getId().getName(), 0);
            return;
        }
        
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
    }

    /**
     * Verifica che l'identificatore da stampare sia stato dichiarato.
     * 
     * @param node Nodo print da visitare
     */
    @Override
    public void visit(NodePrint node) {
        node.getId().accept(this);
        if (resType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            return;
        }
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
    }

    /**
     * Verifica il tipo dell'identificatore dereferenziato.
     * 
     * @param node Nodo deref da visitare
     */
    @Override
    public void visit(NodeDeref node) {
        node.getId().accept(this);
    }

    /**
     * Imposta il tipo risultante in base al tipo della costante.
     * 
     * @param node Nodo costante da visitare
     */
    @Override
    public void visit(NodeCost node) {
        resType = new TypeDescriptor(
            node.getType() == LangType.INT ? 
            TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT);
    }
}

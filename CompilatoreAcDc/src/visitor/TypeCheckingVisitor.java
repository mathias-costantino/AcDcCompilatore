package visitor;

import ast.*;
import symbolTable.SymbolTable;
import symbolTable.Attributes;

public class TypeCheckingVisitor implements IVisitor {
    private TypeDescriptor resType;
    
    public TypeCheckingVisitor() {
        SymbolTable.init(); // Reinizializza la tabella dei simboli per ogni nuovo visitor
    }
    
    public TypeDescriptor getResType() {
        return resType;
    }

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

    @Override
    public void visit(NodeDecl node) {
        String varName = node.getId().getName();
        System.out.println("Visita dichiarazione di variabile: " + varName + ", tipo: " + node.getType());

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

        System.out.println("Tentativo di inserire variabile: " + varName);
        
        // Modifica qui: aggiungi il punto alla fine del messaggio di errore
        if (!SymbolTable.enter(varName, new Attributes(node.getType()))) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Errore: Variabile " + varName + " già dichiarata.", 0);  
            return;
        }

        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
    }

    
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

    @Override
    public void visit(NodePrint node) {
        node.getId().accept(this);
        if (resType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            return;
        }
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
    }

    @Override
    public void visit(NodeDeref node) {
        node.getId().accept(this);
    }

    @Override
    public void visit(NodeCost node) {
        resType = new TypeDescriptor(
            node.getType() == LangType.INT ? 
            TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT);
    }
}

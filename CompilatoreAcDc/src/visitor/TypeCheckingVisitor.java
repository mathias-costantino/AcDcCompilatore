/*package visitor;

import ast.*;
import symbolTable.SymbolTable;
import symbolTable.SymbolTable.Attributes;

public class TypeCheckingVisitor implements IVisitor {
    private TypeDescriptor resType;
    
    public TypeDescriptor getResType() {
        return resType;
    }

    @Override
    public void visit(NodeProgram node) {
        for (NodeDecSt decSt : node.getDecSts()) {
            ((NodeAST)decSt).accept(this);
        }
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
    }

    @Override
    public void visit(NodeId node) {
        Attributes attr = SymbolTable.lookup(node.getName());
        if (attr == null) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR, 
                "Variable " + node.getName() + " not declared");
            return;
        }
        resType = new TypeDescriptor(attr.getTipo() == LangType.INT ? 
            TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT);
    }

    @Override
    public void visit(NodeDecl node) {
        if (node.getInit() != null) {
            node.getInit().accept(this);
            TypeDescriptor initType = resType;
            
            if (initType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
                resType = initType;
                return;
            }
            
            TypeDescriptor declType = new TypeDescriptor(
                node.getType() == LangType.INT ? 
                TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT);
                
            if (!declType.compatibile(declType, initType)) {
                resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                    "Type mismatch in declaration of " + node.getId().getName());
                return;
            }
        }
        
        if (!SymbolTable.enter(node.getId().getName(), 
            new Attributes(node.getType()))) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Variable " + node.getId().getName() + " already declared");
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
    public void visit(NodePrint node) {
        node.getId().accept(this);
        if (resType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            return;
        }
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
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
                "Incompatibilità di tipo nell'assegnazione a " + node.getId().getName());
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
}*/
/*
package visitor;

import ast.*;
import symbolTable.SymbolTable;
import symbolTable.SymbolTable.Attributes;

public class TypeCheckingVisitor implements IVisitor {
    private TypeDescriptor resType;

    public TypeDescriptor getResType() {
        return resType;
    }

    @Override
    public void visit(NodeProgram node) {
    	System.out.println("Visiting NodeProgram...");
        for (NodeDecSt decSt : node.getDecSts()) {
            ((NodeAST) decSt).accept(this);
            if (resType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            	System.out.println("Error found during declaration: " + resType.getMsg());
                return; 
            }
        }
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
        System.out.println("NodeProgram processed successfully.");
    }

    @Override
    public void visit(NodeId node) {
    	System.out.println("Visiting NodeId: " + node.getName());
        Attributes attr = SymbolTable.lookup(node.getName());
        if (attr == null) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Variabile " + node.getName() + " non dichiarata.");
            return;
        }
        resType = new TypeDescriptor(
            attr.getTipo() == LangType.INT ? 
            TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT
        );
        System.out.println("NodeId " + node.getName() + " type: " + resType.getTipo());
    }

    @Override
    public void visit(NodeDecl node) {
    	System.out.println("Visiting NodeDecl: " + node.getId().getName());
        if (!node.getId().getName().matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Identificatore non valido: " + node.getId().getName());
            return;
        }

        // Analisi dell'inizializzatore (se presente)
        if (node.getInit() != null) {
            node.getInit().accept(this);
            TypeDescriptor initType = resType;

            if (initType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            	System.out.println("Error in initializer: " + initType.getMsg());
                return; // Propaga l'errore se l'inizializzatore è invalido
            }

            TypeDescriptor declType = new TypeDescriptor(
                node.getType() == LangType.INT ? 
                TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT
            );

            if (!declType.compatibile(declType, initType)) {
                resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                    "Mismatch di tipo nella dichiarazione di " + node.getId().getName());
                System.out.println("Error: " + resType.getMsg());
                return;
            }
        }

        /*
        // Inserimento nella SymbolTable
        if (!SymbolTable.enter(node.getId().getName(),
            new Attributes(node.getType()))) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Variabile " + node.getId().getName() + " già dichiarata.");
            System.out.println("Error: " + resType.getMsg());
            return;
        }
        
        if (!SymbolTable.enter(node.getId().getName(), new Attributes(node.getType()))) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Variabile " + node.getId().getName() + " già dichiarata.");
            System.out.println("Error: " + resType.getMsg());
            return;
        }


        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
        System.out.println("NodeDecl " + node.getId().getName() + " processed successfully.");
    }

    @Override
    public void visit(NodeBinOp node) {
    	System.out.println("Visiting NodeBinOp...");
        node.getLeft().accept(this);
        TypeDescriptor leftType = resType;
        node.getRight().accept(this);
        TypeDescriptor rightType = resType;

        if (leftType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            resType = leftType;
            System.out.println("Error in left operand: " + leftType.getMsg());
            return;
        }
        if (rightType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
            resType = rightType;
            System.out.println("Error in right operand: " + rightType.getMsg());
            return;
        }

        // Gestione speciale per la divisione
        if (node.getOp() == LangOper.DIVIDE &&
            (leftType.getTipo() == TypeDescriptor.TipoTD.FLOAT ||
             rightType.getTipo() == TypeDescriptor.TipoTD.FLOAT)) {
            node = new NodeBinOp(LangOper.DIV_FLOAT, node.getLeft(), node.getRight());
        }

        resType = new TypeDescriptor(
            (leftType.getTipo() == TypeDescriptor.TipoTD.FLOAT || 
             rightType.getTipo() == TypeDescriptor.TipoTD.FLOAT) ?
            TypeDescriptor.TipoTD.FLOAT : TypeDescriptor.TipoTD.INT
        );
        System.out.println("NodeBinOp processed with result type: " + resType.getTipo());
    }

    @Override
    public void visit(NodePrint node) {
    	System.out.println("Visiting NodePrint...");
        node.getId().accept(this);
        if (resType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
        	System.out.println("Error in print statement: " + resType.getMsg());
            return;
        }
        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
        System.out.println("NodePrint processed successfully.");
    }

    @Override
    public void visit(NodeAssign node) {
    	System.out.println("Visiting NodeAssign...");
        node.getId().accept(this);
        TypeDescriptor idType = resType;

        if (idType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
        	System.out.println("Error in assignment (id): " + idType.getMsg());
            return;
        }

        node.getExpr().accept(this);
        TypeDescriptor exprType = resType;

        if (exprType.getTipo() == TypeDescriptor.TipoTD.ERROR) {
        	System.out.println("Error in assignment (expr): " + exprType.getMsg());
            return;
        }

        if (!idType.compatibile(idType, exprType)) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR,
                "Incompatibilità di tipo nell'assegnazione a " + node.getId().getName());
            System.out.println("Error: " + resType.getMsg());
            return;
        }

        resType = new TypeDescriptor(TypeDescriptor.TipoTD.OK);
        System.out.println("NodeAssign processed successfully.");
    }

    @Override
    public void visit(NodeDeref node) {
    	System.out.println("Visiting NodeDeref...");
        node.getId().accept(this);
    }

    @Override
    public void visit(NodeCost node) {
    	System.out.println("Visiting NodeCost...");
        resType = new TypeDescriptor(
            node.getType() == LangType.INT ? 
            TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT
        );
        System.out.println("NodeCost processed with type: " + resType.getTipo());
    }
}*/

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
        // Debug: stampa il nome della variabile
        System.out.println("Visita variabile: " + node.getName());
        
        Attributes attr = SymbolTable.lookup(node.getName());
        if (attr == null) {
            resType = new TypeDescriptor(TypeDescriptor.TipoTD.ERROR, 
                "Errore: Variabile " + node.getName() + " non dichiarata", 0);
            return;
        }
        resType = new TypeDescriptor(attr.getTipo() == LangType.INT ? 
            TypeDescriptor.TipoTD.INT : TypeDescriptor.TipoTD.FLOAT);
    }

    /*
    @Override
    public void visit(NodeDecl node) {
        // Debug: stampa il tipo e il nome della variabile che stai dichiarando
        System.out.println("Visita dichiarazione: " + node.getId().getName() + ", tipo: " + node.getType());
        System.out.println("Nome variabile dichiarata: " + node.getId().getName());


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
                    "Errore: Tipo non compatibile nella dichiarazione di " + node.getId().getName(), 0);
                return;
            }
        }*/
    

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
                "Errore: Variabile " + varName + " già dichiarata.", 0);  // Aggiunto il punto finale
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

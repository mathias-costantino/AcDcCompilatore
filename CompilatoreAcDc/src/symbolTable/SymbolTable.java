package symbolTable;

import ast.LangType;
import java.util.HashMap;

public class SymbolTable {
    private static HashMap<String, Attributes> table;

    /*
    public static class Attributes {
        private LangType tipo;
        
        // Costruttore
        public Attributes(LangType tipo) {
            this.tipo = tipo;
        }
        
        // Getter
        public LangType getTipo() {
            return tipo;
        }
        
        @Override
        public String toString() {
            return "Attributes{tipo=" + tipo + "}";
        }
    }*/

    static {
        init(); // Inizializza la tabella simboli appena la classe viene caricata
    }
    
    public static void init() {
    	table = new HashMap<>();
    	System.out.println("Tabella dei simboli inizializzata."); // Debug
    }
/*
    public static boolean enter(String id, Attributes entry) {
        if (table.containsKey(id)) {
            // Se la variabile esiste già, verifica se il tipo è lo stesso
            Attributes existingAttr = table.get(id);
            if (existingAttr.getTipo() != entry.getTipo()) {
                System.out.println("Errore: Variabile " + id + " già dichiarata con tipo diverso. Tipo esistente: "
                                   + existingAttr.getTipo() + ", tipo tentato: " + entry.getTipo());
                return false;
            }
        }
        table.put(id, entry);
        System.out.println("Variabile dichiarata: " + id + " con tipo: " + entry.getTipo());
        return true;
    }*/

    public static boolean enter(String id, Attributes entry) {
        if (id == null || id.trim().isEmpty() || id.equals(";")) {
            System.out.println("ERRORE: Tentativo di inserire un identificatore non valido: '" + id + "'");
            return false;
        }
        
        if (table.containsKey(id)) {
            Attributes existingAttr = table.get(id);
            if (existingAttr.getTipo() != entry.getTipo()) {
                System.out.println("Errore: Variabile " + id + " già dichiarata con tipo diverso. " +
                                 "Tipo esistente: " + existingAttr.getTipo() + 
                                 ", tipo tentato: " + entry.getTipo());
                return false;
            }
        }
        table.put(id, entry);
        System.out.println("Variabile dichiarata con successo: " + id + " con tipo: " + entry.getTipo());
        return true;
    }
    
    public static Attributes lookup(String id) {
        Attributes found = table.get(id);
        if (found != null) {
            System.out.println("Variabile trovata: " + id + " con tipo: " + found.getTipo()); // Debug
        } else {
            System.out.println("Variabile non trovata: " + id); // Debug
        }
        return found;
    	//return table.get(id);
    }

    public static String toStr() {
    	String tableString = table.toString();
        System.out.println("Tabella dei simboli: " + tableString); // Debug
        return tableString;
        //return table.toString();
    }

    public static int size() {
    	System.out.println("Dimensione della tabella dei simboli: " + table.size()); // Debug
        return table.size();
    }
}

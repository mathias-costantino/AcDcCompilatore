package symbolTable;

import java.util.HashMap;

/**
 * @author Mathias Costantino, 20043922
 */
public class SymbolTable {
    private static HashMap<String, Attributes> table;

    static {
        init(); 
    }
    
    public static void init() {
    	table = new HashMap<>();
    	System.out.println("Tabella dei simboli inizializzata."); // Debug
    }

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
    }

    public static String toStr() {
    	String tableString = table.toString();
        return tableString;
    }

    public static int size() {
        return table.size();
    }
}

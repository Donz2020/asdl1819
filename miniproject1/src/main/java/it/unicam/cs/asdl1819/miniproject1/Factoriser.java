package it.unicam.cs.asdl1819.miniproject1;

import java.util.Iterator;
import java.util.SortedSet;

/**
 * Un fattorizzatore è un agente che fattorizza un qualsiasi numero naturale nei
 * sui fattori primi.
 * 
 * @author Luca Tesei (template) **Joel Sina** (implementazione)
 *
 */
public class Factoriser {
    /**
     * Fattorizza un numero restituendo il multinsieme dei suoi fattori primi. La
     * molteplicità di ogni fattore primo esprime quante volte il fattore stesso
     * divide il numero fattorizzato. Per convenzione non viene mai restituito il
     * fattore 1. Il minimo numero fattorizzabile è 1. In questo caso viene
     * restituito un multinsieme vuoto.
     * 
     * @param n un numero intero da fattorizzare
     * @return il multinsieme dei fattori primi di n
     * @throws IllegalArgumentException se si chiede di fattorizzare un numero
     *                                  minore di 1.
     */

    public Multiset<Integer> getFactors(int n) {
    	if(n<1) {
    		throw new IllegalArgumentException("Stai cercando di fattorizzare un numero minore di 1");
    	}
    	MyMultiset<Integer> fattori = new MyMultiset<Integer>();
    	
    	 if(n==1) {
         	return fattori;
         }
    	 
    	//Creo un crivello passando come numero il parametro n sotto radice quadrata.
    	//Si utilizza inoltre Math.ceil per ottenere il più piccolo double che è maggiore del valore
    	// Il tutto viene castato ad un intero
    	CrivelloDiEratostene c = new CrivelloDiEratostene((int)Math.ceil(Math.sqrt(n)));
    	//Ricevo i numeri primi dal crivello
        SortedSet<Integer> s = c.getPrimes();
        //Creo l'iterator per scorrere la lista
        Iterator<Integer> iter = s.iterator();
        
        boolean next = false;
        //Scorro finché esiste un prossimo elemento
        while(iter.hasNext()) {
        	Integer item = iter.next();
        	next = false;
        	//Scorro finché il flag next è diverso da false quindi true, per inserire
        	//l'elemento più volte. Appena si va nell'else si esce dal ciclo e si prende il prossimo
        	//numero contenuto nel SortedSet resituito dal crivello
        	while(!next) {
        		if(n%item == 0) {
            		fattori.add(item);
            		n /= item;
            	}else {
            		next = true;
            	}
        	}
        }
        
        //Se alla fine del ciclo, n è diverso da 1 allora lo aggiungiamo
        if(n != 1) {
        	fattori.add(n);
        }
        
        return fattori;
    }

}

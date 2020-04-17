package it.unicam.cs.asdl1819.miniproject1;

import java.util.TreeSet; // Utilizzare questa classe per il SortedSet
import java.util.Iterator;
import java.util.SortedSet;

/**
 * Il crivello di Eratostene è un modo per determinare tutti i numeri primi da
 * {@code 1} a un certo intero {@code n} assegnato.
 * 
 * @author Luca Tesei (template), ** Joel Sina** (implementazione
 *
 */

public class CrivelloDiEratostene {

	//Dichiaro un array di tipo booleano
	public boolean[] primeArray;
	public int n;
	/**
	 * Costruisce il crivello di Eratostene fino a un certo numero. Il numero deve
	 * essere almeno 2.
	 * 
	 * @param n numero di entrate nel crivello
	 * 
	 * @throws IllegalArgumentException se il numero {@code n} è minore di {@code 2}
	 */
	public CrivelloDiEratostene(int n) {
		if (n < 2) {
			throw new IllegalArgumentException("Il numero deve essere almeno 2");
		}
		
		this.n=n;
		//Creo l'array con dimensione n+1
		primeArray = new boolean[n+1];
		
		//Scorro tutto l'array impostandolo tutto a true
		for (int i = 1; i <= n; i++) {
            primeArray[i] = true;
        }
		
		//Scorro fino a quando i*i è minore o uguale a n
		//Controllo che l'array in quella posizione sia true, in caso positivo
		//tutti i multipli di quel numero vengono impostati a false
		
		//L'indice viene usato per capire se l'elemento è primo o no in base al valore true o false
		for (int i = 2; i <= Math.sqrt(n); i++) {
            if (primeArray[i]) {
                for (int j = i; i*j <= n; j++) {
                    primeArray[i*j] = false;
                }
            }
        }
		
	}

	/**
	 * Cerca nel crivello l'indice del numero primo successivo a un numero dato.
	 * 
	 * @param n il numero da cui partire
	 * @return il numero primo successivo a {@code n} in questo crivello oppure -1
	 *         se in questo crivello non ci sono numeri primi maggiori di {@code n}
	 * @throws IllegalArgumentException se il numero passato {@code n} eccede la
	 *                                  capacità di questo crivello o se è un numero
	 *                                  minore di 1.
	 */
	public int nextPrime(int n) {
		if(!(n<=this.getCapacity()) || n<1) {
			throw new IllegalArgumentException("Il numero deve essere almeno 2");
		}
		
		// Scorro la lista, se l'array in quella posizione è true e l'indice è maggiore
		//della variabile n allora ritorno l'indice
		for(int i = 1; i<primeArray.length;i++) {
			if(this.primeArray[i] && i>n) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * Restituisce l'insieme dei numeri primi calcolati attraverso questo crivello.
	 * Per convenzione il numero primo {@code 1} non viene incluso nel risultato.
	 * 
	 * @return l'insieme dei numeri primi calcolati attraverso questo crivello.
	 */
	public SortedSet<Integer> getPrimes() {		
		
		SortedSet<Integer> crivello = new TreeSet<Integer>();
		
		//Scorro tutta la lista e se l'array nella posizione "i" è true allora lo aggiungo 
		//nel TreeSet e si restituisce tutto alla fine
		
		for (int i = 2; i <= n; i++) {
            if (primeArray[i]) {
            	crivello.add(i);
            }
        }

		return crivello;
	}

	/**
	 * Restituisce la capacità di questo crivello, cioè il numero massimo di
	 * entrate.
	 * 
	 * @return la capacità di questo crivello
	 */
	public int getCapacity() {
		//Ritorna la dimensione del crivello
		return this.n;
	}

	/**
	 * Controlla se un numero è primo. Può rispondere solo se il numero passato come
	 * parametro è minore o uguale alla capacità di questo crivello.
	 * 
	 * @param n il numero da controllare
	 * @return true se il numero passato è primo
	 * @throws IllegalArgumentException se il numero passato {@code n} eccede la
	 *                                  capacità di questo crivello o se è un numero
	 *                                  minore di 1.
	 */
	public boolean isPrime(int n) {
		if (!(n <= this.getCapacity()) || n<1) {
			throw new IllegalArgumentException("Il numero deve essere almeno 2");
		}
		
		//Controllo subito se 2 è primo, in caso affermativo allora ritorno true
		//in quanto ritornerebbe false dall'if successivo
		if(n==2) {
			return true;
		}
		
		if (n % 2 == 0) {
			return false;
		}
		
		// se il numero non è divisible per 2 allora cerchiamo tutti i dispari
		//se la divisione del numero con l'indice restituisce 0 allora il numero non è primo
		//altrimenti una volta finito il for si ritorna il true finale del metodo
		for (int i = 3; i <= Math.sqrt(n); i += 2) {
			if (n % i == 0) {
				return false;
			}
		}
		
		return true;
	}


}

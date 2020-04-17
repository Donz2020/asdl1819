package it.unicam.cs.asdl1819.miniproject1;

import java.util.HashSet; // Utilizzare questa classe per i set
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 
 * 
 * MyMultiset utilizza un HashSet dove all'interno si salvano oggetti di una classe interna
 * chiamata {@code Node}. Dentro l'oggetto Node vengono salvati l'elemento e la frequenza
 * di questo elemento. Si utilizza il metodo {@code searchElement(Object el} per cercare se
 * un dato elemento esiste e fare operazioni di aggiunta o aggiornamenti
 * 
 * @author Luca Tesei (template) **Joel Sina** (implementazione)
 *
 * @param <E> il tipo degli elementi del multiset
 * 
 */
public class MyMultiset<E> implements Multiset<E> {
	
	public Set<Node> set;
	public int size = 0;

	// L'iteratore personalizzato fa uso dell'iteratore dell'HashSet per ricevere gli elementi
	// dalla lista. Una volta instanziato si controlla se la dimensione sia maggiore di 0 così
	// ci accertiamo che esista almeno un elemento.
	// Si mostra un elemento in base alla molteplicità
	
	private class Itr implements Iterator<E> {
		private Node pointer;
		private int cont = size; //Dichiaro cont uguale alla dimensione
		private int frequenza;
		private Iterator<Node> iter = set.iterator();
		
		private Itr() {
			//Controllo se esistono degli elementi nel set passando il size
			if(cont>0){
				//Punto all'elemento salvando anche la frequenza per un check futuro
				pointer = iter.next();
				frequenza = pointer.getFrequenza();
			}
		}

		public boolean hasNext() {
			//Controllo se il cont è diverso da 0 in caso positivo esiste un prossimo elemento
			return cont!=0;
		}

		public E next() {
			 if (!this.hasNext()) {
				 throw new NoSuchElementException("Tentativo di ottenere il prossimo elemento che non esiste");
			 }
	                
			//Prendo l'elemento da restituire
			E elemento =  pointer.getElemento();
			
			//Controllo se la frequenza sia maggiore di 1 in quanto bisogna restituire lo stesso 
			//elemento più volte di seguito in base alla loro frequenza
			//Appena la frequenza va ad uno, si va nel blocco else e si punta al prossimo elemento
			// Diminuisco cont ad ogni return dell'elemento così è più facile capire se esiste un prossimo elemento o no
			if(frequenza > 1) {
				frequenza --;
			}else {	
				if(cont>1) {
					pointer = iter.next();
	            	frequenza = pointer.getFrequenza();
				}
            	cont--;
            	return elemento;
            }     
			cont--;
			return elemento;
		}
	}
	
	/*
	 * Si crea una classe Node con dentro l'elemento passato all'array list e la frequenza.
	 * Viene istanziato un oggetto di questa classe per salvare dentro l'hashset del MyMultiset
	 */

	private class Node {
		private E elemento;
		private int frequenza;

		public Node(E el, int freq) {
			this.setElemento(el);
			this.setFrequenza(freq);
		}

		//Ritorno l'elemento
		public E getElemento() {
			return elemento;
		}

		//Setto l'elemento
		public void setElemento(E elemento) {
			this.elemento = elemento;
		}
		
		//Ritorno la frequenza
		public int getFrequenza() {
			return frequenza;
		}

		//Setto la frequenza
		public void setFrequenza(int frequenza) {
			this.frequenza = frequenza;
		}
		//Faccio la somma della frequenza con il parametro in entrata
		public void sumFrequenza(int frequenza) {
			this.frequenza += frequenza;
		}

		@Override
		public int hashCode() {
			return (Integer) this.elemento;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (elemento == null) {
				if (other.elemento != null)
					return false;
			} else if (!elemento.equals(other.elemento))
				return false;
			if (frequenza != other.frequenza)
				return false;
			return true;
		}
	}

	/**
	 * Crea un multiset vuoto.
	 */
	public MyMultiset() {
		this.set = new HashSet<Node>();
	}

	
	public int size() {
		//Ritorno il size che si modifica ad ogni aggiornamento del set
		return this.size;
	}

	public int count(Object element) {
		if (element == null) {
			throw new NullPointerException("L'elemento non può essere nullo");
		}

		
		//Cerco se l'elemento esiste, in caso negativo viene ritornato null
		Node nodo = this.searchElement(element);
		if(nodo!=null) {
			return nodo.getFrequenza();
		}
		
		return 0;
	}

	public int add(E element, int occurrences) {
		if (occurrences < 0) {
			throw new IllegalArgumentException("Il numero deve essere maggiore di 0");
		}
		if (element == null) {
			throw new NullPointerException("L'elemento non può essere nullo");
		}
		
		boolean trovato = false;
		
		//Controllo se l'elemento esiste, in caso positivo aumento
		//la frequenza con il numero delle occorrenze e aumentando la size
		Node nodo = this.searchElement(element);
		if(nodo!=null) {
			int frequenza = nodo.getFrequenza();
			// Se le occorrenze sono 0 allora ritorniamo la frequenza dell'elemento esistente
			if (occurrences == 0) {
				return frequenza;
			}
			// Si controlla se si ha già la frequenza massima oppure se la frequenza
			// esistente + le occorrenze portino un valore maggiore della dimensione massima
			if (frequenza+occurrences > Integer.MAX_VALUE || frequenza == Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Questo elemento ha già il numero massimo degli elementi!");
			}
			nodo.sumFrequenza(occurrences);
			
			this.size += occurrences;
			trovato = true;
			return frequenza;
		}
		
		//In caso non sia stato trovato l'oggetto, lo creo e lo aggiungo come nuovo, aumentando la size
		
		if (!trovato) {
			if (occurrences == 0) {
				return 0;
			}

			this.set.add(new Node(element, occurrences));
			this.size += occurrences;
			return 0;
		}
		return 0;
	}

	public void add(E element) {
		if (element == null) {
			throw new NullPointerException("L'elemento non può essere nullo");
		}
		
		// Richiamo il metodo sopra creato passando come numero di occorrenze 1
		this.add(element, 1);
	}

	public int remove(Object element, int occurrences) {
		if (occurrences < 0) {
			throw new IllegalArgumentException("Il secondo parametro non può essere negativo!");
		}
		if (element == null) {
			throw new NullPointerException("L'elemento non può essere nullo");
		}
		
		//Verifico che l'elemento esiste, se esiste si controlla
		//se la frequenza meno le occorrenze porti minore di 1, in caso questo controllo
		//risultasse true allora si rimuove l'oggetto del tutto e si diminuisce il size generale
		Node nodo = this.searchElement(element);
		if(nodo!=null) {
			int frequenza = nodo.getFrequenza();
			if (frequenza - occurrences < 1) {
				this.set.remove(nodo);
				this.size -= frequenza;
				return frequenza;
			}
			//Sommiamo un valore negativo alla frequenza per diminuirlo
			nodo.sumFrequenza(-occurrences);
			this.size -= occurrences;
			return frequenza;
		}
		
		return 0;
	}

	public boolean remove(Object element) {
		if (element == null) {
			throw new NullPointerException("L'elemento non può essere nullo");
		}
		
		//Si richiama il metodo remove sopra creato passando come numero per le occorrenze
		//da rimuovere 1. Siccome il metodo sopra ritorna un intero con la frequenza precedente
		// si fa un controllo sul valore restituito
		int result = this.remove(element, 1);
		if (result > 0) {
			return true;
		}
		return false;

	}

	public int setCount(E element, int count) {
		
		if (count < 0) {
			throw new IllegalArgumentException("Il secondo parametro non può essere negativo!");
		}
		if (element == null) {
			throw new NullPointerException("L'elemento non può essere nullo");
		}

		boolean trovato = false;
		int frequenza = 0;

		//Si controlla se l'elemento esiste in caso positivo si fa uso del metodo
		//setFrequenza() della classe Node per impostare la frequenza
		//Si fa un controllo per capire se dobbiamo diminuire o aumentare il numero della size
		Node nodo = this.searchElement(element);
		if(nodo!=null) {
			frequenza = nodo.getFrequenza();
			nodo.setFrequenza(count);

			if(frequenza <= count) {
				this.size += count-frequenza;
			}else {
				this.size -= frequenza-count;
			}
			trovato = true;
		}
		if(!trovato) {
			this.add(element,count);
		}
		return frequenza;
	}

	public Set<E> elementSet() {
		
		Set<E> newSet = new HashSet<E>();
		//Siccome dentro l'hashSet vengono salvati oggetti di tipo Node noi scorriamo
		//il nostro set e aggiungiamo l'elemento che sta salvato dentro gli oggetti Node
		//nel newSet da restituire
		for (Node el : this.set) {
			newSet.add(el.getElemento());
		}
		return newSet;
	}

	public Iterator<E> iterator() {
		//Si usa la classe Itr creato sopra
		return new Itr();
	}

	public boolean contains(Object element) {
		if (element == null) {
			throw new NullPointerException("L'elemento non può essere nullo");
		}
		
		//Si cerca se l'elemento esiste, in caso positivo viene restituito true, altrimenti false
		Node nodo = this.searchElement(element);
		if(nodo!=null) {
			return true;
		}
		return false;
	}

	public void clear() {
		//La classe HashSet implementa un metodo clear per pulire, uso quel metodo e imposto
		//la variabile size = 0
		this.set.clear();
		this.size = 0;
	}

	public boolean isEmpty() {
		//Controllo se la size è uguale a 0, se lo è allora è vuoto, altrimenti ha almeno un elemento
		return this.size == 0;
	}

  /*
  * (non-Javadoc)
  * 
  * @see java.lang.Object#hashCode()
  */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((set == null) ? 0 : set.hashCode());
		result = prime * result + size;
		return result;
	}
  /*
  * (non-Javadoc)
  * 
  * @see java.lang.Object#equals(java.lang.Object)
  */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MyMultiset other = (MyMultiset) obj;
			
		if (set == null) {
			if (other.set != null) {
				return false;
			}
		} else if (!set.equals(other.set)) {
			return false;
		}
		if (size != other.size) {
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo per cercare se un elemento esiste utilizzando un iteratore per scorrere la lista
	 * 
	 * @param el L'elemento che si vuole cercare
	 * @return L'oggetto Node salvato nel nostro set oppure null se non esiste
	 */
	public Node searchElement(Object el) {
	
		for (Node elemento : this.set) {
			if(elemento.getElemento().equals(el)) {
				return elemento;
			}
		}
		return null;
	}


}

package it.unicam.cs.asdl1819.miniproject2;
/**
 * 
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Un AVLTree è un albero binario di ricerca che si mantiene sempre bilanciato.
 * In questa particolare classe si possono inserire elementi ripetuti di tipo
 * {@code E} e non si possono inserire elementi {@code null}.
 * 
 * @author Luca Tesei
 * 
 * @param E il tipo degli elementi che possono essere inseriti in questo
 *          AVLTree. La classe {@code E} deve avere un ordinamento naturale
 *          definito tra gli elementi.
 *
 */
public class AVLTree<E extends Comparable<E>> {

	/**
	 * Gli elementi di questa classe sono i nodi di un AVLTree, che è la classe
	 * "involucro" della struttura dati.
	 * 
	 * @author Luca Tesei
	 *
	 */
	public class AVLTreeNode {
		// etichetta del nodo
		private E el;

		// molteplicità dell'elemento el
		private int count;

		// sottoalbero sinistro
		private AVLTreeNode left;

		// sottoalbero destro
		private AVLTreeNode right;

		// genitore del nodo, null se questo nodo è la radice dell'AVLTree
		private AVLTreeNode parent;

		// altezza del sottoalbero avente questo nodo come radice
		private int height;

		/**
		 * Create an AVLTreeNode as a root leaf
		 *
		 * @param el the element
		 */
		public AVLTreeNode(E el) {
			this.el = el;
			this.count = 1;
			this.left = null;
			this.right = null;
			this.parent = null;
			this.height = 0;
		}

		/**
		 * Create an AVLTreeNode node containing one element to be considered child of
		 * the given parent.
		 *
		 * @param el     the element
		 * @param parent the parent of the node
		 */
		public AVLTreeNode(E el, AVLTreeNode parent) {
			this.el = el;
			this.count = 1;
			this.left = null;
			this.right = null;
			this.parent = parent;
			this.height = 0;
		}

		/**
		 * Restituisce il nodo predecessore di questo nodo. Si suppone che esista un
		 * nodo predecessore, cioè che questo nodo non contenga l'elemento minimo del
		 * sottoalbero di cui è radice.
		 *
		 * @return il nodo predecessore
		 */
		public AVLTreeNode getPredecessor() {
			//Se esiste un figlio sinistro allora prendo il massimo di quel figlio
			if (this.getLeft() != null) {
				return this.getLeft().getMaximum();
			}
			/*
			 * Prendo il parent di un nodo e il nodo stesso e li metto in una variabile. Tramite una ricerca ricorsiva salgo l'albero
			 * finché il parent non è diverso da null OPPURE il nodo non è più figlio sinistro
			 */
			
			AVLTreeNode p = this.getParent();
			AVLTreeNode t = this;
			while (p != null && t == p.getLeft()) {
				t = p;
				p = t.parent;
			}
			
			//Se il parent è diverso da null allora lo ritorno altrimenti non esiste un predecessore

			if (p != null) {
				return p;
			}

			return null;

		}

		/**
		 * Restituisce il nodo successore di questo nodo. Si suppone che esista un nodo
		 * successore, cioè che questo nodo non contenga l'elemento massimo del
		 * sottoalbero di cui è radice.
		 *
		 * @return il nodo successore
		 */
		public AVLTreeNode getSuccessor() {
			//Se esiste un figlio destro allora prendo il minimo di quel figlio
			if (this.getRight() != null) {
				return this.getRight().getMinimum();
			}
			/*
			 * Prendo il parent di un nodo e il nodo stesso e li metto in una variabile. Tramite una ricerca ricorsiva salgo l'albero
			 * finché il parent non è diverso da null OPPURE il nodo non è più figlio destro
			 */
			AVLTreeNode p = this.getParent();
			AVLTreeNode t = this;

			while (p != null && t == p.getRight()) {
				t = p;
				p = t.parent;
			}
			//Se il parent è diverso da null allora lo ritorno altrimenti non esiste un predecessore
			if (p != null) {
				return p;
			}

			return null;
		}

		/**
		 * Restituisce il nodo contenente l'elemento massimo del sottoalbero di cui
		 * questo nodo è radice.
		 *
		 * @return il nodo contenente l'elemento massimo del sottoalbero di cui questo
		 *         nodo è radice.
		 */
		public AVLTreeNode getMaximum() {
			
			AVLTreeNode current = this;
			//Il massimo si trova sulla foglia più a destra, quindi scorro fino all'ultima foglia
			while (current.getRight() != null) {
				current = current.getRight();
			}

			return current;
			// return null;
		}

		/**
		 * Restituisce il nodo contenente l'elemento minimo del sottoalbero di cui
		 * questo nodo è radice.
		 *
		 * @return il nodo contenente l'elemento minimo del sottoalbero di cui questo
		 *         nodo è radice.
		 */
		public AVLTreeNode getMinimum() {
			AVLTreeNode current = this;
			//Il minimo si trova sulla foglia più a sinistra, quindi scorro fino all'ultima foglia
			while (current.getLeft() != null) {
				current = current.getLeft();
			}

			return current;
			// return null;
		}

		/**
		 * Determina se questo è un nodo foglia.
		 *
		 * @return true se questo nodo non ha figli, false altrimenti.
		 */
		public boolean isLeaf() {
			//Se non esistono figlio destro E sinistro allora il nodo è una foglia
			return (this.left == null && this.right == null);
		}

		/**
		 * Restituisce l'altezza del sottoalbero la cui radice è questo nodo.
		 *
		 * @return l'altezza del sotto albero la cui radice è questo nodo.
		 */
		public int getHeight() {
			return this.height;
		}

		/**
		 * Aggiorna l'altezza del sottoalbero la cui radice è questo nodo supponendo che
		 * l'altezza dei nodi figli sia già stata aggiornata.
		 */
		public void updateHeight() {
			//Se esiste il figlio sinistro ma non il destro allora l'altezza è filgio sinistro + 1
			if (this.getLeft() != null && this.getRight() == null) {
				this.height = this.getLeft().getHeight() + 1;
				//Se esiste figlio destro ma non il sinistro allora l'altezza è figlio destro + 1
			} else if (this.getLeft() == null && this.getRight() != null) {
				this.height = this.getRight().getHeight() + 1;
				//Se esistono entrambi i figli allora l'altezza è il massimo delle due + 1. Utilizzo la funzione Math.max per prendere il massimo
			} else if (this.getLeft() != null && this.getRight() != null) {
				this.height = Math.max(this.getLeft().getHeight(), this.getRight().getHeight()) + 1;
			} else {
				//Se è una foglia allora altezza 0. Questo else è utile quando si ricalcolano le altezze dopo le rotazioni
				this.height = 0;
			}
		}

		/**
		 * Determina il fattore di bilanciamento di questo nodo. Se il nodo è una foglia
		 * il fattore di bilanciamento è 0. Se il nodo ha solo il figlio sinistro allora
		 * il fattore di bilanciamento è l'altezza del figlio sinistro + 1. Se il nodo
		 * ha solo il figlio destro allora il fattore di bilanciamento è l'altezza del
		 * figlio destro + 1, moltiplicata per -1. Se il nodo ha entrambi i figli il
		 * fattore di bilanciamento è l'altezza del figlio sinistro meno l'altezza del
		 * figlio destro.
		 *
		 * @return il fattore di bilanciamento di questo nodo.
		 */
		public int getBalanceFactor() {
			//Se il nodo è foglia fattore 0
			if (this.isLeaf()) {
				return 0;
			}
			//Se esiste figlio sinistro ma non destro allora il fattore è altezza figlio sinistro +1
			if (this.getLeft() != null && this.getRight() == null) {
				return this.getLeft().getHeight() + 1;
			}
			//Se esiste figlio destro ma non sisnistro allora il fattore è l'altezza del figlio sinistro moltiplicato per -1 e poi sommato 1
			if (this.getRight() != null && this.getLeft() == null) {
				return (this.getRight().getHeight() + 1) * -1;
			}
			//Se esistono entrambi i figli allroa è l'altezza figlio sinistro - altezza figlio destro
			return this.getLeft().getHeight() - this.getRight().getHeight();
		}

		/**
		 * Determina se questo nodo e tutti i suoi discendenti hanno un fattore di
		 * bilanciamento compreso tra -1 e 1.
		 *
		 * @return true se questo nodo e tutti i suoi discendenti sono bilanciati, false
		 *         altrimenti.
		 */
		public boolean isBalanced() {
			int fattoreBilanciamento = this.getBalanceFactor();
			//Se il fattore di bilanciamento è compreso tra -1 e 1 allora facciamo i vari check, 
			//sennò è inutile andare a vedere i figli se questo nodo è già sbilanciato
			//quindi in caso negativo ritorniamo false direttamente
			//Da notare che si effettuano chiamate ricorsive al metodo isBalanced nei figli
			if (fattoreBilanciamento >= -1 && fattoreBilanciamento <= 1) {
				AVLTreeNode left = this.getLeft();
				AVLTreeNode right = this.getRight();
				//Se esiste il figlio sinistro ma non il destro allora controlliamo se il figlio sinistro è bilanciato
				//Se esiste il figlio sinistro ma anc
				if (left != null) {
					if (right == null) {
						return left.isBalanced();
					} else {
						return left.isBalanced() && right.isBalanced();
					}
				}
				//Se esiste il figlio destro ma non il sinistro allora controlliamo se il figlio destro è bilanciato
				if (right != null) {
					if (left == null) {
						return right.isBalanced();
					}
				}

				//Se il nodo non ha figli allora ritorniamo che il nodo è bilanciato;
				if (left == null && right == null) {
					return true;
				}

			}
			return false;
		}

		/**
		 * @return the el
		 */
		public E getEl() {
			return el;
		}

		/**
		 * @param el the el to set
		 */
		public void setEl(E el) {
			this.el = el;
		}

		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}

		/**
		 * @param count the count to set
		 */
		public void setCount(int count) {
			this.count = count;
		}

		/**
		 * @return the left
		 */
		public AVLTreeNode getLeft() {
			return left;
		}

		/**
		 * @param left the left to set
		 */

		public void setLeft(AVLTreeNode left) {
			this.left = left;
		}

		/**
		 * @return the right
		 */
		public AVLTreeNode getRight() {
			return right;
		}

		/**
		 * @param right the right to set
		 */
		public void setRight(AVLTreeNode right) {
			this.right = right;
		}

		/**
		 * @return the parent
		 */
		public AVLTreeNode getParent() {
			return parent;
		}

		/**
		 * @param parent the parent to set
		 */
		public void setParent(AVLTreeNode parent) {
			this.parent = parent;
		}

		/**
		 * @param height the height to set
		 */
		public void setHeight(int height) {
			this.height = height;
		}

		/*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer s = new StringBuffer();
            s.append("(");
            s.append(this.el);
            s.append(", ");
            if (this.left == null)
                s.append("()");
            else
                s.append(this.left.toString());
            s.append(", ");
            if (this.right == null)
                s.append("()");
            else
                s.append(this.right.toString());
            s.append(")");
            return s.toString();
        }

		/**
		 * Ricerca un elemento a partire da questo nodo.
		 *
		 * @param el the element to search for
		 *
		 * @return the node containing the element or null if the element is not found
		 */
		public AVLTreeNode search(E el) {
			AVLTreeNode header = this;
			//Tramite un ciclo while scorriamo a destra o a sinistra del nodo dal quale partiamo per andare a trovare un certo elemento
			//Sappiamo che se il valore da cercare è minore del valore del nodo che stiamo trattando allora andiamo a sinistra, altrimenti andiamo a destra.
			//Appena abbiamo trovato l'elemento allora ritorniamo il nodo
			//Se non dovesse ritornare niente dall'interno del ciclo while allora ritorna il null esterno
			while (header != null) {
				if (el.compareTo(header.getEl()) == 0) {
					return header;
				} else if (el.compareTo(header.getEl()) < 0) {
					if (header.getLeft() == null) {
						return null;
					}
					header = header.getLeft();
				} else {
					if (this.getRight() == null) {
						return null;
					}
					header = header.getRight();
				}
			}
			return null;
		}

		/**
		 * Inserisce un elemento nell'albero AVL a partire da questo nodo. Se l'elemento
		 * è già presente ne aumenta semplicemente la molteplicità di uno. Se l'elemento
		 * non è presente aggiunge un nodo nella opportuna posizione e poi procede al
		 * ribilanciamento dell'albero se l'inserimento del nuovo nodo provoca uno
		 * sbilanciamento in almeno un nodo.
		 *
		 * @param el l'elemento da inserire
		 *
		 * @return il numero di confronti tra elementi della classe {@code E} effettuati
		 *         durante l'inserimento.
		 */
		public int insert(E el) {
			int confronti = 0;
			/*
			 * Header all'inizio punta alla root. Se l'elemento da inserire è maggiore dell'elemento del nodo allora dobbiamo andare a destra
			 * Se l'elemento da inserire è minore dell'elemento del nodo allora dobbiamo andare a destra. Se l'elemento del nodo che stiamo trattando eguaglia
			 * l'elemento da inserire allora semplicemente aumentiamo la sua molteplicità
			 * Per ogni nodo che ci spostiamo aumentiamo il contatore dei confronti
			 */
			AVLTreeNode header = this;
			while (header != null) {
				if (el.compareTo(header.getEl()) == 0) {
					header.setCount(header.getCount() + 1);
					return confronti;
				} else if (el.compareTo(header.getEl()) < 0) {
					confronti++;
					if (header.getLeft() == null) {
						header.setLeft(new AVLTreeNode(el, header));
						break;
					}
					header = header.getLeft();
				} else {
					confronti++;
					if (header.getRight() == null) {
						header.setRight(new AVLTreeNode(el, header));
						break;
					}
					header = header.getRight();
				}
			}

			AVLTreeNode perUpdate = header;
			AVLTreeNode nodo = header;
			
			//La variabile perUpdate punta al nodo dove abbiamo inserito l'elemento. Così parte dal nodo in questione fino alla root per aggiornare le altezze
			 
			boolean flag = false;
			while (!flag) {
				perUpdate.updateHeight();
				if (perUpdate.getParent() != null) {
					perUpdate = perUpdate.getParent();
				} else {
					flag = true;
				}
			}
			perUpdate.updateHeight();
			/*
			 * La varibaile nodo punta al nodo dove abbiamo inserito l'elemento. Una volta aggiornato le altezze dobbiamo controllare se il fattore
			 * di bilanciamento è cambiato oppure no.
			 * 
			 * Se il fattore di bilanciamento del nodo è maggiore di 1 allora abbiamo aggiunto un nodo a sinistra, se è meno uno abbiamo aggiunto a destra
			 * 
			 * Se abbiamo aggiunto a sinistra allora controlliamo che il figlio sinistro del nodo abbia un fattore di 1. 
			 * Se il figlio ha un fattore di 1 allora dobbiamo effettuare una rotazione a sinistra in quanto è stato aggiunto a destra; 
			 * altrimenti dobbiamo effettuare una rotazione Sinistra-Destra
			 * 
			 * Se abbiamo aggiunto a destra allora controlliamo che il figlio destro del nodo abbiamo un fattore di -1
			 * Se il figlio ha un fattore di -1 dobbiamo effettuare una rotazione una rotazione a destra in quanto è stato aggiunto a destra
			 * altrimenti dobbiamo effettuare una rotazione Destra-Sinistra
			 * 
			 * Se il fattore di bilanciamento è compreso tra -1 e 1 allora prendiamo il genitore
			 */

			
			while (nodo != null) {
				if (nodo.getBalanceFactor() > 1) {
					if (nodo.getLeft().getBalanceFactor() == 1) {
						nodo.SS();
						break;
					} else {
						nodo.SD();
						break;
					}
				} else if (nodo.getBalanceFactor() < -1) {
					if (nodo.getRight().getBalanceFactor() == -1) {
						nodo.DD();
						break;
					} else {
						nodo.DS();
						break;
					}
				} else {
					nodo = nodo.parent;
				}
			}

			return confronti;
		}

		
		private void DD() {
			AVLTreeNode nodo = this;
			AVLTreeNode child = nodo.getRight();
			//Se il nodo che dobbiamo ruotare è la root allora
			//mettiamo come root il figlio e al figlio cambiamo il parent. 
			//Al vecchio root cambiamo il parent e lo mettiamo col figlio
			//Infine cambiamo il valore del root
			if (nodo == root) {
				child.setParent(null);
				root = child;
			} else {
				AVLTreeNode padre = nodo.getParent();
				//Se il nodo da ruotare è un figlio destro allora
				if (nodo == padre.getRight()) {
					//Al padre mettiamo come figlio destro il figlio destro del nodo
					//Al figlio cambiamo parent
					padre.setRight(child);
					child.setParent(padre);
				}else {
					//Altrimenti al padre mettiamo come figlio sinistro il figlio destro del nodo
					//Al figlio cambiamo parent
					padre.setLeft(child);
					child.setParent(padre);
				}
			}
			//Se il figlio sinistro del figlio è diverso da null
			if (child.getLeft() != null) {
				//Allora al parent del figlio sinistro del figlio lo cambiamo nel nodo che stiamo ruotando
				child.getLeft().setParent(nodo);
				//Cambiamo infine anche il figlio destro del nodo con il figlio sinistro del figlio
				nodo.setRight(child.getLeft());
			} else {
				//Altrimenti al figlio destro del nodo mettiamo null
				nodo.setRight(null);
			}
			
			//Cambiamo i puntatori al parent del nodo e al figlio sinistro del figlio
			nodo.setParent(child);
			child.setLeft(nodo);
			AVLTreeNode puntatore = nodo;
			
			//Aumentiamo le altezze fino alla root

			while (puntatore.getParent()!=null) {
				puntatore.updateHeight();
				puntatore = puntatore.getParent();
			}
			puntatore.updateHeight();
		}

		private void SS() {
			
			//La ruotazione a sinistra è lo specchio di quello a destra
			
			AVLTreeNode nodo = this;
			AVLTreeNode child = nodo.getLeft();
			if (nodo == root) {
				child.setParent(null);
				root = child;
			} else {
				AVLTreeNode padre = nodo.getParent();
				if (nodo == padre.getLeft()) {
					padre.setLeft(child);
					child.setParent(padre);
				}else {
					padre.setRight(child);
					child.setParent(padre);
				}
			}
			if (child.getRight() != null) {
				child.getRight().setParent(nodo);
				nodo.setLeft(child.getRight());
			} else {
				nodo.setLeft(null);
			}
				
			nodo.setParent(child);
			child.setRight(nodo);;
			AVLTreeNode puntatore = nodo;

			while (puntatore.getParent()!=null) {
				puntatore.updateHeight();
				puntatore = puntatore.getParent();
			}
			puntatore.updateHeight();
		}

		private void SD() {
			AVLTreeNode nodo = this;
			AVLTreeNode child = nodo.getLeft();
			AVLTreeNode nephew = child.getRight();
			if (nodo == root) {
				//Se il nodo da ruotare è la root allora cambiamo il root con il nipote
				nephew.setParent(null);
				root = nephew;
			} else {
				
				AVLTreeNode padre = nodo.getParent();
				//Se il nodo da cambiare è un figlio sinistro allora
				if (nodo == padre.getLeft()) {
					//Al posto del nodo mettiamo il nipote e cambiamo il puntatore al parent
					padre.setLeft(nephew);
					nephew.setParent(padre);
				} else {
					//Al posto del nodo mettiamo il nipote e cambiamo il puntatore al parent
					padre.setRight(nephew);
					nephew.setParent(padre);
				}
			}
			
			//Se il figlio sinistro del nipote è diverso da null allora mettiamo come parent il figlio del nodo da ruotare
			if (nephew.getLeft() != null) {
				nephew.getLeft().setParent(child);
			}
			
			//Se il figlio destro del nipote è diverso da null allora mettiamo come parent il nodo
			if (nephew.getRight() != null) {
				nephew.getRight().setParent(nodo);
			}
				
			//Come figlio destro del figlio del nodo da ruotare mettiamo il figlio sinistro del nipote
			//Come figlio sinistro del nodo da ruotare mettiamo il figlio destro del nipote;
			child.setRight(nephew.getLeft());
			nodo.setLeft(nephew.getRight());
			
			//Come figlio sinistro del nipote mettiamo il figlio del nodo da ruotare
			nephew.setLeft(child);
			//Come figlio destro mettiamo il nodo da ruotare
			nephew.setRight(nodo);
			
			//Cambiamo i parent
			child.setParent(nephew);
			nodo.setParent(nephew);
			
			//Aumentiamo le altezze
			nodo.updateHeight();
			child.updateHeight();
			
			//Aumentiamo le altezze fino alla root
			AVLTreeNode pointer = nephew;
			
			while (pointer.getParent()!=null) {
				pointer.updateHeight();
				pointer = pointer.getParent();
			}
			pointer.updateHeight();
		}

		private void DS() {
			
			//La rotazione destra-sinistra è lo specchio della rotazione sinistra-destra
			
			AVLTreeNode nodo = this;
			AVLTreeNode child = nodo.getRight();
			AVLTreeNode nephew = child.getLeft();
			if (nodo == root) {
				nephew.setParent(null);
				root = nephew;
			} else {
				AVLTreeNode padre = nodo.getParent();
				if (nodo == padre.getRight()) {
					padre.setRight(nephew);
					nephew.setParent(padre);
				} else {
					padre.setLeft(nephew);
					nephew.setParent(padre);
				}
			}
			if (nephew.getRight() != null) {
				nephew.getRight().setParent(child);
			}
				
			if (nephew.getLeft() != null) {
				nephew.getLeft().setParent(nodo);
			}
				
			child.setLeft(nephew.getRight());
			nodo.setRight(nephew.getLeft());
			
			nephew.setRight(child);
			nephew.setLeft(nodo);
			
			child.setParent(nephew);
			nodo.setParent(nephew);
			
			nodo.updateHeight();
			child.updateHeight();
			AVLTreeNode pointer = nephew;
			
			while (pointer.getParent()!=null) {
				pointer.updateHeight();
				pointer = pointer.getParent();
			}
			pointer.updateHeight();

		}


		private List<E> inOrderVisit(ArrayList<E> array) {
			AVLTreeNode header = this;
			
			/*
			 * Partiamo dalla root e andiamo tutto a sinistra poi risaliamo l'albero e mettiamo il valore nell'array
			 * Se esiste un valore destro allora andiamo anche lì e cerchiamo in modo ricorsivo sennò risaliamo l'albero
			 * in modo ricorsivo fino alla root mettendo i valori nell'array durante il nostro cammino e poi passiamo al lato destro dell'albero
			 * Alla fine di tutto ritorniamo l'array
			 */
			if (header.getLeft() != null) {
				header.getLeft().inOrderVisit(array);
			}

			int count = header.count;

			while (count > 0) {
				array.add(header.getEl());
				count--;
			}

			if (header.getRight() != null) {
				header.getRight().inOrderVisit(array);
			}

			return array;
		}
	}
	
	// puntatore al nodo radice, se questo puntatore è null allora questo
	// AVLTree è vuoto
	private AVLTreeNode root;

	// Numero di elementi inseriti in questo AVLTree, comprese le ripetizioni
	private int size;

	// Numero di nodi in questo AVLTree
	private int numberOfNodes;

	/**
	 * Costruisce un AVLTree vuoto.
	 */
	public AVLTree() {
		this.root = null;
		this.size = 0;
		this.numberOfNodes = 0;
	}

	/**
	 * Costruisce un AVLTree che consiste solo di un nodo radice.
	 * 
	 * @param rootElement l'informazione associata al nodo radice
	 * @throws NullPointerException se l'elemento passato è null
	 */
	public AVLTree(E rootElement) {
		if (rootElement == null) {
			throw new NullPointerException("L'elemento non può essere null");
		}
		
		//Nella radice mettiamo un nodo con l'elemento aumentiamo la size e aumentiamo anche 
		//il numero dei nodi

		this.root = new AVLTreeNode(rootElement);
		this.size++;
		this.numberOfNodes++;
	}

	/**
	 * Determina se questo AVLTree è vuoto.
	 * 
	 * @return true, se questo AVLTree è vuoto.
	 */
	public boolean isEmpty() {
		return (this.getRoot() == null);
	}

	/**
	 * Restituisce il numero di elementi contenuti in questo AVLTree. In caso di
	 * elementi ripetuti essi vengono contati più volte.
	 * 
	 * @return il numero di elementi di tipo {@code E} presenti in questo AVLTree.
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Restituisce il numero di nodi in questo AVLTree.
	 * 
	 * @return il numero di nodi in questo AVLTree.
	 */
	public int getNumberOfNodes() {
		return this.numberOfNodes;
	}

	/**
	 * Restituisce l'altezza di questo AVLTree. Se questo AVLTree è vuoto viene
	 * restituito il valore -1.
	 * 
	 * @return l'altezza di questo AVLTree, -1 se questo AVLTree è vuoto.
	 */
	public int getHeight() {
		//Se la root non è null allora ritorno l'altezza della root, altrimenti -1
		return this.getRoot() != null ? this.getRoot().getHeight() : -1;
	}

	/**
	 * @return the root
	 */
	public AVLTreeNode getRoot() {
		return this.root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(AVLTreeNode root) {
		this.root = root;
	}

	/**
	 * Determina se questo AVLTree è bilanciato. L'albero è bilanciato se tutti i
	 * nodi hanno un fattore di bilanciamento compreso tra -1 e +1.
	 * 
	 * @return true, se il fattore di bilanciamento di tutti i nodi dell'albero è
	 *         compreso tra -1 e +1.
	 */
	public boolean isBalanced() {
		//Se root è diverso da null allora richiamiamo il metodo isBalanced alla root
		if (this.getRoot() != null) {
			if (this.getRoot().isBalanced()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Inserisce un nuovo elemento in questo AVLTree. Se l'elemento è già presente
	 * viene incrementato il suo numero di occorrenze.
	 * 
	 * @param el l'elemento da inserire.
	 * @return il numero di confronti tra elementi della classe {@code E} effettuati
	 *         durante l'inserimento
	 * @throws NullPointerException se l'elemento {@code el} è null
	 */
	public int insert(E el) {
		if (el == null) {
			throw new NullPointerException("L'elemento non può essere null");
		}
		//Se root è nullo allora mettiamo un elemento
		if (this.getRoot() == null) {
			this.setRoot(new AVLTreeNode(el));
			this.size++;
			this.numberOfNodes++;
			return 0;
		}
		//Se l'elemento esiste allora aumentiamo il numero dei nodi
		
		if (!this.contains(el)) {
			this.numberOfNodes++;
		}
		//Inseriamo l'elemento partendo dalla radice e aumentiamo la dimensione dell'albero
		int confronti = this.getRoot().insert(el);
		this.size++;
		return confronti;
	}

	/**
	 * Determina se questo AVLTree contiene un certo elemento.
	 * 
	 * @param el l'elemento da cercare
	 * @return true se l'elemento è presente in questo AVLTree, false altrimenti.
	 * @throws NullPointerException se l'elemento {@code el} è null
	 */
	public boolean contains(E el) {
		if (el == null) {
			throw new NullPointerException("L'elemento non può essere null");
		}

		// Partiamo dall'albero e tramite il metodo search andiamo a trovare il nodo
		// Se il search ritorna un nodo e non un valore null allora l'elemento esiste
		if (this.getRoot().search(el) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Determina se un elemento è presente in questo AVLTree e ne restituisce il
	 * relativo nodo.
	 * 
	 * @param el l'elemento da cercare
	 * @return il nodo di questo AVLTree che contiene l'elemento {@code el} e la sua
	 *         molteplicità, oppure {@code null} se l'elemento {@code el} non è
	 *         presente in questo AVLTree.
	 * @throws NullPointerException se l'elemento {@code el} è null
	 * 
	 */
	public AVLTreeNode getNodeOf(E el) {
		if (el == null) {
			throw new NullPointerException("L'elemento non può essere null");
		}
		
		//Tramite il metodo search partiamo dalla root e cerchiamo un nodo dove esiste l'elemento
		//che stiamo cercando. Se il search ritorna un nodo e non
		AVLTreeNode nodo = this.getRoot().search(el);
		
		if (nodo != null) {
			return nodo;
		}
		return null;
	}

	/**
	 * Determina il numero di occorrenze di un certo elemento in questo AVLTree.
	 * 
	 * @param el l'elemento di cui determinare il numero di occorrenze
	 * @return il numero di occorrenze dell'elemento {@code el} in questo AVLTree,
	 *         zero se non è presente.
	 * @throws NullPointerException se l'elemento {@code el} è null
	 */
	public int getCount(E el) {
		if (el == null) {
			throw new NullPointerException("L'elemento non può essere null");
		}
		
		//Tramite il metodo search partiamo dalla root e cerchiamo il nodo che ha
		//l'elemento che stiamo cercando. Se il nodo esiste allora ritorniamo la sua molteplicità
		AVLTreeNode nodo = this.getRoot().search(el);
		if (nodo != null) {

			return nodo.getCount();
		}
		return 0;
	}

	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String descr = "AVLTree [root=" + root.el.toString() + ", size=" + size
                + ", numberOfNodes=" + numberOfNodes + "]\n";
        return descr + this.root.toString();
    }

	/**
	 * Restituisce la lista degli elementi contenuti in questo AVLTree secondo
	 * l'ordine determinato dalla visita in-order. Per le proprietà dell'albero AVL
	 * la lista ottenuta conterrà gli elementi in ordine crescente rispetto
	 * all'ordinamento naturale della classe {@code E}. Nel caso di elementi
	 * ripetuti, essi appaiono più volte nella lista consecutivamente.
	 * 
	 * @return la lista ordinata degli elementi contenuti in questo AVLTree, tenendo
	 *         conto della loro molteplicità.
	 */
	public List<E> inOrderVisit() {

		ArrayList<E> lista = new ArrayList<E>();
		//Passiamo al metodo inOrderVisit che esiste nel nodo un array dove si riempirà 
		//con gli elementi in ordine crescente
		if (!this.isEmpty()) {
			return this.getRoot().inOrderVisit(lista);
		}
		return lista;
	}

	/**
	 * Restituisce l'elemento minimo presente in questo AVLTree.
	 * 
	 * @return l'elemento minimo in questo AVLTree, {@code null} se questo AVLTree è
	 *         vuoto.
	 */
	public E getMinimum() {
		
		//Partiamo dalla root e cerchiamo il minimo tramite il metodo dichiarato in un nodo
		return this.getRoot() != null ? this.getRoot().getMinimum().getEl() : null;
	}

	/**
	 * Restituisce l'elemento massimo presente in questo AVLTree.
	 * 
	 * @return l'elemento massimo in questo AVLTree, {@code null} se questo AVLTree
	 *         è vuoto.
	 */
	public E getMaximum() {
		//Partiamo dalla root e cerchiamo il massimo tramite il metodo dichiarato in un nodo
		return this.getRoot() != null ? this.getRoot().getMaximum().getEl() : null;
	}

	/**
	 * Restituisce l'elemento <b>strettamente</b> successore, in questo AVLTree, di
	 * un dato elemento. Si richiede che l'elemento passato sia presente
	 * nell'albero.
	 * 
	 * @param el l'elemento di cui si chiede il successore
	 * @return l'elemento <b>strettamente</b> successore, rispetto all'ordinamento
	 *         naturale della classe {@code E}, di {@code el} in questo AVLTree,
	 *         oppure {@code null} se {@code el} è l'elemento massimo.
	 * @throws NullPointerException     se l'elemento {@code el} è null
	 * @throws IllegalArgumentException se l'elemento {@code el} non è presente in
	 *                                  questo AVLTree.
	 */
	public E getSuccessor(E el) {
		if (el == null) {
			throw new NullPointerException("L'elemento non può essere null");
		}
		
		//Prima cerchiamo il nodo che ha questo elemento
		AVLTreeNode nodo = this.getRoot().search(el);
		//Se il nodo non esiste allora lanciamo l'eccezione
		if (nodo == null) {
			throw new IllegalArgumentException("Il nodo non è stato trovato");
		}		
		//Controlliamo se l'elemento combacia con il massimo dell'albero, in questo caso ritorniamo
		//null in quanto non esiste un successore per il massimo, altrimenti ritorniamo il successore
		//tramite il metodo successore dichiarato nel nodo
		
		if (this.getRoot().getMaximum().getEl() == el) {
			return null;
		}
		
		return nodo.getSuccessor().getEl();
	}

	/**
	 * Restituisce l'elemento <b>strettamente</b> predecessore, in questo AVLTree,
	 * di un dato elemento. Si richiede che l'elemento passato sia presente
	 * nell'albero.
	 * 
	 * @param el l'elemento di cui si chiede il predecessore
	 * @return l'elemento <b>strettamente</b> predecessore rispetto all'ordinamento
	 *         naturale della classe {@code E}, di {@code el} in questo AVLTree,
	 *         oppure {@code null} se {@code el} è l'elemento minimo.
	 * @throws NullPointerException     se l'elemento {@code el} è null
	 * @throws IllegalArgumentException se l'elemento {@code el} non è presente in
	 *                                  questo AVLTree.
	 */
	public E getPredecessor(E el) {
		if (el == null) {
			throw new NullPointerException("L'elemento non può essere null");
		}
		//Prima cerchiamo il nodo che ha questo elemento
		AVLTreeNode nodo = this.getRoot().search(el);
		//Se il nodo non esiste allora lanciamo l'eccezione
		if (nodo == null) {
			throw new IllegalArgumentException("Il nodo non è stato trovato");
		}
		//Controlliamo se l'elemento combacia con il minimo dell'albero, in questo caso ritorniamo
		//null in quanto non esiste un predecessore per il minimo, altrimenti ritorniamo il predecessore
		//tramite il metodo predecessore dichiarato nel nodo
		if (this.getRoot().getMinimum().getEl() == el) {
			return null;
		}
		return nodo.getPredecessor().getEl();
	}

}

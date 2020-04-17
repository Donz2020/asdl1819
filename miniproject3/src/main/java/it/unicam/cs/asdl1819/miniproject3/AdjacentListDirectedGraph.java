package it.unicam.cs.asdl1819.miniproject3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Implementazione dell'interfaccia {@code Graph<V,E>} per grafi diretti
 * utilizzando liste di adiacenza per la rappresentazione.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Luca Tesei
 *
 * @param <V> etichette dei nodi
 * @param <E> etichette degli archi
 */
public class AdjacentListDirectedGraph<V, E> implements Graph<V, E> {

	// Ho creato una variabile di tipo Map in quanto per ogni chiave (sarebbe il
	// nodo del grafo) associo un set (un set di oggetti di tipo GraphEdge)
	Map<GraphNode<V>, Set<GraphEdge<V, E>>> graph;

	/**
	 * Crea un grafo vuoto.
	 */
	public AdjacentListDirectedGraph() {
		this.graph = new HashMap<GraphNode<V>, Set<GraphEdge<V, E>>>();
	}

	// Il metodo size di un Map restituisce il numero di mappature chiavi-valori,
	// siccome le chiavi sono i nodi allora restituisco il numero
	public int nodeCount() {
		return this.graph.size();
	}

	// Ci servono gli archi totali quindi per ogni nodo prendo quanto è la size del
	// Set e lo salvo dentro il cont
	// Ho utilizzato il metodo entrySet() della Map per prendere ogni riga. con
	// getKey() si può ricevere la chiave ovvero il nodo
	// con getValue() si può ottenre il valore associato alla chiave quindi il set,
	// a sua volta richiamo il metodo size
	public int edgeCount() {
		int cont = 0;
		for (Entry<GraphNode<V>, Set<GraphEdge<V, E>>> entry : this.graph.entrySet()) {
			cont += entry.getValue().size();
		}
		return cont;
	}

	// Siccome il metodo size() di un grafo restituisce i nodi più gli archi allora
	// utilizziamo i metodi implementati sopra e ritorniamo il numero
	public int size() {
		return this.nodeCount() + this.edgeCount();
	}

	// Utilizzo il metodo isEmpty() del Map per verificare se è vuoto o no
	public boolean isEmpty() {
		return this.graph.isEmpty();
	}

	// Pulisco il grafo con il metodo clear() del map
	public void clear() {
		this.graph.clear();
	}

	public boolean isDirected() {
		return true;
	}

	// Utilizziamo il keySet() per ricevere un set di tutti i nodi
	public Set<GraphNode<V>> getNodes() {
		return this.graph.keySet();
	}

	// Facciamo un controllo che il nodo che vogliamo far entrare non ci sia già.
	// In caso ci sia allora ritorniamo false,
	// In caso non ci sia allora mettiamo dentro al grafo con il metodo put la
	// chiave che sarebbe il nodo e alla chiave facciamo
	// corrispondere un HashSet così salviamo dentro gli archi
	public boolean addNode(GraphNode<V> node) {
		if (node == null) {
			throw new NullPointerException("L'elemento node è null");
		}

		if (!this.graph.containsKey(node)) {
			this.graph.put(node, new HashSet<GraphEdge<V, E>>());
			return true;
		}
		return false;
	}

	public boolean removeNode(GraphNode<V> node) {
		if (node == null)
			throw new NullPointerException("Tentativo di rimuovere un nodo null");
		throw new UnsupportedOperationException("Rimozione dei nodi non supportata");
	}

	// Utilizzo il metodo containsKey() della classe Map
	public boolean containsNode(GraphNode<V> node) {
		if (node == null) {
			throw new NullPointerException("L'elemento node è null");
		}

		return this.graph.containsKey(node);
	}

	public int getNodeIndex(V label) {
		if (label == null)
			throw new NullPointerException("Tentativo di ricercare un nodo con etichetta null");
		throw new UnsupportedOperationException("Ricerca dei nodi con indice non supportata");
	}

	public GraphNode<V> getNodeAtIndex(int i) {
		throw new UnsupportedOperationException("Ricerca dei nodi con indice non supportata");
	}

	// Siccome è un grafo orientato sappiamo che i nodi adiacenti sono il secondo
	// nodo dell'arco in quanto il primo è il nodo che vogliamo ricevere gli
	// adiacenti
	// Creo una variable tempSet di tipo Set. Con un ciclo for scorro tutto
	// l'HashSet
	// associato alla chiave del nodo salvato e prendiamo soltanto
	// i nodi di destinazione
	public Set<GraphNode<V>> getAdjacentNodes(GraphNode<V> node) {
		if (node == null) {
			throw new NullPointerException("Il node è null");
		}

		if (!this.containsNode(node)) {
			throw new IllegalArgumentException("Il nodo passato non esiste");
		}

		Set<GraphNode<V>> tempSet = new HashSet<GraphNode<V>>();

		for (GraphEdge<V, E> arco : this.graph.get(node)) {
			tempSet.add(arco.getNode2());
		}
		return tempSet;
	}

	// Un po' diverso dal metodo getAdjacentNodes in quanto dobbiamo vedere se il
	// nodo passato come parametro è il destinatario.
	// Dobbiamo scorrere tutti gli archi del grafo e per ogni archo controlliamo se
	// il secondo nodo cioè quello destinatario è uguale al nodo passato.
	// In caso positivo lo aggiungiamo nel set
	public Set<GraphNode<V>> getPredecessorNodes(GraphNode<V> node) {
		if (node == null) {
			throw new NullPointerException("Il parametro node è null");
		}
		if (!this.containsNode(node)) {
			throw new IllegalArgumentException("Questo nodo non esiste");
		}
		Set<GraphNode<V>> tempSet = new HashSet<GraphNode<V>>();
		for (Entry<GraphNode<V>, Set<GraphEdge<V, E>>> riga : this.graph.entrySet()) {
			for (GraphEdge<V, E> arco : riga.getValue()) {
				if (arco.getNode2().equals(node)) {
					tempSet.add(arco.getNode1());
				}
			}
		}
		return tempSet;
	}

	// Scorro tutto il grafo e per ogni riga prendo il set associato al nodo, allora
	// con il metodo addAll lo aggiungo alla variabile di appoggio set
	public Set<GraphEdge<V, E>> getEdges() {
		Set<GraphEdge<V, E>> tempSet = new HashSet<GraphEdge<V, E>>();
		for (Entry<GraphNode<V>, Set<GraphEdge<V, E>>> nodo : this.graph.entrySet()) {
			tempSet.addAll(nodo.getValue());
		}
		return tempSet;
	}

	// Controlliamo se esiste già come arco, se non esiste allora lo aggiungiamo.
	// Tramite un AND controlliamo che ci sia, in caso negativo lo aggiungiamo
	// Il resultato di questo boolean check lo ritorniamo
	public boolean addEdge(GraphEdge<V, E> edge) {
		if (edge == null) {
			throw new NullPointerException("Il parametro edge è null");
		}
		if (!edge.isDirected()) {
			throw new IllegalArgumentException("Il tipo di grafo deve combaciare");
		}
		if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) {
			throw new IllegalArgumentException("Uno dei due nodi non esiste");
		}

		Set<GraphEdge<V, E>> tempSet = this.graph.get(edge.getNode1());

		return (!tempSet.contains(edge) && tempSet.add(edge));
	}

	public boolean removeEdge(GraphEdge<V, E> edge) {
		throw new UnsupportedOperationException("Rimozione degli archi non supportata");
	}

	// Prendiamo tutto il set associato al nodo nel grafo. Per prenderlo utilizziamo
	// il metodo get del Map e passiamo come parametro il primo nodo
	// ovvero quello di partenza per l'arco. Dopo tramite il metodo contains() di un
	// Set lo cerchiamo e ritorniamo true o false in base al risultato
	public boolean containsEdge(GraphEdge<V, E> edge) {

		if (edge == null) {
			throw new NullPointerException("Il parametro edge è null");
		}

		if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) {
			throw new IllegalArgumentException("Uno dei due nodi non esiste");
		}

		Set<GraphEdge<V, E>> tempSet = this.graph.get(edge.getNode1());

		return tempSet.contains(edge);
	}

	// Siccome quello associato alla chiave è un Set allora ritorniamo direttamente
	// il valore associato
	public Set<GraphEdge<V, E>> getEdges(GraphNode<V> node) {
		if (node == null) {
			throw new NullPointerException("Il parametro node non esiste");
		}
		if (!this.containsNode(node)) {
			throw new IllegalArgumentException("Questo nodo non esiste");
		}
		return this.graph.get(node);
	}

	// Come per il metodo getPredecessorNodes() scorriamo tutto il grafo. Per ogni arco dopo controlliamo che il nodo
	// destinatario equivale al nodo passato come paraemtro
	// in caso positivo lo aggiungiamo al set di appoggio
	// Non faccio un controllo se il nodo di Uscita è uguale al nodo destinatario in quanto è ammesso il cappio
	public Set<GraphEdge<V, E>> getIngoingEdges(GraphNode<V> node) {
		if (node == null) {
			throw new NullPointerException("Il parametro node è null");
		}

		if (!this.containsNode(node)) {
			throw new IllegalArgumentException("Questo nodo non esiste");
		}

		Set<GraphEdge<V, E>> tempSet = new HashSet<GraphEdge<V, E>>();

		for (Entry<GraphNode<V>, Set<GraphEdge<V, E>>> riga : this.graph.entrySet()) {
			for (GraphEdge<V, E> arco : riga.getValue()) {
				if (arco.getNode2().equals(node)) {
					tempSet.add(arco);
				}
			}
		}
		return tempSet;
	}

	// Siccome la classe che stiamo implementando fa riferimento ad un grafo
	// orientato allora dobbiamo restituire gli archi in entrata più gli archi in
	// uscita
	// Per ricvere gli archi in uscita basta trovare il Set associato al nodo che
	// stiamo trattando e utilizzare il metodo size per la dimensione
	// Per vedere gli archi in uscita utilizziamo il metodo getIngoingEdges()
	// implementato sopra e siccome restituisce un set allora
	// prendiamo con size il suo valore. Ritorniamo la somma dei due size
	public int getDegree(GraphNode<V> node) {
		if (node == null) {
			throw new NullPointerException("Il parametro node è null");
		}
		if (!this.containsNode(node)) {
			throw new IllegalArgumentException("Uno dei due nodi non esiste");
		}
		return this.graph.get(node).size() + this.getIngoingEdges(node).size();
	}

	public Set<GraphEdge<V, E>> getEdgesBetween(int index1, int index2) {
		throw new UnsupportedOperationException("Operazioni con indici non supportate");
	}

	// Siccome è un grafo orientato allora troviamo il set che corrisponde a node1 e
	// per ogni arco che sia uguale a node2 lo aggiungiamo in una variabile di
	// appoggio
	public Set<GraphEdge<V, E>> getEdgesBetween(GraphNode<V> node1, GraphNode<V> node2) {
		if (node1 == null || node2 == null) {
			throw new NullPointerException("Uno dei due nodi nei parametri è null");
		}
		if (!this.containsNode(node1) || !this.containsNode(node2)) {
			throw new IllegalArgumentException("Uno dei due nodi non esiste");
		}
		Set<GraphEdge<V, E>> tempSet = new HashSet<GraphEdge<V, E>>();

		for (GraphEdge<V, E> arco : this.graph.get(node1)) {
			if (arco.getNode2().equals(node2)) {
				tempSet.add(arco);
			}
		}
		return tempSet;
	}

	// Riceviamo tutti i nodi tramite il metodo getNodes(). Scorriamo con un ciclo
	// for e se l'etichetta del nodo corrisponde con l'etichetta passata ritorniamo
	// il nodo
	public GraphNode<V> getNode(V label) {
		if (label == null) {
			throw new NullPointerException("Label è null");
		}

		for (GraphNode<V> nodo : this.getNodes()) {
			if (nodo.getLabel() == label) {
				return nodo;
			}
		}

		throw new IllegalArgumentException("Il nodo non è stato trovato");
	}

}

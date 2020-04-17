package it.unicam.cs.asdl1819.miniproject3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Gli oggetti di questa classe sono calcolatori di cammini minimi con sorgente
 * singola su un certo grafo diretto e pesato dato. Il grafo su cui lavorare
 * deve essere passato quando l'oggetto calcolatore viene costruito e non può
 * contenere archi con pesi negativi. Il calcolatore implementa il classico
 * algoritmo di Dijkstra per i cammini minimi con sorgente singola utilizzando
 * una coda con priorità che estrae il minimo in tempo lineare rispetto alla
 * lunghezza della coda. In questo caso il tempo di esecuzione dell'algoritmo di
 * Dijkstra è {@code O(n^2)} dove {@code n} è il numero dei nodi del grafo.
 * 
 * @author Luca Tesei
 *
 * @param <V>
 *            il tipo delle etichette dei nodi del grafo
 * @param <E>
 *            il tipo delle etichette degli archi del grafo
 */
public class DijkstraShortestPathComputer<V, E> {

    //Le variabili di istanza
	private Graph<V,E> graph;
	private boolean computed = false;
	private GraphNode<V> lastSource = null;
    /**
     * Crea un calcolatore di cammini minimi a sorgente singola per un grafo
     * diretto e pesato privo di pesi negativi.
     * 
     * @param graph
     *                  il grafo su cui opera il calcolatore di cammini minimi
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato è vuoto
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato non è diretto
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato non è pesato,
     *                                      cioè esiste almeno un arco il cui
     *                                      peso è {@code Double.NaN}.
     * @throws IllegalArgumentException
     *                                      se il grafo passato contiene almeno
     *                                      un peso negativo.
     */
    public DijkstraShortestPathComputer(Graph<V, E> graph) {
    	if(graph==null) {
    		throw new NullPointerException("Il parametro passato è null");
    	}
    	
    	if(graph.isEmpty()) {
    		throw new IllegalArgumentException("Il grafo è vuoto");
    	}
    	
    	if(!graph.isDirected()) {
    		throw new IllegalArgumentException("Il grafo non è orientato");
    	}
    	//Faccendo i controlli che il grafo passato non sia vuoto e diretto andiamo che gli archi siano tutti pesati e non negativi
    	for(GraphEdge<V,E> arco: graph.getEdges()) {
    		if(arco.getWeight()<0) {
    			throw new IllegalArgumentException("Il grafo contiene un peso negativo");
    		}
    		if(Double.isNaN(arco.getWeight())){
    			throw new IllegalArgumentException("Il grafo contiene un arco non pesato");
    		}
    	}
    	
    	this.graph = graph;
    }

    /**
     * Inizializza le informazioni necessarie associate ai nodi del grafo
     * associato a questo calcolatore ed esegue l'algoritmo di Dijkstra sul
     * grafo.
     * 
     * @param sourceNode
     *                       il nodo sorgente da cui calcolare i cammini minimi
     *                       verso tutti gli altri nodi del grafo
     * @throws NullPointerException
     *                                      se il nodo passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                      se il nodo passato non esiste nel
     *                                      grafo associato a questo calcolatore
     */
    public void computeShortestPathsFrom(GraphNode<V> sourceNode) {
    	if(sourceNode == null) {
    		throw new NullPointerException("Il parametro passato è null");
    	}
    	if(!this.getGraph().containsNode(sourceNode)) {
    		throw new IllegalArgumentException("Il nodo passato non esiste all'interno del grafo");
    	}
    	//Se non è stato mai computato allora mettiamo computed uguale a true
    	if(!this.computed) {
    		this.computed = true;
    	}
    	//Mettiamo come lastSource il nodo passato come parametro
    	this.lastSource = sourceNode;
    	
    	ArrayList<GraphNode<V>> coda = new ArrayList<GraphNode<V>>();
    	
    	//Prepariamo i nodi con la distanza, il nodo di origine lo mettiamo a 0 e gli altri li mettiamo ad un valore di POSITIVE_INFINITY
    	//Aggiungiamo inoltre il nodo ad una coda
    	for(GraphNode<V> nodo : this.getGraph().getNodes()) {
    		if(nodo.equals(sourceNode)) {
    			nodo.setFloatingPointDistance(0);
    		}else {
    			nodo.setFloatingPointDistance(Double.POSITIVE_INFINITY);
    		}
    		nodo.setPrevious(null);
    		coda.add(nodo);
    	}
    	//Finché la coda non è vuota andiamo a ciclare
    	while(!coda.isEmpty()) {
    		GraphNode<V> nodoMinimo = null;
    		//Dobbiamo trovare il nodo con il valore minimo di distanza e lo salviamo dentro nodoMinimo
    		for(GraphNode<V> nodo: coda) {
    			if(nodoMinimo==null || nodo.getFloatingPointDistance()<nodoMinimo.getFloatingPointDistance()) {
    				nodoMinimo = nodo;
    			}
    		}
    		//Rimuoviamo dalla coda il nodoMinimo
    		coda.remove(nodoMinimo);
    		
    		//Prendiamo i vicini del nodoMinimo così possiamo ricevere gli archi tra il nodoMinimo e per ogni vicino
    		for(GraphNode<V> vicino: this.getGraph().getAdjacentNodes(nodoMinimo)) {
    			Set<GraphEdge<V,E>> archi = this.getGraph().getEdgesBetween(nodoMinimo, vicino);
    			//Siccome il grafo può ammettere più archi tra due nodi ma con peso diverso
    			//allora come per il nodoMinimo troviamo l'arco con il peso minimo
    			GraphEdge<V,E> arcoMinimo = null;
    			for(GraphEdge<V,E> arco : archi) {
    				if(arcoMinimo == null || arco.getWeight() < arcoMinimo.getWeight()) {
    					arcoMinimo = arco;
    				}
    			}
    			//Il nuovo peso sarà la distanza del nodoMinimo + il peso dell'arcoMinimo
    			//Se il peso è minore della distanza del nodo adiacente allora cambiamo la distanza e cambiamo anche il previous e lo facciamo 
    			//corrispondere con il nodoMinimo
    			Double peso = nodoMinimo.getFloatingPointDistance() + arcoMinimo.getWeight();
    			if(peso < vicino.getFloatingPointDistance()) {
    				vicino.setFloatingPointDistance(peso);
    				vicino.setPrevious(nodoMinimo);
    			}
    			
    		}
    	}
    }

    /**
     * Determina se è stata invocata almeno una volta la procedura di calcolo
     * dei cammini minimi a partire da un certo nodo sorgente specificato.
     * 
     * @return true, se i cammini minimi da un certo nodo sorgente sono stati
     *         calcolati almeno una volta da questo calcolatore
     */
    public boolean isComputed() {
    	//Utilizziamo la variabile booleana di istanza computed per sapere se è stato computato almeno una volta o no
        return this.computed;
    }

    /**
     * Restituisce il nodo sorgente specificato nell'ultima chiamata effettuata
     * a {@code computeShortestPathsFrom(GraphNode<V>)}.
     * 
     * @return il nodo sorgente specificato nell'ultimo calcolo dei cammini
     *         minimi effettuato
     * 
     * @throws IllegalStateException
     *                                   se non è stato eseguito nemmeno una
     *                                   volta il calcolo dei cammini minimi a
     *                                   partire da un nodo sorgente
     */
    public GraphNode<V> getLastSource() {
    	if(!this.isComputed()) {
    		throw new IllegalStateException("Il calcolo dei cammini minimi non è stato mai eseguito!");
    	}
    	//Utilizziamo la variabile lastSource di istanza
        return this.lastSource;
    }

    /**
     * Restituisce il grafo su cui opera questo calcolatore.
     * 
     * @return il grafo su cui opera questo calcolatore
     */
    public Graph<V, E> getGraph() {
        return this.graph;
    }

    /**
     * Restituisce una lista di archi dal nodo sorgente dell'ultimo calcolo di
     * cammini minimi al nodo passato. Tale lista corrisponde a un cammino
     * minimo tra il nodo sorgente e il nodo target passato.
     * 
     * @param targetNode
     *                       il nodo verso cui restituire il cammino minimo
     *                       dalla sorgente
     * @return la lista di archi corrispondente al cammino minimo; la lista è
     *         vuota se il nodo passato è il nodo sorgente. Viene restituito
     *         {@code null} se il nodo passato non è raggiungibile dalla
     *         sorgente
     * 
     * @throws NullPointerException
     *                                      se il nodo passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                      se il nodo passato non esiste
     * 
     * @throws IllegalStateException
     *                                      se non è stato eseguito nemmeno una
     *                                      volta il calcolo dei cammini minimi
     *                                      a partire da un nodo sorgente
     * 
     */
    public List<GraphEdge<V, E>> getShortestPathTo(GraphNode<V> targetNode) {
        // TODO implementare
    	if(targetNode == null) {
    		throw new NullPointerException("Il parametro passato è null");
    	}
    	if(!this.getGraph().getNodes().contains(targetNode)) {
    		throw new IllegalArgumentException("Il nodo passato non esiste all'interno del grafo");
    	}
    	if(!this.isComputed()) {
    		throw new IllegalStateException("Il calcolo dei cammini minimi non è stato mai eseguito!");
    	}
    	//Prendiamo il nodo esistente nel grafo che abbia come etichetta la stessa del nodo passato come parametro ovvero targetNode
    	GraphNode<V> destinazione = this.getGraph().getNode(targetNode.getLabel());
    	ArrayList<GraphEdge<V,E>> lista = new ArrayList<GraphEdge<V,E>>();
    	//Se il nodo di destinazione corrisponde all'ultimo nodo con il quale abbiamo computato ritorniamo una lista vuota
    	if(destinazione.equals(this.getLastSource())) {
    		return lista;
    	}
    	//Se esiste un previous del nodo di destinazione allora facciamo un ciclo.
    	//Troviamo gli archi dal nodo prima di quello di destinazione e destinazione. Siccome possono esserci più archi troviamo il minimo
    	//Alla fine del ciclo aggiungiamo nella posizione 0 dell'ArrayList l'arcoMinimo i futuri cicli faranno scorrere l'ArrayList 
    	//ogni volta che lo aggiungiamo all'inizio, dopo tutto questo sostituiamo alla destinazione il suo previous
    		while(destinazione.getPrevious()!=null) {
    			Set<GraphEdge<V,E>> archi = this.getGraph().getEdgesBetween(destinazione.getPrevious(), destinazione);
    			GraphEdge<V,E> minArco = null;
    			for(GraphEdge<V,E> arco : archi) {
    				if(minArco == null || arco.getWeight() < minArco.getWeight()) {
    					minArco = arco;
    				}
    			}
    			lista.add(0,minArco);
    			destinazione = destinazione.getPrevious();
    		}
    	
    	//Se la lista è uvota ritorniamo null in quanto non è stato possibile trovare una via per la destinazione
    	//Altrimenti ritorniamo la lista
    	
        return lista.isEmpty() ? null : lista;
    }

    /**
     * Genera una stringa di descrizione di un path riportando i nodi
     * attraversati e i pesi degli archi. Nel caso di cammino vuoto genera solo
     * la stringa {@code "[ ]"}.
     * 
     * @param path
     *                 un cammino minimo
     * @return una stringa di descrizione del cammino minimo
     * @throws NullPointerException
     *                                  se il cammino passato è nullo
     */
    public String printPath(List<GraphEdge<V, E>> path) {
        if (path == null)
            throw new NullPointerException(
                    "Richiesta di stampare un path nullo");
        if (path.isEmpty())
            return "[ ]";
        // Costruisco la stringa
        StringBuffer s = new StringBuffer();
        s.append("[ " + path.get(0).getNode1().toString());
        for (int i = 0; i < path.size(); i++)
            s.append(" -- " + path.get(i).getWeight() + " --> "
                    + path.get(i).getNode2().toString());
        s.append(" ]");
        return s.toString();
    }
    
    public static void main(String [] args){
    	
    	//Grafo creato in base a https://www.cs.usfca.edu/~galles/visualization/Dijkstra.html per verificare 
    	
    	 Graph<Integer, String> g = new AdjacentListDirectedGraph<Integer, String>();
    	 GraphNode<Integer> zero = new DefaultGraphNode<Integer>(0);
    	 GraphNode<Integer> uno = new DefaultGraphNode<Integer>(1);
    	 GraphNode<Integer> due = new DefaultGraphNode<Integer>(2);
    	 GraphNode<Integer> tre = new DefaultGraphNode<Integer>(3);
    	 GraphNode<Integer> quattro = new DefaultGraphNode<Integer>(4);
    	 GraphNode<Integer> cinque = new DefaultGraphNode<Integer>(5);
    	 GraphNode<Integer> sei = new DefaultGraphNode<Integer>(6);
    	 GraphNode<Integer> sette = new DefaultGraphNode<Integer>(7);
    	 g.addNode(zero);
    	 g.addNode(uno);
    	 g.addNode(due);
    	 g.addNode(tre);
    	 g.addNode(quattro);
    	 g.addNode(cinque);
    	 g.addNode(sei);
    	 g.addNode(sette);
    	 GraphEdge<Integer, String> _1 = new DefaultGraphEdge<Integer, String>(zero,quattro, "da 0 a 4", true, 5);
    	 GraphEdge<Integer, String> _2 = new DefaultGraphEdge<Integer, String>(quattro,sei, "da 4 a 6", true, 5);
    	 GraphEdge<Integer, String> _3 = new DefaultGraphEdge<Integer, String>(sei,due, "da 6 a 2", true, 9);
    	 //GraphEdge<Integer, String> _3_2 = new DefaultGraphEdge<Integer, String>(sei,due, "da 6 a 2", true, 1);
    	 GraphEdge<Integer, String> _4 = new DefaultGraphEdge<Integer, String>(due,cinque, "da 2 a 5", true, 2);
    	 GraphEdge<Integer, String> _5 = new DefaultGraphEdge<Integer, String>(cinque,tre, "da 5 a 3", true, 9);
    	 GraphEdge<Integer, String> _6 = new DefaultGraphEdge<Integer, String>(cinque,sette, "da 5 a 7", true, 5);
    	 GraphEdge<Integer, String> _7 = new DefaultGraphEdge<Integer, String>(sette,cinque, "da 7 a 5", true, 9);
    	 GraphEdge<Integer, String> _8 = new DefaultGraphEdge<Integer, String>(tre,cinque, "da 3 a 5", true, 5);
    	 GraphEdge<Integer, String> _9 = new DefaultGraphEdge<Integer, String>(tre,uno, "da 3 a 1", true, 5);
    	 GraphEdge<Integer, String> _10 = new DefaultGraphEdge<Integer, String>(uno,tre, "da 1 a 3", true, 6);
    	 GraphEdge<Integer, String> _11 = new DefaultGraphEdge<Integer, String>(uno,sei, "da 1 a 6", true, 3);
    	 g.addEdge(_1);
    	 g.addEdge(_2);
    	 g.addEdge(_3);
    	 //g.addEdge(_3_2);
    	 g.addEdge(_4);
    	 g.addEdge(_5);
    	 g.addEdge(_6);
    	 g.addEdge(_7);
    	 g.addEdge(_8);
    	 g.addEdge(_9);
    	 g.addEdge(_10);
    	 g.addEdge(_11);
    	 
    	  DijkstraShortestPathComputer<Integer, String> c = new DijkstraShortestPathComputer<Integer, String>(g);
    	  c.computeShortestPathsFrom(zero);
    	  System.out.println(c.printPath(c.getShortestPathTo(sette)));
	}

}

package it.polito.tdp.food.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	private int tempo;
	private Set<Food> cibiPreparati;
	private PriorityQueue<Event> coda;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private int numeroStazioni;
	private Food partenza;
	private int tempoTotale;


	public Simulator(Graph<Food, DefaultWeightedEdge> grafo, int numeroStazioni, Food partenza) {
		super();
		this.grafo = grafo;
		this.numeroStazioni = numeroStazioni;
		this.partenza = partenza;
	}

	public void init() {
		tempo=0;
		tempoTotale=0;
		cibiPreparati= new HashSet<Food>();
		coda= new PriorityQueue<Event>();
		
		cibiPreparati.add(partenza);
		for (int i=0; i<numeroStazioni&&i<listaVicini(partenza).size(); i++)
		{
			Food arrivo= listaVicini(partenza).get(i);
			coda.add(new Event(arrivo, getPeso(partenza,arrivo)));
			cibiPreparati.add(arrivo);
		}
		
	};
	
	private List<Food> listaVicini(Food partenza) {
		List<Food> verticiVicini= new LinkedList<Food>();
		List<DefaultWeightedEdge> archiVicini= new LinkedList<DefaultWeightedEdge>(grafo.edgesOf(partenza));
		archiVicini.sort(new Comparator<DefaultWeightedEdge>() {

			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				Double d1= grafo.getEdgeWeight(o1);
				Double d2= grafo.getEdgeWeight(o2);
				return d1.compareTo(d2);
			}
		});
		for (DefaultWeightedEdge e: archiVicini)
		{
			verticiVicini.add(Graphs.getOppositeVertex(grafo, e, partenza));
		}
		return verticiVicini;
	}
	
	private int getPeso(Food partenza, Food arrivo)
	{
		return (int) grafo.getEdgeWeight(grafo.getEdge(partenza, arrivo));
	}

	public void run() {
	
		while (!coda.isEmpty())
		{
			Event e = coda.poll();
			Food ciboDaAggiungere= getFoodMaxCalorie(e.getCibo());
			if (ciboDaAggiungere!=null)
				
			{
			int durataCiboDaAggiungere= getPeso(e.getCibo(), ciboDaAggiungere);
			Event daAggiungere= new Event(ciboDaAggiungere, tempo+durataCiboDaAggiungere);
			coda.add(daAggiungere);
			cibiPreparati.add(ciboDaAggiungere);
			tempoTotale=tempoTotale+durataCiboDaAggiungere;
			}
			
			
		}
}

	private Food getFoodMaxCalorie(Food cibo) {
		List<Food> listaVicini= listaVicini(cibo);
		for (Food f: listaVicini)
			if (!cibiPreparati.contains(f))
				return f;
		return null;
	}

	public Object getNumCibi() {
		// TODO Auto-generated method stub
		return cibiPreparati.size();
	}

	public Object getTempoTotale() {
		// TODO Auto-generated method stub
		return tempoTotale;
	};
	
	
}

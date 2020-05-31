package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;


public class Model {
	private TreeMap<Integer, Food> mappaCibi;
	private FoodDao dao;
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private Simulator sim;
	
	public Model() {
		dao=new FoodDao();
	}
	
	public TreeMap<Integer, Food> getMappaCibi() {
		return mappaCibi;
	}
	
	public void creaGrafo(int porzioni) {
		grafo= new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		mappaCibi= new TreeMap<Integer, Food>();
		dao.getCibiPerPorzione(mappaCibi, porzioni);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(grafo, mappaCibi.values());
		
		//Aggiungo gli archi
		for(Coppie c: dao.listAllCoppie(mappaCibi)) {
			Graphs.addEdge(grafo, c.getF1(), c.getF2(), c.getPeso());
		}
	}
	
	public List<Food> best(Food cibo){
		List<DefaultWeightedEdge> archi= new LinkedList<DefaultWeightedEdge>(grafo.edgesOf(cibo));
		System.out.println(archi);
		List<Food> best= new LinkedList<Food>();
		archi.sort(new Comparator<DefaultWeightedEdge>() {
			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				if (grafo.getEdgeWeight(o1)-grafo.getEdgeWeight(o2)<0)
					return 1;
				else return -1;
			}
		});
		if (archi.size()<5)
		{
			return null;
		}
		for (int i=0; i<5; i++)
		{
			best.add(Graphs.getOppositeVertex(grafo, archi.get(i), cibo));
		}
		return best;
	}

	public Object getNumArchi() {
		// TODO Auto-generated method stub
		return grafo.edgeSet().size();
	}

	public Object getNumVertici() {
		// TODO Auto-generated method stub
		return grafo.vertexSet().size();
	}

	public String simula(int k, Food cibo) {
		sim= new Simulator(grafo, k, cibo);
		sim.init();
		sim.run();
		return String.format("Sono stati preparati %d cibi in %d minuti", sim.getNumCibi(), sim.getTempoTotale());
	}
}

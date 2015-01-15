package me.allevato.graph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * A list of nodes and edges. Also stores a list of edges attached to each node ( the indexed edges list).
 * @author adam
 *
 * @param <NodeType> the class of nodes to use
 * @param <EdgeType> the class of edges to use
 * @param <ExtraI> the type of extra info attached to nodes
 * @param <ExtraIE> the type of extra info attached to edges
 */
public class Graph<NodeType extends Node<ExtraI>, EdgeType extends Edge<ExtraIE>, ExtraI, ExtraIE> {
	private Vector<NodeType> nodes;
	private ArrayList<EdgeType> edges;
	private Vector<ArrayList<EdgeType>> indexedEdges;
	
	protected Iterator<NodeType> nodeIterator;
	protected Iterator<EdgeType> edgeIterator;
	
	public Graph() {
		Node.resetNodes(); //reset indices
		Edge.resetEdges();
		nodes = new Vector<NodeType>();
		edges = new ArrayList<EdgeType>();
		indexedEdges = new Vector<ArrayList<EdgeType>>();
	}

	public Vector<NodeType> getNodes() {
		return nodes;
	}

	public ArrayList<EdgeType> getEdges() {
		return edges;
	}

	public Iterator<NodeType> getNodeIterator() {
		return nodes.iterator();
	}

	public void setNodeIterator(Iterator<NodeType> nodeIterator) {
		this.nodeIterator = nodeIterator;
	}

	public Iterator<EdgeType> getEdgeIterator() {
		return edges.iterator();
	}

	public void setEdgeIterator(Iterator<EdgeType> edgeIterator) {
		this.edgeIterator = edgeIterator;
	}

	public NodeType n(int x) {
		//System.out.println(nodes.get(1));
		return nodes.get(x);
	}

	public ArrayList<EdgeType> ne(int x) {
		//System.out.println(nodes.get(1));
		return indexedEdges.get(x);
	}
	
	public void addNode(NodeType n) {
		nodes.add(n);
		//System.out.println("adding indexed edge list for node id " + n.getId());
		indexedEdges.add(new ArrayList<EdgeType>());
	}
	public void addEdge(EdgeType e) {
		edges.add(e);
		indexedEdges.elementAt(e.getSource()).add(e);
	}


	public Vector<ArrayList<EdgeType>> getIndexedEdges() {
		return indexedEdges;
	}
	
	public int getNumNodes() {
		return nodes.size();
	}
}
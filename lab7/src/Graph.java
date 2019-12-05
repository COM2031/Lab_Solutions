/* @author Dr. Manal Helal for @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * Led by Prof. Steve Schneider
 * @date created: November 2019
 * @University of Surrey
 * Week 7 Lab: Graph Algorithms & Data Structures
 */


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.TreeSet;

/* The Graph<V> class is intended to be a generic class that define various graph representations and various algorithms
 * Time permits, more can be added as required, and contributions are welcomes
 * Some references to create the current representations and algoriths are:
 * 1. Examples from Daiel Liang book on Java 
 * 2. Princeton University Algorithms Course
 * 3. https://www.geeksforgeeks.org/
 * 4. Previous implementations by Andre Gruning and Steve Schnieder for COM1029 & COM2031 for Data strucures and Algorithms modules in Surrey University
 * 
 * You can experiment by creating more constructors that takes different types of graph representations and generate all or some of the representations that you need
 * 
 * 
 */

public class Graph<V> { 

	public V[] vertices;	// vertices if type V as passed in the template

	// various representations of edges
	public int[][] edges; // incidence list of Edge 2D arrays rather than linked list, as first dimension is the number of edges, 
	// and the second dimension is 2 to identify the 2 vertices incident on the edge, there could be a third value in the second dimension
	// for the cost/weight in weighted graphs
	public List<List<Edge>> neighbors; // Adjacency lists, having first dimension as list of vertices, and second as the neighbours of this vertex. So one edge would be
	// defined twice, one time in the source vertex, and another time in the destination vertex.
	public List<Edge> adjacencyList; // Adjacency List for all edges defined once
	public int[][] adjacencyMatrix; // Adjacency Matrix, vertices as rows (sources) and vertices as columns (destinations) and content of the cell is zero when there is
	// no edge, or 1 when there is an edge and unweighted graphs, or the cost/weight for weighted graphs

	/* You can experiment with defining incidence matrix, a matrix of 0's and 1's whose rows represent vertices and whose columns represent edges */

	/* You can experiment with defining Laplacian matrix as a modified form of the adjacency matrix that incorporates information about the degrees of the vertices.  */
	/* You can experiment with defining distance matrix, with both its rows and columns indexed by vertices, but rather than containing specific edge information in each cell, 
	 * 													 it contains the length of a shortest path between two vertices. */


	/********** Various Constructors
	 * 
	 * @param edges
	 * @param vertices
	 * 
	 * and transform to the various representations
	 * 
	 * 
	 */


	// this constructor takes edges as incidence list of 2D arrays, and generate the remaining graph representations
	public Graph (int[][] edges, V[] vertices, boolean mat) {
		if (mat) { // of the 2D edges data contain adjacency matrix structure, number of vertices as first and second dimensions, then read it as follows
			this.adjacencyMatrix = edges;

			this.adjacencyList = new ArrayList<Edge>();
			// create an adjacency list from the adjacency matrix
			for (int i = 0; i < edges.length; i++) {
				for (int j = 0; j < edges.length; j++) {
					if (edges[i][j] > 0) {
						Edge e = new Edge(i, j, edges[i][j]);

						this.adjacencyList.add(e); 
					}
				}
			}
			// create adjacency 2D list from adjacency object list
			this.edges = new int[this.adjacencyList.size()][3];
			for (int i = 0; i < this.adjacencyList.size(); i++) {
				this.edges[i][0] = this.adjacencyList.get(i).src;
				this.edges[i][1] = this.adjacencyList.get(i).dest;
				this.edges[i][2] = this.adjacencyList.get(i).cost;
			}
		}
		else { // else, this 2D edges is an adjacency list, in which the 1D is the number of edges and second dimension contain either 2 or 3 elements, first 2 define source and destination nodes, and third data is present is the weight
			this.edges = edges;
			this.adjacencyMatrix = new int[vertices.length][vertices.length];
			this.adjacencyList = new ArrayList<Edge>();
			// create an adjacency matrix from the incidence list defined as array
			for (int i = 0; i < edges.length; i++) {
				Edge e;
				if (edges[i].length > 2) {// if the weight was included 
					this.adjacencyMatrix[edges[i][0]][edges[i][1]] = edges[i][2];
					e = new Edge(edges[i][0], edges[i][1], edges[i][2]);
				}
				else {
					this.adjacencyMatrix[edges[i][0]][edges[i][1]] = 1;
					e = new Edge(edges[i][0], edges[i][1]);
				}
				adjacencyList.add(e); 
			}

		}
		createAdjacencyLists(this.edges, vertices.length);
		this.vertices = vertices;

	}

	// this constructor takes Edges as incidence list of Edge Objects, and array of vertices, and generate the remaining graph representations
	public Graph (List<Edge> edges, V[] vertices) {
		if (edges != null) {
			this.edges = new int[edges.size()][3];
			this.adjacencyList = edges;
			for (int i = 0; i < edges.size(); i++) {
				this.edges[i][0] = edges.get(i).src;
				this.edges[i][1] = edges.get(i).dest;
				this.edges[i][2] = edges.get(i).cost;
			}
			this.adjacencyMatrix = new int[vertices.length][vertices.length];
			// create an adjacency matrix from the incidence list defined as list of Edge Objects
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).cost > 0)
					this.adjacencyMatrix[edges.get(i).src][edges.get(i).dest] = edges.get(i).cost;
				else
					this.adjacencyMatrix[edges.get(i).src][edges.get(i).dest] = 1;
			}

		}
		this.vertices = vertices;
		createAdjacencyLists(this.edges, vertices.length);

	}

	// this constructor takes Edges as incidence list of Edge Objects, and List of vertices Objects, and generate the remaining graph representations
	public Graph(List<Edge> edges, List<V> vertices, Class<V> c) {
		if (edges != null) {
			this.adjacencyList = edges;
			this.edges = new int[edges.size()][3];
			for (int i = 0; i < edges.size(); i++) {
				this.edges[i][0] = edges.get(i).src;
				this.edges[i][1] = edges.get(i).dest;
				this.edges[i][2] = edges.get(i).cost;
			}
			this.adjacencyMatrix = new int[vertices.size()][vertices.size()];
			// create an adjacency matrix from the incidence list defined as list of Edge Objects
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).cost > 0)
					this.adjacencyMatrix[edges.get(i).src][edges.get(i).dest] = edges.get(i).cost;
				else
					this.adjacencyMatrix[edges.get(i).src][edges.get(i).dest] = 1;
			}
		}
		// Use Array native method to create array
		// of a type only known at run time

		if (vertices != null) {
			@SuppressWarnings("unchecked")
			V[] verticesArr = (V[]) Array.newInstance(c, vertices.size());
			for (int i=0; i<verticesArr.length; i++)
				verticesArr[i] = vertices.get(i);
			this.vertices = verticesArr;

			// creating the 2D adjacency list for each vertex
			createAdjacencyLists(this.edges, this.vertices.length);
		}
	}



	/** Create adjacency lists for each vertex */
	private void createAdjacencyLists(int[][] edges, int numberOfVertices) {

		// Create a linked list
		this.neighbors = new ArrayList<List<Edge>>();
		for (int i = 0; i < numberOfVertices; i++) 
			this.neighbors.add(new ArrayList<Edge>());


		for (int i = 0; i < edges.length; i++) {
			int u = edges[i][0];
			int v = edges[i][1];
			int cost = 0; // for unweighed graphs
			if (edges[i].length > 2) // if the weight was included 
				cost = edges[i][2];
			Edge e = new Edge(u, v, cost);
			this.neighbors.get(u).add(e);
		}
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		if (v < 0 || v >= this.vertices.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.vertices.length -1));
	}

	// Returns true if edge u-v is a valid edge to be include in MST. An edge is valid if one end is 
	// already included in MST and other is not in MST. 
	boolean isValidEdge(int u, int v, boolean[] inMST) { 
		if (inMST != null) {
			if (u == v) 
				return false; 
			if (inMST[u] == false && inMST[v] == false) 
				return false; 
			else if (inMST[u] == true && inMST[v] == true) 
				return false; 
			else
				return true;
		}
		else { // if the method is called without MST, it just checks if the edge is already defined
			for (int i = 0; i < this.edges.length; i++) {
				if ((this.edges[i][0] == u) && (this.edges[i][1] == v)) 
					return true;
			}
			return false;
		}

	} 
	/********** Various Graph Properties and editing
	 * 
	 * 
	 * query graph components, cycles and add more edges and vertices
	 * printing
	 * removing, updating can be implemented as well
	 * 
	 */

	public int getDegree (int v) {
		validateVertex(v);
		return this.neighbors.get(v).size();
	}


	public List<Edge> getNeighbors(int index) {
		return neighbors.get(index);
	}
	/** Return the number of vertices in the graph */
	public  int getSize() {
		return this.vertices.length;
	}

	/** Return the vertices in the graph */
	public  V[] getVertices(){
		return this.vertices;
	}

	/** Return the object for the specified vertex index */
	public  V getVertex(int index) {
		return this.vertices[index];
	}

	/** Return the index for the specified vertex object, any Vertex type should override toString to make this method word as expected */

	public int getIndex(String name) {
		for (int i = 0; i < this.vertices.length ;  i++) {
			if (name.equalsIgnoreCase(this.vertices[i].toString()))
				return i;

		}
		return -1;

	}

	// https://www.baeldung.com/java-graph-has-a-cycle

	public boolean isCyclic() {
		boolean[] BeingVisited = new boolean[this.vertices.length];
		boolean[] visited = new boolean[this.vertices.length];
		return isCyclic(0, BeingVisited, visited);

	}

	public boolean isCyclic(int sourceVertex, boolean[] BeingVisited, boolean[] visited) {

		BeingVisited[sourceVertex] = true;

		for (Edge neighbor : this.getNeighbors(sourceVertex)) {
			if (BeingVisited[neighbor.dest]) {
				// backward edge exists
				return true;
			} else if (!visited[neighbor.dest] && isCyclic(neighbor.dest, BeingVisited, visited)) {
				return true;
			}
		}

		BeingVisited[sourceVertex] = false;
		visited[sourceVertex] = true;
		return false;
	}

	public List<Integer> getACycle(int u) {
		List<Integer> searchOrder = new ArrayList<Integer>();
		int[] parent = new int[this.vertices.length];
		for (int i = 0; i < parent.length; i++) {
			parent[i] = -1;
		}

		boolean[] isVisited = new boolean[this.vertices.length];

		return getACycle(u, u, parent, searchOrder, isVisited); 
	}
	private List<Integer> getACycle(int first, int v, int[] parent, List<Integer> searchOrder, boolean[] isVisited) {
		searchOrder.add(v);
		isVisited[v] = true;

		for (Edge i : this.neighbors.get(v)) { // check all neighbours, and label them as visited
			if(first == i.dest) { // first the last vertices are the same in any cycle.
				if(searchOrder.size() > 2) { // minimum 3 vertices to form a cycle, first and last be the same, or any more in the middle
					return searchOrder;
				}
			}
			if (!isVisited[i.dest]) {
				parent[i.dest] = v;
				int[] newParent = java.util.Arrays.copyOf(parent, parent.length);
				boolean[] newIsVisited = java.util.Arrays.copyOf(isVisited, parent.length);
				@SuppressWarnings("unchecked")
				List<Integer> newSearchOrder = (List<Integer>) ((ArrayList<Integer>)searchOrder).clone();
				// this is depth first, get further neighbours of each child before going to siblings, 
				List<Integer> result = getACycle(first, i.dest, newParent, newSearchOrder, newIsVisited);
				if(result != null) {
					return result;
				}
			} 

		}
		return null;
	}

	// This function returns true if graph graph is Bipartite, else false 
	boolean isBipartite() { 
		// Create a colour array to store colours assigned to all vertices. 
		// Vertex number is used as index  in this array. The value '-1' 
		// of colorArr[i] is used to indicate  that no colour is assigned 
		// to vertex 'i'. The value 1 is used to indicate first colour 
		// is assigned and value 0 indicates second colour is assigned. 
		int colorArr[] = new int[this.vertices.length]; 
		for (int i=0; i<this.vertices.length; ++i) 
			colorArr[i] = -1; 

		// Assign first colour to source 
		colorArr[0] = 1; 

		// Create a queue (FIFO) of vertex numbers  
		// and enqueue source vertex for BFS traversal 
		LinkedList<Integer>q = new LinkedList<Integer>(); 
		q.add(0); 

		// Run while there are vertices in queue (Similar to BFS) 
		while (q.size() != 0) 
		{ 
			// Dequeue a vertex from queue 
			int u = q.poll(); 

			// Return false if there is a self-loop  
			if (this.adjacencyMatrix[u][u] == 1) 
				return false;  

			// Find all non-coloured adjacent vertices 
			for (int v=0; v<this.vertices.length; ++v) 
			{ 
				// An edge from u to v exists  
				// and destination v is not coloured 
				if (this.adjacencyMatrix[u][v]==1 && colorArr[v]==-1) 
				{ 
					// Assign alternate colour to this adjacent v of u 
					colorArr[v] = 1-colorArr[u]; 
					q.add(v); 
				} 

				// An edge from u to v exists and destination 
				//  v is coloured with same colour as u 
				else if (this.adjacencyMatrix[u][v]==1 && colorArr[v]==colorArr[u]) 
					return false; 
			} 
		} 
		// If we reach here, then all adjacent vertices can 
		// be coloured with alternate colour 
		return true; 
	} 

	/** Print the edges */
	public void printEdges() {

		for (int u = 0; u < neighbors.size(); u++) {
			System.out.print(getVertex(u) + " (" + u + "): ");
			for (int j = 0; j < neighbors.get(u).size(); j++) {
				System.out.print("(" + u + ", " +
						neighbors.get(u).get(j) + ") ");
			}
			System.out.println();
		}  
	}

	public List<List<Integer>> getConnectedComponents() {
		List<List<Integer>> list = new ArrayList<List<Integer>>();

		List<Integer> vertexIndices = new ArrayList<Integer>();	
		for (int i = 0; i < this.vertices.length; i++)
			vertexIndices.add(i);  

		while (vertexIndices.size() > 0) {
			Tree tree = dfs(vertexIndices.get(0));
			list.add(tree.getSearchOrder());
			vertexIndices.removeAll(tree.getSearchOrder());
		}

		return list;
	}

	/** Clear graph */
	public void clear() {
		this.vertices = null;
		this.edges = null; 
		this.neighbors = null; 
		this.adjacencyList = null; 
		this.adjacencyMatrix = null;

	}

	/** Add a vertex to the graph */  
	public void addVertex(V vertex, Class<V> c) {

		List<V> verticeslist;
		if (this.vertices == null)
			verticeslist =  new ArrayList<V>();
		else
			verticeslist  =  new ArrayList<V>(Arrays.asList(this.vertices));

		verticeslist.add(vertex);

		@SuppressWarnings("unchecked")
		V[] verticesArr = (V[]) Array.newInstance(c, verticeslist.size());
		for (int i=0; i<verticesArr.length; i++)
			verticesArr[i] = verticeslist.get(i);
		this.vertices = verticesArr;
	}

	/** Add an edge to the graph */  
	public void addEdge(int u, int v) {
		addEdge (u, v, 0);
	}

	/** Add an edge to the graph */  
	public void addEdge(int u, int v, int c) {
		validateVertex(u);
		validateVertex(v);
		if (this.adjacencyList == null)
			this.adjacencyList = new ArrayList<Edge>();
		Edge e = new Edge (u, v, c);
		this.adjacencyList.add(e);

		this.edges = new int[this.adjacencyList.size()][3];

		for (int i = 0; i < this.adjacencyList.size(); i++) {
			this.edges[i][0] = this.adjacencyList.get(i).src;
			this.edges[i][1] = this.adjacencyList.get(i).dest;
			this.edges[i][2] = this.adjacencyList.get(i).cost;
		}
		this.adjacencyMatrix = new int[vertices.length][vertices.length];
		// create an adjacency matrix from the incidence list defined as list of Edge Objects
		for (int i = 0; i < this.adjacencyList.size(); i++) {
			if (this.adjacencyList.get(i).cost > 0)
				this.adjacencyMatrix[this.adjacencyList.get(i).src][this.adjacencyList.get(i).dest] = this.adjacencyList.get(i).cost;
			else
				this.adjacencyMatrix[this.adjacencyList.get(i).src][this.adjacencyList.get(i).dest] = 1;
		}

		createAdjacencyLists(this.edges, vertices.length);
	}


	/**
	 * Returns a string representation of this graph.
	 *
	 * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
	 *         followed by the <em>V</em> adjacency lists
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.vertices.length + " vertices, " + this.edges.length  + " edges " + System.getProperty("line.separator"));
		for (int v = 0; v < this.vertices.length; v++) {
			s.append(v + ": ");
			for (Edge e : neighbors.get(v)) {
				s.append(e + " ");
			}
			s.append(System.getProperty("line.separator"));
		}
		return s.toString();
	}


	/********** Various Graph Path Finding algorithms
	 * 
	 * 
	 * find path between source and destination using bfs, and using dfs traversal order
	 * find all paths between source and all other vertices in the graph using dijkstra using adjacency lists and adjacency matrices
	 * 
	 */

	public List<V> getPath(int u, int v) {
		validateVertex(u);
		validateVertex(v);

		Tree tree = bfs(u);
		ArrayList<V> path = new ArrayList<>();

		do {
			path.add(this.vertices[v]);
			v = tree.parent[v];
		} while (v != -1);

		Collections.reverse(path);
		return path;
	}

	public List<V> getPath_dfs(int u, int v) {
		Tree tree = dfs(u);
		ArrayList<V> path = new ArrayList<>();

		do {
			path.add(this.vertices[v]);
			v = tree.parent[v];
		} while (v != -1);

		Collections.reverse(path);
		return path;
	}

	// A utility function to print the constructed distance array 
	void printSolution(String title, int dist[]) { 
		System.out.println (title);
		System.out.println("Vertex \t\t Distance from Source"); 
		for (int i = 0; i < this.vertices.length; i++) 
			System.out.println(i + " \t\t " + dist[i]); 
	} 



	// Function that implements Dijkstra's single source shortest path 
	// algorithm for a graph represented using adjacency matrix 
	// representation and using priority queue data structures
	public int[] dijkstra_am(int src) { 
		int dist[] = new int[this.vertices.length]; 	// The output array. dist[i] will hold 
		// the shortest distance from src to i 

		// sptSet[i] will true if vertex i is included in shortest 
		// path tree or shortest distance from src to i is finalised 
		Boolean sptSet[] = new Boolean[this.vertices.length]; 

		// Initialise all distances as INFINITE and stpSet[] as false 
		for (int i = 0; i < this.vertices.length; i++) { 
			dist[i] = Integer.MAX_VALUE; 
			sptSet[i] = false; 
		} 

		// Distance of source vertex from itself is always 0 
		dist[src] = 0; 

		// Find shortest path for all vertices 
		for (int count = 0; count < this.vertices.length - 1; count++) { 
			// Pick the minimum distance vertex from the set of vertices 
			// not yet processed. u is always equal to src in first 
			// iteration. 
			int u = minDistance(dist, sptSet); 

			// Mark the picked vertex as processed 
			sptSet[u] = true; 

			// Update dist value of the adjacent vertices of the 
			// picked vertex. 
			for (int v = 0; v < this.vertices.length; v++) 

				// Update dist[v] only if is not in sptSet, there is an 
				// edge from u to v, and total weight of path from src to 
				// v through u is smaller than current value of dist[v] 
				if (!sptSet[v] && this.adjacencyMatrix[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + this.adjacencyMatrix[u][v] < dist[v]) 
					dist[v] = dist[u] + this.adjacencyMatrix[u][v]; 
		} 

		// return the constructed distance array 
		return dist; 
	} 

	//The dijkstra method calculates distances of shortest paths from src to all 
	//vertices, using adjacency list and Min Heap 
	public int[] dijkstra_al(int src){
		int INFINITY = Integer.MAX_VALUE;
		boolean[] SPT = new boolean[this.vertices.length];
		//	            //create heapNode for all the vertices
		MinHeap.HeapNode [] heapNodes = new MinHeap.HeapNode[this.vertices.length];
		for (int i = 0; i <this.vertices.length ; i++) {
			heapNodes[i] = new MinHeap.HeapNode();
			heapNodes[i].vertex = i;
			heapNodes[i].key = INFINITY;
		}
		//decrease the distance for the first index
		heapNodes[src].key = 0;
		//add all the vertices to the MinHeap
		MinHeap minHeap = new MinHeap(this.vertices.length);
		for (int i = 0; i <this.vertices.length ; i++) {
			minHeap.insert(heapNodes[i]);
		}
		//while minHeap is not empty
		while(!minHeap.isEmpty()){
			//extract the min
			MinHeap.HeapNode extractedNode = minHeap.extractMin();
			//extracted vertex
			int extractedVertex = extractedNode.vertex;
			SPT[extractedVertex] = true;
			//iterate through all the adjacent vertices
			List<Edge> list = this.neighbors.get(extractedVertex);
			//LinkedList<Edge> list = this.neighbours[extractedVertex];
			for (int i = 0; i <list.size() ; i++) {
				Edge edge = list.get(i);
				int destination = edge.dest;
				//only if  destination vertex is not present in SPT
				if(SPT[destination]==false ) {
					///check if distance needs an update or not
					//means check total weight from source to vertex_V is less than
					//the current distance value, if yes then update the distance
					int newKey =  heapNodes[extractedVertex].key + edge.cost ;
					int currentKey = heapNodes[destination].key;
					if(currentKey>newKey){
						decreaseKey(minHeap, newKey, destination);
						heapNodes[destination].key = newKey;
					}
				}
			}
		}
		//return the distances array
		int dist[] = new int[this.vertices.length];
		for (int i = 0; i < this.vertices.length; i++) 
			dist[i] = heapNodes[i].key;

		return dist;
	}

	public void decreaseKey(MinHeap minHeap, int newKey, int vertex){
		//get the index which distance's needs a decrease;
		int index = minHeap.indexes[vertex];
		//get the node and update its value
		MinHeap.HeapNode node = minHeap.mH[index];
		node.key = newKey;
		minHeap.bubbleUp(index);
	}

	/********** Various Graph MST Finding algorithms
	 * 
	 * BFS traversal, and DFS one generating trees without consideration to cost (not minimum).
	 * prims using adjacency lists and adjacency matrices
	 * Kruskal algorithm using adjacency matrix, and another kruskal greedy algorithm sorted adjacency lists
	 * 
	 */


	/** Initial method to call the later recursive method */
	public Tree dfs(int v) {
		List<Integer> searchOrder = new ArrayList<Integer>();
		int[] parent = new int[vertices.length];
		for (int i = 0; i < parent.length; i++)
			parent[i] = -1; // Initialise parent[i] to  -1

		// Mark visited vertices
		boolean[] isVisited = new boolean[vertices.length];

		// Recursively search
		dfs(v, parent, searchOrder, isVisited);

		// Return a search tree
		Tree tree = new Tree(v, parent, searchOrder);

		return tree;

	}

	/** Recursive method for DFS search */
	private void dfs(int v, int[] parent, List<Integer> searchOrder,
			boolean[] isVisited) {
		// Store the visited vertex
		searchOrder.add(v);
		isVisited[v] = true; // Vertex v visited

		for (Edge e : neighbors.get(v)) {
			// If an adjacent is not visited, then recur for that adjacent 
			if (!isVisited[e.dest]) {
				parent[e.dest] = v; // The parent of vertex i is v
				dfs(e.dest, parent, searchOrder, isVisited); // Recursive search
			}
			// If ancestors of source and destination are the same, then there is a cycle. 


		}

	}

	/** Starting bfs search from vertex v */

	public Tree bfs(int v) {

		List<Integer> searchOrder = new ArrayList<Integer>();
		int[] parent = new int[getSize()]; // size of vertices
		for (int i = 0; i < parent.length; i++)
			parent[i] = -1; // Initialise parent[i] to  -1

		java.util.LinkedList<Integer> queue =
				new java.util.LinkedList<Integer>(); // list used as a queue
		boolean[] isVisited = new boolean[getSize()];
		queue.offer(v); // Enqueue v
		isVisited[v] = true; // Mark it visited

		while (!queue.isEmpty()) {
			int u = queue.poll(); // Dequeue to u
			searchOrder.add(u); // u searched
			for (Edge e : getNeighbors(u)) {
				if (!isVisited[e.dest]) {
					queue.offer(e.dest); // Enqueue w
					parent[e.dest] = u; // The parent of w is u
					isVisited[e.dest] = true; // Mark it visited
				}
			}
		}
		return new Tree(v, parent, searchOrder);
	}		
	//A utility function to find the vertex with minimum distance value, 
	// from the set of vertices not yet included in shortest path tree 

	int minDistance(int dist[], Boolean sptSet[]) 
	{ 
		// Initialise minmum value 
		int min = Integer.MAX_VALUE, min_index = -1; 

		for (int v = 0; v < this.vertices.length; v++) 
			if (sptSet[v] == false && dist[v] <= min) { 
				min = dist[v]; 
				min_index = v; 
			} 

		return min_index; 
	} 

	// method used to find the mst using Prims algorithm with adjacency lists,  
	// split vertices sets on MST Set and remaining set, add first vertex to MST 
	// find minimum cost edges when they do not form cycles and connecting one vertex in the MST and one outside (remaining set).
	// Union the found valid edge with MST set
	// Time Complexity O(E Log E).
	public Tree prims_al() { 

		// Whether a vertex is in PriorityQueue or not 
		Boolean[] inMST = new Boolean[this.vertices.length]; 
		MinHeap.HeapNode[] RemainingSet = new MinHeap.HeapNode[this.vertices.length]; 

		// Stores the parents of a vertex 
		int[] parent = new int[this.vertices.length]; 
		// Use TreeSet instead of PriorityQueue as the remove function of the PQ is O(n) in java. 
		TreeSet<MinHeap.HeapNode> RemainingSetQueue = new TreeSet<MinHeap.HeapNode>(new MinHeap.comparator()); 

		for (int i = 0; i < this.vertices.length; i++) { 
			RemainingSet[i] = new MinHeap.HeapNode(); 
			// Initialise to false 
			inMST[i] = false; 

			// Initialise key values to infinity 
			RemainingSet[i].key = Integer.MAX_VALUE; 
			RemainingSet[i].vertex = i; 
			parent[i] = -1; 
			RemainingSetQueue.add(RemainingSet[i]); 
		} 

		// Include the source vertex in mstset 
		inMST[0] = true; 

		// Set key value to 0 
		// so that it is extracted first out of PriorityQueue 
		RemainingSet[0].key = 0; 

		// Loops until the queue is not empty 
		while (!RemainingSetQueue.isEmpty()) { 

			// Extracts a node with min key value 
			MinHeap.HeapNode closestNode = RemainingSetQueue.pollFirst(); 

			// Include that node into mstset 
			inMST[closestNode.vertex] = true; 

			// For all adjacent vertex of the extracted vertex V 
			for (Edge neighbor : this.neighbors.get(closestNode.vertex)) { 

				// If V is in queue 
				if (inMST[neighbor.dest] == false) { 
					// If the key value of the adjacent vertex is more than the extracted key 
					// update the key value of adjacent vertex to update first remove and add the updated vertex 
					if (RemainingSet[neighbor.dest].key > neighbor.cost) { 
						RemainingSetQueue.remove(RemainingSet[neighbor.dest]); 
						RemainingSet[neighbor.dest].key = neighbor.cost; 
						RemainingSetQueue.add(RemainingSet[neighbor.dest]); 
						parent[neighbor.dest] = closestNode.vertex; 
					} 
				} 
			} 
		} 
		List<Integer> searchOrder = new ArrayList<Integer>();
		searchOrder.add(RemainingSet[0].vertex);  
		// Prints the vertex pair of mst 
		for (int o = 1; o < this.vertices.length; o++) {
			//System.out.println(parent[o] + " "
			//		+ "-"
			//		+ " " + o); 
			searchOrder.add(parent[o]);
		}

		return new Tree(RemainingSet[0].vertex, parent, searchOrder);
	}
	// Time Complexity : O(V3), This time using adjacency matrix
	// split vertices sets on MST Set and remaining set, 
	// find minimum cost edges when they do not form cycles and connecting one vertex in the MST and one outside (remaining set). 
	// Union MST with the found edge
	public Tree prims_am() {  
		boolean []inMST = new boolean[this.vertices.length]; 

		// Include first vertex in MST 
		inMST[0] = true; 

		// Keep adding edges while number of included 
		// edges does not become V-1. 
		int edge_count = 0, mincost = 0; 
		List<Integer> searchOrder = new ArrayList<Integer>();

		// Stores the parents of a vertex 
		int[] parent = new int[this.vertices.length];
		while (edge_count < this.vertices.length - 1) { 
			// Find minimum weight valid edge in a and b that is less than the min found so far.  
			int min = Integer.MAX_VALUE, a = -1, b = -1; 
			for (int i = 0; i < this.vertices.length; i++) { 
				for (int j = 0; j < this.vertices.length; j++)  {              
					if ((this.adjacencyMatrix[i][j]  > 0) && (this.adjacencyMatrix[i][j] < min)) {
						// isValidEdge will be true, when the edge is connecting one edge in the inMST and one in the remaining set, 
						// will be false when both edges are inMST or both are in the remaining set
						if (isValidEdge(i, j, inMST))  { 
							min = this.adjacencyMatrix[i][j]; 
							a = i; 
							b = j; 

						} 
					} 
				} 
			} 


			if (a != -1 && b != -1)  { 
				edge_count++;

				searchOrder.add(b);
				mincost = mincost + min; 
				inMST[b] = inMST[a] = true; 
				parent[b] = a; 
				searchOrder.add(a);
				searchOrder.add(b);
			}
			System.out.printf("Edge %d:(%d, %d) cost: %d \n",  
					edge_count, a, b, min);
		} 
		//System.out.printf("\n Minimum cost = %d \n", mincost); 
		return new Tree(searchOrder.get(0), parent, searchOrder);
	} 
	// Find set of vertex i 
	int find(int i, int[] parent) { 
		while (parent[i] != i) 
			i = parent[i]; // keep going up as long as the parent is different from the current vertex
		return i; 
	} 

	// Does union of i and j. It returns 
	// false if i and j are already in same 
	// set. 
	void union1(int i, int j, int[] parent) { 
		// get the ancestors of i and j, that is supposed to be src and dest of a given edge, and make i's ancestor parent of j's ancestor 
		int a = find(i, parent); 
		int b = find(j, parent); 
		parent[b] = a; 
	} 

	// Finds MST using Kruskal's algorithm from adjacency matrix
	//  basic difference with Prims is that the valid edge does not need to connect the vertices in the MST with those outside, 
	//    it can be any minimum edge that does not form a cycle
	// 1. Sort all the edges in non-decreasing order of their weight.
	// 2. Find the minimum weight edge. Check if it forms a cycle with the spanning tree formed so far. If cycle is not formed, discard this edge
	//	  Union the valid Edge with the MST found so far
	// 3. Repeat step#2 until there are (V-1) edges in the spanning tree.

	// the following is not efficient, but it is easy to understand
	// https://www.geeksforgeeks.org/kruskals-algorithm-simple-implementation-for-adjacency-matrix/

	public Tree kruskal_am() { 
		int mincost = 0; // Cost of min MST. 

		// Initialise sets of disjoint sets. 
		// Stores the parents of a vertex 
		int[] parent = new int[this.vertices.length];
		for (int i = 0; i < this.vertices.length; i++) 
			parent[i] = i; 

		// Include minimum weight edges one by one 
		int edge_count = 0; 
		List<Integer> searchOrder = new ArrayList<Integer>();
		while (edge_count < this.vertices.length - 1) { 
			int min = Integer.MAX_VALUE, a = -1, b = -1; 
			for (int i = 0; i < this.vertices.length; i++) { 
				for (int j = 0; j < this.vertices.length; j++) { 
					// when both vertices parents/ancestors are not equal, this means there is no cycle and safe to add to MST
					if (find(i, parent) != find(j, parent) && this.adjacencyMatrix[i][j] < min) { 
						min = this.adjacencyMatrix[i][j]; 
						a = i; 
						b = j;
					} 
				} 
			} 

			union1(a, b, parent); 
			searchOrder.remove((Integer) a); // remove a as an Object if it was added before to add them in the right order now
			searchOrder.remove((Integer) b); // remove b as an Object if it was added before to add them in the right order now
			searchOrder.add(a);
			searchOrder.add(b);
			edge_count++;
			//System.out.printf("Edge %d:(%d, %d) cost:%d \n", 
			//		edge_count, a, b, min); 
			mincost += min; 
		} 
		//System.out.printf("\n Minimum cost= %d \n", mincost); 
		return new Tree(searchOrder.get(0), parent, searchOrder, mincost);
	} 

	/*
	The algorithm is a Greedy Algorithm. The Greedy Choice is to pick the smallest weight edge that does not cause a cycle in the MST constructed so far. 
	Instead of searching all adjacency matrix, it sorts the adjacency list by cost/weight first, 
	then uses MinHeap data structure to add all vertices as the remaining set 
	take a valid edge by the ascending order of weight and add both vertices to the minimum ST found so far
	find / union methods, find the minimum valid edge that does not form a cycle in the MST
	Union this vertices incidence on this edge to the MST in order.
	 */

	public Tree  Kruskal_greedy() { 
		Edge result[] = new Edge[this.vertices.length];  // This will store the resultant MST edges
		int[] parent = new int[this.vertices.length];
		for (int i = 0; i < this.vertices.length; i++) 
			parent[i] = i; 

		List<Integer> searchOrder = new ArrayList<Integer>(); 	// this will store the resultant MST vertices visiting order, which is not even feasible in this algorithm, 
		// because the choice of the next minimum edge does not need to be connected to the already found added in MST

		int e = 0;  // An index variable, used for result[] 
		int i = 0;  // An index variable, used for sorted edges 


		// Step 1:  Sort all the edges in non-decreasing order of their 
		// weight.  If we are not allowed to change the given graph, we 
		// can create a copy of array of edges 
		Collections.sort(this.adjacencyList); 


		// Allocate memory for creating Vertices subsets , using HeapNode as set, in which vertex is the parent, and key is the rank
		MinHeap.HeapNode subsets[] = new MinHeap.HeapNode[this.vertices.length]; 

		// Create Vertices subsets with single elements 
		for(i=0; i<this.vertices.length; ++i) {
			subsets[i]=new MinHeap.HeapNode(); 
			subsets[i].vertex = i; 
			subsets[i].key = 0; 
		} 

		i = 0;  // Index used to pick next edge 

		// Number of edges to be taken is equal to V-1 
		while ((e < this.vertices.length - 1) && (i<this.edges.length)) 
		{ 
			// Step 2: Pick the smallest edge. And increment  
			// the index for next iteration 
			Edge next_edge = new Edge(); 
			next_edge = this.adjacencyList.get(i++); 

			// From the minimum cost edge found, when both vertices parents/ancestors are not equal, this means there is no cycle and safe to add to MST 
			int x = MinHeap.find(subsets, next_edge.src); 
			int y = MinHeap.find(subsets, next_edge.dest); 

			// If including this edge does't cause cycle, 
			// include it in result and increment the index of result for next edge 
			if (x != y) 
			{ 
				result[e++] = next_edge; 
				MinHeap.Union(subsets, x, y); 
				parent[y] = x; 
				searchOrder.remove((Integer) x); // remove i as an Object if it was added before to add them in the right order now
				searchOrder.remove((Integer) y); // remove j as an Object if it was added before to add them in the right order now
				searchOrder.add(x);
				searchOrder.add(y);
			} 
			// Else discard the next_edge 
		} 

		// print the contents of result[] to display 
		// the built MST 
		//System.out.println("Following are the edges in " +  
		//                             "the constructed MST"); 
		//for (i = 0; i < e; ++i) 
		//    System.out.println(result[i].src+" -- " +  
		//          result[i].dest+" == " + result[i].cost); 

		return new Tree(searchOrder.get(0), parent, searchOrder);
	} 



	/********** Various classes/structures used in graph algorithms
	 * 
	 * 
	 * tree
	 * edge
	 * 
	 */


	/** Tree inner class inside the AbstractGraph class */
	/** To be discussed in Section 27.5 */
	public class Tree {
		private int root; // The root of the tree
		private int[] parent; // Store the parent of each vertex
		private List<Integer> searchOrder; // Store the search order
		public int MSTCost = 0; // while traversing in DFS, if a cycle is detected, this flag is set
		/** Construct a tree with root, parent, and searchOrder */
		public Tree(int root, int[] parent, List<Integer> searchOrder) {
			this.root = root;
			this.parent = parent;
			this.searchOrder = searchOrder;
		}

		public Tree(int root, int[] parent, List<Integer> searchOrder, int mincost) {
			this (root, parent, searchOrder);
			this.MSTCost = mincost;
		}

		/** Return the root of the tree */
		public int getRoot() {
			return root;
		}

		/** Return the parent of vertex v */
		public int getParent(int v) {
			return parent[v];
		}

		/** Return an array representing search order */
		public List<Integer> getSearchOrder() {
			return searchOrder;
		}

		/** Return number of vertices found */
		public int getNumberOfVerticesFound() {
			return searchOrder.size();
		}

		/** Return the path of vertices from a vertex to the root */
		public List<V> getPath(int index) {
			ArrayList<V> path = new ArrayList<V>();

			do {
				path.add(getVertex(index));
				index = parent[index];
			}
			while (index != -1);

			return path;
		}

		/** Print a path from the root to vertex v */
		public void printPath(int index) {
			List<V> path = getPath(index);
			System.out.print("A path from " + getVertex(root) + " to " +
					getVertex(index) + ": ");
			for (int i = path.size() - 1; i >= 0; i--)
				System.out.print(path.get(i) + " ");
			System.out.println();
		}

		/** Print the whole tree */
		public void printTree() {
			System.out.println("Root is: " + getVertex(root));
			System.out.print("Edges: ");
			for (int i = 0; i < parent.length; i++) {
				if (parent[i] != -1) {
					// Display an edge
					System.out.print("(" + getVertex(parent[i]) + ", " +
							getVertex(i) + ") ");
				}
			}
			System.out.println();
		}
	}

	/** Edge inner class inside the AbstractGraph class */
	public static class Edge implements Comparable<Edge> {
		public int src; // Starting vertex of the edge
		public int dest; // Ending vertex of the edge
		public int cost; // for weighted graphs
		/** Construct an edge for (u, v) */
		public Edge(int u, int v) {
			this.src = u;
			this.dest = v;
		}
		public Edge(int u, int v, int c) {
			this.src = u;
			this.dest = v;
			this.cost = c;
		}

		public Edge() {
			// TODO Auto-generated constructor stub
		}
		public String toString () {
			return this.src + "->" + this.dest + ":"+ this.cost ;	
		}

		// Comparator function used for sorting edges  
		// based on their weight 
		public int compareTo(Edge compareEdge) 
		{ 
			return this.cost-compareEdge.cost; 
		} 
	}


}

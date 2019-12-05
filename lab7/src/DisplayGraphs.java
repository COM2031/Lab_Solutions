
/* @author Dr. Manal Helal for @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * Led by Prof. Steve Schneider
 * Based on Examples from Daiel Liang book on Java and 
 * Princeton University Algorithms Course
 * Geeks for Geeks website
 * various resources after debugging and fixing errors and generalising forr various tests cases
 *  and using uniform data structures to cover as much as possible from the different graph representations as possible
 *  for any bug or issues, report to manal@manalhelal.com
 * @date created: November 2019
 * @University of Surrey
 * Week 7 Lab: Graph Algorithms & Data Structures
 */

/* For drawing weights, this might be needed
 * 
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
 * 
 * */

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.*;



public class DisplayGraphs<V> extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public Graph<V> graph;

	public void createPanel () {
		GraphPanel graphPanel = new GraphPanel(graph);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					setContentPane(graphPanel);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
	}
	public DisplayGraphs() { 
		createPanel ();

	}

	public DisplayGraphs (int[][] edges, V[] vertices, boolean mat) {
		this.graph =  new Graph<V>(edges, vertices, mat);
		createPanel ();
	}

	public DisplayGraphs (Graph<V> graph) {
		this.graph =  graph;
		createPanel ();

	}
	class GraphPanel extends JPanel {

		public Graph<V> graph;
		public GraphPanel(Graph<V> graph) {
			this.graph = graph;
		}
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);

			// Draw vertices
			City[] vertices 
			= (City[]) graph.getVertices();    
			for (int i = 0; i < graph.getSize(); i++) {
				int x = vertices[i].x;
				int y = vertices[i].y;
				String name = vertices[i].name;

				g.fillOval(x - 8, y - 8, 16, 16);   // Display a vertex
				g.drawString(name, x - 12, y - 12); // Display the name
			}

			// Draw edges for pair of vertices


			for (Graph.Edge e: graph.adjacencyList) {
				City c1 = (City) graph.vertices[e.src];
				int x1 = c1.x;
				int y1 = c1.y;
				City c2 = (City) graph.vertices[e.dest];
				int x2 = c2.x;
				int y2 = c2.y;
				g.setColor(Color.BLACK);  

				g.drawLine(x1, y1, x2, y2); // Draw an edge for (i, v)
				// the following draws the cost if existing, and has unpleasant drawing, and needs testing and improvements
				/*
				if (e.cost > 0) {
					Graphics2D g2 = (Graphics2D) g;
					g.drawString(String.valueOf(e.cost), (x1 + x2), (y1 + y2)); // Display the name 
					String text = String.valueOf(e.cost);
					Font font = g2.getFont();

					FontRenderContext context = g2.getFontRenderContext();
					int textWidth = (int) font.getStringBounds(text, context).getWidth();
					LineMetrics ln = font.getLineMetrics(text, context);
					int textHeight = (int) (ln.getAscent() + ln.getDescent());
					int x = x1 + ((x2-x1) - textWidth)/2;
					int y = (int)(y1 + ((y2-y1) + textHeight)/2 - ln.getDescent());
					g2.drawString(text, (int) x, (int) y);
				}
				 */	
			}
		}

	}

	static class City  {
		public int x, y;
		public String name;

		City(String name, int x, int y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString () {
			return name;

		}


	}




	public static <V> void graphTests (String title, Graph<V> g, int vertexSource, int vertexDest) {

		System.out.println("*** Testing Graph Properties ****");
		System.out.println();
		System.out.println();

		System.out.println("There are " + g.vertices.length + " vertices and " + g.edges.length + " edges in " + title);
		System.out.println("The vertex with index 1 is " + g.getVertex(1));
		System.out.println("The index for " + g.vertices[vertexSource] + " is " + vertexSource);
		boolean bipartitie = g.isBipartite ();
		if (bipartitie)
			System.out.println("Graph " + title + " is bipartitie");
		else
			System.out.println("Graph " + title + " is not bipartitie");

		//System.out.println("The edges for " + title + ":");
		//g.printEdges();

		System.out.println();
		System.out.println();
		System.out.println("*** Testing BFS ****");
		System.out.println();
		System.out.println();

		// Test BFS From vertexSource
		Graph<V>.Tree bfs = g.bfs(vertexSource); 
		System.out.println(bfs.getNumberOfVerticesFound() +
				" vertices are searched in this  " + title + " BFS order:");
		bfs.printTree();

		/*
      // Alternative Testing of BFS Properties
     	java.util.List<Integer> searchOrders = bfs.getSearchOrder();
		System.out.println(bfs.getNumberOfVerticesFound() +
       " vertices are searched in this  " + title + " BFS order:");
     for (int i = 0; i < searchOrders.size(); i++)
       System.out.println(g.getVertex(searchOrders.get(i)));

     for (int i = 0; i < searchOrders.size(); i++)
       if (bfs.getParent(i) != -1)
         System.out.println("parent of " + g.getVertex(i) + 
           " is " + g.getVertex(bfs.getParent(i)));
		 */	
		System.out.println();
		System.out.println();
		System.out.println("*** Testing DFS ****");
		System.out.println();
		System.out.println();

		// Test DFS From vertexSource

		Graph<V>.Tree dfs = g.dfs(vertexSource); 
		System.out.println(bfs.getNumberOfVerticesFound() +
				" vertices are searched in this " + title + " DFS order:");
		dfs.printTree();

		/*
      // Alternative Testing of DFS Properties
      searchOrders = dfs.getSearchOrder();
     System.out.println(dfs.getNumberOfVerticesFound() + " vertices are searched in this " + title + " DFS order:");
     for (int i = 0; i < searchOrders.size(); i++)
      System.out.print(g.getVertex(searchOrders.get(i)) + " ");
     System.out.println();

     for (int i = 0; i < searchOrders.size(); i++)
       if (dfs.getParent(i) != -1)
        System.out.println("parent of " + g.getVertex(i) +
          " is " + g.getVertex(dfs.getParent(i)));
		 */
		System.out.println();
		System.out.println();
		System.out.println("*** Testing Connectivity ****");
		System.out.println();
		System.out.println();

		List<List<Integer>> conn = g.getConnectedComponents ();

		System.out.println(conn.size() + " connected components in " + title + " in DFS order:");
		for (int i = 0; i< conn.size(); i++) {// search connected components in DFS order
			for (int j = 0; j < conn.get(i).size(); j++)
				System.out.print(conn.get(i).get(j) + " ");
			System.out.println();

		}
		System.out.println();
		System.out.println();
		System.out.println("Alternatively by names in " + title + " example:");
		for (int i = 0; i< conn.size(); i++) {// search connected components in DFS order
			System.out.print( g.vertices[conn.get(i).get(0)]);
			for (int j = 1; j < conn.get(i).size(); j++)
				System.out.print(" -> " + g.vertices[conn.get(i).get(j)]);
			System.out.println();
		}
		System.out.println();
		System.out.println();

		if (g.isCyclic()) {
			System.out.println(title + " is Cyclic");
			List<Integer> cycle = null; 
			int i = 0;
			// return all cycles from all vertices
			do {
				cycle = g.getACycle (i);

				if (cycle != null) {
					System.out.print("The detected Cycle From  " + g.vertices[i] + ": ");
					for (int j = cycle.size() - 1; j >= 0; j--)
						System.out.print(" -> " + g.vertices[cycle.get(j)] );
					System.out.println();
				}
				i ++;
			}while (i < g.vertices.length);
		}
		else
			System.out.println(title + " is not Cyclic");

		System.out.println();
		System.out.println();
		System.out.println("*** Testing Path Finding ****");
		System.out.println();
		System.out.println();
		List<V> path = g.getPath(vertexSource, vertexDest);

		System.out.print("The BFS Order path from " + g.vertices[vertexSource] + " to " + g.vertices[vertexDest] + " in " + title + " is ");
		if (path.size() > 0) {
			System.out.print(path.get(0));
			for (int i = 1; i < path.size(); i++)
				System.out.print(" -> " + path.get(i));
			System.out.println();
		}




		path = g.getPath_dfs(vertexSource, vertexDest);

		System.out.print("The DFS Order path from " + g.vertices[vertexSource] + " to " + g.vertices[vertexDest] + " in " + title + " is ");
		if (path.size() > 0) {
			System.out.print(path.get(0));
			for (int i = 1; i < path.size(); i++)
				System.out.print(" -> " + path.get(i));
			System.out.println();
		}

		System.out.println();
		System.out.println();
		System.out.println("*** Testing Minimum Spanning Tree Algotrithms ****");
		System.out.println();
		System.out.println();

		System.out.println();
		System.out.println();
		Long startTime = System.currentTimeMillis();
		Graph<V>.Tree  tree = g.prims_al();
		Long endTime = System.currentTimeMillis();
		Long elapsedTime = endTime - startTime;
		// print the constructed distance array 
		System.out.println ("Prims MST from adjacency list O(E Log E) for " + title + " executed in "+ elapsedTime + " ms");
		tree.printTree();

		if (!bipartitie) { // the Prisms using adjacency matrix algorithm does not work with bipartite graphs 
			System.out.println();
			System.out.println();
			startTime = System.currentTimeMillis();
			tree = g.prims_am();
			endTime = System.currentTimeMillis();
			elapsedTime = endTime - startTime;
			// print the constructed distance array 
			System.out.println ("Prims MST from adjacency Matrix O(V^3) for " + title + " executed in "+ elapsedTime + " ms");
			tree.printTree();

		}

		if (g.edges.length < 400) { // Do not run this inefficient algorithm for more than 400 edges, or parallelise if you wish
			System.out.println();
			System.out.println();
			startTime = System.currentTimeMillis();
			tree = g.kruskal_am();
			endTime = System.currentTimeMillis();
			elapsedTime = endTime - startTime;
			// print the constructed distance array 
			System.out.println ("Kruskal MST from adjacency Matrix O(V^2) for " + title + " executed in "+ elapsedTime + " ms");
			tree.printTree();
		}

		System.out.println();
		System.out.println();
		startTime = System.currentTimeMillis();
		tree = g.Kruskal_greedy();
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		// print the constructed distance array 
		System.out.println ("Kruskal MST using an efficient greedy algorithm (  O(ElogE) or O(ElogV) ) for " + title + " executed in "+ elapsedTime + " ms");
		tree.printTree();

		System.out.println();
		System.out.println();
		startTime = System.currentTimeMillis();
		int[] dist = g.dijkstra_al(0);
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		// print the constructed distance array 
		System.out.println ("Dijkstra using greedy from adjacency Lists O(V^2) or reduced to O(ELogV) for " + title + " executed in "+ elapsedTime + " ms");
		g.printSolution("Dijkstra from adjacency Lists for " + title, dist); 

		System.out.println();
		System.out.println();
		startTime = System.currentTimeMillis();
		dist = g.dijkstra_am(0);
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		// print the constructed distance array 
		System.out.println ("Dijkstra from adjacency Matrix O(V^2) for " + title + " executed in "+ elapsedTime + " ms");
		g.printSolution("Dijkstra from adjacency Matrix for " + title, dist); 



		System.out.println();
		System.out.println("**************************************************************");
		System.out.println();
	}

	//Function to remove duplicates from an ArrayList 
	public static <T> List<T> removeDuplicates(List<T> list) 
	{ 

		// Create a new ArrayList 
		ArrayList<T> newList = new ArrayList<T>(); 

		// Traverse through the first list 
		for (T element : list) { 

			// If this element is not present in newList 
			// then add it 
			if (!newList.contains(element)) { 

				newList.add(element); 
			} 
		} 

		// return the new list 
		return newList; 
	} 

	public static void USCitiesExample () {
		// First example for US cities and roads using class City as the template for the graph vertex
		// edges are incidence list using 2d arrays 
		City[] USCities = {new City("Seattle", 75, 50),
				new City("San Francisco", 50, 210),
				new City("Los Angeles", 75, 275), new City("Denver", 275, 175),
				new City("Kansas City", 400, 245),
				new City("Chicago", 450, 100), new City("Boston", 700, 80),
				new City("New York", 675, 120), new City("Atlanta", 575, 295),
				new City("Miami", 600, 400), new City("Dallas", 408, 325),
				new City("Houston", 450, 360) };

		// Edge array for graph in Figure in the lab sheet
		int[][] USRoads = {
				{0, 1}, {0, 3}, {0, 5}, {1, 0}, {1, 2}, {1, 3},
				{2, 1}, {2, 3}, {2, 4}, {2, 10},
				{3, 0}, {3, 1}, {3, 2}, {3, 4}, {3, 5},
				{4, 2}, {4, 3}, {4, 5}, {4, 7}, {4, 8}, {4, 10},
				{5, 0}, {5, 3}, {5, 4}, {5, 6}, {5, 7},
				{6, 5}, {6, 7}, {7, 4}, {7, 5}, {7, 6}, {7, 8},
				{8, 4}, {8, 7}, {8, 9}, {8, 10}, {8, 11},
				{9, 8}, {9, 11}, {10, 2}, {10, 4}, {10, 8}, {10, 11},
				{11, 8}, {11, 9}, {11, 10}
		};
		DisplayGraphs<City> USFrame = new DisplayGraphs<City>(USRoads, USCities, false);

		USFrame.setLocationRelativeTo(null);
		USFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		USFrame.setSize(800, 500);
		USFrame.setVisible(true);

		graphTests("US Cities Graph", USFrame.graph, USFrame.graph.getIndex("Chicago"), USFrame.graph.getIndex("Miami"));


	}

	public static void UKCitiesExample () {
		// Test on UK Map - weighted graph
		// second example for UK cities and roads using class City as the template for the graph vertex
		// This is what you studies in COM1029, reading from a file lines of edges containing source destination weight, 
		// and creating the graph using List<City> and List<Edge> 
		// the x y coordinates for drawing us using the Point file of the first lab in COM2031


		List<City> UKCities = new ArrayList<City>();

		List<Graph.Edge> UKRoads = new ArrayList<Graph.Edge>();
		Graph<City> UKgraph =  new Graph<City>(UKRoads, UKCities, City.class);

		int x = 50, y = 50;
		Point gridSize = new Point(1200, 800);
		try{
			FileReader fin = new FileReader("UKgraph.txt");
			Scanner graphFile = new Scanner(fin);

			// Read the edges and insert
			String line;
			while(graphFile.hasNextLine()){
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);

				try{
					if (st.countTokens() != 3){
						System.err.println("Skipping bad line" + line);
						continue;
					}
					String source = st.nextToken();
					String dest = st.nextToken();
					int cost = Integer.parseInt(st.nextToken());
					int sourceIndex, destIndex;

					if (UKgraph.getIndex(source) == -1) {
						UKgraph.addVertex(new City(source, x, y), City.class);
						Point p = Point.nextPoint(new Point(x, y), 50, gridSize);
						p = Point.nextPoint(p, 50, gridSize);
						p = Point.nextPoint(p, 50, gridSize);
						x = p.x;
						y = p.y;
					}
					sourceIndex = UKgraph.getIndex(source);
					if (UKgraph.getIndex(dest) == -1) {
						UKgraph.addVertex(new City(dest, x, y), City.class);
						Point p = Point.nextPoint(new Point(x, y), 50, gridSize);
						p = Point.nextPoint(p, 50, gridSize);
						p = Point.nextPoint(p, 50, gridSize);
						x = p.x;
						y = p.y;
					}
					destIndex = UKgraph.getIndex(dest);
					UKgraph.addEdge (sourceIndex, destIndex, cost);

				}
				catch (NumberFormatException e){
					System.err.println("Skipping bad line " + line);
				} 
			}
			graphFile.close();



			DisplayGraphs <City> UKframe = new DisplayGraphs<City>(UKgraph);
			//City[] UKCities_array =  DisplayUSMap.graph.getVertices();
			UKframe.setLocationRelativeTo(null);
			UKframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			UKframe.setSize(gridSize.x, gridSize.y);
			UKframe.setVisible(true);

			graphTests("UK Cities Graph", UKframe.graph, UKframe.graph.getIndex("London"), UKframe.graph.getIndex("Cardiff"));



		}
		catch(IOException e){
			System.err.println(e);    
		}

	}

	public static void parent_chileExample () {
		// Third example for parent/child relationships using String as vertex class type
		// edges are incidence list using 2d arrays 
		// No Display was created for this graph for testing


		String[] names = {"Peter", "Jane", "Mark", "Cindy", "Wendy"};
		int[][] parent_child = {{0, 2}, {1, 2}, {2, 4}, {3, 4}};   

		Graph<String> graph2 = new Graph<String> (parent_child, names, false);
		graphTests("Parent/Children Graph", graph2, graph2.getIndex("Peter"), graph2.getIndex("Wendy"));

	}

	public static void movies_performersExample () {
		// Test on movies - undirected unweighted graph
		// fourth example for movies and performers using class String as the template for the graph vertex (both movies and performers are just a name)
		//  and creating the graph using List<City> and List<Edge> 
		// too big to be drawn, so there will be no display frame

		// The movies are many, I reduced to 100 movies, but with so many performers, so it takes time to load and process.
		// You can take this with you to your parallel and distributed modules to see what you can do for distributed graphs, and those
		// that do not fit in memory
		// we are reading the 100 movies, but I included other csv files for 1260, and 4188 movies, 
		// They are simplified from the "|" delimited files produced by Princepton university algorithms course:
		// https://introcs.cs.princeton.edu/java/data/

		List<String> movies_performers = new ArrayList<String>();

		List<Graph.Edge> edges = new ArrayList<Graph.Edge>();
		try{
			// file contains a movie name, then list if performers separated by commas
			FileReader fin = new FileReader("movies_100.csv");
			Scanner graphFile = new Scanner(fin);

			// Read the line, and create the vertices and edges
			String line;
			while(graphFile.hasNextLine()){
				line = graphFile.nextLine();
				String[] tokens= line.split(",");
				if (tokens.length <= 0){
					System.err.println("Skipping bad line" + line);
					continue;
				}

				// adding movies
				movies_performers.add(tokens[0]);

				// adding performers
				for (int i = 1; i< tokens.length; i++)       
					movies_performers.add(tokens[i]);  

			}
			graphFile.close();
			// Remove duplicates 
			movies_performers = removeDuplicates(movies_performers); 
		}
		catch(IOException e){
			System.err.println(e);    
		}
		Graph<String>graph =  new Graph<String>(edges, movies_performers, String.class);

		try{
			// file contains a movie name, then list if performers separated by commas
			FileReader fin = new FileReader("movies_100.csv");
			Scanner graphFile = new Scanner(fin);

			// Read the line, and create the vertices and edges
			String line;
			while(graphFile.hasNextLine()){
				line = graphFile.nextLine();
				//StringTokenizer st = new StringTokenizer(line);
				String[] tokens= line.split(",");
				if (tokens.length <= 0){
					System.err.println("Skipping bad line" + line);
					continue;
				}
				int sourceIndex, destIndex;
				String movie = tokens[0];

				sourceIndex = graph.getIndex(movie);



				for (int i = 1; i< tokens.length; i++) {

					String performer = tokens[i];


					destIndex = graph.getIndex(performer);
					graph.addEdge(sourceIndex, destIndex);

				}
			}
			graphFile.close();


			int sourceVertex = graph.edges.length / 2;
			int destVertex = graph.edges[sourceVertex].length / 2;

			graphTests("Movies Graph", graph, sourceVertex, destVertex);



		}
		catch(IOException e){
			System.err.println(e);    
		}

		City[] nodes = { new City("node1", 50, 210),
				new City("node2", 75, 275), 
				new City("node3", 400, 245),
				new City("node4", 450, 100), 
				new City("node5", 675, 120)};


		int adjacencyMatrix[][] = { {0, 4, 3, 0, 0}, 
				{0, 0, 7, 5, 0}, 
				{0, 0, 0, 8, 3}, 
				{0, 0, 0, 0, 2}, 
				{0, 0, 0, 0, 0}
		};

		Graph<City> graph3 = new Graph<City> (adjacencyMatrix, nodes, true);
		//graphTests("Max Flow Graph", graph2, graph2.getIndex("node1"), graph2.getIndex("node5"));
		DisplayGraphs<City> graphFrame = new DisplayGraphs<City>(graph3);
		graphFrame.setLocationRelativeTo(null);
		graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graphFrame.setSize(800, 500);
		graphFrame.setVisible(true);

		graphTests("UK Cities Graph", graph3, graph3.getIndex("node1"), graph3.getIndex("node4"));

	}

	public static void lab7Example () {
		City[] nodes = { new City("node1", 50, 210),
				new City("node2", 75, 275), 
				new City("node3", 400, 245),
				new City("node4", 450, 100), 
				new City("node5", 675, 120)};


		int adjacencyMatrix[][] = { {0, 9, 2, 0, 0}, 
				{0, 0, 5, 3, 0}, 
				{0, 0, 0, 2, 4}, 
				{0, 0, 0, 0, 6}, 
				{0, 0, 0, 0, 0}
		};

		Graph<City> graph3 = new Graph<City> (adjacencyMatrix, nodes, true);
		//graphTests("Max Flow Graph", graph2, graph2.getIndex("node1"), graph2.getIndex("node5"));
		DisplayGraphs<City> graphFrame = new DisplayGraphs<City>(graph3);
		graphFrame.setLocationRelativeTo(null);
		graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graphFrame.setSize(800, 500);
		graphFrame.setVisible(true);

		graphTests("Lab 7 Example", graph3, graph3.getIndex("node1"), graph3.getIndex("node4"));

	}

	public static void main(String[] args) {
		/*
		try {
			PrintStream fileOut = new PrintStream("out"+ System.currentTimeMillis()+".txt");
			System.setOut(fileOut);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 */

		//Five graph examples, defined differently and tested the same way.

		USCitiesExample();


		UKCitiesExample ();

		parent_chileExample();

		movies_performersExample();

		lab7Example();

	}

}
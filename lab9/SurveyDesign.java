
/* @author Dr. Manal Helal for @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * Led by Prof. Steve Schneider
 * @date created: November 2019
 * @University of Surrey
 * Week 9 Lab: Survey Design Problem reduced to Ford Fulkerson algorithm of last week  
 */


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.sun.corba.se.impl.orbutil.graph.Graph;



public class SurveyDesign {

	static int[][] maxFlow;		// the calculated maxflow
	static int[][] rGraph;			// the calculated residual graph

	public static void printAdjacencyMatrix (int[][] adjMat) {

		for (int i=0; i<adjMat.length; i++) {
			for (int j=0; j<adjMat[i].length; j++) {
					System.out.print(adjMat[i][j] + "\t");
			}
			System.out.println();
		}

	}

	static class Product {
		public String name;
		public int min;
		public int max;
		Product (String name, int min, int max) {
			this.name = name;
			this.min = min;
			this.max = max;
		}
	}
	static class Customer {
		public String name;
		int [] hasProducts;
		List<Integer> surveyProducts; // will be initialised by the GenerateSurvey method, to select the products from those the customer has to ask about
		public int min;
		public int max;
		Customer (String name, int min, int max, int [] hasProducts) {
			this.name = name;
			this.min = min;
			this.max = max;
			this.hasProducts = hasProducts;
		}
	}

	

	

	static List<Customer> GenerateSurvey(Customer [] customers, Product [] products, int[][] flow, int[][] graph, int startOfSecondset, int cut_cap) { 

		List<Customer> SurveyCustomers = new ArrayList<Customer>();
		// Using the max Flow passed from the Ford Fulkerson algorithm, find vertices reachable from s using DFS
		boolean[] isVisited = new boolean[graph.length];  
		fordFulkerson.dfs(flow, 0, isVisited); 

		// find all edges from a reachable vertex to a reachable vertex from the max flow found by Ford Fulkerson Algorithm
		// add the source vertex to set A, 
		// add the destination vertex to set B

		System.out.println ("The matching is as follows " + cut_cap + ". The following are edges from reachable vertex to reachable vertex:");
		// skipping first and last vertices for the added source and sink vertices to reduce the problem to max flow
		for (int i = 1; i < graph.length-1; i++) { 
			for (int j = 1; j < graph.length-1; j++) { 
				
					if (flow[i][j] > 0 && isVisited[i] && isVisited[j]) { 
						System.out.println("{" + String.valueOf(i-1) + " -> " + String.valueOf(j-startOfSecondset) + " }");
						if (customers[i-1].surveyProducts == null)
							customers[i-1].surveyProducts = new ArrayList<Integer>();
						if(!customers[i-1].surveyProducts.contains(j-startOfSecondset))
							customers[i-1].surveyProducts.add(j-startOfSecondset);
						if(!SurveyCustomers.contains(customers[i-1]))
							SurveyCustomers.add(customers[i-1]);

					} 
			} 
		}

		// Set A contains
		if (SurveyCustomers.size() > 0) {
			for (int i = 0; i<SurveyCustomers.size(); i++) {
				System.out.print("Customer:  " + SurveyCustomers.get(i).name + " will be asked about products: ");
				for (int j = 0; j<SurveyCustomers.get(i).surveyProducts.size(); j++) {
					System.out.print(products[SurveyCustomers.get(i).surveyProducts.get(j)].name + ", ");
				}
				System.out.println("");
			}
		}
		return SurveyCustomers;
	} 



	public static boolean hasProduct (Customer customer, int productIndex) {
		for (int i = 0; i<customer.hasProducts.length; i++)
			if (customer.hasProducts[i] == productIndex)
				return true;
		return false;
	}

	

	static class SupplyDemandNode {
		public int pv;
		SupplyDemandNode (int pv) {
			this.pv = pv;
		}
	}

	// input is demand/supply graph, and return true if there is circulation or false if there is not.
	// the max flow is calculated after adding source and sink and connect 
	// source to all supply vertices with -pv as capacity, and connect all demand vertices to sink with pv as capacity

	public static boolean circulationToMaxFlow (int[][] g, SupplyDemandNode nodes[], int startOfSecondset) {

		
		// if adjacency list is empty return no circulation
		if ((g == null) || (g.length == 0))
			return false;


		int maxSupplies = 0;  // how many max questions can be asked by consumers
		int minDemands = 0;	// how many minimum questions are needed for every product
		
		int s = 0;
		int t = g.length - 1;
		
		// the source as vertex zero, and  sink as last vertex are already added
		int[][] adjMatrix = new int[g.length][g.length];

		// this way we created one adjacency matrix containing all vertices  adding source and sink


		// we copy the values from the  Graph adjacency matrix to the new one copying capacities 
		// taking care of indices to account for the newly added vertices
		for (int i = 0; i<g.length;i++) {
			if (nodes[i].pv < 0) 
				maxSupplies += nodes[i].pv;
			else
				minDemands += nodes[i].pv;
			for (int j = 0; j<g[i].length;j++) {
				adjMatrix[i][j] = g[i][j];
			}
		}

		// define new edges between source '0' and all supply vertices and inverting the sign of the supply to make it positive
		for (int i = 1; i<startOfSecondset;i++) {
			if (nodes[i].pv < 0) 
				adjMatrix[s][i]	= -1 * nodes[i].pv;
		}
		// define new edges between all vertices in second set and the sink 'bpGraph[0].length-1'
		for (int i = startOfSecondset; i<g.length-1;i++) {
			if (nodes[i].pv > 0) 
				adjMatrix[i][t] = nodes[i].pv;
		}

		
		// find max flow
		fordFulkerson maxFlowFordFulkerson = new fordFulkerson(); 
		// the min cut is printed inside the ford Fulkerson method called in the following line
		maxFlow = maxFlowFordFulkerson.fordFulkerson(adjMatrix, 0, t);
		rGraph = maxFlowFordFulkerson.rGraph;
		int flowValue = 0;
		for (int i=0;i<maxFlow.length; i++)
				flowValue += maxFlow[i][t];

		System.out.println("The maximum possible flow of the given graph is calculated as " + flowValue); 
		// there is circulation if the number of questions asked are more than required by products and less than or equal 
		// the maximum accepted by customers
		
		// however, this is not the valid test for this question as the bounds for each consumer and producer needs to be checked as in the calling function
		return ((flowValue >= Math.abs(minDemands)) && (flowValue <= Math.abs(maxSupplies)));

	}

	

	// The method takes Customers array and products array and produces and
	// 1. transform them to BiPartitie Edmond Matrix,
	// 2. create the adjacency matrix for the max flow circulation with lower bounds  
	// by adding source and sink and connect 
	// source to all vertices in first set, and connect all vertices in second set to sink
	// 3. Run the Ford Fulkerson algorithm with lower bounds 
	// 4. and check if there is circulation with lower bounds
	// 5. Generate the Surevey by matching the customer to the products survey questions that they will have


	public static List<Customer> SDtoMaxFlow (Customer [] customers, Product [] products) {

		if ((customers == null) || (customers.length == 0))
			return null;
		if ((products == null) || (products.length == 0))
			return null;

		// 1. build a bipartite graph in Edmond Matrix Notation
		// the first dimension is the number of customers in U, and the second dimension is number of products, and set to true the [i][j] index in which customer i owns product j
		// primitive boolean type has default value of false		

		boolean bpGraph[][] = new boolean[customers.length][products.length]; 
		// set to true only for customers who own products
		for (int i = 0; i < customers.length; i++) {
			for (int j = 0; j < products.length; j++) {
				if (hasProduct(customers[i], j))
					bpGraph[i][j] = true;
			}

		}
		int s = 0;
		int t = bpGraph.length + bpGraph[0].length + 1;
		

		// the bipartite graph is defined as two sets of vertices U, and V
		// the first dimension is the number of edges in U, and the second dimension is the set in V
		if ((bpGraph == null) || (bpGraph.length == 0))
			return null;
		if (bpGraph[0].length == 0)
			return null;


		int startOfSecondset = bpGraph.length + 1; // helps in the matching function
		// 2. add source as vertex zero, and add sink as last vertex
		int[][] adjMatrix = new int[bpGraph.length + bpGraph[0].length + 2][bpGraph.length + bpGraph[0].length + 2];
		SupplyDemandNode[] nodes = new SupplyDemandNode[bpGraph.length + bpGraph[0].length + 2];
		for (int i = 0; i< nodes.length;i ++)
			nodes[i] = new SupplyDemandNode(0);

		// this way we created one adjacency matrix containing all vertices in the 2 sets
		// adding source and sink


		// we copy the values from the biPartite Graph to the adjacency matrix
		// take care of indices to account for the newly added vertices

		// For each customer i who purchased product j create a directed edge (i, j) 
		// with an upper flow bounds of 1. This models the requirement that customer i 
		// will be surveyed at most once about product j, 
		// and customers will be asked only about products they purchased.
		for (int i = 0; i<bpGraph.length;i++) {
			for (int j = 0; j<bpGraph[i].length;j++) {
				if (bpGraph[i][j])
					adjMatrix[i+1][j+bpGraph.length+1] = 1;		
			}
		}

		// define new edges between source '0' and all vertices in first set
		// Create a source vertex s and connect it to all the customers, 
		// where the edge from s to customer i has lower and upper flow bounds 
		// of ci and ci', modelled as ci' - ci. This models the requirement that customer i will be asked 
		// about at least ci and at most ci' products

		for (int i = 1; i<=bpGraph.length;i++) {
			adjMatrix[s][i]	= customers[i-1].max - customers[i-1].min;
			nodes[i].pv -= customers[i-1].max;	// make this maximum to allow asking for as many questions as acceptable by the consumer to satisfy the min of the products
			//nodes[s].pv += customers[i-1].min;
		}
		// define new edges between all vertices in second set and the sink 'bpGraph[0].length-1'
		// Create a sink vertex t, and create an edge from product j to t with lower and upper flow bounds 
		// of pj and pj' modelled as pj' -pj'. This models the requirement that there are at least pj and at most pj'
		// customers will be asked about product j.

		for (int j = 0; j<bpGraph[0].length;j++) {
			adjMatrix[j+bpGraph.length+1][t] = products[j].max - products[j].min;
			nodes[j+bpGraph.length+1].pv += products[j].min; // make this minimum to enforce lower bounds, and pushing to other products flow 
			//nodes[t].pv -= products[j].min;
		}


		// Create an edge (s, t) to create a circulation. 
		//Its lower bound is set to zero and its upper bound can be set to any very large value.
		adjMatrix[t][s]	= Integer.MAX_VALUE;

		//System.out.println("Initial AdjMatrix");
		//printAdjacencyMatrix(adjMatrix);
		

		// 3. Retrieve the max flow 


		boolean checkCirculation = circulationToMaxFlow(adjMatrix, nodes, startOfSecondset);
		if (checkCirculation)
			System.out.println("The graph did produce a circulation on total flow");
		else
			System.out.println("The graph did not produce a circulation on total flow");


		//System.out.println("maxFlow");
		//printAdjacencyMatrix(maxFlow);
		
		// 4. find out if there is a circulation to check if a survey is feasible or not
		// circulation in this example would be that the flow maximised is within the bounds given on the edges

		boolean productsSatisfied = true;
		int flowValue = 0;
		for (int j = 0; j<bpGraph[0].length;j++) {
			if ((maxFlow[j+bpGraph.length+1][t] > products[j].max) || (maxFlow[j+bpGraph.length+1][t] < products[j].min))
				productsSatisfied = false;
			flowValue += maxFlow[j+bpGraph.length+1][t] ;
		}
		boolean customersSatisfied = true;
		for (int i=1;i<=bpGraph.length; i++) {
			if ((maxFlow[s][i] > customers[i-1].max) || (maxFlow[s][i] < customers[i-1].min))
				customersSatisfied = false;
		}

		//there is circulation of both bounds satisfied for all customers and products


		if (!productsSatisfied || !customersSatisfied) {
			System.out.println ("The given customers and products data when reduced to max flow, did not produce a flow that satifies the lower and upper bounds by all individual customers and/or products, and hence a Survey with the given bounds is not feasible.");
			return null;
		}

		System.out.println("The maximum question the survey can include as the max flow of the given graph is calculated as " + flowValue); 
		// 5. Generate the matching, similar to the 
		return GenerateSurvey(customers, products, maxFlow, adjMatrix, startOfSecondset, flowValue);

	}


	//Driver Program 
	public static void main(String args[]) { 

		// First Example:
		/*
		 * when given dataset as follows:
		 * 	Consumers	Questions Range		Products
			Mark		2:4					Samsung TV, Samsung Phone, Samsung Tablet
			Steve		2:5					iPhone, iPad, Apple TV
			John		2:6					Sony TV, Nokia Phone
			Mary		2:7					Blackberry Phone, LG TV
			Elizabeth	2:5					Samsung Phone, Sony TV
			Michael		1:4					Blackberry Phone, Samsung TV, iPad
			Helen		1:10				iPhone, Sony TV
			Margret		1:5					LG TV, Samsung Phone


			and products ranges as follows:
			
			Products	Questions Range
			Samsung TV	2:5
			Samsung Phone	2:6
			Samsung Tablet	1:5
			iPhone	2:5
			iPad	2:5
			Apple TV	1:4
			Sony TV	2:5
			Nokia Phone	1:5
			Blackberry Phone	2:4
			LG TV	2:4

			For example initial data structures as follows, and then further graph transformations are in the methods called.
		 */

		Product [] products = {new Product("Samsung TV",2,5), // index 0
				new Product("Samsung Phone",	2,6),// index 1
				new Product("Samsung Tablet",1,5),// index 2
				new Product("iPhone",2,5),// index 3
				new Product("iPad",2,5),// index 4
				new Product("Apple TV",	1,4),// index 5
				new Product("Sony TV",2,5),// index 6
				new Product("Nokia Phone",1,5),// index 7
				new Product("Blackberry Phone",2,4),// index 8
				new Product("LG TV",2,4)};// index 9
		Customer [] customers = {new Customer("Mark", 2,4, new int[] {0, 1, 2}),
				new Customer("Steve",2,5, new int[] {3, 4, 5}),
				new Customer("John", 2,6, new int[] {6, 7}),
				new Customer("Mary", 2, 7, new int[] {8, 9}),
				new Customer("Elizabeth", 2, 5, new int[] {1, 6}),
				new Customer("Michael", 1, 4, new int[] {8, 0, 4}),
				new Customer("Helen", 1, 10, new int[] {3, 6}),
				new Customer("Margret", 1, 5, new int[] {9, 1})};


		SurveyDesign sd = new SurveyDesign(); 

		sd.SDtoMaxFlow(customers, products);

		// second example based on the one in the lecture slides, but last customer is accepting 1 as lower bound to satisfy the bounds tests

		Product [] products_1 = {new Product("p1", 2, 3), // index 0
				new Product("p2", 2, 3),// index 1
				new Product("p3", 2, 3),// index 2
				new Product("p4", 2, 3),// index 3
				new Product("p5", 2, 3)};// index 4
		Customer [] customers_1 = {new Customer("c1", 2, 3, new int[] {0, 1, 3}),
				new Customer("c2", 2, 3, new int[] {0, 1, 2, 4}),
				new Customer("c3", 2, 3, new int[] {0, 2, 3, 4}),
				new Customer("c4", 1, 3, new int[] {2, 3, 4})};


		sd.SDtoMaxFlow(customers_1, products_1);



		// Third example from that will not produce a circulation

		Product [] products_2 = {new Product("p1", 2, 2), // index 0
				new Product("p2", 2, 2),// index 1
				new Product("p3", 2, 2),// index 2
				new Product("p4", 2, 2),// index 3
				new Product("p5", 2, 2)};// index 4
		Customer [] customers_2 = {new Customer("c1", 2, 3, new int[] {0, 1, 3}),
				new Customer("c2", 2, 3, new int[] {0, 1, 2, 4}),
				new Customer("c3", 2, 3, new int[] {0, 2, 3, 4}),
				new Customer("c4", 2, 3, new int[] {2, 3, 4})};


		sd.SDtoMaxFlow(customers_2, products_2);
		
		// Now the lab sheet first example:2a
		
		
		Product [] products_3 = {new Product("p1", 2, 3), // index 0
				new Product("p2", 2, 3),// index 1
				new Product("p3", 2, 3)};// index 2
				
		Customer [] customers_3 = {new Customer("c1", 1, 2, new int[] {0, 1, 2}),
				new Customer("c2", 1, 2, new int[] {0, 1}),
				new Customer("c3", 1, 2, new int[] {0, 1, 2}),
				new Customer("c4", 1, 2, new int[] {1, 2})};


		sd.SDtoMaxFlow(customers_3, products_3);
		
		// Now the lab sheet first example:2b
		
		
			Product [] products_4 = {new Product("p1", 3, 4), // index 0
					new Product("p2", 3, 4),// index 1
					new Product("p3", 3, 4), // index 2
					new Product("p4", 3, 4)}; // index 3
					
			Customer [] customers_4 = {new Customer("c1", 1, 3, new int[] {0, 1}),
					new Customer("c2", 1, 3, new int[] {0, 2, 3}),
					new Customer("c3", 1, 3, new int[] {1, 2, 3}),
					new Customer("c4", 1, 3, new int[] {1, 2}), 
					new Customer("c5", 1, 3, new int[] {0, 1, 2, 3}),
					new Customer("c6", 1, 3, new int[] {1, 2, 3})};
			sd.SDtoMaxFlow(customers_4, products_4);
	} 

}


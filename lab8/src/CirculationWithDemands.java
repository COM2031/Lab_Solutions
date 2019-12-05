
/* @author Dr. Manal Helal for @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * Led by Prof. Steve Schneider
 * @date created: November 2019
 * @University of Surrey
 * Week 8 Lab: Reduce Circulation with Demands Algorithm using Ford Fulkerson algorithm of last week  using the adjacency matrices approach
 * 
 * It is educationally easier to follow the adjacency matrix graph representation
 * However, and adjacency list approach is adopted in this link:
 * https://github.com/SleekPanther/circulation-with-demands-network-flow
 * 
 * Using similar data structures as used in Princeton university for faster algorithms for bigger graphs:
 * https://algs4.cs.princeton.edu/64maxflow/
 * 
 */



import static org.junit.Assert.assertEquals;

public class CirculationWithDemands {

	static class SupplyDemandNode {
		public int pv;
		SupplyDemandNode (int pv) {
			this.pv = pv;
		}
	}

	// input is demand/supply graph, and return true if there is circulation or false if there is not.
	// the max flow is calculated after adding source and sink and connect 
	// source to all supply vertices with -pv as capacity, and connect all demand vertices to sink with pv as capacity


	public boolean circulationToMaxFlow (int[][] g, SupplyDemandNode nodes[]) {

		
		// if adjacency list is empty return no circulation
		if ((g == null) || (g.length == 0))
			return false;


		int maxSupplies = 0;
		
		int s = 0;
		int t = g.length + 1;
		
		// add source as vertex zero, and add sink as last vertex
		int[][] adjMatrix = new int[g.length + 2][g.length + 2];

		// this way we created one adjacency matrix containing all vertices  adding source and sink


		// we copy the values from the  Graph adjacency matrix to the new one copying capacities 
		// taking care of indices to account for the newly added vertices
		for (int i = 0; i<g.length;i++) {
			if (nodes[i].pv < 0) 
				maxSupplies += nodes[i].pv;
			for (int j = 0; j<g[i].length;j++) {
				adjMatrix[i+1][j+1] = g[i][j];

			}
		}

		// define new edges between source '0' and all supply vertices and inverting the sign of the supply to make it positive
		for (int i = 1; i<=g.length;i++) {
			if (nodes[i-1].pv < 0) 
				adjMatrix[s][i]	= -1 * nodes[i-1].pv;
		}
		// define new edges between all vertices in second set and the sink 'bpGraph[0].length-1'
		for (int i = 1; i<=g.length;i++) {
			if (nodes[i-1].pv > 0) 
				adjMatrix[i][t] = nodes[i-1].pv;
		}


		// find max flow
		fordFulkerson maxFlowFordFulkerson = new fordFulkerson(); 
		// the min cut is printed inside the ford Fulkerson method called in the following line
		int[][] maxFlow = maxFlowFordFulkerson.fordFulkerson(adjMatrix, 0, t);
		int flowValue = 0;
		for (int i=0;i<adjMatrix.length; i++)
			flowValue += maxFlow[i][t];

		System.out.println("The maximum possible flow of the given graph is calculated as " + flowValue); 

		return flowValue == Math.abs(maxSupplies);

	}

	//Driver Program 
	public static void main(String args[]) { 

		// First Example: First graph in lab sheet 8, 2a, is 6 supply/demand nodes with given values
		// and 8 edges with some given capacities
		// we will define the nodes using the inner class SupplyDemandNode,
		// then define the values by scanning the graph image from bottom left to right, then up from left to right

		SupplyDemandNode[] nodes = {new SupplyDemandNode(-9),
				new SupplyDemandNode(12),
				new SupplyDemandNode(0),
				new SupplyDemandNode(10),
				new SupplyDemandNode(-9),
				new SupplyDemandNode(-2)};

		// Now will define the edges as adjacency matrix
		// by default int primitive value in Java is zero (no edge)
		// then will set only the edges as seen in the image again scanning the output edges from each node
		int g[][] = new int[6][6]; 

		g[0][1] = 4;
		g[0][4] = 9;
		g[2][3] = 9;
		g[2][5] = 4;
		g[4][1] = 5;
		g[4][2] = 20;
		g[5][1] = 6;
		g[5][3] = 5;


		CirculationWithDemands circulation = new CirculationWithDemands(); 


		boolean circulationResult = circulation.circulationToMaxFlow(g, nodes);
		if (circulationResult)
			System.out.println( "There is circulation in the First Example"); 
		else
			System.out.println( "There is no circulation in the First Example"); 
		assertEquals("Maximum Flow for the given graph is calculated incorrectly as " + circulationResult, circulationResult, true);

		// Second Example: First graph in lab sheet 8, 2b, is 11 supply/demand nodes with given values
		// and 20 edges with some given capacities
		// we will define the nodes using the inner class SupplyDemandNode,
		// then define the values by scanning the graph image from orange vertex with -9 capacity, and scan the vertices with breadth first

		SupplyDemandNode[] nodes_2 = {new SupplyDemandNode(-9),
				new SupplyDemandNode(1),
				new SupplyDemandNode(-6),
				new SupplyDemandNode(-5),
				new SupplyDemandNode(12),
				new SupplyDemandNode(7), 
				new SupplyDemandNode(1), 
				new SupplyDemandNode(-12),
				new SupplyDemandNode(5),
				new SupplyDemandNode(2), 
				new SupplyDemandNode(4)
		};

		// Now will define the edges as adjacency matrix
		// by default int primitive value in Java is zero (no edge)
		// then will set only the edges as seen in the image again scanning the output edges from each node
		int g2[][] = new int[nodes_2.length][nodes_2.length]; 

		// keep track of the indices you gave to nodes, to use in the edges indices
		g2[0][1] = 3;
		g2[0][2] = 3;
		g2[0][3] = 4;
		g2[0][4] = 4;
		g2[1][5] = 9;
		g2[2][1] = 4;
		g2[2][6] = 5;
		g2[3][2] = 3;
		g2[3][4] = 8;
		g2[3][8] = 5;
		g2[4][10] = 8;
		g2[5][2] = 3;
		g2[6][7] = 2;
		g2[6][9] = 4;
		g2[6][10] = 3;
		g2[7][3] = 6;
		g2[7][4] = 12;
		g2[7][8] = 12;
		g2[8][10] = 7;
		g2[9][5] = 2;
		g2[10][9] = 5;


		circulationResult = circulation.circulationToMaxFlow(g2, nodes_2);
		if (circulationResult)
			System.out.println( "There is circulation in the Second Example"); 
		else
			System.out.println( "There is no circulation in the Second Example"); 
		assertEquals("Maximum Flow for the given graph is calculated incorrectly as " + circulationResult, circulationResult, true);

		// Third and Fourth Examples from
		// https://github.com/SleekPanther/circulation-with-demands-network-flow
		// The Both graphs have 4 supply/demand nodes with same given values
		// and 5 edges with different given capacities, one will give circulation, and one will not
		// we will define the nodes using the inner class SupplyDemandNode,
		// then define the values by scanning the graph image from a to d

		SupplyDemandNode[] nodes_3 = {new SupplyDemandNode(-3),
				new SupplyDemandNode(-3),
				new SupplyDemandNode(2),
				new SupplyDemandNode(4)
		};

		// Now will define the edges as adjacency matrix
		// by default int primitive value in Java is zero (no edge)
		// then will set only the edges as seen in the image again scanning the output edges from each node
		int g3[][] = new int[nodes_3.length][nodes_3.length]; 

		// keep track of the indices you gave to nodes, to use in the edges indices
		g3[0][2] = 3;
		g3[1][0] = 3;
		g3[1][2] = 2;
		g3[1][3] = 2;
		g3[2][3] = 2;



		circulationResult = circulation.circulationToMaxFlow(g3, nodes_3);
		if (circulationResult)
			System.out.println( "There is circulation in the Third Example"); 
		else
			System.out.println( "There is no circulation in the Third Example"); 
		assertEquals("Maximum Flow for the given graph is calculated incorrectly as " + circulationResult, circulationResult, true);

		// Now only change the edges capacities to show no circulation example
		int g4[][] = new int[nodes_3.length][nodes_3.length]; 

		// keep track of the indices you gave to nodes, to use in the edges indices
		g4[0][2] = 1;
		g4[1][0] = 3;
		g4[1][2] = 2;
		g4[1][3] = 2;
		g4[2][3] = 2;



		circulationResult = circulation.circulationToMaxFlow(g4, nodes_3);
		if (circulationResult)
			System.out.println( "There is circulation in the Fourth Example"); 
		else
			System.out.println( "There is no circulation in the Fourth Example"); 
		assertEquals("Maximum Flow for the given graph is calculated incorrectly as " + circulationResult, circulationResult, false);


	}

}



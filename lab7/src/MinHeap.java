import java.util.Comparator;

class MinHeap {
	int capacity;
	int currentSize;
	
	// The HeapNode class is also used as set, in which vertex is the parent, and key is the rank, when retrieved as an edge, it is also the cost/weight
	static class HeapNode{
		int vertex;
		int key;
	}
	
	 // Comparator class created for PriorityQueue 
    // returns 1 if node0.key > node1.key 
    // returns 0 if node0.key < node1.key and 
    // returns -1 otherwise 
    static class comparator implements Comparator<HeapNode> { 
  
        @Override
        public int compare(HeapNode node0, HeapNode node1) 
        { 
            return node0.key - node1.key; 
        } 
    } 
    
    
    
 	// A utility function to find set of an element i 
    // (uses path compression technique) 
    public static int find(HeapNode subsets[], int i) 
    { 
        // find root and make root as parent of i (path compression) 
        if (subsets[i].vertex != i) // recursively get the parent that is different from the current vertex
            subsets[i].vertex = find(subsets, subsets[i].vertex); 
  
        return subsets[i].vertex; 
    } 
  
    // https://www.geeksforgeeks.org/union-find/
    // https://www.geeksforgeeks.org/union-find-algorithm-set-2-union-by-rank/
    
    // A function that does union of two sets of x and y 
    // (uses union by rank) 
    public static void Union(HeapNode subsets[], int x, int y) 
    { 
        int xroot = find(subsets, x); 
        int yroot = find(subsets, y); 
  
        // Attach smaller rank tree under root of high rank tree 
        // (Union by Rank) 
        if (subsets[xroot].key < subsets[yroot].key) 
            subsets[xroot].vertex = yroot; 
        else if (subsets[xroot].key > subsets[yroot].key) 
            subsets[yroot].vertex = xroot; 
  
        // If ranks are same, then make one as root and increment 
        // its rank by one 
        else
        { 
            subsets[yroot].vertex = xroot; 
            subsets[xroot].key++; 
        } 
    } 
    
	HeapNode[] mH;
	int [] indexes; //will be used to decrease the distance
	
	public MinHeap(int capacity) {
		this.capacity = capacity;
        mH = new HeapNode[capacity + 1];
        indexes = new int[capacity];
        mH[0] = new HeapNode();
        mH[0].key = Integer.MIN_VALUE;
        mH[0].vertex=-1;
        currentSize = 0;
    }
	
	public void display() {
		for (int i = 0; i <=currentSize; i++) {
			System.out.println(" " + mH[i].vertex + "   distance   " + mH[i].key);
	    }
		System.out.println("________________________");
	}
	
	public void insert(HeapNode x) {
		currentSize++;
		int idx = currentSize;
        mH[idx] = x;
        indexes[x.vertex] = idx;
        bubbleUp(idx);
    }
	
	public void bubbleUp(int pos) {
		int parentIdx = pos/2;
		int currentIdx = pos;
		while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
			HeapNode currentNode = mH[currentIdx];
			HeapNode parentNode = mH[parentIdx];
			//swap the positions
            indexes[currentNode.vertex] = parentIdx;
            indexes[parentNode.vertex] = currentIdx;
            swap(currentIdx,parentIdx);
            currentIdx = parentIdx;
            parentIdx = parentIdx/2;
        }
    }
	
	public HeapNode extractMin() {
		HeapNode min = mH[1];
		HeapNode lastNode = mH[currentSize];
		// update the indexes[] and move the last node to the top
        indexes[lastNode.vertex] = 1;
        mH[1] = lastNode;
        mH[currentSize] = null;
        sinkDown(1);
        currentSize--;
        return min;
	}
	public void sinkDown(int k) {
		int smallest = k;
		int leftChildIdx = 2 * k;
		int rightChildIdx = 2 * k+1;
		if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
			smallest = leftChildIdx;
		}
		if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
			smallest = rightChildIdx;
	    }
		if (smallest != k) {
			HeapNode smallestNode = mH[smallest];
			HeapNode kNode = mH[k];
			//swap the positions
            indexes[smallestNode.vertex] = k;
            indexes[kNode.vertex] = smallest;
            swap(k, smallest);
            sinkDown(smallest);
		}
	}
	
	public void swap(int a, int b) {
		HeapNode temp = mH[a];
        mH[a] = mH[b];
        mH[b] = temp;
	}
	
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	public int heapSize(){
		return currentSize;
	}
}
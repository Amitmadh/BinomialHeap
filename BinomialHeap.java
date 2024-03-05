
/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;
	//initialize empty heap
	public  BinomialHeap() {
		
	}
	//initialize heap with the binomial tree: node
	public  BinomialHeap(HeapNode node) {
		this.last = node;
		this.min = node;
		this.size = node.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public HeapNode getLast() {
		return last;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setLast(HeapNode last) {
		this.last = last;
	}
	
	public void setMin(HeapNode min) {
		this.min = min;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 * Time Complexity: O(logn)
	 */
	public HeapItem insert(int key, String info) 
	{   
		//create node
		HeapItem newItem = new HeapItem(key, info);
		HeapNode newNode = new HeapNode(newItem);
		if (this.empty()) {
			this.setLast(newNode);
			this.setMin(newNode);
			this.size = 1;
			return newItem;
		} else {
			//create heap with that node
			BinomialHeap newBinomialHeap = new BinomialHeap(newNode);
			this.meld(newBinomialHeap);
			return newItem;
		}
	}

	/**
	 * 
	 * Delete the minimal item
	 * Time Complexity: O(logn)
	 */
	public void deleteMin()
	{

	}

	/**
	 * 
	 * Return the minimal HeapItem
	 * Time Complexity: O(1)
	 */
	public HeapItem findMin()
	{
		if (this.empty()) {
			return null;
		} else {
			return min.getHeapItem();
		}
	}
	
	/**
	 * 
	 * pre: 0 < diff < item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * Time Complexity: O(logn)
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    

	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    

	}

	/**
	 * 
	 * Meld the heap with heap2
	 * Time Complexity: O(logn)
	 */
	public void meld(BinomialHeap heap2)
	{
		if (this.empty()) {
			this.setLast(heap2.getLast());
			this.setSize(heap2.getSize());
			this.setMin(heap2.findMin().getNode());
			return;
		}
		if (heap2.empty()) {
			return;
		}
		//put the heap with the larger maximum rank tree in maxHeap and other in minHeap
		BinomialHeap minHeap;
		BinomialHeap maxHeap;
		if (this.getLast().getRank() < heap2.getLast().getRank()) {
			minHeap = this;
			maxHeap = heap2;
		} else {
			minHeap = heap2;
			maxHeap = this;
		}
		//get minimum node and size of the melded heap
		HeapNode minNode;
		if (this.findMin().getKey() < heap2.findMin().getKey()) {
			minNode = this.min;
		} else {
			minNode = heap2.min;
		}
		int size = minHeap.getSize() + maxHeap.getSize();
		//set the last trees in the heaps in minHeapCurr and maxHeapCurr
		HeapNode minTreeCurr = minHeap.getLast().getNext();
		HeapNode maxTreeCurr = maxHeap.getLast().getNext();
		HeapNode maxTreePrev = maxHeap.getLast();
		
		int numOfTreeInMinHeap = minHeap.numTrees();
		for (int i = 0; i < numOfTreeInMinHeap; i++) {
			HeapNode temp = minTreeCurr.getNext();
			//post: minHeapCurr.rank <= maxHeapCurr.rank
			while (minTreeCurr.getRank() > maxTreeCurr.getRank()) {
				maxTreePrev = maxTreeCurr;
				maxTreeCurr = maxTreeCurr.getNext();
			}
			//put minTreeCurr between maxTreeCurr and maxTreePrev 
			minTreeCurr.setNext(maxTreeCurr);
			maxTreePrev.setNext(minTreeCurr);
			if (maxTreeCurr == maxTreePrev) {
				maxTreePrev = minTreeCurr;
			}
			//link the trees that needed
			while (minTreeCurr.getRank() == maxTreeCurr.getRank() && maxHeap.numTrees() > 2) {
				HeapNode nodeAfter = maxTreeCurr.getNext();
				HeapNode link = HeapNode.link(maxTreeCurr, minTreeCurr);
				maxTreePrev.setNext(link);
				link.setNext(nodeAfter);
				minTreeCurr = link;
				maxTreeCurr = link.getNext();
			}
			if (maxTreeCurr.getRank() == minTreeCurr.getRank() && maxHeap.numTrees() == 2) {
				HeapNode link = HeapNode.link(maxTreeCurr, minTreeCurr);
				maxHeap.setLast(link);
			}
			//move to next tree in minHeap
			minTreeCurr = temp;
		}
		this.setLast(maxHeap.getLast());
		this.setSize(size);
		this.setMin(minNode);
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return this.getSize();
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		if (this.getLast() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		if (this.empty()) {
			return 0;
		} else {
			int num = 1;
			HeapNode first = this.getLast().getNext();
			HeapNode next = first.getNext();
			while (next != first) {
				next = next.getNext();
				num++;
			}
			return num;
		}
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
		
		public HeapNode(HeapItem item) {
			this.item = item;
			item.setNode(this);
			this.rank = 0;
			this.next = this;
		}
		
		public HeapItem getHeapItem() {
			return item;
		}
		
		public HeapNode getChild() {
			return child;
		}
		
		public HeapNode getNext() {
			return next;
		}
		
		public HeapNode getParent() {
			return parent;
		}
		
		public int getRank() {
			return rank;
		}
		
		public int getKey() {
			return item.getKey();
		}
		
		public String getInfo() {
			return item.getInfo();
		}
		
		public void setHeapItem(HeapItem item) {
			this.item = item;
		}
		
		public void setChild(HeapNode child) {
			this.child = child;
		}
		
		public void setNext(HeapNode next) {
			this.next = next;
		}
		
		public void setParent(HeapNode parent) {
			this.parent = parent;
		}
		
		public void setRank(int rank) {
			this.rank = rank;
		}
		
		private static HeapNode link(HeapNode tree1, HeapNode tree2) {
			//minTree and maxTree by value of their root`s key
			HeapNode minTree;
			HeapNode maxTree;
			if (tree1.getKey() >= tree2.getKey()) {
				minTree = tree2;
				maxTree = tree1;
			} else {
				minTree = tree1;
				maxTree = tree2;
			}
			
			//add maxTree as child of minTree 
			if (minTree.getChild() == null) {
				maxTree.setNext(maxTree);
			} else {
				maxTree.setNext(minTree.getChild().getNext());
				minTree.getChild().setNext(maxTree);
			}
			maxTree.setParent(minTree);
			minTree.setChild(maxTree);
			minTree.setRank(minTree.getRank()+1);
			minTree.setNext(minTree);
			
			return minTree;
		}
		
		private int size() {
			return (int)Math.pow(2, this.getRank());
		}
		
		private void heapifyUp() {
			HeapNode node = this;
			while (node.getParent() != null && node.getKey() < node.getParent().getKey()) {
				HeapItem temp = node.getParent().getHeapItem();
				node.getParent().setHeapItem(node.getHeapItem());
				node.getHeapItem().setNode(node.getParent());
				node.setHeapItem(temp);
				temp.setNode(node);
				node = node.getParent();
			}
		}
	}
	
	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		public HeapItem(int key, String info) {
			this.key = key;
			this.info = info;
		}
		
		public HeapNode getNode() {
			return node;
		}
		
		public int getKey() {
			return key;
		}
		
		public String getInfo() {
			return info;
		}
		
		public void setNode(HeapNode node) {
			this.node = node;
		}
		
		public void setKey(int key) {
			this.key = key;
		}
		
		public void setInfo(String info) {
			this.info = info;
		}
	}
	
}

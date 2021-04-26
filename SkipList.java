// John Cunningham
//
// Spring 2019
// Dr. Szumlanski
// COP 3503 0001

// Hey! I wanted to try something new and have created a table contents below, if this makes grading
// easier (or asthetically pleasing), let me know.

// Overview
// 	This is a program is a generic Skiplist, which is a linked list at heart, but gives O(log(n)) 
// runtime for search by allowing each node to refer to multiple nodes by assigning each node a
// random height.  
// 
//	Height is equal to ⌈log2n⌉ (the ceiling of logn with a base of 2 where n is equal to the number
// of elements. This means that if inserting an element so that ⌈log2(n + 1)⌉ > ⌈log2n⌉, the max h-
// eight of the list should be increased to that amount
//
// TABLE OF CONTENTS
//
//  68 | class Node<Type> (68)
//     | Type data
//     | ArrayList<Node<>Type> next
// 	
//	REQUIRED: (line number | signiture)
//  79 | Node(height)
//	95 | Node(<Type> data)
//	108| public Node<Type> next(int height)
//	122| public Type value()
//	127| public int height()
//
//	UTILITY METHODS
// 	133| public void setNext(int level, Node<Type> node)
//  139| public void grow()
//	145| public void growRandom()
//  154| public void trim(int height)
//  
//  169| class SkipList <type extends Comoparable<type>> (169)
//		 Node<type> head
//		 int size
//
//  REQUIRED:
//  169| SkipList()
//	177| SkipList(int height)
//	191| public int size()
//	198| public int height()
//	204| public Node<type> head()
//	210| public void insert(type data)
//	217| public void insert(type data, int height)
//	313| public void delete(type data)
//  431| public boolean contains(type data)
//  463| public Node<type> get(type data)
// 	652| public static void double difficultyRating()
//  657| public static double hoursSpent()
//  
//  UTILITY METHODS
//  499| private static int getMaxHeight(int n)
//  518| private static int generateRandomHeight(int maxHeight)
//  537| private void growSkipList()
//  574| private void shrinkSkipList()
//  619| private void printSkipList()


import java.lang.Math;
import java.util.*;


class Node<Type>
{
	// Each node contains one unit of data that is also comparable.
	// The ArrayList is a list of references to other objects. Each element of the array
	// represents one level of the array; therefore, the size of the ArrayList is the height of
	// the node.

	Type data; 
	ArrayList<Node<Type>> next; 

	// This creates a node with a specified height.
	Node(int height)
	{	
		next = new ArrayList<>();

		// To properly initalize the height of a node, I add a height amount of null references  
		for (int i = 0; i < height; i++)
		{
			next.add(null);
		}

		this.data = null;

	}

	// This functions exactly like the Node(int height) method, but allows for the input of a value
	// other than null.
	Node(Type data, int height)
	{
		next = new ArrayList<>();

		for (int i = 0; i < height; i++)
		{
			next.add(null);
		}

		this.data = data;
	}

	// This function goes into the "next" ArrayList and returns what a certain level refers to.
	public Node<Type> next(int level)
	{
			if (level >= this.height() || level < 0)
			{
				return null;
			}	

			else
			{
				return next.get(level);
			}
	
	}

	public Type value()
	{
		return this.data;
	}

	public int height()
	{
		return this.next.size();
	}

	// Set the level of this node to the node that is passed to this function
	public void setNext(int level, Node<Type> node)
	{
		this.next.set(level - 1, node);
	}

	// increases this particular node's height by one
	public void grow()
	{
		this.next.add(null);
	}

	// 50% chance to grow this particular node
	public void growRandom()
	{
		int j = (int)(Math.random() * 2);
		if (j == 0)
			this.next.add(null);
		
	}

	// Shrinks this particular node to a new height (the one passed to the function)
	public void trim(int height)
	{
		int grabbedHeight = this.next.size();
		int grabbedHeight2 = this.next.size();

		for (int i = 0; i < ( grabbedHeight - height); i++)
		{
			this.next.remove(--grabbedHeight2);
		}
	}

}

// To create a functioning SkipList, the object msust implement comparable to be able to sort and
// traverse the SKipList
public class SkipList <type extends Comparable<type>>
{	
	Node<type> head;
	// A primitive integer is used to keep track of how many elements are in this particular
	// SkipList.
	int size;

	// Creates a default Skiplist with an initial height of 0 and a head of size 1.
	SkipList()
	{
		this.head = new Node<type>(1);
		this.size = 0;
	}

	// Creates a SkipList with a specified height and initializes height to 0.
	public SkipList(int height)
	{
		this.head = new Node<type>(height);
		this.size = 0;
	}

	// Returns the size of the SkipList
	public int size()
	{
		return this.size;
	}

	// Since the head node will always have the max possible height within the SkipList, returning
	// the size of the head node will always be the current height of the SkipList.
	public int height()
	{
		return head.height();
	}

	// Returns the head.
	public Node<type> head()
	{
		return this.head;
	}

	// This function inserts a node with a random height 
	public void insert(type data)
	{
		int height = generateRandomHeight(getMaxHeight(this.size + 1));
		insert(data, height);
	}
	
	// Given a height and value, insert a new node.
	public void insert(type data, int height)
	{
		// Conditional: if inserting this value will cause the SkipList to increase in size,
		// increase the size of the entire SkipList.
		if ((getMaxHeight(this.size + 1) > getMaxHeight(this.size) && this.size != 0))
		{	
			if ( getMaxHeight(this.size + 1) > this.head.next.size())
			{
				this.growSkipList();
			}

		}

		// This is the node we must insert.
		Node<type> nodeToInsert = new Node<>(data, height);

		// Contains the nodes of what should reference to the inserted node.
		ArrayList<Node<type>> prevBookMarks = new ArrayList<>();

		// This Node will traverse the SkipList in logn time.
		Node<type> prevNodesB;

		// if the SkipList is empty (Still has a head node)
		if (head.next.get(0) == null)
		{
			for (int i = 0; i < nodeToInsert.next.size(); i++)
				head.next.set(i, nodeToInsert);
			this.size++;
			return;
		}

		// prevBookMarks is initalized ot the head.next 
		for (int i = 0; i < this.head.next.size(); i++)
		{
			prevBookMarks.add(this.head);
		}

		// prevNodesB is initalized to the head f
		prevNodesB = this.head;

		// This loop will traverse the SkipList with the desired runtime.
		// I start by going to the top of the head, then working my way down.
		for (int i = prevNodesB.height() - 1; i > -1; i--)
		{
			// if the next node of height i is null, go down a level.
			if (prevNodesB.next.get(i) == null)
			{
				// Reguardless of the next node of height i being null, I need to keep track
				// of the previous node incase the inserted node
				prevBookMarks.set(i, prevNodesB);
				continue;
			}

			// This is the result of comparing the next node of height i to what is being inserted.
			int comparison = prevNodesB.next.get(i).data.compareTo(nodeToInsert.data);

			// if the next value is smaller than what is being inserted, move prevNodesB to that 
			// Node.
			if (comparison < 0)
			{	
				// prevBookMark must should reference the value before prevNodesB moves to the right.
				prevBookMarks.set(i, prevNodesB);

				// prevNodesB is moved forward.
				prevNodesB = prevNodesB.next.get(i);

				// i is reset to the new current nodes height
				i = prevNodesB.next.size();
			}

			// if the next value is greater than or equal to the pointer, the correct position has
			// been found.
			else if (comparison >= 0)
			{
				for (int j = 0; j < prevNodesB.next.size(); j++)
					prevBookMarks.set(j, prevNodesB);
			}

		}

		// bookmarks now contains all the nodes that should now reference to what I need to insert.
		// prevNodesB contains the node directly next to where I need to insert so it isn't needed.
		
		// I need to set the nodeToInsert.next to the appropriate values by using the bookmark.
		// Now, I need to set the bookmarks.next.get(element) to reference to nodeToInsert.

		for (int i = 0; i < nodeToInsert.height(); i++)
		{
			nodeToInsert.next.set(i, prevBookMarks.get(i).next.get(i));
			prevBookMarks.get(i).next.set(i,nodeToInsert);
		}

		this.size++;	

	}

	public void delete(type data)
	{
		Node<type> nodeSurfer = this.head;
		ArrayList<Node<type>> bookmarks = new ArrayList<>();

		// bookmarks contains references to head and is of size this.head.next.size() 
		// nodeSurfer currently refers to the head node

		// this for loop will make nodeSurfer = the first node that contains data
		// boomarks will contain a list of all previous references that could refer to 
		// nodesurfer if the nodesurfer was of max height.


		// bookmark currently is an arraylist that contains head.height number of references to head.
		// nodeSurfer currently refers to the head node.


		for (int i = nodeSurfer.next.size() - 1; i > -1; i--)
		{
			// check if the next node refers to a node
			if (nodeSurfer.next.get(i) == null)
			{
				// if not, go down a node.
				continue;
			}

			// check value of highest next node is what we are looking for

			if (nodeSurfer.next.get(i).data.compareTo(data) == 0)
			{
				// At this point, the node before the targeted node is detected,
				// bookmark copies the value of the node up until the height of i.
				for (int j = 0; j <= i; j++)
				bookmarks.add(nodeSurfer);

				// nodeSurfer is now the value
				nodeSurfer = nodeSurfer.next.get(i);
				
				// This for loop is to move bookmarks. 
				for (int j = bookmarks.size() - 1; j > -1; j--)
				{
					while (bookmarks.get(j).next.get(j).data.compareTo(data) < 0)
					{
						bookmarks.set(j, bookmarks.get(j).next.get(j));
					}
					
					boolean actualValue = bookmarks.get(j).next.get(j).data.compareTo(data) == 0;

					// If a node is found with the same value, but has a lower height (hash values
					// don't equal each other and by finding another node with the same value,
					// this new node is the first value and therefore the new value deleted)

					if ((bookmarks.get(j).next.get(j) != nodeSurfer) &&  actualValue)
					{
						nodeSurfer = bookmarks.get(j).next.get(j);	
					}

				}


				break;
			}

			// if the next value is greater than what should be deleted, the function then
			// then goes down a level, or exits the loop.
			else if (nodeSurfer.next.get(i).data.compareTo(data) > 0)
			{
				continue;
			}

			else if (nodeSurfer.next.get(i).data.compareTo(data) < 0)
			{
				nodeSurfer = nodeSurfer.next.get(i);
				// i++ ensures that we don't exit the loop early and if I reach the value of 0,
				// the fuction will continue to loop until the next value is larger or equal to 
				// data.
				i++; 
			}


		}

		// After we have exited the loop above:
		// nodeSurfer = node to be deleted, if the node isn't found, nodeSurfer.data != data.
		// Bookmark contains all previous nodes that reference nodeSurfer up to its height.

		// This is a saftey net to return if no node should be deleted
		if (nodeSurfer == null || (nodeSurfer.data == null) || nodeSurfer.data.compareTo(data) != 0)
			return;

		// This dereferences node surfer by making all previous nodes that refer to the element
		// to the next value. Java's garbage collection then detects that nothing refers to this
		// particular node and erases it.

		for (int i = nodeSurfer.next.size() - 1; i > -1; i--)
		{
			bookmarks.get(i).next.set(i, nodeSurfer.next.get(i));
		}

		this.size--;
		
		// Now that there are the apporopirate number of nodes,
		// This conditional will detect whether to decrease the height of the SkipList
		if (getMaxHeight(this.size) < this.head.next.size())
		{	
			// Simple debug stuff
			// System.out.println("-----------------------------");
			// this.printSkipList();
			// System.out.println("-----------------------------");
			this.shrinkSkipList();

			// Double checks that height is apopropriately set.
			this.head.trim(getMaxHeight(this.size));

		}
		
	}

	public boolean contains(type data)
	{

		Node<type> surfer = this.head;

		// This for loop will traverse the SkipList in appropriate time. 
		for (int i = surfer.next.size() - 1; i >= 0; i--)
		{
			// If next node is null, go down a level or exit loop.
			if (surfer.next.get(i) == null)
				continue;
			
			// If the value 
			if (surfer.next.get(i).data.compareTo(data) == 0)
				{
					return true;
				}

			else if (surfer.next.get(i).data.compareTo(data) > 0)
				continue;

			else if (surfer.next.get(i).data.compareTo(data) < 0)
			{
				surfer = surfer.next.get(i);
				i++;
			}
		}

		return false;
	}

	// Literally public boolean contains(type data) but returns a node instead of boolean value
	public Node<type> get(type data)
	{
		if (data == null)
			return head;

		Node<type> surfer = this.head;

		for (int i = surfer.next.size() - 1; i >= 0; i--)
		{

			if (surfer.next.get(i) == null)
				continue;
			
			if (surfer.next.get(i).data.compareTo(data) == 0)
				{
					return surfer.next.get(i);
				}

			else if (surfer.next.get(i).data.compareTo(data) > 0)
				continue;

		 

			else if (surfer.next.get(i).data.compareTo(data) < 0)
			{
				surfer = surfer.next.get(i);
				i++;
			}

		}

		return null;

	}

	// Given the number of elements in a SkipList, this returns max possible height of the SkipList.
	private static int getMaxHeight(int n)
	{
		int d = (int)(Math.log(n)/Math.log(2));
		double b = (Math.log((double)n)/Math.log(2));
		int c;

		if (n == 1)
			return 1;

		if ((double)d < b)
			c = d + 1;
		else{
			c = (int)b;
		}

		return c;
	}

	// Generates a random height given an upperbound.
	private static int generateRandomHeight(int maxHeight)
	{	
		int k = 1;

		int j = (int)(Math.random() * 2);
		
		while (j == 0 && k < maxHeight)
		{
			j = (int)(Math.random() * 2);
			k++;
		}
			
		return k;
	}


	// This function will increase the size of the skiplist
	// This function will have a 50% chance to actually increase a node

	private void growSkipList()
	{
		//  Add 1 height to head
		if (getMaxHeight(size + 1) > this.head.next.size())
		this.head.grow();
		
		// nodeSurfer will be the surfer that has a chance to increase
		// this size of a node.
		Node<type> prevNode = head;
		Node<type> nodeSurfer = head;

		//heightMinus1 is the current height,
		int heightMinus1 = prevNode.next.size() - 1;
		int heightMinus2 = prevNode.next.size() - 2;


		// In this loop, we will tread through each highest node to then increase height
		// Exit loop when nodeSurfer.next.get(height - 1) == 0;

		while (nodeSurfer.next.get(heightMinus2) != null)
		{

			nodeSurfer = nodeSurfer.next.get(heightMinus2);
			nodeSurfer.growRandom();
			if (nodeSurfer.next.size() == this.head.next.size())
			{
				prevNode.next.set(heightMinus1, nodeSurfer);
				prevNode = nodeSurfer;
			}
		}

		nodeSurfer.next.set(nodeSurfer.next.size() - 1, null);

	}


	// Decreases the maximum height.
	private void shrinkSkipList()
	{
		// Base Case: If there was only one element in the array, double check that that the 
		// head node points to nothing and return.
		if (this.size == 0)
		{
			this.head.next.clear();
			return;
		}

		// Up to this point, this.size reflects the actual size of the SkipList. So,
		// max = the maximum height of the SkipList
		int max = getMaxHeight(this.size);

		// surfer and prevNode are initialized to head. surfer will traverse the linked list while
		// prevNode will be the node before the node surfer is pointing to
		Node<type> surfer = head;
		Node<type> prevNode = head;

		int oldHeight = surfer.next.size();
		int newHeight = max;

		if (surfer.next.get(newHeight - 1) == null)
		{	
			head.trim(newHeight);
			return;
		}

		// The newHeight is the new max height of the SkipList. Everything above this height is 
		// irrelevant and so we just traverse through all pointers that have a height of this upper
		// bound and trim their heights to match.
		while (surfer != null)
		{
			surfer = surfer.next.get(newHeight);
			prevNode.trim(newHeight);
			prevNode = surfer;
		}

	}

	// Prints the SkipList by traversing through each pointer at the lowest level.
	// Also prints a list of sizes corresponding to each element in the array followed by a
	// list of the contents of the SkipList.
	// (WARNING: Should only be used for small SkipLists with single or double digit values for
	//  formatting experience)
	private void printSkipList()
	{
		ArrayList<Integer> sizes = new ArrayList<>();
		ArrayList<type> contents = new ArrayList<>();

		// While the finder node traverses the SkipList and doesn't equall null...
		for (Node<type> finder = this.head; finder != null; finder = finder.next(0))
		{	
			System.out.println("Data is " + finder.data);
			// Print out what each height of the indiviudal node points to without causing a 
			// NullPointerExceptionError.			
			for (int i = 0; i < finder.height(); i++)
			{
				if (finder.next(i) != null)
				System.out.println("Height " + i + " refers to " + finder.next(i).data);
				
				else
				System.out.println("Height " + i + " refers to null" );

			}

			// Mark down the size and contents of each node.
			sizes.add(finder.next.size());
			contents.add(finder.data);

			System.out.println();
		}

		System.out.println(sizes);
		System.out.println(contents);

	}

	public static double difficultyRating()
	{
		return 5;
	}

	public static double hoursSpent()
	{
		return 42;
	}	

}
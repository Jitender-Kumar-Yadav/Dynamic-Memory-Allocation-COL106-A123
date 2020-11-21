// Implements Dictionary using Doubly Linked List (DLL)
// Implement the following functions using the specifications provided in the class List

public class A1List extends List {

    private A1List next; // Next Node
    private A1List prev;  // Previous Node 

    public A1List(int address, int size, int key) { 
        super(address, size, key);
    }
    
    public A1List(){
        super(-1,-1,-1);
        // This acts as a head Sentinel

        A1List tailSentinel = new A1List(-1,-1,-1); // Intiate the tail sentinel
        
        this.next = tailSentinel;
        tailSentinel.prev = this;
    }

    public A1List Insert(int address, int size, int key)
    {
		if(this.next == null){
			return null;
			//current node is a tail sentinel, error in insertion
		}
		A1List newNode = new A1List(address, size, key); //create a new node with given values
		//modify all pointers so as to insert the node newNode at the correct position
		newNode.prev = this; //set this current node as the previous node to the new node created
		newNode.next = this.next; //set the node next to the current node as the next pointer of the new Node created
		if(this.next != null){
			this.next.prev = newNode;
		}
		this.next = newNode; //modify the pointers of the next and previous nodes of the new Node created
        return newNode;
    }

    public boolean Delete(Dictionary d) 
    {
		if(d.key == -1 && d.address == -1 && d.size == -1){
			return false;
			//removal of head and tail sentinels is not permitted
		}
		A1List it = this.search(d);
		//search for the dictionary d in the list and store it in it
		if(it != null){
			it = it.prev;
			it.next = it.next.next;
			it.next.prev = it;
			return true;
			//if it is not null, we point it to the previous node to the node searched
			//we set the next pointer of this new it to the element following the searched node
			//we set the prev of the next node to the new it to it
			//hence the dictionary has been deleted from the list and true is returned
		}
		//if it is null, it implies that d did not exist in A1List and hence false is returned
        return false;
    }

    public A1List Find(int k, boolean exact)
    {
		A1List it = this.getFirst(); //move to the first node of the list
		if(it != null){
			return it.Find_current(k, exact);
			//calls the Find_current function at the first node
		}
		return it;
		//return the null node if it is null
    }

    public A1List getFirst()
    {
		A1List it = this;
		//traverse the list backwards till we reach the head sentinel
        while(it.prev != null){
			it = it.prev;
		}
		//check if the list is empty, that is the node next to head sentinel is tail sentinel or not
		if(it.next.next != null){
			//the next node to head sentinel is not a tail sentinel, the list has atleast one element
			return it.next; //returns the element next to the head sentinel
		}
		//if the node next to head sentinel is a tail sentinel, the list is empty
		return null;
    }
    
    public A1List getNext() 
    {
		if(this.next != null && this.next.next != null){
			return this.next; //the next pointer is not a null pointer
		}
		//the next pointer is null or given list is null or the given node is the last element followed by a tail sentinel
        return null;
    }

    public boolean sanity()
    {
        return true;
    }

	//The following functions are private and have been called to make some functions easier.
	
	private A1List search(Dictionary d)
	{
		//this function searches a dictionary d in the A1List, if it exists
		//if d is found it returns the pointer to it
		//otherwise null is returned
		
		A1List it = this.getFirst();
		it = it.Find_current(d.key, true);
		//find the node with key as d.key starting from the first node
		while(it != null && (it.address != d.address || it.size != d.size)){
			it = it.next.Find_current(d.key, true);
			
			//this loop is entered if one node with key as d.key is found and it does not match the other attributes of d
			//in that case, we perform search for d.key starting from the next node (i.e. it.next)
		}
		return it;
		//the loop stops when
		//1. it is null and hence no such node exists leading to returning of null pointer OR
		//2. it points to a node which matches d
	}
	
	private A1List Find_current(int k, boolean exact)
	{
		//this function searches the A1List from current node
		//all the semantics are same as the Find function
		//except that the find does a search starting from the first Node
		//while Find_current searches it from the current node, only in the forward direction
		
		A1List it = this;
		if(it.next != null){
			//this block is executed if it is not the tail sentinel
			while(it.next != null && ((exact && it.key != k) || (!exact && it.key < k))){
				it = it.next;
				
				//this loop keeps on traversing the list
				//in the forward direction only
				//starting from the current node
				
				//the loop continues until one of the two conditions fail
				//1. it becomes a tail sentinel
				//2. it matches the required conditions:
				//it has key k (if exact is true) or it has key larger thank k (if exact is false)
			}
			if(it.next == null){
				return null;
				//if it is a tail, null is returned, implying node not found
			}
			return it;
			//if it ends at a non-tail, then it is the required node and is returned
		}
		return null;
	}
	
	//The following functions have been defined to make debugging easier and can be called only for A1List
	
	public void printNode()
	{
		//this function prints the content of a node
		System.out.println(this.address + " " + this.size + " " + this.key);
	}
	
	public void printList()
	{
		//this function prints a List starting from the first element to the end excluding the sentinel nodes
		System.out.println("Starting to print");
		A1List it = this.getFirst();
		while(it.next != null){
			it.printNode();
			it = it.next;
		}
		System.out.println("Print ended");
	}
	
	public int countNodes()
	{
		//this function counts the total number of nodes in the list
		int count = 0;
		for (Dictionary d = this.getFirst(); d != null; d = d.getNext()) count = count + 1;
		return count;
	}
}



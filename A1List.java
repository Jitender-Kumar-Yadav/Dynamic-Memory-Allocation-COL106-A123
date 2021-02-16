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
		if(d == null){return true;}
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
		A1List it1 = this;
		A1List it2 = this;
		/* check the existence of loop in the forward direction
		alongside the verification of loop invariants 1. node.next.prev = node and 2. node.prev.next = node */
		while(it2.next != null && it2.next.next != null){
			if(it1.next.prev != it1){
				return false; //Inavriant 1 unsattisfied at it1
			}
			it2 = it2.next.next; //move it2 by 2 nodes per iteration
			it1 = it1.next; //move it1 by 1 node per iteration
			if(it1.prev.next != it1){
				return false; //Inavriant 2 unsattisfied at it1
			}
			if(it1 == it2){
				return false;
				/* since it1 and it2 are moving at different speeds, it is natural that they do not meet.
				However if they meet, it means they are both struck in a cycle,
				which means the given A1List has a cycle and hence sanity returns false */
			}
		}
		while(it1.next != null){
			/* This loop stops only if it1 reaches the tail sentinel.
			This was not possible until it2 had already reached the tail sentinel 
			since it2 moved at a faster speed and hence this condition was not verified 
			in the last loop.*/
			if(it1.next.prev != it1){
				return false; //Inavriant 1 unsattisfied at it1
			}
			it1 = it1.next;
			if(it1.prev.next != it1){
				return false; //Inavriant 2 unsattisfied at it1
			}
		}
		if(it1.key != -1 || it1.address != -1 || it1.size != -1){
			return false;
			//this if ensures that when it1.next is null
			//that is when it1 is tail sentinel, it takes (-1,-1,-1) only.
		}
		/* check the existence of loop in the backward direction
		alongside the verification of loop invariants node.next.prev = node and node.prev.next = node */
		it1 = this;
		it2 = this;
        while(it2.prev != null && it2.prev.prev != null){
			if(it1.prev.next != it1){
				return false; //Inavriant 1 unsattisfied at it1
			}
			it2 = it2.prev.prev; //move it2 by 2 nodes per iteration
			it1 = it1.prev; //move it1 by 1 node per iteration
			if(it1.next.prev != it1){
				return false; //Inavriant 2 unsattisfied at it1
			}
			if(it1 == it2){
				return false;
				/* since it1 and it2 are moving at different speeds, it is natural that they do not meet.
				However if they meet, it means they are both struck in a cycle,
				which means the given A1List has a cycle and hence sanity returns false */
			}
		}
		while(it1.prev != null){
			/* This loop stops only if it1 reaches the head sentinel.
			This was not possible until it2 had already reached the head sentinel 
			since it2 moved at a faster speed and hence this condition was not verified 
			in the last loop.*/
			if(it1.prev.next != it1){
				return false; //Inavriant 1 unsattisfied at it1
			}
			it1 = it1.prev;
			if(it1.next.prev != it1){
				return false; //Inavriant 2 unsattisfied at it1
			}
		}
		if(it1.key != -1 || it1.address != -1 || it1.size != -1){
			return false;
			//this if ensures that when it1.prev is null
			//that is when it1 is head sentinel, it takes (-1,-1,-1) only.
		}
		
		return true; //if all invariants are satisfied, the list is sane
    }

	//The following functions are private and have been called to make other functions easier.
	
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
}



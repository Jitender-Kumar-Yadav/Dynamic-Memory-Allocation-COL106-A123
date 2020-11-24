// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java

public class BSTree extends Tree {

    private BSTree left, right;     // Children.
    private BSTree parent;          // Parent pointer.
        
    public BSTree(){  
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
    }    

    public BSTree(int address, int size, int key){
        super(address, size, key);
    }

    public BSTree Insert(int address, int size, int key) 
    {
		BSTree newNode = new BSTree(address, size, key); //store the data to be inserted in a new node
		if(this.parent == null){
			//this block is executed if the current node is a sentinel
			if(this.right == null){
				//if the sentinel has no right child, it is the only node that us the tree is empty
				this.right = newNode; //set the node to be inserted as the right child and hence the root
				newNode.parent = this; //set the sentinel as the parent of the newNode
				return newNode;
			}
			else{
				//if the tree is non-empty this block gets executed
				return this.right.Insert(address, size, key); //insert the node in the right subtree
			}
		}
		else{
			//if the current node is non-sentinel or container node, this block is executed
			if(newNode.compare(this)){
				//the node to be inserted is lesser or equal to than this,
				//in this case new node goes to the left
				if(this.left == null){
					//the left subtree is empty,insert newNode at that place
					this.left = newNode;
					newNode.parent = this;
					return newNode;
				}
				else{
					//this block is executed if the left subtree is not empty
					//insert the node in the left subtree
					return this.left.Insert(address, size, key);
				}
			}
			else{
				//the node to be inserted is more than this,
				//in this case new node goes to the right
				if(this.right == null){
					//the right subtree is empty,insert newNode at that place
					this.right = newNode;
					newNode.parent = this;
					return newNode;
				}
				else{
					//this block is executed if the right subtree is not empty
					//insert the node in the right subtree
					return this.right.Insert(address, size, key);
				}
			}
		}
    }

    public boolean Delete(Dictionary d)
    {
		if(d.key == -1 && d.address == -1 && d.size == -1){
			return false;
			//removal of sentinel node is not permitted
		}
		BSTree it = this.search(d); //search the dictionary d and store in it
		if(it != null){
			this.DelNode(it);
			return true;
		}
		//if it is null, it implies that d did not exist in BSTree and hence false is returned
        return false;
    }
        
    public BSTree Find(int key, boolean exact)
    {
		BSTree it = this.getFirst(); //the first element is the smallest key element
		if(it != null){
			//if the first element is not null
			it = it.Find_current(key, exact); //find the element with given key conditions and return the smallest
		}
		return it;
		//it is null if tree is empty or the element is not found
		//else returns the least element with given key, i.e. it
    }

    public BSTree getFirst()
    {
		BSTree it = this;
		it = it.getRoot();
		if(it != null){
			//it is the root and will be null if the tree is empty
			//thus when it is not null, it points to the root
			while(it.left != null){
				it = it.left; //keep moving left
				//since the left child is smaller than it, it keeps on moving left
				//till the left node is null
				//this should be the smallest element
			}
		}
		return it;
		//if it.getRoot() was null, BSTree would be empty and it == null
		//else it would be the leftmost element and hence the smallest
    }

    public BSTree getNext()
    {
		BSTree it = this;
		if(it.parent == null){
			return it.getFirst();
			//if it is a sentinel node, its first element must be returned
		}
		if(it.right != null){
			//if it has a right node, or a right subtree, inorder is performed on that subtree
			it = it.right; //shift to the rightmost element
			while(it.left != null){
				it = it.left; //keep moving to the left till the left node is null
				//inorder keeps on recursively going left and left
				//until the leftmost node is encountered
			}
			return it; //it must be the next element in the recursive inorder
		}
		else{
			//if there is no right child, inorder must take it to the parent
			while(it.parent.parent != null && it.parent.right == it){
				//this block is executed if it is the right node of its parent
				//thus in the inorder the right node has been called already
				//so this continues till a node it is encountered such that it is the left child of its parent
				//or till the parent becomes the sentinel node
				it = it.parent;
				//After every iteration, it points at a node whose right child has been traversed
				//and which is a container node
			}
			if(it.parent.parent != null){
				//it reaches a node which is not the root
				//it must be a node whose right child has been traversed
				//and which is the left child of its parent
				//its parent must be the untraversed node
				return it.parent;
			}
		}
        return null;
		//null is returned if it has no right child and its parent is a sentinel node, thus it must be the last node
    }

    public boolean sanity()
    { 
        return false;
    }
	
	//The following functions are only helper functions and will be kept private.
	public boolean compare(Dictionary a)
	{
		//this function returns true if this is smaller than (or equal to) a, else false
		return (this.key != a.key)?(this.key < a.key):(this.address <= a.address);
			//if keys of this and a are not same,
			//the function returns true of key of this is lesser
			//and false otherwise
			//if keys of a and this are same
			//it returns whether this has smaller (or at least equal) address than a
	}
	
	public BSTree getRoot(){
		//this function returns the root of the binary tree
		//if the tree is empty, null is returned
		BSTree it = this;
		if(it.parent != null || it.right != null){
			//this block is executed if it has a parent, that is it is not a sentinel node
			//or if it is sentinel node, it has a right child, that is it is not empty
			while(it.parent != null){
				//this loop is executed if this is a container node
				it = it.parent; //keep on going up
			}
			return it.right;
			//the loop continues till it reaches a node whose parent is null which is the sentinel node by definition
			//the root is the right child of this sentiel
		}
        return null;
		//if the parent is null and right chid is null
		//the node is a sentinel without a child, hence the tree is empty
	}
	
	public BSTree Find_current(int key, boolean exact)
	{
		//this function searches the BSTree from current node
		//all the semantics are same as the Find function
		//except that the find does a search starting from the least Node
		//while Find_current searches it from the current node, only in the forward direction
		BSTree it = this;
		while(it != null){
			//it traverses to the next element in the inorder and stops only when the last element is reached
			if((exact && it.key == key) || (!exact && it.key >= key)){
				return it;
				//if exact is true, the condition checks if it has key same as required and returns it if true
				//if exact is false, condition checks whether it has a key higher than the key required
				//in case of not exact, the smallest it.key is returned satisying the given condition
				//thus, binary tree implements Best Fit Algorithm for searching an element
			}
			if(exact && it.key > key){
				return null;
				//since the loop checks every it.key with key
				//it returns null, whenever the value of it.key exceeds key
				//in case an exact search is required
			}
			it = it.getNext(); //it traverses the inorder of the tree
		}
        return null; //if it becomes null, the required key is not found till the end and hence null is returned
	}
	
	public BSTree search(Dictionary d)
	{
		BSTree it = this.getFirst(); //move to the first element of the inorder
		it = it.Find_current(d.key, true); //find the first element that matches d.key
		while(it != null && (it.address != d.address || it.size != d.size)){
			it = it.getNext().Find_current(d.key, true);
			//this loop is entered if one node with key as d.key is found and it does not match the other attributes of d
			//in that case, perform search for d.key starting from the next node in the inorder
		}
		return it;
		//the loop stops when
		//1. it is null and hence no such node exists leading to returning of null pointer OR
		//2. it points to a node which matches d
	}
	
	public void DelNode(BSTree Node){
		//deletes the node T from the given BSTree
		//the reference to the node T is known
		BSTree it = Node;
		if(it.left == null && it.right == null){
			//it is a leaf
			if(it.parent.left == it){
				//check if it is a left child of the parent
				it.parent.left = null;
			}
			else{
				//otherwise it would be the right child of its parent
				it.parent.right = null;
			}
			it.parent = null;
		}
		else if(it.left == null){
			//only right child of it exists
			it.key = it.right.key;
			it.address = it.right.address;
			it.size = it.right.size; //copy the right child in it
			it.right.parent = null;
			it.right = null; //delete the right child
		}
		else if(it.right == null){
			//only left child of it exists
			it.key = it.left.key;
			it.address = it.left.address;
			it.size = it.left.size; //copy the left child in it
			it.left.parent = null;
			it.left = null; //delete the left child
		}
		else{
			it = it.right; //move to the right child more than it
			while(it.left != null){
				it = it.left;
				//keep moving left till the least node larger than Node is reached
				//it is thus the successor of Node and must have maximum one child
			}
			Node.key = it.key;
			Node.address = it.address;
			Node.size = it.size; //copy the contents of it in Node
			Node.DelNode(it); //Delete it from the tree
		}
	}
	
	//The following functions are only for debugging
	public void printNode(){
		if(this.parent == null){
			System.out.println("(" + this.address + "," + this.size + "," + this.key + ")");
			return;
		}
		String t = "L";
		if(this.parent.right == this){
			t = "R";
		}
		System.out.println("(" + this.address + "," + this.size + "," + this.key + ")" + t);
	}
	public void print(){
		BSTree it = this.getRoot();
		System.out.println("Starting to print Preorder");
		if(it != null){
			it.preorder();
		}
		System.out.println("Ended Printing");
		System.out.println("Starting to print order");
		it = it.getFirst();
		while(it != null){
			it.printNode();
			it = it.getNext();
		}
		System.out.println("Ended Printing");
	}
	private void preorder(){
		BSTree it = this;
		it.printNode();
		if(it.left != null){
			it.left.preorder();
		}
		if(it.right != null){
			it.right.preorder();
		}
	}
	private void inorder(){
		BSTree it = this;
		if(it.left != null){
			it.left.inorder();
		}
		it.printNode();
		if(it.right != null){
			it.right.inorder();
		}
	}
}


 



// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java
import java.util.*;

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
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
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
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
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
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
		BSTree it = this.getRoot(); //the root is called
		if(it != null){
			//if the root element is not null
			it = it.Find_current(key, exact); //find the element with given key conditions and return the smallest
		}
		return it;
		//it is null if tree is empty or the element is not found
		//else returns the least element with given key, i.e. it
    }

    public BSTree getFirst()
    {
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
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
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
		BSTree it = this;
		if(it.parent == null){
			return null;
			//if it is a sentinel node, null returned
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
		/*Time Complexity: O(n), n = number of nodes
		Space Complexity: O(n)*/
		HashSet<BSTree> data = new HashSet<BSTree>(); //define an efficient DS to store data
		BSTree it = this;
		//go to the root and store each element in the HashSet data
		//check if there is a cycle in the path to root by comparing the element in data
		if(it.parent != null || it.right != null){
			//this block is executed if it has a parent, that is it is not a sentinel node
			//or if it is sentinel node, it has a right child, that is it is not empty
			data.add(it);
			while(it.parent != null){
				//this loop is executed if this is a container node
				it = it.parent; //keep on going up
				if(data.contains(it)){
					return false; //if element is already in data the list has a cycle
				}
				data.add(it);
			}
			it = it.right;
			//the loop continues till it reaches a node whose parent is null which is the sentinel node by definition
			//the root is the right child of this sentiel
		}
		else{
			return true;
		}
		data.clear();
		//check if a cycle is there in the list starting from root
		//simultaneously check the invariants regarding parent and child
		return it.checkloop(data);
    }
	
	//The following functions are only helper functions and will be kept private.
	private boolean compare(Dictionary a)
	{
		//this function returns true if this is smaller than (or equal to) a, else false
		/*Time Complexity: O(1)
		Space Complexity: O(1)*/
		return (this.key != a.key)?(this.key < a.key):(this.address <= a.address);
			//if keys of this and a are not same,
			//the function returns true of key of this is lesser
			//and false otherwise
			//if keys of a and this are same
			//it returns whether this has smaller (or at least equal) address than a
	}
	
	private BSTree getRoot(){
		//this function returns the root of the binary tree
		//if the tree is empty, null is returned
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
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
	
	private BSTree Find_current(int key, boolean exact)
	{
		//this function searches the BSTree from current node
		//all the semantics are same as the Find function
		//except that the find does a search starting from the root Node
		//while Find_current searches it from the current node in the current subtree
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
		BSTree it = this;
		if(key <= it.key){
			//it has a key more than or equal to the required key
			//check if the left subtree has one of the required keys or not
			if(it.left == null){
				//there is no left subtree
				//check if it is the required key
				if((exact && it.key == key) || !exact){
					return it; //it has a key which matches the conditions return it
				}
				return null; //if exact is true but it's key which does not match key null is returned
			}
			BSTree it2 = it.left.Find_current(key, exact); //if left subtree exists, search for the key there
			//recursively assumed that the best fit key of it.left is found
			if(it2 == null){
				//there is no key in the left subtree which matches given key
				//handle similar to it.left == null
				if((exact && it.key == key) || !exact){
					return it; //it has a key which matches the conditions return it
				}
				return null; //if exact is true but it's key which does not match key null is returned
			}
			//if it2 exists and is non null return it as the required element
			return it2;
		}
		//the key of it is smaller than the key needed, search in the right subtree
		if(it.right != null){
			it = it.right.Find_current(key, exact); //search in the right subtree
			return it; //it is the required element
		}
		return null; //if no right subtree, return null as key not found
	}
	
	private BSTree search(Dictionary d)
	{
		//this function searches a dictionary in the current subtree
		//returns null if dictionary not found
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
		BSTree it = this;
		if(d.key < it.key){
			//it has a key more than or equal to the required key
			//check if the left subtree has one of the required keys or not
			if(it.left == null){
				//there is no left subtree
				return null; //no element has key matching d.key
			}
			return it.left.search(d); //if left subtree exists, search for the key there
		}
		else if(d.key > it.key){
			//the key of it is smaller than the key needed, search in the right subtree
			if(it.right == null){
				return null; //if no right subtree, it has the largest key, not matching d
			}
			return it.right.search(d); //search for the key in the right subtree if it exists
		}
		else{
			if(it.key == d.key && it.address == d.address && it.size == d.size){
				return it; //it is the required element
			}
			else{
				if(it.left != null){
					BSTree it_l = it.left.search(d); //search in the left subtree
					if(it_l != null){
						return it_l; //it_l is the required element if found
					}
				}
				//the left subtree has no match for data, go for the right subtree
				if(it.right != null){
					BSTree it_r = it.right.search(d); //search in the right subtree
					if(it_r != null){
						return it_r; //it_r is the required element if found
					}
				}
				return null; //if the search yields no output in left or right subtree, the key does not exist and null is returned
			}
		}
	}
	
	private void DelNode(BSTree Node){
		//deletes the node Node from the given BSTree
		//the reference to the node Node is known
		/*Time Complexity: O(h), h = height of binary search Tree
		Space Complexity: O(1)*/
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
			return;
		}
		else if(it.left == null){
			//only right child of it exists
			if(it.parent.right == it){
				it.parent.right = it.right; //shift right child of parent to its only child
			}
			else{
				it.parent.left = it.right; //shift left child of parent to its only child
			}
			it.right.parent = it.parent; //shift parent of child to own parent
			it.parent = null;
			it.right = null;
			return;
		}
		else if(it.right == null){
			//only left child of it exists
			if(it.parent.right == it){
				it.parent.right = it.left; //shift right child of parent to its only child
			}
			else{
				it.parent.left = it.left; //shift left child of parent to its only child
			}
			it.left.parent = it.parent; //shift parent of child to own parent
			it.parent = null;
			it.left = null;
			return;
		}
		else{
			BSTree it1 = it.getNext(); //move to the successor of it
			int key1 = it1.key;
			int address1 = it1.address;
			int size1 = it1.size; //copy the contents of it in Node
			this.DelNode(it1); //Delete it from the tree
			Node.key = key1;
			Node.address = address1;
			Node.size = size1; //copy the contents of it in Node
			return;
		}
	}
	
	private boolean checkloop(HashSet<BSTree> h){
		/*Time Complexity: O(n), n = number of nodes
		Space Complexity: O(1)*/
		if(h.contains(this)){
			return false; //the given element exists before insertion thus loop found
		}
		if(this.parent.left != this && this.parent.right != this){
			return false; //loop invariant it.parent.right or left = it fails
		}
		h.add(this);
		if(this.left == null){
			if(this.right == null){
				return true; //this is a leaf
			}
			if(this.right.parent != this){
				return false; //invariant x.right.parent = x fails
			}
			return this.right.checkloop(h); //check whether loop exists in left tree
		}
		if(this.left.parent != this){
			return false; //invariant x.left.parent = x fails
		}
		if(this.right == null){
			return this.left.checkloop(h); //check if loop exists in left tree
		}
		if(this.right.parent != this){
			return false; //invariant x.right.parent = x fails
		}
		return (this.right.checkloop(h) && this.left.checkloop(h)); //check if both left and right subtree include no loop
	}
}
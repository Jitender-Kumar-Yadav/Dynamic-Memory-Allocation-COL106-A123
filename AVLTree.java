// Class: Height balanced AVL Tree
// Binary Search Tree
import java.lang.Math;

public class AVLTree extends BSTree {
    
    private AVLTree left, right;     // Children. 
    private AVLTree parent;          // Parent pointer. 
    private int height;  // The height of the subtree
        
    public AVLTree() { 
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        
    }

    public AVLTree(int address, int size, int key) { 
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions. 
    // Some of the functions may be directly inherited from the BSTree class and nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.
    
    public AVLTree Insert(int address, int size, int key) 
    { 
        /*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree newNode = new AVLTree(address, size, key); //store the data to be inserted in a new node
		AVLTree last;
		if(this.parent == null){
			//this block is executed if the current node is a sentinel
			if(this.right == null){
				//if the sentinel has no right child, it is the only node that us the tree is empty
				this.right = newNode; //set the node to be inserted as the right child and hence the root
				newNode.parent = this; //set the sentinel as the parent of the newNode
				last = newNode;
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
					last = newNode;
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
					last = newNode;
				}
				else{
					//this block is executed if the right subtree is not empty
					//insert the node in the right subtree
					return this.right.Insert(address, size, key);
				}
			}
		}
		Rebalance_I(last); //rebalance the tree starting at last
		return last; //Return the last inserted node
    }

    public boolean Delete(Dictionary d)
    {
        /*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		if(d.key == -1 && d.address == -1 && d.size == -1){
			return false;
			//removal of sentinel node is not permitted
		}
		AVLTree it = this.search(d); //search the dictionary d and store in it
		if(it != null){
			this.DelNode(it);
			return true;
		}
		//if it is null, it implies that d did not exist in AVLTree and hence false is returned
        return false;
    }
        
    public AVLTree Find(int k, boolean exact)
    { 
        /*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree it = this.getRoot(); //the root is called
		if(it != null){
			//if the root element is not null
			it = it.Find_current(k, exact); //find the element with given key conditions and return the smallest
		}
		return it;
		//it is null if tree is empty or the element is not found
		//else returns the least element with given key, i.e. it
    }

    public AVLTree getFirst()
    { 
        /*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree it = this;
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
		//if it.getRoot() was null, AVLTree would be empty and it == null
		//else it would be the leftmost element and hence the smallest
    }

    public AVLTree getNext()
    {
        /*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree it = this;
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
        return super.sanity();
    }
	
	//The following are helper functions and will be kept private
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
	
	private AVLTree getRoot(){
		//this function returns the root of the AVL tree
		//if the tree is empty, null is returned
		/*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree it = this;
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
	
	private AVLTree Find_current(int key, boolean exact)
	{
		//this function searches the AVLTree from current node
		//all the semantics are same as the Find function
		//except that the find does a search starting from the root Node
		//while Find_current searches it from the current node in the current subtree
		/*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree it = this;
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
			AVLTree it2 = it.left.Find_current(key, exact); //if left subtree exists, search for the key there
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
	
	private AVLTree search(Dictionary d)
	{
		//this function searches a dictionary in the current subtree
		//returns null if dictionary not found
		/*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree it = this;
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
					AVLTree it_l = it.left.search(d); //search in the left subtree
					if(it_l != null){
						return it_l; //it_l is the required element if found
					}
				}
				//the left subtree has no match for data, go for the right subtree
				if(it.right != null){
					AVLTree it_r = it.right.search(d); //search in the right subtree
					if(it_r != null){
						return it_r; //it_r is the required element if found
					}
				}
				return null; //if the search yields no output in left or right subtree, the key does not exist and null is returned
			}
		}
	}
	
	private void DelNode(AVLTree Node){
		//deletes the node Node from the given AVLTree
		//the reference to the node Node is known
		/*Time Complexity: O(h), h = height of AVL Tree
		Space Complexity: O(1)*/
		AVLTree it = Node;
		AVLTree last;
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
			last = it.parent; //store parent of deleted node in last
			it.parent = null;
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
			last = it.right; //store right child of deleted node in last
			it.parent = null;
			it.right = null;
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
			last = it.left; //store left child of deleted node in last
			it.parent = null;
			it.left = null;
		}
		else{
			AVLTree it1 = it.getNext(); //move to the successor of it
			int key1 = it1.key;
			int address1 = it1.address;
			int size1 = it1.size; //copy the contents of it in Node
			this.DelNode(it1); //Delete it from the tree
			Node.key = key1;
			Node.address = address1;
			Node.size = size1; //copy the contents of it in Node
			return;
		}
		Rebalance_D(last); //rebalance the tree starting at last
		return;
	}
	
	private void Rebalance_I(AVLTree T){
		//rebalances the AVLTree at node T after Insertion
		//traces the tree to root till it is balanced
		AVLTree it = T; //define iterator
		AVLTree it_c, it_g;
		it_g = it;
		it_g.height = it_g.balanced();
		if(it_g.parent == null){
			return;
		}
		it_c = it_g.parent;
		if(it_c.parent == null){
			return;
		}
		it_c.height = it_c.balanced();
		it = it_c.parent; //no imbalance possible till height 2 appears
		if(it.parent == null){
			return;
		}
		AVLTree curr = null;
		while(it.parent != null){
			//continue the loop till it reaches the sentinel node
			if(it.balanced() == -1){
				if(it_c == it.left){
					if(it_g == it_c.left){
						//Case Left Left
						RightRotate(it);
						curr = it_g;
						it_g.height = it_g.balanced();
						it.height = it.balanced();
						it_c.height = it_c.balanced();
					}
					else{
						//Case Left Right
						LeftRotate(it_c);
						RightRotate(it);
						curr = it;
						it.height = it.balanced();
						it_c.height = it_c.balanced();
						it_g.height = it_g.balanced();
					}
				}
				else{
					if(it_g == it_c.left){
						//Case Right Left
						RightRotate(it_c);
						LeftRotate(it);
						curr = it;
						it.height = it.balanced();
						it_c.height = it_c.balanced();
						it_g.height = it_g.balanced();
					}
					else{
						//Case Right Right
						LeftRotate(it);
						curr = it_g;
						it_g.height = it_g.balanced();
						it.height = it.balanced();
						it_c.height = it_c.balanced();
					}
				}
			}
			else{
				it.height = it.balanced();
				curr = it_c;
			}
			it_g = curr;
			it_c = it_g.parent;
			it = it_c.parent; //move to the top
		}
	}
	
	private void Rebalance_D(AVLTree T){
		//rebalances the AVLTree at node T after Deletion
		//traces the tree to root till it is balanced
		AVLTree it = T; //define iterator
		AVLTree it_c, it_g;
		it.height = it.balanced();
		if(it.parent == null){
			return;
		}
		it = it.parent;
		if(it.parent == null){
			return;
		}
		it.height = it.balanced();
		it = it.parent; //no imbalance possible till height 2 appears
		if(it.parent == null){
			return;
		}
		it_c = it.Hlargechild();
		it_g = it_c.Hlargechild(); //no imbalance possible till height 2 appears
		AVLTree curr = null;
		while(it.parent != null){
			//continue the loop till it reaches the sentinel node
			if(it.balanced() == -1){
				if(it_c == it.left){
					if(it_g == it_c.left){
						//Case Left Left
						RightRotate(it);
						curr = it_c.parent;
						it_g.height = it_g.balanced();
						it.height = it.balanced();
						it_c.height = it_c.balanced();
					}
					else{
						//Case Left Right
						LeftRotate(it_c);
						RightRotate(it);
						curr = it_g.parent;
						it.height = it.balanced();
						it_c.height = it_c.balanced();
						it_g.height = it_g.balanced();
					}
				}
				else{
					if(it_g == it_c.left){
						//Case Right Left
						RightRotate(it_c);
						LeftRotate(it);
						curr = it_g.parent;
						it.height = it.balanced();
						it_c.height = it_c.balanced();
						it_g.height = it_g.balanced();
					}
					else{
						//Case Right Right
						LeftRotate(it);
						curr = it_c.parent;
						it_g.height = it_g.balanced();
						it.height = it.balanced();
						it_c.height = it_c.balanced();
					}
				}
				it_g.height = it_g.balanced();
				it_c.height = it_c.balanced();
				it.height = it.balanced();
			}
			else{
				it.height = it.balanced();
				curr = it.parent;
			}
			it = curr;
			it_c = it.Hlargechild();
			it_g = it_c.Hlargechild(); //move to the top
		}
	}
	
	private void LeftRotate(AVLTree y){
		//causes a left rotation at node y
		//returns the topmost node
		AVLTree z = y.right; //get the right child of y, this becomes the new top node
		AVLTree Tmid = z.left; //the left child of z is detached from z and added to y
		if(y.parent.left == y){
			y.parent.left = z; //shift child of parent of y to z
		}
		else{
			y.parent.right = z; //shift child of parent of y to z
		}
		z.parent = y.parent; //shift parent of y to that of z
		y.right = Tmid;
		if(Tmid != null){
			Tmid.parent = y; //set Tmid as right child of y
		}
		z.left = y;
		y.parent = z; //set y as the left child of z
	}
	
	private void RightRotate(AVLTree z){
		//causes a right rotation at node z
		//returns the topmost node
		AVLTree y = z.left; //get the left child of z, this becomes the new top node
		AVLTree Tmid = y.right; //the right child of y is detached from y and added to z
		if(z.parent.left == z){
			z.parent.left = y; //shift child of parent of z to y
		}
		else{
			z.parent.right = y; //shift child of parent of z to y
		}
		y.parent = z.parent; //shift parent of y to that of z
		z.left = Tmid;
		if(Tmid != null){
			Tmid.parent = z; //set Tmid as left child of z
		}
		y.right = z;
		z.parent = y; //set z as right left child of y
	}
	
	private int balanced(){
		//this function returns -1 if this is not balanced
		//else it returns the height of this
		int l, r; //these represent the heights of left and right trees
		if(left == null){
			//if left child is null, set l (left height) to -1
			l = -1;
		}
		else{
			l = left.height;
		}
		if(right == null){
			//if right child is null, set r (right height) to -1
			r = -1;
		}
		else{
			r = right.height;
		}
		int diff = Math.abs(l - r); //store the difference of heights
		if(diff <= 1){
			return Math.max(l + 1, r + 1); //if the tree is balanced at this, return the height
		}
		return -1; //this is not balanced
	}
	
	private AVLTree Hlargechild(){
		//returns the child of this with larger height
		if(left == null){
			return right;
		}
		if(right == null){
			return left;
		}
		return (left.height >= right.height)? left : right; //return larger height node if both not null
	}
}
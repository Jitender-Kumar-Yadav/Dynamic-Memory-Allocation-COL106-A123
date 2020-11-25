// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.

public class A2DynamicMem extends A1DynamicMem {
      
    public A2DynamicMem() {  super(); }

    public A2DynamicMem(int size) { super(size); }

    public A2DynamicMem(int size, int dict_type) { super(size, dict_type); }

    // In A2, you need to test your implementation using BSTrees and AVLTrees. 
    // No changes should be required in the A1DynamicMem functions. 
    // They should work seamlessly with the newly supplied implementation of BSTrees and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test using BSTrees and AVLTrees. 
    //Your BST (and AVL tree) implementations should obey the property that keys in the left subtree <= root.key < keys in the right subtree. How is this total order between blocks defined? It shouldn't be a problem when using key=address since those are unique (this is an important invariant for the entire assignment123 module). When using key=size, use address to break ties i.e. if there are multiple blocks of the same size, order them by address. Now think outside the scope of the allocation problem and think of handling tiebreaking in blocks, in case key is neither of the two. 
    public void Defragment() {
		Dictionary alt;
		alt = new BSTree(); //initialise BST if type = 2
		if(this.type == 3){
			alt = new AVLTree(); //initialise AVL if type = 3
		}
		Dictionary it = freeBlk.getFirst(); //start at the first element of freeBlk
		while(it != null){
			alt.Insert(it.address, it.size, it.address); //insert the elements of it in alt with key = address
			it = it.getNext(); //traverse to the next element
			//this loop ends when end of the list freeBlk is reached
		}
		Dictionary Deleted = new A1List(-1, -1, -1); //an auxillary dictionary to contain the node to be deleted
		Dictionary current = alt.getFirst(); //current statys at some node of alt
		if(current == null){
			return; //if the first element of alt is null, the function returns without a change
		}
		Dictionary next = current.getNext(); //next stays at the next node of alt
		while(next != null){
			//traverse the list till the end is reached
			if(current.address + current.size == next.address){//check if current and next are contiguous
				//insert the merged node in freeBlk
				freeBlk.Insert(current.address, current.size + next.size, current.size + next.size);
				Deleted.key = current.size;
				Deleted.address = current.address;
				Deleted.size = current.size; //store the data of current in deleted with key as size
				freeBlk.Delete(Deleted); //delete this from freeBlk list
				Deleted.key = next.size;
				Deleted.address = next.address;
				Deleted.size = next.size; //store the data of next in deleted with key as size
				freeBlk.Delete(Deleted); //delete this from freeBlk list
				//modify current node and delete the 'next' node
				current.size = current.size + next.size;
				Deleted.key = next.address;
				Deleted.address = next.address;
				Deleted.size = next.size; //store the data of next in deleted with key as address
				alt.Delete(Deleted); //remove the node Deleted from alt
				next = current.getNext(); //move the next pointer to next element to current
			}
			else{
				current = next;
				next = next.getNext(); //in case they are not contiguous, move forward
			}
		}
		//the loop ends when next reaches the last and then a null pointer
    }
}
// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {
      
    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return ;
    }

    // In A1, you need to implement the Allocate and Free functions for the class A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only (A1List.java).
    // While inserting into the list, only call insert at the head of the list
    // Please note that ALL insertions in the DLL (used either in A1DynamicMem or used independently as the "dictionary" class implementation) are to be made at the HEAD (from the front).
    // Also, the find-first should start searching from the head (irrespective of the use for A1DynamicMem). Similar arguments will follow with regards to the ROOT in the case of trees (specifying this in case it was not so trivial to anyone of you earlier)
    public int Allocate(int blockSize)
	{
		//find the node in dictionary that has size atleast blockSize
		Dictionary free = freeBlk.Find(blockSize, false);
		if(blockSize > 0 && free != null){
			//this block runs if the dictionary free of suitable size is found
			int k = free.key;
			int ad = free.address;
			int s = free.size; //save the content in free to local variables
			//allocate the suitable space and insert a block in allocBlk list with key as the address to the block
			allocBlk.Insert(ad, blockSize, ad);
			//delete the node from freeBlk
			freeBlk.Delete(free);
			//add any unused space from free to the freeBlk list
			if(s > blockSize){
				freeBlk.Insert(ad + blockSize, s - blockSize, s - blockSize);
				//this insertion takes place when the searched block has a memory available more than what is required
				//the key of freeBlk is the size of the block
			}
			return ad;
			//since the search was successful return the starting address
		}
        return -1;
		//if free is not found or Allocate 0 is called, the allocate function fails
    } 
    // return 0 if successful, -1 otherwise
    public int Free(int startAddr)
	{
		//search the required block from allocated blocks with the given address
		Dictionary current = allocBlk.Find(startAddr, true);
		if(current != null){
			//this branch is executed if the Block to be freed exists in allocBlk
			//insert this block in FreeBlk after freeing the memory with the size of the block as key
			freeBlk.Insert(current.address, current.size, current.size);
			//delete this block from the allocBlk list
			allocBlk.Delete(current);
			return 0;
			//returns 0 as the Free operation was successful
		}
        return -1;
		//if the required block is not found in allocated Block list, the free function returns -1
    }
}
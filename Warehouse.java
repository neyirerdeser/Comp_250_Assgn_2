//package assignment2;

public class Warehouse{

	protected Shelf[] storage;
	protected int nbShelves;
	public Box toShip;
	public UrgentBox toShipUrgently;
	static String problem = "problem encountered while performing the operation";
	static String noProblem = "operation was successfully carried out";
	
	public Warehouse(int n, int[] heights, int[] lengths){
		this.nbShelves = n;
		this.storage = new Shelf[n];
		for (int i = 0; i < n; i++){
			this.storage[i]= new Shelf(heights[i], lengths[i]);
		}
		this.toShip = null;
		this.toShipUrgently = null;
	}
	
	public String printShipping(){
		Box b = toShip;
		String result = "not urgent : ";
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n" + "should be already gone : ";
		b = toShipUrgently;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}
	
 	public String print(){
 		String result = "";
		for (int i = 0; i < nbShelves; i++){
			result += i + "-th shelf " + storage[i].print();
		}
		return result;
	}
	
 	public void clear(){
 		toShip = null;
 		toShipUrgently = null;
 		for (int i = 0; i < nbShelves ; i++){
 			storage[i].clear();
 		}
 	}
 	
 	/**
 	 * initiate the merge sort algorithm
 	 */
	public void sort(){
		mergeSort(0, nbShelves -1);
	}
	
	/**
	 * performs the induction step of the merge sort algorithm
	 * @param start
	 * @param end
	 */
	protected void mergeSort(int start, int end){
		if(start<end) {
			int mid = (start+end)/2;
			mergeSort(start,mid);
			mergeSort(mid+1,end);
			merge(start,mid,end);
		}
	}
	
	/**
	 * performs the merge part of the merge sort algorithm
	 * @param start
	 * @param mid
	 * @param end
	 */
	protected void merge(int start, int mid, int end){     // if {0 1 2 3 4 5 6}
			int n1 = mid-start+1;                              // L = 0 1 2 3
			int n2 = end-mid;                                  // R = 4 5 6
			Shelf[] L = new Shelf[n1+2];
			Shelf[] R = new Shelf[n2+2];
			for(int i=1; i<=n1; i++) L[i] = storage[start+i-1];
			for(int i=1; i<=n2; i++) R[i] = storage[mid+i];
			L[n1+1] = new Shelf(1001,5);
			R[n2+1] = new Shelf(1001,5);
			
			int i=1, j=1;                      // i for L, j for R
			for(int k=start; k<=end; k++)      // and k for merged array
				if(L[i].height <= R[j].height) {
					storage[k] = L[i];
					i++;
				} else {
					storage[k] = R[j];
					j++;
				}
		}
	
	/**
	 * Adds a box is the smallest possible shelf where there is room available.
	 * Here we assume that there is at least one shelf (i.e. nbShelves >0)
	 * @param b
	 * @return problem or noProblem
	 */
	public String addBox (Box b){
		int i=0;
		while(b.height>storage[i].height || storage[i].availableLength<b.length) {
			i++;
			if(i>=storage.length) return problem;
		}
		storage[i].addBox(b);
		return noProblem;
	}
	
	/**
	 * Adds a box to its corresponding shipping list and updates all the fields
	 * @param b
	 * @return problem or noProblem
	 */
	public String addToShip (Box b){
		/* checking whether it's an urgentBox or not:
		 * when an UgentBox is printed the adress will be like, UrgentBox@xxxx
		 * otherwise it will be like , Box@xxxx
		 * this works this way even if the UrgentBox is declared as a Box using UrgentBox constructor
		 */
		String address = b.toString();
		if(address.charAt(0) == 'U') {
			// ADD TO THE URGENT LIST
			// if the list is empty
			if(toShipUrgently==null) toShipUrgently = (UrgentBox)b;
			// if tehre's only one box on it
			else if(toShipUrgently.next==null) {
				UrgentBox moveBack = toShipUrgently;
				toShipUrgently = (UrgentBox)b;
				toShipUrgently.next = moveBack;
			// if there's at least two boxes on it
			} else {
				// we need to go from back to forward and move everything one step back
				// ill add the box to the end of the list and swap the way up
				// note: declaring these as Box to avoid Box-UrgentBox complication
				Box moveBack = toShipUrgently;
				Box oneBefore = toShipUrgently;
				int counter = 0;
				while(moveBack!=null) {
					counter++;
					moveBack = moveBack.next;
				}
				// add to the list
				moveBack = toShipUrgently;
				for(int i=0; i<counter; i++) moveBack = moveBack.next;
				moveBack.next = b;
				// move it up
				moveBack = toShipUrgently;
				while(moveBack!=null) {
					for(int i=0; i<counter; i++) moveBack = moveBack.next;
					for(int i=0; i<counter-1; i++) oneBefore = oneBefore.next;
					oneBefore.next = b;
					b.previous = oneBefore;
					moveBack.previous = b;
					counter--;
				}
			}
			return noProblem;
		} else if(address.charAt(0) == 'B'){
			// ADD TO THE NORMAL LIST
			// if the list is empty
			if(toShip==null) toShip = b;
			// if tehre's only one box on it
			else if(toShip.next==null) {
				Box moveBack = toShip;
				toShip = b;
				toShip.next = moveBack;
			// if there's at least two boxes on it
			} else {
				// we need to go from back to forward and move everything one step back
				// ill add the box to the end of the list and swap the way up
				Box moveBack = toShip;
				Box oneBefore = toShip;
				int counter = 0;
				while(moveBack!=null) {
					counter++;
					moveBack = moveBack.next;
				}
				// add to the list
				moveBack = toShip;
				for(int i=0; i<counter; i++) moveBack = moveBack.next;
				moveBack.next = b;
				// move it up
				moveBack = toShip;
				while(moveBack!=null) {
					for(int i=0; i<counter; i++) moveBack = moveBack.next;
					for(int i=0; i<counter-1; i++) oneBefore = oneBefore.next;
					oneBefore.next = b;
					b.previous = oneBefore;
					moveBack.previous = b;
					counter--;
				}
			}
			return noProblem;
		} else return problem;
	}

	/**
	 * Find a box with the identifier (if it exists)
	 * Remove the box from its corresponding shelf
	 * Add it to its corresponding shipping list
	 * @param identifier
	 * @return problem or noProblem
	 */
	public String shipBox (String identifier){
		for(int i=0; i<storage.length; i++) {
			Shelf inSearch = storage[i];
			Box b = storage[i].removeBox(identifier);
			if(b!=null) {
				addToShip(b);
				return noProblem;
			}
		}
		return problem;
	}
	
	/**
	 * if there is a better shelf for the box, moves the box to the optimal shelf.
	 * If there are none, do not do anything
	 * @param b
	 * @param position
	 */
	public void moveOneBox (Box b, int position){
		// it will be as if you're actually picking the box up, therefore removing from the shelf
		// then placing it back again, finding the perfect place
		storage[position].removeBox(b.id);
		addBox(b);
	}
	
	/**
	 * reorganize the entire warehouse : start with smaller shelves and first box on each shelf.
	 */
	public void reorganize (){
		/* the challenge with this method was to properly go through each box on a shelf even after a box is moved
		 * moving boxes changes the next previous system that we use to navigate inbetween the boxes
		 * my solution to this was to add the Strings both of which containing the placement information of the warehouse
		 * one before a box is moved and one after all bozxes on ashelf is moved (theoretically)
		 * if there's a difference between these String, it means that a box might have been skipped due to the problem I worte about
		 * so the same shelf goes through another reorganization, until no change is needed.
		 */
		for(int i=0; i<storage.length; i++) {
			Box toMove = storage[i].firstBox;
			String beforeMove = print();	
			while(toMove!=null) {
				moveOneBox(toMove,i);
				toMove = toMove.next;
			}
			String afterMove = print();
			if(!beforeMove.equals(afterMove))
				i--; // therefore this same for itearation will repeat
		}
	}

}
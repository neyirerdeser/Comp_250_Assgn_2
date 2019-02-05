//package assignment2;

public class Shelf {
	
	protected int height;
	protected int availableLength;
	protected int totalLength;
	protected Box firstBox;
	protected Box lastBox;

	public Shelf(int height, int totalLength){
		this.height = height;
		this.availableLength = totalLength;
		this.totalLength = totalLength;
		this.firstBox = null;
		this.lastBox = null;
	}
	
	protected void clear(){
		availableLength = totalLength;
		firstBox = null;
		lastBox = null;
	}
	
	public String print(){
		String result = "( " + height + " - " + availableLength + " ) : ";
		Box b = firstBox;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}
	
	/**
	 * Adds a box on the shelf. Here we assume that the box fits in height and length on the shelf.
	 * @param b
	 */
	public void addBox(Box b){
		if (firstBox == null) { 
			firstBox = lastBox = b;
		} else if (firstBox == lastBox) {
			firstBox.next = b;
			b.previous = firstBox;
			lastBox = b;
		} else {
			lastBox.next = b;
			b.previous = lastBox;
			lastBox = b;
		}
		availableLength -= b.length;
	}
	
	/**
	 * If the box with the identifier is on the shelf, remove the box from the shelf and return that box.
	 * If not, do not do anything to the Shelf and return null.
	 * @param identifier
	 * @return
	 */
	public Box removeBox(String identifier){
		Box b = firstBox;
		while(b!=null) {
			if(b.id.equals(identifier)) {
				if(firstBox==lastBox) {
					firstBox = lastBox = null;
				} else if(b==firstBox) {
					firstBox = b.next;
					firstBox.previous = null;
				} else if(b==lastBox) {
					lastBox = b.previous;
					lastBox.next = null;
				} else {
					Box prev = b.previous;
					Box next = b.next;
					prev.next = next;
					next.previous = prev;
				}
				b.next = null;
				b.previous = null;
				availableLength += b.length;
				return b;
			}
			b = b.next;
		}
		return null;
	}
	
}

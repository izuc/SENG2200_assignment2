import java.util.Iterator;
import java.lang.Iterable;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/*
* The LinkedList<E> is a generic class that contains its own Iterator<E> and implements Iterable<E> so it can be used within an 
* enhanced for loop. It contains the ability to: add nodes to the first & last positions; to insert a node directly after
* another; to remove both the head & tail nodes; to clear the list completely; to iterate the list using next; and to remove
* a specified node. 
* @author Lance Baker (c3128034).
*/
public class LinkedList<E> implements Iterable<E> {
	private static final String NO_MORE_ELEMENTS = "No more elements";
	private static final String NO_ELEMENT_TO_REMOVE = "No element to remove";
	
	private Node<E> head; // The head sentinel.
	private Node<E> tail; // The tail sentinel.
	private int size; // The size of the list.
	private int modificationCount; // Modification count in the list.
	
	public LinkedList() {
		this.head = new Node<E>(); 
		this.tail = new Node<E>();
		this.head.setNext(this.tail); // Sets the next to the tail.
		this.tail.setPrevious(this.head); // Sets the tail previous to the head.
		this.size = 0; // Size is zero.
	}
	
	/*
	* The append method accepts an object and will add it as the last node in the list. 
	*/
	public void append(E data) {
		Node<E> node = new Node<E>(data);
		// Getting the previous node of the tail, and setting the next node to the newly created node.
		this.tail.getPrevious().setNext(node);
		// Setting the previous to the tail's previous node.
		node.setPrevious(this.tail.getPrevious());
		// Setting the next to the tail.
		node.setNext(this.tail);
		// Setting the tail's previous to the newly created node.
		this.tail.setPrevious(node);
		this.size++;
		this.modificationCount++;
	}
	
	/*
	* The prepend method accepts an object and will add it as the first node in the list.
	*/
	public void prepend(E data) {
		Node<E> node = new Node<E>(data); // Instantiates a new Node containing the data.
		// Setting the previous to the head.
		node.setPrevious(this.head);
		// Setting the node's next to the next node of the head.
		node.setNext(this.head.getNext());
		// Setting the next node of the head's previous to be the node.
		this.head.getNext().setPrevious(node);
		// Setting the next node of the head to the node.
		this.head.setNext(node);
		this.size++; // Increments size.
		this.modificationCount++;
	}
	
	/*
	* The insert method is used to directly insert a object in the list after the specified previous node.
	*/
	public void insert(Node<E> previous, E data) {
		// If the previous is not the tail node. This check ensures that there is no 
		// data added after the last sentinel by mistake.
		if (previous != this.tail) {
			Node<E> node = new Node<E>(data); // Instantiates a new Node containing the data.
			node.setNext(previous.getNext()); // Sets the next node to be the previous node's next.
			node.setPrevious(previous); // Sets node's previous to the previous.
			node.getNext().setPrevious(node); // Sets the next node's previous to the node.
			previous.setNext(node); // Sets the previous's next node to the node.
			this.size++; // Increments the size.
			this.modificationCount++;
		} else {
			// Otherwise, if the previous is the tail node it will
			// then just append the data to the list.
			this.append(data);
		}
	}
	
	/*
	* The removeFirst method chops the first node off the list.
	*/
	public void removeFirst() {
		if (!this.isEmpty()) { // Checks if the list is not empty.
			// Sets the next next element of the head's previous to be just point to the head.
			this.head.getNext().getNext().setPrevious(this.head);
			// Sets the heads next element to be pointing to the head's next next.
			this.head.setNext(this.head.getNext().getNext());
			this.size--; // Decrements the size.
			this.modificationCount++;
		} else {
			// If it is empty, it will throw a ListEmptyException.
			throw new ListEmptyException();
		}
	}
	
	/*
	* The removeLast method chops the last node off the list.
	*/
	public void removeLast() {
		if (!this.isEmpty()) { // Checks if the list is not empty.
			// Sets the tail's previous to point to the tail's previous previous.
			this.tail.setPrevious(this.tail.getPrevious().getPrevious());
			// Sets the tail's previous's next to point to the tail.
			this.tail.getPrevious().setNext(this.tail);
			this.size--; // Decrements the size.
			this.modificationCount++;
		} else {
			// If it is empty, it will throw a ListEmptyException.
			throw new ListEmptyException();
		}
	}
	
	/*
	* The clear method iterates until the list is empty, chopping the nodes off the head one by one.
	*/
	public void clear() {
		while (!this.isEmpty()) { // Iterates until the list is empty.
			this.removeFirst(); // Removes the head.
		}
	}
	
	/*
	* A getter used to retrieve the size of the list.
	*/
	public int getSize() {
		return this.size; // Returns the size.
	}
	
	/*
	* The isEmpty method checks whether the list is empty. Returns true if there
	* are no elements, false otherwise.
	*/
	public boolean isEmpty() {
		// Checks if the head's next is pointing to the tail. If so,
		// then the list is empty.
		return (this.head.getNext() == this.tail); 
	}
	
	/*
	* The getter for retrieving the head sentinel node.
	*/
	public Node<E> getHead() {
		return this.head; // Returns the head sentinel node.
	}
	
	/*
	* The getter for retrieving the tail sentinel node.
	*/
	public Node<E> getTail() {
		return this.tail; // Returns the tail sentinel node.
	}
	
	/*
	* The remove method receives the Node<E> to be removed.
	*/
	protected void remove(Node<E> node) {
		// Checks to ensure the list is not empty.
		if (!this.isEmpty()) {
			// Checks to ensure that the received node is not a sentinel node.
			if ((node != this.head) && (node != this.tail)) {
				// Declares the next, which points to the current's next node.
				Node<E> next = node.getNext();
				// Declares the previous, which points to the current's previous node.
				Node<E> previous = node.getPrevious();
				// Sets the next node's previous to the previous.
				next.setPrevious(previous);
				// Sets the previous node's next to the next.
				previous.setNext(next);
				this.size--; // Decrements the size.
				this.modificationCount++;
			} else {
				// If it is a sentinel, it will throw an exception stating the current node cannot be removed.
				throw new IllegalStateException(NO_ELEMENT_TO_REMOVE);
			}
		} else {
			// If it is empty, it will throw a ListEmptyException.
			throw new ListEmptyException();
		}
	}
	
	/*
	* A required method for the Iterable interface. 
	* It will return a instantiated LinkedListIterator object as a Iterator<E>.
	*/
	public Iterator<E> iterator() {
		return new LinkedListIterator(this.getModificationCount()); 
	}
	
	/*
	* A getter for the modification count.
	*/
	public int getModificationCount() {
		return this.modificationCount;
	}
	
	/*
	* The LinkedListIterator is a private inner class that implements the Iterator<E> interface. It 
	* interacts directly with the instance data belonging to the LinkedList<E>, which will allow for
	* it to traverse the LinkedList using the hasNext() and next() methods. It also contains a remove() method
	* which will remove the current object being iterated from the LinkedList<E>.
	*/
	private class LinkedListIterator implements Iterator<E> {
		private static final String MODIFICATION_ERROR = "Concurrent modification detected";
		private Node<E> current; // The current Node<E> position.
		private int modificationCount; // The expected mod count of the linked list.
		private boolean nextCalled; // A boolean to keep track of when the next() is called.
		
		public LinkedListIterator(int modificationCount) {
			// Sets the current position to be the head sentinel node.
			this.current = LinkedList.this.getHead();
			// Sets the mod count to the mod count of the linked list.
			this.modificationCount = modificationCount;
		}
		/*
		* A required method for the Iterator interface. It will check if there is a next node.
		*/
		public boolean hasNext() {
			// If the current's next is not the tail sentinel node.
			return (this.current.getNext() != LinkedList.this.getTail());
		}
		
		/*
		* A required method for the Iterator interface. 
		* If it has a next node, it will grab the current nodes next and place the reference in the current node.
		* Otherwise, if there isn't a next it will throw an exception indicating there are no more elements.
		* @return - The object data.
		*/
		public E next() {
			// Checks if there is a next.
			if (this.hasNext()) {
				// If there haven't been any changes.
				if (this.modificationCount == LinkedList.this.getModificationCount()) {
					// If there is, it will set the current to the currents next node.
					this.current = this.current.getNext();
					this.nextCalled = true; // Sets the next call boolean to true; allowing for the current node to be removed.
				} else {
					// Otherwise, it will throw an exception indicating that the list has been changed.
					throw new ConcurrentModificationException(MODIFICATION_ERROR);
				}
			} else {
				// Throws an Exception indicating there are no more elements.
				throw new NoSuchElementException(NO_MORE_ELEMENTS);
			}
			// Returns the current node's data.
			return this.current.getData();
		}
		
		/*
		* The remove method removes the current node from the list. If the current node
		* is one of the sentinel nodes, it will then throw an exception indicating that there 
		* is no element to be removed.
		*/
		public void remove() {
			// If the next method has been invoked.
			if (this.nextCalled) {
				// And there hasn't been any changes to the list
				if (this.modificationCount == LinkedList.this.getModificationCount()) {
					// Then the current node can be removed.
					LinkedList.this.remove(this.current);
					// Increments the mod count to keep it identical to the linke list.
					this.modificationCount++;
					// Sets the next called boolean to false, which will only allow this method
					// to be reinvoked once the next has been called again.
					this.nextCalled = false;
					// Sets the data reference of the current node to null, which will free
					// up any memory.
					this.current.setData(null);
				} else {
					// Throws the exception indicating the list has been concurrently modified.
					throw new ConcurrentModificationException(MODIFICATION_ERROR);
				}
			} else {
				// The next wasn't called, therefore there is no element to remove.
				throw new IllegalStateException(NO_ELEMENT_TO_REMOVE);
			}
		}
	}
}

/*
* The ListEmptyException class extends the IllegalStateException. It is used to 
* throw an Exception when the LinkedList is empty.
*/
class ListEmptyException extends IllegalStateException {
	private static final String LIST_EMPTY = "The list is empty.";
	
	public ListEmptyException() {
		super(LIST_EMPTY); // Invokes the super contructor, passing the message.
	}
}
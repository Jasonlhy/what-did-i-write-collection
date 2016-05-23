/**
 * Title:         ListStack.java
 * Description:   A Stack class by composition using LinkedList object
 * Company:       ICT HKIVE(TY)
 * @author        Patrick Tong
 * modified by Liu Ho Yin
 */

import java.util.EmptyStackException;

public class ListStackComp  implements java.io.Serializable{

	private LinkedList list;	// compose with a LinkedList object

    public ListStackComp() {
		list = new LinkedList();
    }

	public boolean empty() {
		return list.isEmpty();
	}

	public Object push(Object item) {
		list.addToHead(item);
		return item;
	}

	public Object pop() {
		if (empty()) throw new EmptyStackException();
		return list.removeFromHead();
	}

	public Object peek() {
		if (empty()) throw new EmptyStackException();
		return list.get(1);
	}

	public int search(Object item) {

		for (int i = 1; i <= list.count(); i++)
			if (item.equals(list.get(i))) return i;
		return -1;

	}

	// Provide also the toString() method (for TestStack.java)
	public String toString() {
		return list.toString();
	}
	public int size(){
		return list.count();
		
	}

}

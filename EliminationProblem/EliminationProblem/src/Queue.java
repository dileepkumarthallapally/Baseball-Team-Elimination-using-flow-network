import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> {
    private int no_of_ele;               // number of elements on queue
    private Node<Item> start;    // beginning of queue
    private Node<Item> end;     // end of queue

    
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

       public Queue() {
        start = null;
        end  = null;
        no_of_ele = 0;
    }

   
    public boolean isEmpty() {
        return start == null;
    }

   
    public int size() {
        return no_of_ele;     
    }

   
    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return start.item;
    }

    
    public void enqueue(Item item) {
        Node<Item> old = end;
        end = new Node<Item>();
        end.item = item;
        end.next = null;
        if (isEmpty()) start = end;
        else           old.next = end;
        no_of_ele++;
    }

    
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = start.item;
        start = start.next;
        no_of_ele--;
        if (isEmpty()) end = null;   // to avoid loitering
        return item;
    }

   
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    } 

   
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(start);  
    }

    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> start) {
            current = start;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }


}

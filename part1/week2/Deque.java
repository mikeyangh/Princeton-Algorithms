import java.util.Iterator;
public class Deque<Item> implements Iterable<Item> 
{
    private Node first;
    private Node last;
    private int size;
    
    private class Node
    {
        Item item;
        Node left;
        Node right;
    }
   // construct an empty deque
   public Deque()
   {
       first = null;
       last = null;
       size = 0;
   }
   // is the deque empty?
   public boolean isEmpty()  
   {
       return size == 0;
   }
   // return the number of items on the deque   
   public int size()     
   {
       return size;
   }
   // insert the item at the front    
   public void addFirst(Item item)
   {
       if (item == null)
       {
           throw new java.lang.NullPointerException("item is null!"); 
       }
       Node oldfirst = first;
       first = new Node();
       first.item = item;
       first.right = oldfirst;
       first.left = null;
       if (oldfirst != null)
       {
           oldfirst.left = first;
       }
       size++;
       if (size == 1)
       {
           last = first;
       }
   }
   // insert the item at the end
   public void addLast(Item item)
   {
       if (item == null)
       {
           throw new java.lang.NullPointerException("item is null!"); 
       }
       Node oldlast = last;
       last = new Node();
       last.item = item;
       if (oldlast != null)
       {
           oldlast.right = last;
       }
       last.left = oldlast;
       last.right = null;
       size++;
       if (size == 1)
       {
           first = last;
       }
   }
   // delete and return the item at the front
   public Item removeFirst()
   {
       if (isEmpty())
       {
           throw new java.util.NoSuchElementException("No element!");
       }
       Item item = first.item;
       first = first.right;
       if (first != null)
       {    
           first.left = null;
       }
       size--;
       if (size == 0)
       {
           last = null;
       }
       return item;
   }
   // delete and return the item at the end    
   public Item removeLast()
   {
       if (isEmpty())
       {
           throw new java.util.NoSuchElementException("No element!");
       }
       Item item = last.item;
       last = last.left;
       if (last != null)
       {
           last.right = null;
       }
       size--;
       if (size == 0)
       {
           first = null;
       }
       return item;
   }
   // return an iterator over items in order from front to end
   public Iterator<Item> iterator()
   {
       return new DoubleListIterator();
   }
   private class DoubleListIterator implements Iterator<Item>
   {
       private Node current = first;
       public boolean hasNext()
       {
           return current != null;
       }
       public void remove()
       {
           throw new java.lang.UnsupportedOperationException("Not supported");
       }
       public Item next()
       {
           if (current == null)
           {
               throw new java.util.NoSuchElementException("No element!");
           }
           Item item = current.item;
           current = current.right;
           return item;
       }
   }
   // unit testing
   public static void main(String[] args)
   {
       Deque<Integer> intDeque = new Deque<Integer>();
       intDeque.addFirst(12);
       intDeque.addFirst(23);
       intDeque.addLast(88);
       intDeque.removeFirst();
       intDeque.removeLast();
       //intDeque.removeLast();
       //intDeque.removeFirst();
       for (Integer x : intDeque)
           StdOut.println(x);
   }
}
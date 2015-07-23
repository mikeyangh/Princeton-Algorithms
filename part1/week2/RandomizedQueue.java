import java.util.Iterator;
public class RandomizedQueue<Item> implements Iterable<Item>
{
   private Item[] q;
   private int size;
   // construct an empty randomized queue
   public RandomizedQueue()     
   {
       q = (Item[]) new Object[2];
       size = 0;
   //    first = 0;
   }
   // is the queue empty?
   public boolean isEmpty()
   {
       return size == 0;
   }
   // return the number of items on the queue
   public int size() 
   {
       return size;
   }
   // resize the underlying array
    private void resize(int max) {
        assert max >= size;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < size; i++) 
        {
            temp[i] = q[i];
        }
        q = temp;
    }
   // add the item
   public void enqueue(Item item)
   {
       if (item == null)
       {
           throw new java.lang.NullPointerException("item is null!"); 
       }
       if (size == q.length)
       {
           resize(2*size);
       }
       q[size] = item;
       size++;
   }
   // delete and return a random item
   public Item dequeue()  
   {
       if (isEmpty())
       {
           throw new java.util.NoSuchElementException("No element!");
       }
       int index = StdRandom.uniform(size);
       Item item = q[index];
       q[index] = q[size-1];
       q[size-1] = null;
       size--;
       if (size > 0 && size == q.length/4)
       {
           resize(2*size);
       }
       return item;
   }
   // return (but do not delete) a random item
   public Item sample()
   {
       if (isEmpty())
       {
           throw new java.util.NoSuchElementException("No element!");
       }
       int index = StdRandom.uniform(size);
       Item item = q[index];
       return item;
   }

   
   // return an independent iterator over items in random order
   public Iterator<Item> iterator()
   {
       return new ArrayIterator();
   }

   
   // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> 
    {
        private int i = 0;
        private Item[] iterator;
        public ArrayIterator()
        {
            iterator = (Item[]) new Object[size];
            for (int i = 0; i < size;i++)
            {
                iterator[i] = q[i];
            }
            StdRandom.shuffle(iterator);
        }
        public boolean hasNext()  
        {
            return i < size;                  
        }
        public void remove()      { throw new java.lang.UnsupportedOperationException();  }

        public Item next() 
        {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = iterator[i];
            i++;
            return item;
        }
    }
   // unit testing
   public static void main(String[] args)
   {
       RandomizedQueue<Integer> intRQ = new RandomizedQueue<Integer>();
       intRQ.enqueue(5);
       intRQ.enqueue(6);
       intRQ.enqueue(7);
       intRQ.enqueue(8);
       intRQ.enqueue(9);
       for (Integer x : intRQ)
           StdOut.println(x);
       
       
   }
}
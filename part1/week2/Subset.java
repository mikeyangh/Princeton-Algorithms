public class Subset {
   public static void main(String[] args)
   {
       int k = Integer.parseInt(args[0]);
       RandomizedQueue<String> ycgRQ = new RandomizedQueue<String>();
       while (!StdIn.isEmpty())
       {
           ycgRQ.enqueue(StdIn.readString());
       }
       for (int i = 0; i < k; i++)
       {
           StdOut.println(ycgRQ.dequeue());
       }
   }
}
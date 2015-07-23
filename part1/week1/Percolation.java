public class Percolation 
{
   private int n;
   private boolean[] openArray;
   private WeightedQuickUnionUF ycgUF;   
   private WeightedQuickUnionUF backwashUF;
   // create N-by-N grid, with all sites blocked
   public Percolation(int N)              
   {
       if (N <= 0)
       {
           throw new IllegalArgumentException("Inlegal argument!!!");
       }
       n = N;
       ycgUF = new WeightedQuickUnionUF(N*N+2);
       backwashUF = new WeightedQuickUnionUF(N*N+2);
       
       for (int i = 1; i <= N; i++)
       {
             ycgUF.union(0, i);
             ycgUF.union(N*N+1, N*(N-1)+i);
             backwashUF.union(0, i);
       }
       openArray = new boolean[N*N+2];
       for (int i = 0; i < N*N+2; i++)
       {
           openArray[i] = false;
       }
   }
    // open site (row i, column j) if it is not open already
   public void open(int i, int j)         
   {
       if (i < 1 || i > n) 
           throw new IndexOutOfBoundsException("row out of bounds");
       if (j < 1 || j > n) 
           throw new IndexOutOfBoundsException("column out of bounds");
       int id = (i-1)*n+j;
       if (!isOpen(i, j))
       {
           openArray[(i-1)*n+j] = true;
       }
       if (i-1 > 0)  //up
       {
           if (isOpen(i-1, j))
           {
               ycgUF.union(id-n, id);
               backwashUF.union(id-n, id);
           }
       }
       if (i+1 <= n)
       {
           if (isOpen(i+1, j))
           {
               ycgUF.union(id+n, id);
               backwashUF.union(id+n, id);
           }
       }
       if (j-1 > 0)
       {
           if (isOpen(i, j-1))
           {
               ycgUF.union(id-1, id);
               backwashUF.union(id-1, id);
           }
       }
       if (j+1 <= n)
       {
           if (isOpen(i, j+1))
           {
               ycgUF.union(id+1, id);
               backwashUF.union(id+1, id);
           }
       }
   }
   // is site (row i, column j) open?
   public boolean isOpen(int i, int j)     
   {
       if (i < 1 || i > n) 
           throw new IndexOutOfBoundsException("row out of bounds");
       if (j < 1 || j > n) 
           throw new IndexOutOfBoundsException("column out of bounds");
       return openArray[(i-1)*n+j];
   }
    // is site (row i, column j) full?
   public boolean isFull(int i, int j)    
   {
       if (!isOpen(i, j))
       {
           return false;
       }
       else
       {
           if (i < 1 || i > n) 
               throw new IndexOutOfBoundsException("row out of bounds");
           if (j < 1 || j > n) 
               throw new IndexOutOfBoundsException("column out of bounds");
           //return (ycg_UF.find(0)==ycg_UF.find((i-1)*N+j));
           return backwashUF.connected(0, (i-1)*n+j);
       }
   }
     // does the system percolate?
   public boolean percolates()           
   {
       //return (ycg_UF.find(0)==ycg_UF.find((N*N+1));
       if (n == 1)
       {
           return this.isOpen(1, 1);
       }      
       else
           return ycgUF.connected(0, n*n+1);
           
   }
    // test client (optional)
   public static void main(String[] args)
   {
       
   }

}
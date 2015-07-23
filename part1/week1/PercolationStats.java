public class PercolationStats 
{
    private int n;
    private int t;
    private double[] fraction;
    //private int[] countStep;
    //private Percolation ycgPercolation;
    private double mean;
    private double deviation;
     // perform T independent experiments on an N-by-N grid
   public PercolationStats(int N, int T)   
   {
       if (N <= 0 || T <= 0)
       {
           throw new IllegalArgumentException("Illegal argument!!!");
       }
       n = N;
       t = T;
       fraction = new double[t];
       //countStep = new int[t];
       for (int i = 0; i < t; i++)
       {
           Percolation ycgPercolation = new Percolation(n);
           int countStep = 0;
           while (!ycgPercolation.percolates())
           {
               int row = StdRandom.uniform(n)+1;
               int col = StdRandom.uniform(n)+1;
               if (!ycgPercolation.isOpen(row, col))
               {
                   ycgPercolation.open(row, col);
                   countStep++;
                   //StdOut.println("!!!");
               }
           }
           fraction[i] = (double) countStep/(n*n);
       }

       mean = StdStats.mean(fraction);
       deviation = StdStats.stddev(fraction);
   }
   // sample mean of percolation threshold
   public double mean() 
   {
       return mean;
   }
    // sample standard deviation of percolation threshold
   public double stddev()     
   {
       return deviation;
   }
     // low  endpoint of 95% confidence interval
   public double confidenceLo()   
   {
       return (mean-1.96*deviation/Math.sqrt(t));
   }
    // high endpoint of 95% confidence interval
   public double confidenceHi()    
   {
       return (mean+1.96*deviation/Math.sqrt(t));
   }
   // test client (described below)
   public static void main(String[] args)   
   {
       int N, T;
       N =  Integer.parseInt(args[0]);
       T =  Integer.parseInt(args[1]);
       PercolationStats ycgPercolationStats = new PercolationStats(N, T);
        StdOut.println("mean = " + ycgPercolationStats.mean());  
        StdOut.println("stddev = " + ycgPercolationStats.stddev());  
        StdOut.println("95% confidence interval " 
                           + ycgPercolationStats.confidenceLo() + ", " 
                           + ycgPercolationStats.confidenceHi());  
    }  
}
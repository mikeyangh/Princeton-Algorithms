/**
 * Auto Generated Java Class.
 */
import java.util.*;
public class Brute 
{    
    /* ADD YOUR CODE HERE */
    public static void main(String[] args)
    {
        In input = new In(args[0]);
        
        int N = input.readInt();
        Point [] PointArray = new Point[N];  
        //StdOut.println(N);
        
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (int i = 0; i < N; i++)
        {
            int x = input.readInt();
            int y = input.readInt();
            //StdOut.println(x + "," + y);
            PointArray[i] = new Point(x, y);
            PointArray[i].draw();
            //StdOut.println(PointArray[i].toString());
        }
        Quick3way.sort(PointArray);
        //StdOut.println("=============================");
        for (int a = 0; a < N; a++)
        {
            for (int b = a+1; b < N; b++)
            {
                for (int c = b+1; c < N; c++)
                {
                    for (int d = c+1; d < N; d++)
                    {
                            if (PointArray[a].slopeTo(PointArray[b]) == PointArray[a].slopeTo(PointArray[c]) &&
                                PointArray[a].slopeTo(PointArray[b]) == PointArray[a].slopeTo(PointArray[d]))
                            {
                                //StdOut.println(PointArray[a].slopeTo(PointArray[b]) + " " + PointArray[a].slopeTo(PointArray[c]) + " " + PointArray[a].slopeTo(PointArray[d]));
                                StdOut.println(PointArray[a].toString() + " -> " + PointArray[b].toString() + " -> " 
                                                   + PointArray[c].toString() + " -> " + PointArray[d].toString());
                                PointArray[a].drawTo(PointArray[d]);
                            }
                    }
                }
            }
        }
        
    }
}

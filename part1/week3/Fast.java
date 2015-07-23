import java.util.*;

public class Fast 
{
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
        /*
        StdOut.println("=============================");
        Arrays.sort(PointArray, 0 ,N , PointArray[0].SLOPE_ORDER);
        for (int i = 0; i < N; i++)
        {
            StdOut.println(PointArray[i].toString() + PointArray[0].slopeTo(PointArray[i]));
        }*/
        
        for (int i = 0; i < N; i++)
        {
            Point[] TempArray = new Point[N];
            TempArray = (Point[]) PointArray.clone();
            Arrays.sort(TempArray, 0 ,N , PointArray[i].SLOPE_ORDER);

            for (int j = 0; j < N; j++)
            {
                int start = j;
                int count = 1;
                while(j+1 < N && PointArray[i].slopeTo(TempArray[j]) == PointArray[i].slopeTo(TempArray[j+1]))
                {
                    j++;
                    count++;
                }
                if (count > 2)
                {
                    //Arrays.sort(TempArray, start ,j+1 , Point.Y_ORDER);
                    if (PointArray[i].compareTo(TempArray[start]) < 0)
                    {
                        StdOut.print(PointArray[i].toString());
                        for (int k = start; k <= j; k++)
                        {
                            StdOut.print(" -> ");
                            StdOut.print(TempArray[k].toString());
                        }
                        StdOut.print("\n");
                        PointArray[i].drawTo(TempArray[j]);
                    }                    
                }
            }
            
            
            
            
            /*
            if (i == 0)
            {
                for (int j = 0; j < N; j++)
                {
                    StdOut.println(PointArray[j].toString());
                }
                StdOut.println("=============================");
                for (int j = 0; j < N; j++)
                {
                    StdOut.println(TempArray[j].toString());
                }
            }*/
            /*
            for (int j = 0; j < N; j++)
            {
                StdOut.println(PointArray[j].toString());
            }
            StdOut.println("=============================");
            for (int j = 0; j < N; j++)
            {
                StdOut.println(TempArray[j].toString());
            }*/
        }
        
        
   }
}
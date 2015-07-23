public class PointSET 
{    
    private SET<Point2D> bruteSet;
    public PointSET()                               // construct an empty set of points 
    {
        bruteSet = new SET<Point2D>();
    }
    public boolean isEmpty()                      // is the set empty? 
    {
        return bruteSet.size() == 0;
    }
    public int size()                         // number of points in the set
    {
        return bruteSet.size();
    }
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new java.lang.NullPointerException();
        bruteSet.add(p);
    }
   public boolean contains(Point2D p)            // does the set contain point p? 
   {
       if (p == null) throw new java.lang.NullPointerException();
       return bruteSet.contains(p);
   }
   public void draw()                         // draw all points to standard draw 
   {
       StdDraw.setPenColor(StdDraw.BLACK);
       StdDraw.setPenRadius(.01);
       for (Point2D point : bruteSet)
           point.draw(); 
   }
   public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
   {
       if (rect == null) throw new java.lang.NullPointerException();
       Queue<Point2D> rangePoints = new Queue<Point2D>();
       for (Point2D point : bruteSet)
       {
           if (rect.contains(point))
           {
               rangePoints.enqueue(point);
           }
       }
       return rangePoints;
   }
   public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
   {
       if (p == null) throw new java.lang.NullPointerException();
       Point2D neighbor = new Point2D(0.0, 0.0);
       if (bruteSet.isEmpty())
           return null;
       double min = Double.MAX_VALUE;
       for (Point2D point : bruteSet)
       {
           double distance = p.distanceSquaredTo(point);
           if (distance < min)
           {
               min = distance;
               neighbor = point;
           }
       }
       return neighbor;
   }

   public static void main(String[] args)                  // unit testing of the methods (optional) 
   {
       String filename = "kdtree/circle10.txt";
       In in = new In(filename);
       PointSET ycgPoints = new PointSET();
       while (!in.isEmpty())
       {
           double x = in.readDouble();
           double y = in.readDouble();
           Point2D point = new Point2D(x, y);
           //System.out.println("x = " + x + ", " + " y = " + y);
           ycgPoints.insert(point);
       }
       ycgPoints.draw();
       Point2D point1 = new Point2D(0.9, 0.9);
       System.out.println(ycgPoints.nearest(point1).toString());
   }
}
public class KdTree 
{    
    private Node root;
    private int size;
    
    private static class Node 
    {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean isEven;
        
        public Node(Point2D p, RectHV rect, boolean isEven)
        {
            this.p = p;
            this.rect = rect;
            this.isEven = isEven;
            lb = null;
            rt = null;
        }
        public boolean isEven()
        {
            return isEven;
        }
            
    }
    
    public KdTree()                               // construct an empty set of points 
    {
        root = null;
        size = 0;
    }
    public boolean isEmpty()                      // is the set empty? 
    {
        return size() == 0;
    }
    public int size()                         // number of points in the set
    {
        return this.size;
    }
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new java.lang.NullPointerException();
        if (!contains(p))
        {
            if (size == 0)
            {
                RectHV rect = new RectHV(0.0, 0.0, 1.0,1.0);
                root = new Node(p, rect, false);
            }
            else
            {
                put(root, p);
            }
            size++;
        }
    }

    private void put(Node node, Point2D p)
    {
        if (node.isEven())
        {
            if (p.y() < node.p.y())
            {
                if (node.lb == null)
                {
                    RectHV rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(),node.p.y());
                    Node newnode = new Node(p, rect, !node.isEven());
                    node.lb = newnode;
                }
                else put(node.lb, p);
            }
            else
            {
                if (node.rt == null)
                {
                    RectHV rect = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(),node.rect.ymax());
                    Node newnode = new Node(p, rect, !node.isEven());
                    node.rt = newnode;
                }
                else put(node.rt, p);
            }
        }
        else
        {
            if (p.x() < node.p.x())
            {
                if (node.lb == null)
                {
                    RectHV rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(),node.rect.ymax());
                    Node newnode = new Node(p, rect, !node.isEven());
                    node.lb = newnode;
                }
                else put(node.lb, p);
            }
            else
            {
                if (node.rt == null)
                {
                    RectHV rect = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(),node.rect.ymax());
                    Node newnode = new Node(p, rect, !node.isEven());
                    node.rt = newnode;
                }
                else put(node.rt, p);
            }
        }
    }

   public boolean contains(Point2D p)            // does the set contain point p? 
   {
       return search(root, p);
   }
   private boolean search(Node node, Point2D p)
   {
       if (node == null)
       {
           return false;
       }
       if (node.p.equals(p))
       {
           return true;
       }
       if (node.isEven())
       {
           if (p.y() < node.p.y())
            {
                return search(node.lb, p);
            }
            else
            {
                return search(node.rt, p);
            }
       }
       else
       {
           if (p.x() < node.p.x())
            {
                return search(node.lb, p);
            }
            else
            {
                return search(node.rt, p);
            }
       }
   }

   public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
   {
       if (rect == null) throw new java.lang.NullPointerException();
       Queue<Point2D> rangePoints = new Queue<Point2D>();
       
       range(root, rect, rangePoints);
       
       return rangePoints;
   }
   private void range(Node node, RectHV rect, Queue<Point2D> rangePoints)
   {
       if(node != null)
       {
           if (rect.contains(node.p))
               rangePoints.enqueue(node.p);
           if (node.lb != null && rect.intersects(node.lb.rect))
           {
               range(node.lb, rect, rangePoints);
           }
           if (node.rt != null && rect.intersects(node.rt.rect))
           {
               range(node.rt, rect, rangePoints);
           }
       }      
   }

   private Point2D neighbor;
   private double distance;
   
   public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
   {
       neighbor = new Point2D(0.0, 0.0);
       distance = Double.MAX_VALUE;
       if (p == null) throw new java.lang.NullPointerException();
       if (root == null)
       {
           return null;
       }
       nearest(root, p);
       return neighbor;
   }
 
   private void nearest(Node node, Point2D p)
   {
       if (node != null)
       {
           if (p.distanceSquaredTo(node.p) < distance)
           {
               neighbor = node.p;
               distance = p.distanceSquaredTo(node.p);
               //System.out.println(neighbor.toString() + " " + distance);
           }
           if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < distance && node.rt != null && node.rt.rect.distanceSquaredTo(p) < distance) 
           {
               if (node.lb.rect.distanceSquaredTo(p) < node.rt.rect.distanceSquaredTo(p))
               {
                   nearest(node.lb, p);
                   nearest(node.rt, p);
               }
               else
               {
                   nearest(node.rt, p);
                   nearest(node.lb, p);
               }
           }
           else if ((node.lb == null || node.lb.rect.distanceSquaredTo(p) >= distance) && node.rt != null && node.rt.rect.distanceSquaredTo(p) < distance)
           {
               nearest(node.rt, p);
           }
           else if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < distance && (node.rt == null || node.rt.rect.distanceSquaredTo(p) >= distance))
           {
               nearest(node.lb, p);
           }
       }
   }
   
   public void draw()                         // draw all points and spliting lines to standard draw 
   {
        draw(root);
   }
   private void draw(Node node)
   {
       if (node != null)
       {
           StdDraw.setPenColor(StdDraw.BLACK);
           StdDraw.setPenRadius(.01);
           node.p.draw();
           //System.out.println(node.p.toString());
           if (node.isEven())
           {
               StdDraw.setPenColor(StdDraw.BLUE);
               StdDraw.setPenRadius();
               Point2D point1 = new Point2D(node.rect.xmin(), node.p.y());
               Point2D point2 = new Point2D(node.rect.xmax(), node.p.y());
               point1.drawTo(point2);
           }
           else
           {
               StdDraw.setPenColor(StdDraw.RED);
               StdDraw.setPenRadius();
               Point2D point1 = new Point2D(node.p.x(), node.rect.ymin());
               Point2D point2 = new Point2D(node.p.x(), node.rect.ymax());
               point1.drawTo(point2);
           }
           draw(node.lb);
           draw(node.rt);
       }
   }

   public static void main(String[] args)                  // unit testing of the methods (optional) 
   {
       String filename = "kdtree/circle10.txt";
       In in = new In(filename);
       KdTree ycgPoints = new KdTree();
       while (!in.isEmpty())
       {
           double x = in.readDouble();
           double y = in.readDouble();
           Point2D point = new Point2D(x, y);
           //System.out.println("x = " + x + ", " + " y = " + y);
           ycgPoints.insert(point);
       }
       ycgPoints.draw();
       
       Point2D point1 = new Point2D(0.99, 0.9);
       //System.out.println(ycgPoints.contains(point1));
       RectHV rect1 = new RectHV(0.5, 0.5, 1.0, 1.0);
       //for (Point2D point : ycgPoints.range(rect1))
       //    System.out.println(point.toString());
       point1.drawTo(ycgPoints.nearest(point1));
       //System.out.println(ycgPoints.nearest(point1).toString());
   }
}
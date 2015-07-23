import java.util.Iterator;
public class Board 
{
    private int N;
    private int [][] board;
    //private Board previous = null;
    //private Board next = null;
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)
    {
        N = blocks[0].length;
        board = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            board[i][j] = blocks[i][j];
    }
        
    public int dimension()                 // board dimension N
    {
        return N;
    }
    public int hamming()                   // number of blocks out of place
    {
        int hamming = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] != i*N+j+1)
                {
                    hamming++;
                }
            }
        }
        return (hamming-1);
    }
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int manhattan = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                int number = board[i][j];
                if (number != 0)
                {
                    int goal_i, goal_j;
                    if (number%N == 0)
                    {
                        goal_j = N-1;
                        goal_i = number/N - 1;
                    }
                    else
                    {
                        goal_j = number%N - 1;
                        goal_i = number/N;
                    }
                    manhattan += (Math.abs(i-goal_i)+Math.abs(j-goal_j));
                }
            }
        }
        return manhattan;
    }
    public boolean isGoal()                // is this board the goal board?
    {
        return hamming() == 0;
    }
    public Board twin()                    // a board that is obtained by exchanging two adjacent blocks in the same row
    {
        Board twin_board = new Board(board);
        int temp;
        if (twin_board.board[0][0] != 0 && twin_board.board[0][1] != 0)
        {
            temp = twin_board.board[0][0];
            twin_board.board[0][0] = twin_board.board[0][1];
            twin_board.board[0][1] = temp;
        }
        else
        {
            temp = twin_board.board[1][0];
            twin_board.board[1][0] = twin_board.board[1][1];
            twin_board.board[1][1] = temp;
        }
        return twin_board;
    }
    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != N) return false;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if(this.board[i][j] != that.board[i][j])
                    return false;
            }
        }
        return true;
        //return (this.month == that.month) && (this.day == that.day) && (this.year == that.year);
    }
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Queue<Board> neighborsList = new Queue<Board>();
        int x = 0;
        int y = 0;
        boolean find = false;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if(board[i][j] == 0)
                {
                    x = i;
                    y = j;
                    find = true;
                    break;
                }
            }
            if (find)
                break;
        }
        if (x > 0)
        {
            Board up = new Board(board);
            up.board[x][y] = up.board[x-1][y];
            up.board[x-1][y] = 0;
            //up.previous = this;
            neighborsList.enqueue(up);
        }
        if (x < N-1)
        {
            Board down = new Board(board);
            down.board[x][y] = down.board[x+1][y];
            down.board[x+1][y] = 0;
            //down.previous = this;
            neighborsList.enqueue(down);
        }
        if (y > 0)
        {
            Board left = new Board(board);
            left.board[x][y] = left.board[x][y-1];
            left.board[x][y-1] = 0;
            //left.previous = this;
            neighborsList.enqueue(left);
        }
        if (y < N-1)
        {
            Board right = new Board(board);
            right.board[x][y] = right.board[x][y+1];
            right.board[x][y+1] = 0;
            //right.previous = this;
            neighborsList.enqueue(right);
        }
        return neighborsList;

    }
    // string representation of this board (in the output format specified below)
    public String toString() 
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
 

    // unit tests (not graded)
    public static void main(String[] args) 
    {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        /*
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else 
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        */
        StdOut.println(initial);
        StdOut.println("=======================");
        for (Board board : initial.neighbors())
        {
            StdOut.println(board);
        }
        
   }
}
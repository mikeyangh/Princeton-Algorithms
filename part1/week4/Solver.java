
public class Solver 
{
    private int moves = -1;
    private boolean isSolvable = false;
    private Stack<Board> solution = new Stack<Board>();
    
    private class SearchNode implements Comparable<SearchNode>
    {
        private Board board;
        private int moves;
        private SearchNode parrent;
        private int priority;
        private boolean isTwin;
        
        public SearchNode(Board board, int moves, SearchNode parrent, boolean isTwin)
        {
            this.board = board;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
            this.parrent = parrent;
            this.isTwin = isTwin;
        }
        @Override  
        public int compareTo(SearchNode that) 
        {
            if (this.priority < that.priority) return -1;  
            else if (this.priority > that.priority) return 1;
            else 
            {
                if (this.board.hamming()+this.moves < that.board.hamming()+that.moves)
                {
                    return -1;
                }
                else if (this.board.hamming()+this.moves > that.board.hamming()+that.moves)
                {
                    return 1;
                }
                else return 0;
            }
        }  
    }

    private MinPQ<SearchNode> boardPQ = new MinPQ<SearchNode>();
    

        
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        SearchNode initialNode = new SearchNode(initial, 0, null, false);
        SearchNode twinNode = new SearchNode(initial.twin(), 0, null, true);
        boardPQ.insert(initialNode);
        boardPQ.insert(twinNode);
        while (true)
        {
            SearchNode outNode = boardPQ.delMin();
            if (outNode.board.isGoal())
            {
                if (outNode.isTwin)
                {
                    this.moves = -1;
                    this.isSolvable = false;
                    this.solution = null;
                }
                else
                {
                    this.moves = outNode.moves; 
                    this.isSolvable = true;
                    while (outNode != null)
                    {
                        solution.push(outNode.board);
                        outNode = outNode.parrent;
                    }
                    
                }
                break;
            }
            else
            {
                for (Board neighbor : (outNode.board.neighbors()))
                {
                    SearchNode neighborNode = new SearchNode(neighbor, outNode.moves+1, outNode, outNode.isTwin);
                    if (outNode.parrent == null) 
                        boardPQ.insert(neighborNode);
                    else if (!outNode.parrent.board.equals(neighborNode.board))
                        boardPQ.insert(neighborNode);
                }
            }
        }
    }
    public boolean isSolvable()            // is the initial board solvable?
    {
        return this.isSolvable;
    }
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return this.moves;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        return solution;
    }
    public static void main(String[] args) // solve a slider puzzle (given below)
    {

    }
}
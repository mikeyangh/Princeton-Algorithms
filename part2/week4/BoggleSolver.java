import java.util.*;
public class BoggleSolver
{
    private HashSet<String> dict;
    private BoggleTrieST<Integer> trie;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    
    private static class BoggleTrieST<Value> {
        private static final int R = 26; // A-Z letters
        private static final int OFFSET = 65; // Offset of letter A in ASCII table
        
        public Node root = new Node();

        public static class Node {
            private Object val;
            public Node[] next = new Node[R];
        }

        public boolean NextValidChar(Node last, char ch)
        {
        	return last.next[ch-'A'] != null;
        }
        
        /****************************************************
         * Is the key in the symbol table?
         ****************************************************/
        public boolean contains(String key) {
            return get(key) != null;
        }

        public Value get(String key) {
            Node x = get(root, key, 0);
            if (x == null)
                return null;
            return (Value) x.val;
        }

        public Node get(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                return x;
            char c = key.charAt(d);
            return get(x.next[c - OFFSET], key, d + 1);
        }

        /****************************************************
         * Insert key-value pair into the symbol table.
         ****************************************************/
        public void put(String key, Value val) {
            root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - OFFSET] = put(x.next[c - OFFSET], key, val, d + 1);
            return x;
        }

        public Iterable<String> keys() {
            return keysWithPrefix("");
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            Queue<String> queue = new Queue<String>();
            Node x = get(root, prefix, 0);
            collect(x, prefix, queue);
            return queue;
        }

        public boolean isPrefix(String prefix) {
            return get(root, prefix, 0) != null;
        }
       
        private void collect(Node x, String key, Queue<String> queue) {
            if (x == null)
                return;
            if (x.val != null)
                queue.enqueue(key);
            for (int c = 0; c < R; c++)
                collect(x.next[c - OFFSET], key + (char) c, queue);
        }

        public Iterable<String> keysThatMatch(String pat) {
            Queue<String> q = new Queue<String>();
            collect(root, "", pat, q);
            return q;
        }

        public void collect(Node x, String prefix, String pat, Queue<String> q) {
            if (x == null)
                return;
            if (prefix.length() == pat.length() && x.val != null)
                q.enqueue(prefix);
            if (prefix.length() == pat.length())
                return;
            char next = pat.charAt(prefix.length());
            for (int c = 0; c < R; c++)
                if (next == '.' || next == c)
                    collect(x.next[c - OFFSET], prefix + (char) c, pat, q);
        }
    } 
    
 /////////////////////////////////////////////////////////////////////////   
    public BoggleSolver(String[] dictionary)
    {   
    	dict = new HashSet<>();
    	trie = new BoggleTrieST<>();
        for (String str : dictionary)
        {
            dict.add(str);
            trie.put(str, 0);
        }
    }
   
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        HashSet<String> result = new HashSet<>();
        int M = board.rows();
        int N = board.cols();
        int total = M*N;
       
        HashMap<Integer, Queue<Integer>> neighbor = new HashMap<>();
        
        for (int i = 0; i < total; i++)
        {
            Queue<Integer> temp = getNeighbor(i, M, N);
            neighbor.put(i, temp);
            //System.out.println(i + "'s neighbor: " + temp.toString());
        }
        for (int i = 0; i < total; i++)
        {
        	boolean[] marked = new boolean[total];
        	//Stack<BoggleTrieST.Node> NodeStack = new Stack<>();
        	dfs(i, new StringBuilder(), marked, board, neighbor, result, trie.root.next[board.getLetter(i/N, i%N)-'A']);
        }
     
        return result;
    }
    
    
    private Queue<Integer> getNeighbor(int index,int M, int N)
    {
        Queue<Integer> queue = new Queue<>();
        int row = index / N;
        int col = index % N;
        for (int i = row-1; i <= row+1; i++)
            for (int j = col-1; j <= col+1; j++)
        {
            if (i == row && j == col)
                continue;
            if (i >= 0 && i < M && j >= 0 && j < N)
                queue.enqueue(i*N+j);
        }
        return queue;
    }
    
    private void dfs(int start, StringBuilder str, boolean[] marked, BoggleBoard board, HashMap<Integer, Queue<Integer>> neighbor, HashSet<String> result,BoggleTrieST.Node currentNode)
    {
    	if (currentNode == null)
    		return;
    	
    	Integer number = start;
    	int i = start / board.cols();
    	int j = start % board.cols();
    	int increment = 1;
    	boolean isQ = false;
    	if (board.getLetter(i, j) == 'Q' && currentNode.next['U'-'A'] != null)
    	{
    		increment = 2;
    		str.append("QU");
    		isQ = true;
    	}
    	else if (board.getLetter(i, j) == 'Q')
    	{
    		return;
    	}
    	else
    		str.append(board.getLetter(i, j));
    	
	   	marked[start] = true;
	   	String string = str.toString();
    	if (string.length() > 2 && dict.contains(string))	 
    		result.add(string);
   		if (isQ)
   		{
   			currentNode = currentNode.next['U'-'A'];   				
	   	}
	    for (Integer next : neighbor.get(number))
	   	{
	    	if (!marked[next])   	
		   		dfs(next, str, marked, board, neighbor, result, currentNode.next[board.getLetter(next/board.cols(), next % board.cols())-'A']);
		}

	   	if (increment == 2)
	   		str.delete(str.length()-2,str.length());
	   	else
	   		str.deleteCharAt(str.length()-1);
	   	marked[start] = false;
		
    }
    
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
    	if (dict.contains(word))
    	{
    		int length = word.length();
    		if (length <= 2)
    			return 0;
    		else if (length <= 4 && length >= 3)
    			return 1;
    		else if (length == 5)
    			return 2;
    		else if (length == 6)
    			return 3;
    		else if (length == 7)
    			return 5;
    		else
    			return 11;	
    	}
        return 0;
    }
    
    public static void main(String[] args)
    {
        In in = new In("boggle/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        //BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleSolver test = new BoggleSolver(dictionary);
        //String filename = "boggle/board4x4.txt";
        String filename = "boggle/board-q.txt";
        //String filename = "boggle/board-16q.txt";
        BoggleBoard board = new BoggleBoard(filename);
        for (int i = 0; i < board.rows(); i++)
        {
        	for (int j = 0; j < board.cols(); j++)
        	{
        		System.out.print(board.getLetter(i, j) + " ");
        	}
        	System.out.println();
        }
        int score = 0;
        System.out.println(test.getAllValidWords(board).toString());
        for (String str : test.getAllValidWords(board))
        {
        	score += test.scoreOf(str);
        }
        System.out.println(score);
    }
    
}
import java.util.*;
public class BaseballElimination
{
    private int N;  // number of teams in this division
    private int[] win;  // number of wined games
    private int[] loss;  //number of losed games
    private int[] remain;  //number of remaining games 
    private int[][] interact;  // remaining number of games between team i and team x
    private String[] name; //team name array
    private HashMap<String, Integer> hash;  //team name map to index
    private HashMap<String, Queue<String>> certificate;  //Eliminate certificate for certain eliminated team
    private boolean[] isEliminated;  //already eliminated?
    private boolean[] marked;  //already computed?
    private String teamMax;  //team name with most wins
    public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
    {
        certificate = new HashMap<String, Queue<String>>();
        hash = new HashMap<String, Integer>();
        In input = new In(filename);
        N = Integer.parseInt(input.readLine());
        isEliminated = new boolean[N];
        marked = new boolean[N];
        name = new String[N];
        win = new int[N];
        loss = new int[N];
        remain = new int[N];
        interact = new int[N][N];
        int i = 0;
        int winMax = Integer.MIN_VALUE;
        while (i < N)
        {
            name[i] = input.readString();
            hash.put(name[i], i);
            win[i] = input.readInt();
            if (win[i] > winMax)
            {
                winMax = win[i];
                teamMax = name[i];
            }
            loss[i] = input.readInt();
            remain[i] = input.readInt();
            for (int j = 0; j < N; j++)
            {
                interact[i][j] = input.readInt();
            }
            i++;
        }
        
    }  
    public int numberOfTeams()                        // number of teams
    {
        return N;
    }
        
    public Iterable<String> teams()                                // all teams
    {       
        Queue<String> queue = new Queue<>();
        for (int i = 0; i < N; i++)
            queue.enqueue(name[i]);
        return queue;
    }  
    public int wins(String team)                      // number of wins for given team
    {
        if (!hash.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return win[hash.get(team)];
    }
        
    public int losses(String team)                    // number of losses for given team
    {
        if (!hash.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return loss[hash.get(team)];
    }
    public int remaining(String team)                 // number of remaining games for given team
    {
        if (!hash.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return remain[hash.get(team)];
    }
    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        if (!hash.containsKey(team1) || !hash.containsKey(team2))
            throw new java.lang.IllegalArgumentException();
        return interact[hash.get(team1)][hash.get(team2)];
    }
    
    public boolean isEliminated(String team)              // is given team eliminated?
    {
        if (!hash.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        int index = hash.get(team);
        if (marked[index])
            return isEliminated[index];
        if (win[hash.get(teamMax)] > win[index] + remain[index])
        {
            isEliminated[index] = true;
            marked[index] = true;
            Queue<String> result = new Queue<String>();
            result.enqueue(teamMax);
            certificate.put(team, result);
            return true;
        }
        final int V = N*(N-1)/2+2;
        FlowNetwork network = new FlowNetwork(V);
        int vertex = N;
        for (int i = 0; i < N; i++)
        {
            if (i == index)
                continue;
            for (int j = i+1; j < N; j++)
            {
                if (j == index)
                    continue;
                FlowEdge edge1 = new FlowEdge(vertex, i, Double.MAX_VALUE);
                network.addEdge(edge1);
                FlowEdge edge2 = new FlowEdge(vertex, j, Double.MAX_VALUE);
                network.addEdge(edge2);
                FlowEdge edgeS = new FlowEdge(index, vertex, interact[i][j]);
                network.addEdge(edgeS);
                vertex++;
                //S -> index 
            }
        }
        for (int i = 0; i < N; i++)
        {
            if (i == index)
                continue;
            FlowEdge edgeT = new FlowEdge(i, V-1,win[index] + remain[index] - win[i]);
            network.addEdge(edgeT);
        }       
        //StdOut.println(network.toString());       
        FordFulkerson maxflow = new FordFulkerson(network, index, V-1);
        //StdOut.println(maxflow.value());
        Queue<String> result = new Queue<String>();
        for (int i = 0; i < N; i++)
        {
            if (i == index)
                continue;
            if (maxflow.inCut(i))
                result.enqueue(name[i]);
            //StdOut.println("vertex " + i + " is in s side? " + maxflow.inCut(i));
        }
        certificate.put(team, result);
        marked[index] = true;
        if (result.size() != 0)
        {
            isEliminated[index] = true;
            return true;
        }
        else
        {
            isEliminated[index] = false;
            return false;
        }
    }
    public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        if (!hash.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        int index = hash.get(team);
        if (marked[index])
        {
            if (isEliminated[index])
                return certificate.get(team);
            else
                return null;
        }
        else
        {
            boolean flag = this.isEliminated(team);
            if (flag)
                return certificate.get(team);
            else
                return null;
        }
    }
    
    public static void main(String[] args) 
    {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) 
        {
            if (division.isEliminated(team)) 
            {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) 
                {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else 
                StdOut.println(team + " is not eliminated");
        }
    }
    /*
    public static void main(String[] args)
    {
        //String filename = "baseball/teams5.txt";
        String filename = "baseball/teams4.txt";
        BaseballElimination test = new BaseballElimination(filename);
        //test.isEliminated("Detroit");
        //test.isEliminated("Philadelphia");
        test.isEliminated("Atlanta");
    }    */
}
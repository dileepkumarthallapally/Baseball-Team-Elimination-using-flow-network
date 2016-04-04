import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

class FlowEdge {
    private final int v;             
    private final int w;            
    private final double capacity_value;   
    private double flow_value;             
    public FlowEdge(int v, int w, double capacity_value) {
        if (capacity_value < 0) throw new IllegalArgumentException("edge capacity_value is not positive");
        this.v         = v;
        this.w         = w;  
        this.capacity_value  = capacity_value;
        this.flow_value      = 0;
    }


    public int from()         { return v;        }  
    public int to()           { return w;        }  
    public double capacity_value()  { return capacity_value; }
    public double flow_value()      { return flow_value;     }


    public int otherPoint(int vertexValue) {
        if      (vertexValue == v) return w;
        else if (vertexValue == w) return v;
        else throw new IllegalArgumentException(" endpoint is not legal");
    }

    public double residualCap(int vertexValue) {
        if      (vertexValue == v) return flow_value;              // backward edge
        else if (vertexValue == w) return capacity_value - flow_value;   // forward edge
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    public void addResidualFlowTo(int vertexValue, double delta) {
        if      (vertexValue == v) flow_value -= delta;           // backward edge
        else if (vertexValue == w) flow_value += delta;           // forward edge
        else throw new IllegalArgumentException("Illegal endpoint");
    }


    public String toString() {
        return v + "->" + w + " " + flow_value + "/" + capacity_value;
    }


  
}

class FordFulkerson {
    private boolean[] pathExists;     
    private FlowEdge[] lastEdge;    
    private double maxFlowValue;       
 
    public FordFulkerson(FlowNetwork G, int s, int t) {
        while (existsAugmentingPath(G, s, t)) {
            double bottleneckCapacity = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = lastEdge[v].otherPoint(v)) {
                bottleneckCapacity = Math.min(bottleneckCapacity, lastEdge[v].residualCap(v));
            }
            for (int v = t; v != s; v = lastEdge[v].otherPoint(v)) {
                lastEdge[v].addResidualFlowTo(v, bottleneckCapacity); 
            }
            maxFlowValue += bottleneckCapacity;
        }
    }
    public double maxFlowValue()  {
        return maxFlowValue;
    }
    public boolean inCut(int v)  {
        return pathExists[v];
    }
    private boolean existsAugmentingPath(FlowNetwork G, int s, int t) {
        lastEdge = new FlowEdge[G.V()];
        pathExists = new boolean[G.V()];
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(s);
        pathExists[s] = true;
        while (!q.isEmpty()) {
            int v = q.dequeue();

            for (FlowEdge e : G.adjLst(v)) {
                int w = e.otherPoint(v);
                if (e.residualCap(w) > 0) {
                    if (!pathExists[w]) {
                        lastEdge[w] = e;
                        pathExists[w] = true;
                        q.enqueue(w);
                    }
                    
                }
            }
        }
        return pathExists[t];
    }     
}

class FlowNetwork {
    private final int V;
    private int E;
    private ArrayList<FlowEdge>[] adjLst;
    
    // empty graph with V vertices
    public FlowNetwork(int V) {
        this.V = V;
        this.E = 0;
        adjLst = (ArrayList<FlowEdge>[]) new ArrayList[V];
        for (int v = 0; v < V; v++)
            adjLst[v] = new ArrayList<FlowEdge>();
    }

    public int V() { return V; }
    public int E() { return E; }
    public void addingEdge(FlowEdge e) {
        E++;
        int v = e.from();
        int w = e.to();
        adjLst[v].add(e);
        adjLst[w].add(e);
    }

    
    public Iterable<FlowEdge> adjLst(int v) {
        return adjLst[v];
    }

    
    public Iterable<FlowEdge> edges() {
    	ArrayList<FlowEdge> list = new ArrayList<FlowEdge>();
        for (int v = 0; v < V; v++)
            for (FlowEdge e : adjLst(v)) {
                if (e.to() != v)
                    list.add(e);
            }
        return list;
    }


    
    public String toString() {
        String new_line = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + new_line);
        for (int v = 0; v < V; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : adjLst[v]) {
                if (e.to() != v) s.append(e + "  ");
            }
            s.append(new_line);
        }
        return s.toString();
    }

    
}

public class EliminationProblem {
	private int number_of_teams;
	private int number_of_matches;
	static int kk=0;
	private String[] team_names;
	private int[] teamWins; 
	private int[] team_losses; 
	private int[] remaining_games; 
	private int[][] team_against; 

	private Queue<String> queue_teams;
	ArrayList<String> other_teams ;
		

	private HashMap<String, Integer> Allteams;
	
	private HashMap<Integer,Integer> UofTeams =new HashMap<Integer, Integer>() ;

	public EliminationProblem(String filename) throws NumberFormatException, IOException {
		
		FileReader in = new FileReader(filename);
		BufferedReader br = new BufferedReader(in);
		
		number_of_teams =Integer.parseInt(br.readLine()) ;
		
		
		team_names = new String[number_of_teams];

		teamWins = new int[number_of_teams];
		team_losses = new int[number_of_teams]; 
		remaining_games = new int[number_of_teams]; 
		team_against = new int[number_of_teams][number_of_teams]; 
		queue_teams = new Queue<String>();
		other_teams = new ArrayList<String>();
		Allteams = new HashMap<String, Integer>();
		int i=0;
		while(i < number_of_teams)
		{
			String line = br.readLine();
		    StringTokenizer stt = new StringTokenizer(line," ");
            String teamName = stt.nextToken();
            queue_teams.enqueue(teamName);
            team_names[i] = teamName;
            Allteams.put(teamName, i);
            int wins=Integer.parseInt(stt.nextToken());
		
            teamWins[i] = wins;
            int losses=Integer.parseInt(stt.nextToken());
            team_losses[i] = losses;
            int gamesLeft=Integer.parseInt(stt.nextToken());
            remaining_games[i] = gamesLeft;
            int j=0;
            while(j < number_of_teams)
            {
			team_against[i][j] = Integer.parseInt(stt.nextToken());
			j++;
            }
			i++;
		}
		

	}

	public int numberOfTeams() {
		return number_of_teams;
	}

	public Iterable<String> teams() {
		return queue_teams;
	}
	public int wins(String team) {

		int iIndex = Allteams.get(team);

		return teamWins[iIndex];
	}

	public int losses(String team) {
				int iIndex = Allteams.get(team);
		return team_losses[iIndex];
	}
	public int remaining(String team) {
		int iIndex = Allteams.get(team);

		return remaining_games[iIndex];
	}
	public int against(String team1, String team2)
	{
		int iIndexTeam1 = Allteams.get(team1);
		int iIndexTeam2 = Allteams.get(team2);
		return team_against[iIndexTeam1][iIndexTeam2];
	}
	public boolean isEliminated(String team) throws IOException
	{
		if (!Allteams.containsKey(team)) {
			throw new java.lang.IllegalArgumentException();
		}

		int iThisTeam = Allteams.get(team); 
		
		Queue<String> strqEliminatedTeams = new Queue<String>();
		int iOtherTeams = 0;
		while(iOtherTeams < number_of_teams)
		{
			if (teamWins[iThisTeam] + remaining_games[iThisTeam] < teamWins[iOtherTeams]) 
		    {
			strqEliminatedTeams.enqueue(team);				
		     }
		    iOtherTeams++;
         }
		
		
		if(strqEliminatedTeams.size() > 0)return true;
		number_of_matches = number_of_teams * (number_of_teams - 1) / 2;

		int sourceVertexIndex = number_of_matches + number_of_teams; 
		int targetVertexIndex = sourceVertexIndex + 1; 
		FlowNetwork m_FlowNetwork = new FlowNetwork(number_of_matches+ number_of_teams + 2);
		    int iCurrentVertex = 0; 
			int index=Allteams.get(team);
		for(int i = 0; i < number_of_teams; i++) 
		{
			int j = i + 1;
			while(j < number_of_teams)
			{
				m_FlowNetwork.addingEdge(new FlowEdge(sourceVertexIndex, iCurrentVertex, team_against[i][j]));
			
			    m_FlowNetwork.addingEdge(new FlowEdge(iCurrentVertex, number_of_matches + i, Double.POSITIVE_INFINITY)); 
			    m_FlowNetwork.addingEdge(new FlowEdge(iCurrentVertex, number_of_matches + j, Double.POSITIVE_INFINITY));
			    iCurrentVertex++; 
				j++;
			}
			
			m_FlowNetwork.addingEdge(new FlowEdge(number_of_matches + i, targetVertexIndex, teamWins[iThisTeam] + remaining_games[iThisTeam] - teamWins[i])); // team -> target
		}
		FordFulkerson fordFulkerson = new FordFulkerson(m_FlowNetwork, sourceVertexIndex, targetVertexIndex);
		int g=BuildGraph(team);
		String s[]= new String[1];
				s[0]="sam.txt";
		int Mf = (int)fordFulkerson.maxFlowValue();
		if(Mf<=g)
		{
			System.out.println(team+" iss Eliminated");
			return true;
		}
		return false;
	}

	private int BuildGraph(String team) throws IOException {
		// TODO Auto-generated method stub
		File fout = new File("sam.txt");
		other_teams=new ArrayList<String>();
		FileOutputStream fos = new FileOutputStream(fout);
		HashSet  <Integer> lastNodes = new HashSet<Integer>();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		String s[] = new String[number_of_matches+ number_of_teams + 2];
		int MaxWins=0;
		int index_team=Allteams.get(team);
		MaxWins=teamWins[index_team]+remaining_games[index_team];
		for (String string : queue_teams) {
			int ind =Allteams.get(string);
			UofTeams.put(ind,(MaxWins-teamWins[ind]));
			if(!string.equals(team))
			other_teams.add(string);
			}
		
		
		int vertexCount=0;
		String s1="",s2="";
		int g=0;
		int key=0;
		for(int i=0; i<other_teams.size();i++)
		{
			int j=i;
			while(j<other_teams.size())
			{
				g=g+team_against[ Allteams.get(other_teams.get(i))][Allteams.get(other_teams.get(j))];
			    vertexCount++;
			    s1=s1+vertexCount+" "+team_against[ Allteams.get(other_teams.get(i))][Allteams.get(other_teams.get(j))]+" ";
			
				j++;
			}
			
		}
		bw.write(s1);
		bw.newLine();
		totalVertex = vertexCount;
		for(int i=0; i<other_teams.size();i++)
		{
			for(int j=i;j<other_teams.size();j++)

			{
				//System.out.println(otherTeams.get(i));
				//System.out.println(otherTeams.get(j));
				int cap=team_against[Allteams.get(other_teams.get(i))][Allteams.get(other_teams.get(j))];
				s2=s2+(vertexCount+Allteams.get(other_teams.get(i)))+" "+cap+" "+(vertexCount+Allteams.get(other_teams.get(j)))+" "+cap+" ";
				//System.out.println(m_stTeams.get(otherTeams.get(i)));
			//	System.out.println(m_stTeams.get(otherTeams.get(j)));
				lastNodes.add(Allteams.get(other_teams.get(i)));
				lastNodes.add(Allteams.get(other_teams.get(j)));
				//System.out.println(vertexCount+" Last");
			}
		//	System.out.println(s2);
			bw.write(s2);
			bw.newLine();
			s2="";
			
			totalVertex++;
			//System.out.println("Total:+ "+totalVertex);
		}
		for (Integer Nodess : lastNodes) {
			//System.out.println("entered");
			s2=(totalVertex+10)+" "+UofTeams.get(Nodess);
			//System.out.println(s2);
			bw.write(s2);
			bw.newLine();
			s2="";
		}
		
		
		bw.close();
		return g;
	}
	public Iterable<String> certificateOfElimination(String team)
	{
		if (!Allteams.containsKey(team)) 
		{
			throw new java.lang.IllegalArgumentException();
		}
		int iThisTeam = Allteams.get(team); 
		ArrayList<String> strqSubsetOfTeams = new ArrayList<String>();
	
		for(int iOtherTeams = 0; iOtherTeams < number_of_teams; iOtherTeams++)
		{			
			if(iThisTeam == iOtherTeams)
			{
				continue; 
			}
			else
			{				
				if (teamWins[iThisTeam] + remaining_games[iThisTeam] < teamWins[iOtherTeams]) 
				{
					strqSubsetOfTeams.add(team_names[iOtherTeams]);				
				}				
			}			

		} 
		if(strqSubsetOfTeams.size() > 0)
			return strqSubsetOfTeams;
		
		number_of_matches = number_of_teams * (number_of_teams - 1) / 2;
		int sourceVertexIndex = number_of_matches + number_of_teams; 
		int targetVertexIndex = sourceVertexIndex + 1; 
		FlowNetwork m_FlowNetwork = new FlowNetwork(number_of_matches
				+ number_of_teams + 2);


		int iCurrentVertex = 0;
		int i1 = 0;
		while(i1 < number_of_teams)
		{
			for (int j = i1 + 1; j < number_of_teams; j++) 
			{

				m_FlowNetwork.addingEdge(new FlowEdge(sourceVertexIndex, iCurrentVertex, team_against[i1][j]));
				m_FlowNetwork.addingEdge(new FlowEdge(iCurrentVertex, number_of_matches + i1, Double.POSITIVE_INFINITY)); 
				m_FlowNetwork.addingEdge(new FlowEdge(iCurrentVertex, number_of_matches + j, Double.POSITIVE_INFINITY)); 
				
				iCurrentVertex++; 		
			}

			m_FlowNetwork.addingEdge(new FlowEdge(number_of_matches + i1, targetVertexIndex, teamWins[iThisTeam] + remaining_games[iThisTeam] - teamWins[i1]));
			i1++;
		}
	
		FordFulkerson fordFulkerson = new FordFulkerson(m_FlowNetwork, sourceVertexIndex, targetVertexIndex);

		Queue<String> strqTeamsInMinCut = new Queue<String>();
		
		int iIndexOfTeam = 0;
		int i = number_of_matches;
		while(i < (number_of_matches + number_of_teams))
		{
			if(fordFulkerson.inCut(i))
		    {

			strqTeamsInMinCut.enqueue(team_names[iIndexOfTeam]);				
		    }
		    iIndexOfTeam++;
			i++;
		}
		if(strqTeamsInMinCut.size() > 0) return strqTeamsInMinCut;
		else return null;
		
	}

	
	static int totalVertex=0;
	public static void main(String[] args) throws IOException {
		EliminationProblem division = new EliminationProblem(args[0]);

		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				System.out.print(team + " is eliminated");}else {
				System.out.println(team + " is not eliminated");
			}
		}
	}
}

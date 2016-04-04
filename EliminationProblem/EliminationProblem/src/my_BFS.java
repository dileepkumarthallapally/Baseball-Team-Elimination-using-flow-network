import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;


public class my_BFS {

	/**
	 * @param args
	 */
	static int count=0;
	static Stack<Integer> path = new Stack<Integer>();
	static int Graph[][];
	static boolean [] nodevisited;
	static int [] parent;
	/**
	 * @param args
	 */
	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub
		double starttime=System.currentTimeMillis();
		BufferedReader r= new BufferedReader(new FileReader(args[0]));
		BufferedReader r1= new BufferedReader(new FileReader(args[0]));
		
		count=linecount(r1);
		int source=Integer.parseInt(args[2]);
		//int source=Integer.parseInt("1");
		int destination=Integer.parseInt(args[3]);
		//int destination=Integer.parseInt("3");
		Graph = new int [count*count][count*count];
		Graph=graph_build(r,count);
		bfs_test(count, source, destination,Graph);
		
		System.out.println("Shortest Path is");
		while(!path.empty())
		{
		System.out.print(path.pop());
		System.out.print("  ");
		}
		r.close();
		double endtime=System.currentTimeMillis();
		double time=(endtime-starttime);
		System.out.println();
		System.out.println();
		System.out.println("Elapsed time: "+time+" Milli Seconds");
		}
	
		
	public static int[][] graph_build(BufferedReader br, int ct )throws IOException
	{
		int i=0;
		String line="";
		int[][] Graph1 = new int[ct*ct][ct*ct];
		while((line=br.readLine())!=null)
	    {
	       	StringTokenizer st=new StringTokenizer(line," ");
	       	while (st.hasMoreTokens())
	       	{
	       		int nd=Integer.parseInt(st.nextToken());
	       		int wt=Integer.parseInt(st.nextToken());
	       		Graph1[i][nd]=wt;
	       	}
	       	i++;
	    }	
		return (Graph1);
			
	}
	
	public static int linecount(BufferedReader k)throws IOException
	{
		int c=0;
		while(k.readLine()!=null)
		{
			c++;
		}
		return(c);
	}
	
	public static void bfs_test(int c, int s, int t, int a[][])
	{
		int dest, e, currentnode;
		parent= new int[c];
		nodevisited = new boolean[c];
		Queue<Integer> queue = new LinkedList<Integer>();
		
		for (int v = 0; v<c; v++)
		{
			parent[v] = -1;
            nodevisited[v] = false;
		}
		nodevisited[s]=true;
		queue.add(s);
		
		while(!queue.isEmpty())
		{
			e=queue.remove();
			dest=0;
			while(dest<c)
			{
				if(a[e][dest]>0 && !nodevisited[dest])
				{
					parent[dest]=e;
					queue.add(dest);
					nodevisited[dest]=true;
				}
				dest++;	
				}
			}
		currentnode=t;
		while(parent[currentnode]!=-1)
		{
			path.push(currentnode);
			currentnode=parent[currentnode];
		}
		path.push(s);
		}
	
	public static boolean bfs_check(int c, int s, int t, int g[][])

    {
		boolean check=false;
		bfs_test(c, s, t, g);
		if(nodevisited[t])
			check=true;
		return(check);
		}
}



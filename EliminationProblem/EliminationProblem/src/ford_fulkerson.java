import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class ford_fulkerson {

	/**
	 * @param args
	 */
	static int count=0;
	static int[] parent;
	static int Graph[][];
	/**
	 * @param args
	 */
	public static void main(String[] args)throws IOException 
	
	{
		// TODO Auto-generated method stub
	int k=getMaxFlow(args);	
	System.out.println(k);
	}
	
	
	
	public static int getMaxFlow(String[] args) throws IOException {
		// TODO Auto-generated method stub
		double starttime=System.currentTimeMillis();
		BufferedReader r= new BufferedReader(new FileReader(args[0]));
		BufferedReader r1= new BufferedReader(new FileReader(args[0]));
		int maxflow = 0;
		count=my_BFS.linecount(r1);
		Graph = new int [count*count][count*count];
		Graph=my_BFS.graph_build(r,count);
		int source=0;
		int destination=count-1;
		maxflow = ff_impl(count,Graph, source, destination);
		//System.out.println("Max flow is "+maxflow);
		r.close();
		r1.close();
		double endtime=System.currentTimeMillis();
		double time=(endtime-starttime);
		//System.out.println("Elapsed time: "+time+" Milli Seconds");
		return maxflow;
	}



	public static int ff_impl(int c,int[][] G, int sourcenode, int destnode)
	{
		int mf=0;
		int a,b,flow;
		
		int[][] residual= new int [c+1][c+1];
		for (int s=0; s<c; s++ )
		{
			for (int t=0; t<c; t++)
			{
				residual[s][t]=G[s][t];
			}
		}
		while (my_BFS.bfs_check(c,sourcenode,destnode,residual))
		{
			flow=Integer.MAX_VALUE;
			for(b=destnode;b!=sourcenode;b=my_BFS.parent[b])
			{
				a=my_BFS.parent[b];
				flow=Math.min(flow,residual[a][b]);		
			}
			for(b=destnode;b!=sourcenode;b=my_BFS.parent[b])
			{
				a=my_BFS.parent[b];
				residual[a][b]-=flow;
				residual[b][a]+=flow;
			}
			mf+=flow;
		}
		return(mf);
	}
	public static int[][] ff_residual(int c,int[][] G, int sourcenode, int destnode)
	{
		int mf=0;
		int a,b,flow;
		
		int[][] residual= new int [c+1][c+1];
		for (int s=0; s<c; s++ )
		{
			for (int t=0; t<c; t++)
			{
				residual[s][t]=G[s][t];
			}
		}
		while (my_BFS.bfs_check(c,sourcenode,destnode,residual))
		{
			flow=Integer.MAX_VALUE;
			for(b=destnode;b!=sourcenode;b=my_BFS.parent[b])
			{
				a=my_BFS.parent[b];
				flow=Math.min(flow,residual[a][b]);		
			}
			for(b=destnode;b!=sourcenode;b=my_BFS.parent[b])
			{
				a=my_BFS.parent[b];
				residual[a][b]-=flow;
				residual[b][a]+=flow;
			}
			mf+=flow;
		}
		return(residual);
	}
}
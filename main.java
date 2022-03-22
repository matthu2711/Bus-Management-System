
import edu.princeton.cs.algs4.DijkstraAllPairsSP;
import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class main {

    private static EdgeWeightedDigraph graph;
    private static LinkedList<BusStop> stops;
    private static DijkstraAllPairsSP sp;

    public static void main(String[] args) throws IOException {
        stops = new LinkedList<>();
        createGraph();
        TripTable tp = new TripTable();
        tp.readDate();

        System.out.println(graph);
        printPath(12478, 11075);
    }

    private static EdgeWeightedDigraph createGraph() throws FileNotFoundException {
        Scanner sr = new Scanner(new File("stops.txt"));

        sr.nextLine();
        List<Integer> stops = new ArrayList<>();

        while(sr.hasNext()){
            int stop = (Integer.parseInt(sr.nextLine().split(",")[0]));
            if (!stops.contains(stop)) {
                stops.add(stop);
            }
        }

        Collections.sort(stops);

        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(stops.get(stops.size() - 1) + 1);
        sr = new Scanner(new File("transfers.txt"));
        sr.nextLine();

        // Add edges from transfers.txt with associated weights
        while(sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if(values[2].equals("0"))
                graph.addEdge(new DirectedEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), 2));
            else
                graph.addEdge(new DirectedEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[3]) / 100));
        }

        sr = new Scanner(new File("stop_times.txt"));
        sr.nextLine();
        //System.out.println(graph);

        int tripID = -1;
        int pastStop = -1;
        boolean add = true;
        while(sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if(Integer.parseInt(values[0]) != tripID){
                tripID = Integer.parseInt(values[0]);
            }
            else {
                int to = Integer.parseInt(values[3]);
                for(DirectedEdge e : graph.adj(pastStop))
                    if(e.to() == to)
                        add = false;
                if(add)
                    graph.addEdge(new DirectedEdge(pastStop, to, 1));
            }
            pastStop = Integer.parseInt(values[3]);
            add = true;
        }

        return graph;
    }

    private static void printPath(int from, int to){
        DijkstraSP sp = new DijkstraSP(graph, from);
        StringBuilder sb = new StringBuilder();
        sb.append("The path from ").append(from).append(" to ").append(to).append(" is as follows:\n");
        for (DirectedEdge edge : sp.pathTo(to))
            sb.append(edge.to()).append(" -> ").append(edge.from()).append("\n");
        sb.append("This path has a cost of: ").append(sp.distTo(to)).append("\n");

    }

    private static void printPath(int from, int to){
        DijkstraSP sp = new DijkstraSP(graph, from);
        StringBuilder sb = new StringBuilder();
        sb.append("The path from ").append(from).append(" to ").append(to).append(" is as follows:\n");
        for (DirectedEdge edge : sp.pathTo(to))
            sb.append(edge.to()).append(" -> ").append(edge.from()).append("\n");
        sb.append("This path has a cost of: ").append(sp.distTo(to)).append("\n");

    }
}


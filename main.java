<<<<<<< Updated upstream
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
=======
import edu.princeton.cs.algs4.*;
import org.jetbrains.annotations.NotNull;
>>>>>>> Stashed changes

import java.io.File;
import java.io.IOException;
import java.util.*;

public class main {

    private static EdgeWeightedDigraph graph;
    private static LinkedList<BusStop> stops;

    public static void main(String[] args) throws IOException {
<<<<<<< Updated upstream

=======
        graph = createGraph();
        System.out.println(graph);

        for (DirectedEdge edge : new DijkstraSP(graph, 12478).pathTo(5408))
            System.out.println(edge);
    }

    static EdgeWeightedDigraph createGraph() throws FileNotFoundException {
>>>>>>> Stashed changes
        Scanner sr = new Scanner(new File("stops.txt"));

        sr.nextLine();
        int largestStop = -1;

        while (sr.hasNext()) {
            String stop =  sr.nextLine();
            if (Integer.parseInt(stop.split(",")[0]) > largestStop)
                largestStop = Integer.parseInt(stop.split(",")[0]);
            
            stops.add(new BusStop(stop));
        }

        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(largestStop + 1);
        sr = new Scanner(new File("transfers.txt"));
        sr.nextLine();

        // Add edges from transfers.txt with associated weights
        while (sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if (values[2].equals("0"))
                graph.addEdge(new DirectedEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), 2));
            else
                graph.addEdge(new DirectedEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[3]) / 100));
        }

        sr = new Scanner(new File("stop_times.txt"));
        sr.nextLine();

        int tripID = -1;
        int pastStop = -1;
<<<<<<< Updated upstream
        while(sr.hasNextLine()) {
=======
        boolean add = true;
        while (sr.hasNextLine()) {
>>>>>>> Stashed changes
            String[] values = sr.nextLine().split(",");
            if (Integer.parseInt(values[0]) != tripID) {
                tripID = Integer.parseInt(values[0]);
<<<<<<< Updated upstream
                pastStop = Integer.parseInt(values[3]);
            }
            else {
                graph.addEdge(new DirectedEdge(pastStop, Integer.parseInt(values[3]), 1));
                pastStop = Integer.parseInt(values[3]);
=======
            } else {
                if(validTime(values[1])) {
                    int to = Integer.parseInt(values[3]);
                    for (DirectedEdge e : graph.adj(pastStop))
                        if (e.to() == to)
                            add = false;
                    if (add)
                        graph.addEdge(new DirectedEdge(pastStop, to, 1));
                }
>>>>>>> Stashed changes
            }
        }

        System.out.println(graph.toString());





    }

    static boolean validTime(String time) {
        String[] timeSplit = time.strip().split(":");
        return !(Integer.parseInt(timeSplit[0]) > 24 || Integer.parseInt(timeSplit[1]) > 59 || Integer.parseInt(timeSplit[2]) > 59);
    }
}


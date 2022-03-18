import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class main {

    public static void main(String[] args) throws IOException {

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

        int tripID = -1;
        int pastStop = -1;
        while(sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if(Integer.parseInt(values[0]) != tripID){
                tripID = Integer.parseInt(values[0]);
                pastStop = Integer.parseInt(values[3]);
            }
            else {
                graph.addEdge(new DirectedEdge(pastStop, Integer.parseInt(values[3]), 1));
                pastStop = Integer.parseInt(values[3]);
            }
        }

        System.out.println(graph.toString());





    }
}


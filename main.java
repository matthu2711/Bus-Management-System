import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.TST;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class main {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private static EdgeWeightedDigraph graph;
    private static LinkedList<BusStop> stops;
    private static TST<BusStop> tree;


    public static void main(String[] args) throws IOException {
        stops = new LinkedList<>();
        tree = new TST<>();
        createGraph();
        populateTST();
        TripTable tt = new TripTable();

        for(String key : tree.keysWithPrefix("HASTINGS"))
            System.out.println(tree.get(key));
    }

    static void createGraph() throws FileNotFoundException {
        Scanner sr = new Scanner(new File("stops.txt"));

        sr.nextLine();
        int largestStop = -1;

        while (sr.hasNext()) {
            String stop = sr.nextLine();
            if (Integer.parseInt(stop.split(",")[0]) > largestStop)
                largestStop = Integer.parseInt(stop.split(",")[0]);
            addStop(stop);
        }

        graph = new EdgeWeightedDigraph(largestStop + 1);
        sr = new Scanner(new File("transfers.txt"));
        sr.nextLine();

        // Add edges from transfers.txt with associated weights
        while (sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if (values[2].equals("0"))
                graph.addEdge(new DirectedEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), 2));
            else
                graph.addEdge(new DirectedEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[3]) / 100.0));
        }

        sr = new Scanner(new File("stop_times.txt"));
        sr.nextLine();
        //System.out.println(graph);

        int tripID = -1;
        int pastStop = -1;
        boolean add = true;
        while(sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if (Integer.parseInt(values[0]) != tripID) {
                tripID = Integer.parseInt(values[0]);
            } else {
                if(validateTime(values[1])) {
                    int to = Integer.parseInt(values[3]);
                    for (DirectedEdge e : graph.adj(pastStop))
                        if (e.to() == to)
                            add = false;
                    if (add)
                        graph.addEdge(new DirectedEdge(pastStop, to, 1));
                }
            }
            pastStop = Integer.parseInt(values[3]);
            add = true;
        }
    }

    static void populateTST(){
        for(BusStop stop : stops)
            tree.put(stop.name.strip(), stop);
    }

    static boolean validateTime(String time) {
        String[] timeSplit = time.strip().split(":");
        return !(Integer.parseInt(timeSplit[0]) > 24 || Integer.parseInt(timeSplit[1]) > 59 || Integer.parseInt(timeSplit[2]) > 59);
    }

    static void addStop(String stop){
        String[] values = stop.split(",");
        String parent = "";
        int stopID = -1, code = -1, locType = -1;
        double latitude = -1, longitude = -1;

        if(values.length == 10 && !values[9].equals("") && !values[9].equals(" "))
            parent = values[9];
        if(!values[0].equals("") && !values[0].equals(" "))
            stopID = Integer.parseInt(values[0]);
        if(!values[1].equals("") && !values[1].equals(" "))
            code = Integer.parseInt(values[1]);
        if(!values[4].equals("") && !values[4].equals(" "))
            latitude = Double.parseDouble(values[4]);
        if(!values[5].equals("") && !values[5].equals(" "))
            longitude = Double.parseDouble(values[5]);
        if(!values[8].equals("") && !values[8].equals(" "))
            locType = Integer.parseInt(values[8]);

        String stopName = changeStopName(values[2]);
        stops.add(new BusStop(stopID, code, stopName, values[3], latitude, longitude, values[6], values[7], locType, parent));
    }

    private static String changeStopName(String name){
        String chars = name.substring(0, 2).strip();
        if (chars.equalsIgnoreCase("WB") || chars.equalsIgnoreCase("SB") || chars.equalsIgnoreCase("NB") || chars.equalsIgnoreCase("EB")) {
            return name.substring(3).trim() + name.charAt(2) + name.substring(0,2);
        }
        return name;
    }

    private static void printPath(int from, int to){
        DijkstraSP sp = new DijkstraSP(graph, from);
        int finalVertex = -1;
        StringBuilder sb = new StringBuilder();
        sb.append("The path from ").append(from).append(" to ").append(to).append(" is as follows:\n");
        for (DirectedEdge edge : sp.pathTo(to)) {
            sb.append(edge.from()).append(" -> ");
            finalVertex = edge.to();
        }
        sb.append(finalVertex).append("\n");
        sb.append("This path has a cost of: ").append(sp.distTo(to)).append("\n");
        System.out.println(sb);
    }
}


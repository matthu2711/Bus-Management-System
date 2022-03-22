
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

    public static void main(String[] args) throws IOException {
        stops = new LinkedList<>();
        createGraph();
        TripTable tp = new TripTable();
        tp.readDate();
        System.out.println(graph);
        System.out.println(tp);

        for (DirectedEdge edge : new DijkstraSP(graph, 12478).pathTo(6050))
            System.out.println(edge);
    }

    static void createGraph() throws FileNotFoundException {
        Scanner sr = new Scanner(new File("stops.txt"));

        sr.nextLine();
        int largestStop = -1;

        while (sr.hasNext()) {
            String stop =  sr.nextLine();
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
                graph.addEdge(new DirectedEdge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[3]) / 100));
        }

        sr = new Scanner(new File("stop_times.txt"));
        sr.nextLine();

        int tripID = -1;
        int pastStop = -1;
        boolean add = true;
        while (sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if (Integer.parseInt(values[0]) != tripID) {
                tripID = Integer.parseInt(values[0]);
                pastStop = Integer.parseInt(values[3]);
            } else {
                if(validTime(values[1])) {
                    int to = Integer.parseInt(values[3]);
                    for (DirectedEdge e : graph.adj(pastStop))
                        if (e.to() == to)
                            add = false;
                    if (add)
                        graph.addEdge(new DirectedEdge(pastStop, to, 1));
                }
            }
        }
    }

    static boolean validTime(String time) {
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

        String stopName = change(values[2]);
        stops.add(new BusStop(stopID, code, stopName, values[3], latitude, longitude, values[6], values[7], locType, parent));
    }

    private static String change(String name){
        String chars = name.substring(0, 2).strip();
        if (chars.toUpperCase().equals("WB") || chars.toUpperCase().equals("SB") || chars.toUpperCase().equals("NB") || chars.toUpperCase().equals("EB")) {
            return name.substring(3).trim() + name.substring(2,3) + name.substring(0,2);
        }
        return name;
    }
}


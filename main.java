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
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";

    private static EdgeWeightedDigraph graph;
    private static LinkedList<BusStop> stops;
    private static TST<BusStop> tree;

    //Use main to initialise and populate all data structures and handle all user input and arguments
    public static void main(String[] args) throws IOException {
        System.out.println(ANSI_PURPLE + "\nOpening the Vancouver Bus Management System\n" + ANSI_RESET);

        stops = new LinkedList<>();
        tree = new TST<>();

        //Creates and populates graph
        createGraph();

        //Populates TST with stops and creates TripTable DS using stops
        populateTST();
        TripTable tt = new TripTable();
        boolean exit = false;
        Scanner sr = new Scanner(System.in);
        printCommands();

        // Main function body. Checks the start of stream from input, gets the first word as the command (e/h/ss/st/sp). Then uses following words for
        // the arguments. Acts accordingly and reports any errors encountered from wrong inputs or incorrect values
        while (!exit) {
            System.out.print(ANSI_CYAN + "Insert Command: " + ANSI_RESET);
            if (sr.hasNext()) {
                String input = sr.nextLine();
                if (input.length() == 1) {
                    if (input.equals("h"))
                        printCommands();
                    else if (input.equals("e"))
                        exit = true;
                    else
                        System.out.println(ANSI_RED + "Incorrect Command: Use h to relist valid commands\n" + ANSI_RESET);
                } else {
                    String[] values = input.split(" ");
                    if (values[0].strip().equals("sp")) {
                        if (values.length == 3) {
                            if (values[1].equals(values[2])) {
                                System.out.println(ANSI_RED + "Incorrect Input: Enter two distinct Stop IDs\n" + ANSI_RESET);
                            } else if (validateStop(values[1]) && validateStop(values[2])) {
                                printPath(Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                            }
                        } else
                            System.out.println(ANSI_RED + "Incorrect Input: Must insert two stop IDs\n" + ANSI_RESET);
                    } else if (values[0].strip().equals("ss")) {
                        String search = input.substring(2).strip().toUpperCase();
                        StringBuilder sb = new StringBuilder();
                        for (String key : tree.keysWithPrefix(search)) {
                            sb.append(tree.get(key));
                        }
                        if (sb.toString().length() == 0)
                            System.out.println(ANSI_RED + "There is no stop with Stop Name/Name prefix of \"" + search + "\"\n" + ANSI_RESET);
                        else {
                            System.out.println(sb);
                            System.out.println("The information for each stop with Stop Name/Name prefix of \"" + search + "\" was printed above");
                        }
                    } else if (values[0].strip().equals("st")) {
                        if (validateTime(values[1]))
                            tt.timeQuery(values[1]);
                        else
                            System.out.println(ANSI_RED + "Incorrect Input: Time must be in hh:mm:ss format. Max allowed time is " +
                                    "23:59:59\n" + ANSI_RESET);
                    } else
                        System.out.println(ANSI_RED + "Incorrect Input: Use h to relist valid commands\n" + ANSI_RESET);

                }
            }
        }
        System.out.println(ANSI_PURPLE + "Closing the Vancouver Bus Management System");
    }

    // Uses stop.txt and transfers.txt to get all edges and adds them to an EdgeWeightedDigraph which is used in later calculations
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

        int tripID = -1;
        int pastStop = -1;
        boolean add = true;
        while (sr.hasNextLine()) {
            String[] values = sr.nextLine().split(",");
            if (Integer.parseInt(values[0]) != tripID) {
                tripID = Integer.parseInt(values[0]);
            } else {
                // Ensures times are in correct format and max time is 24:59:59
                if (validateTime(values[1])) {
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

    // Puts the stops into the TST
    static void populateTST() {
        for (BusStop stop : stops)
            tree.put(stop.name.strip(), stop);
    }

    // Ensures the time is in the correct format and the max time allowed is 24:59:59
    static boolean validateTime(String time) {
        try {
            String[] timeSplit = time.strip().split(":");
            return !(Integer.parseInt(timeSplit[0]) > 24 || Integer.parseInt(timeSplit[1]) > 59 || Integer.parseInt(timeSplit[2]) > 59);
        } catch (Exception e) {
            return false;
        }
    }

    // Validates stop ensuring the value is an existing stop in our system
    static boolean validateStop(String ID) {
        int IDint;
        try {
            IDint = Integer.parseInt(ID);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Incorrect Input: Stop ID must be a number\n" + ANSI_RESET);
            return false;
        }
        for (BusStop stop : stops)
            if (stop.stopID == IDint)
                return true;

        System.out.println(ANSI_RED + "There is no stop with Stop ID " + IDint + "\n" + ANSI_RESET);
        return false;
    }

    // Takes the string from the input file, divides up the stops data and saves into our table as a BusStop type
    static void addStop(String stop) {
        String[] values = stop.split(",");
        String parent = "";
        int stopID = -1, code = -1, locType = -1;
        double latitude = -1, longitude = -1;

        if (values.length == 10 && !values[9].equals("") && !values[9].equals(" "))
            parent = values[9];
        if (!values[0].equals("") && !values[0].equals(" "))
            stopID = Integer.parseInt(values[0]);
        if (!values[1].equals("") && !values[1].equals(" "))
            code = Integer.parseInt(values[1]);
        if (!values[4].equals("") && !values[4].equals(" "))
            latitude = Double.parseDouble(values[4]);
        if (!values[5].equals("") && !values[5].equals(" "))
            longitude = Double.parseDouble(values[5]);
        if (!values[8].equals("") && !values[8].equals(" "))
            locType = Integer.parseInt(values[8]);

        String stopName = changeStopName(values[2]);
        stops.add(new BusStop(stopID, code, stopName, values[3], latitude, longitude, values[6], values[7], locType, parent));
    }

    // Rearranges stop name so two characters are at the end of the string
    private static String changeStopName(String name) {
        String chars = name.substring(0, 2).strip();
        if (chars.equalsIgnoreCase("WB") || chars.equalsIgnoreCase("SB") || chars.equalsIgnoreCase("NB") || chars.equalsIgnoreCase("EB")) {
            return name.substring(3).trim() + name.charAt(2) + name.substring(0, 2);
        }
        return name;
    }

    // Formats output for Shortest Path as found by Dijkstra (if it exists) and outputs error if not
    private static void printPath(int from, int to) {
        DijkstraSP sp = new DijkstraSP(graph, from);
        int finalVertex = -1;
        if (sp.hasPathTo(to)) {
            StringBuilder sb = new StringBuilder();
            sb.append("The path from ").append(from).append(" to ").append(to).append(" is as follows:\n");
            for (DirectedEdge edge : sp.pathTo(to)) {
                sb.append(edge.from()).append(" -> ");
                finalVertex = edge.to();
            }
            sb.append(finalVertex).append("\n");
            sb.append("This path has a cost of: ").append(sp.distTo(to)).append("\n");
            System.out.println(sb);
        } else
            System.out.println(ANSI_RED + "No path from " + from + " to " + to + " exists" + ANSI_RESET);
    }

    // When command h is entered, this function is called to output all available commands again to the terminal
    private static void printCommands() {
        System.out.println(ANSI_YELLOW + "Below are the following commands available:\n" + ANSI_RESET);
        System.out.println("Exit: e");
        System.out.println(ANSI_CYAN + "Exits the program\n" + ANSI_RESET);
        System.out.println("Help: h");
        System.out.println(ANSI_CYAN + "Lists these commands again\n" + ANSI_RESET);
        System.out.println("Shortest Path: sp <Source Stop ID> <Destination Stop ID>");
        System.out.println(ANSI_CYAN + "Finds shortest paths between the 2 bus stops, returning the list of stops en route as well as the associated “cost”." + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Example: sp 646 378\n" + ANSI_RESET);
        System.out.println("Stop Search: ss <Stop Name>");
        System.out.println(ANSI_CYAN + "Searches for a bus stop by the name inserted, and returns the full stop information for each stop matching" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Example: ss Hastings \n" + ANSI_RESET);
        System.out.println("Trip Arrival Search: st <Arrival Time (hh:mm:ss)>");
        System.out.println(ANSI_CYAN + "Searches for all trips containing the inputted arrival time and returns all trips sorted by trip ID" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Example: st 9:35:02 \n" + ANSI_RESET);
    }
}


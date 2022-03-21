import edu.princeton.cs.algs4.*;

public class FW {
    private double[][] distances;      // distTo[v][w] = length of shortest v->w path
    private DirectedEdge[][] edgeTo;   // edgeTo[v][w] = last edge on shortest v->w path

    public FW(EdgeWeightedDigraph graph) {
        distances = new double[graph.V()][graph.V()];
        edgeTo = new DirectedEdge[graph.V()][graph.V()];

        for (int i = 0; i < graph.V(); i++)
            for (int j = 0; j < graph.V(); j++)
                if (distances[i][j] == 0 && i != j)
                    distances[i][j] = Integer.MAX_VALUE;

        for(int v = 0; v < graph.V(); v++) {
            for (DirectedEdge edge : graph.adj(v)) {
                distances[edge.from()][edge.to()] = edge.weight();
                edgeTo[edge.from()][edge.to()] = edge;
            }
            if(distances[v][v] > 0.0){
                distances[v][v] = 0.0;
                edgeTo[v][v] = null;
            }
        }

        for (int i = 0; i < graph.V(); i++)
            for (int j = 0; j < graph.V(); j++) {
                if (edgeTo[j][i] == null) continue;
                for (int k = 0; k < graph.V(); k++) {
                    if (distances[j][i] + distances[i][k] < distances[j][k]) {
                        distances[j][k] = distances[j][i] + distances[i][k];
                        edgeTo[j][k] = edgeTo[i][k];
                    }
                }
            }
    }

    public double dist(int s, int t) {
        return distances[s][t];
    }

    public Iterable<DirectedEdge> path(int s, int t) {
        if (!hasPath(s, t)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[s][t]; e != null; e = edgeTo[s][e.from()]) {
            path.push(e);
        }
        return path;
    }

    public boolean hasPath(int s, int t) {
        return distances[s][t] < Integer.MAX_VALUE;
    }

}

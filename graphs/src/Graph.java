import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class Graph {
    int vertexCt;  // Number of vertices in the graph.
    int[][] capacity;  // Adjacency  matrix
    int [][] available; // copy of adjacency matrix
    String graphName;  //The file from which the graph was created.
    int maxFlow = 0;

    public Graph() {
        this.vertexCt = 0;
        this.graphName = "";
    }


    public int getVertexCt() {
        return vertexCt;
    }

    public boolean addEdge(int source, int destination, int cap) {
        //System.out.println("addEdge " + source + "->" + destination + "(" + cap + ")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        capacity[source][destination] = cap;
        available[source][destination] = cap;

        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nThe Graph " + graphName + " \n");

        for (int i = 0; i < vertexCt; i++) {
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d", capacity[i][j]));
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public void makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            capacity = new int[vertexCt][vertexCt];
            available = new int[vertexCt][vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                for (int j = 0; j < vertexCt; j++) {
                    capacity[i][j] = 0;
                    available[i][j] = 0;
                }
            }
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                if (!addEdge(v1, v2, cap))
                    throw new Exception();
            }
            reader.close();
            // added? -------------------------------------------------------
            this.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // NOTE FOR MATH; ABS VAL

    public int pathFlow() {
        ArrayList<Integer[]> queue = new ArrayList<Integer[]>();
        int i = 0;
        int max = 100;
        // for each row in graph
        int cols = available[0].length - 1;
        while (i < cols) {
            // start at rightmost col and search left
            int j = cols;
            // ignore if zero or negative
            while (available[i][j] <= 0 && j > 0) {
                j--;
            }
            if (j <= 0) {
                if (i > 0) {
                    // didn't find anything, doesn't connect
                    // back up to last connection point and check for other paths
                    Integer[] location = queue.get(queue.size() - 1);
                    int newI = location[0];
                    int newJ = location[1];
                    available[newI][newJ] = -available[newI][newJ];
                    max = location[2];

                    queue.remove(queue.size() - 1);
                    // reset loop
                    i = newI;
                } else {
                    // done
                    return 0;
                }
            } else {
                // add to queue
                Integer[] temp = new Integer[] {i, j, max};
                queue.add(temp);

                // check if new max
                if (available[i][j] < max) {
                    max = available[i][j];
                }

                // move to new row for next iteration
                i = j;
            }
        }

        StringBuilder nodeList = new StringBuilder();
        nodeList.append(0);

        // update matrix
        for (int x = 0 ; x < queue.size() ; x++) {
            Integer[] location = queue.get(x);
            int newI = location[0];
            int newJ = location[1];
            available[newI][newJ] -= max;
            nodeList.append(", " + newJ);
        }
        System.out.println("Found flow " + max + ": " + nodeList);
        queue.clear();
        // return flow value for path
        return max;
    }

    public void findFlow() {
        System.out.println("MAX FLOW:");
        int flag = 1;
        while (flag != 0) {
            flag = pathFlow();
            if (flag > 0) {
                // add to max flow
                maxFlow += flag;
            }
        }
        System.out.println("Produced: " + maxFlow);
        System.out.println();
        int flow;
        for (int i = 0 ; i < available.length ; i++) {
            for (int j = 0; j < available[0].length ; j++) {
                flow = capacity[i][j] - Math.abs(available[i][j]);
                if (flow > 0) {
                    System.out.println("Edge (" + i + ", " + j + ") transports " + flow + " cases" );
                }
            }
        }
    }

    public void minR(){
        int[] r = new int[vertexCt];
        // node 0 is always in R
        r[0] = 1;
        // finding nodes in r
        for (int i = 0; i < available.length ; i++) {
            for (int j = 1 ; j < available[0].length ; j++) {
                if (available[i][j] != 0 && r[i] != 0) {
                    r[j] = 1;
                }
            }
        }

        // check for edges leading out of r
        int flow = 0;
        System.out.println();
        System.out.println("MIN CUT: ");
        for (int i = 0; i < capacity.length ; i++) {
            for (int j = 1 ; j < capacity[0].length ; j++) {
                if (capacity[i][j] != 0 && r[i] != 0 && r[j] == 0) {
                    flow = capacity[i][j] - Math.abs(available[i][j]);
                    System.out.println("Edge (" + i + ", " + j + ") transports " + flow + " cases" );
                }
            }
        }
    }

    public static void main(String[] args) {
        Graph graph0 = new Graph();
        // put in text name, was empty before
        graph0.makeGraph("demands2.txt");
        graph0.findFlow();
        graph0.minR();
    }
}
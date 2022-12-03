import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * determine the max flow, min cuts, and edge flow
 * for given graphs
 */
public class Graph {
    int vertexCt;  // Number of vertices in the graph.
    int[][] capacity;  // Adjacency  matrix
    int [][] available; // copy of adjacency matrix
    int[] visited; // map of which nodes have been visited
    String graphName;  //The file from which the graph was created.
    int maxFlow = 0;    // max flow for graph
    int[] path;        // Distance to end node

    public Graph() {
        this.vertexCt = 0;
        this.graphName = "";
    }

    /**
     * add an edge of the graph between two nodes
     * @param source start node
     * @param destination end node
     * @param cap max flow edge can handle
     * @return if edge creation was successful
     */
    public boolean addEdge(int source, int destination, int cap) {
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        capacity[source][destination] = cap;
        available[source][destination] = cap;

        return true;
    }

    /**
     * create a matrix representation of the graph information
     * @return String of formatted matrix
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nThe Graph " + graphName + " \n");

        for (int i = 0; i < vertexCt; i++) {
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d", capacity[i][j]));
            }
            sb.append("\n");
        }
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * read in and process info of graph
     * @param filename file containing graph info
     */
    public void makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            capacity = new int[vertexCt][vertexCt];
            available = new int[vertexCt][vertexCt];
            visited = new int[vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                visited[i] = 0;
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

            // Populate array 'path' which determines the shortest
            // nominal distance from a node to the end node

            // Initialize path array and set end node value to zero
            path = new int[vertexCt];
            for(int i = 0; i < vertexCt; i++) {
                path[i] = 1000;
            }
            path[vertexCt - 1] = 0;
            // Iteratively determine number of edges to end node
            int found = 0;
            int iterations = 0;
            while (found < vertexCt - 1 && iterations < 1000) {
                iterations ++;
                found = 0;
                for (int i = vertexCt - 1; i > 0; i--) {
                    if (path[i] < 1000) {
                        found++;
                        for (int j = 0; j < vertexCt; j++) {
                            if ((capacity[j][i] != 0) && (path[i] + 1 < path[j])) {
                                // Add path value to upstream nodes
                                path[j] = path[i] + 1;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * find the shortest paths through the graph from the start point to the
     * destination, then print a report
     * @return 0 if current path has ended, or the max flow for the path
     */
    public int pathFlow() {
        int[] noLoops = new int[vertexCt]; // prevent loops in path
        for (int i = 0 ; i < vertexCt ; i++) {
            noLoops[i] = 0;
        }
        ArrayList<Integer[]> queue = new ArrayList<>();
        int i = 0;
        int max = 100;
        int score;
        int nextNode;
        // for each row in graph
        int counter = 0;
        int cols = available[0].length - 1;
        while (i < cols) {
            score = 1000;
            nextNode = 0;
            counter++;
            // check all cells for non zero vals
            for (int j = 1 ; j < vertexCt; j++) {
                if (available[i][j] > 0) {
                    // score based on number of times visited and distance from end
                    int scoreTest = path[j] + visited[j] + noLoops[j];
                    if (scoreTest < score) {
                        nextNode = j;
                        score = scoreTest;
                    }
                }
            }
            // didn't find anything
            if (nextNode <= 0) {
                if (i > 0) {
                    // back up to last connection point and check for other paths
                    Integer[] location = queue.get(queue.size() - 1);
                    int newI = location[0];
                    int newJ = location[1];
                    available[newI][newJ] = -available[newI][newJ];
                    max = location[2];
                    noLoops[newJ] = 0;

                    queue.remove(queue.size() - 1);
                    // reset loop
                    i = newI;
                } else {
                    // done
                    return 0;
                }
            } else {
                // add to queue
                Integer[] temp = new Integer[] {i, nextNode, max};
                queue.add(temp);
                noLoops[nextNode] = 1000;

                // check if new max
                if (available[i][nextNode] < max) {
                    max = available[i][nextNode];
                }

                // move to new row for next iteration
                i = nextNode;
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
            visited[newI]++;
        }
        System.out.println("Found flow " + max + ": " + nodeList);
        queue.clear();
        // return flow value for path
        return max;
    }

    /**
     * find valid paths and their max flows, and print
     * a report
     */
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
        maxFlow = 0;
    }

    /**
     * determine which nodes are in 'r', and identify
     * paths from nodes in r to nodes not in r for
     * the min cuts of the graph. Print report.
     */
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
        System.out.println();
    }

    public static void main(String[] args) {
        Graph graph0 = new Graph();
        String[] filenames = new String[] {"demands1.txt", "demands2.txt", "demands3.txt", "demands4.txt",
                "demands5.txt", "demands6.txt", "demands7.txt"};

        // to test one file only
//        System.out.println("demands1.txt");
//        graph0.makeGraph("demands1.txt");
//        graph0.findFlow();
//        graph0.minR();

        // to test all files
        for (String filename : filenames) {
            System.out.println(filename);
            graph0.makeGraph(filename);
            graph0.findFlow();
            graph0.minR();
        }
    }
}
package com.company;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;


class Node {
    private String name;
    private LinkedList < Edge > edgeList;

    public Node(String name) {
        this.name = name;
        edgeList = new LinkedList < > ();
    }

    public String getName() {
        return name;
    }

    public LinkedList < Edge > getEdges() {
        return edgeList;
    }
}

class Edge {
    private int weight;
    private Node destVertex;

    public Edge(Node dest, int w) {
        this.destVertex = dest;
        this.weight = w;
    }

    public Edge(Node dest) {
        this.destVertex = dest;
        this.weight = 1;
    }

    public int getWeight() {
        return weight;
    }

    public Node getDestVertex() {
        return destVertex;
    }
}

class Graph {
    private HashSet < Node > nodes;

    public Graph() {
        nodes = new HashSet < > ();
    }

    public boolean AddEdge(Node v1, Node v2, int weight) {
        return v1.getEdges().add(new Edge(v2, weight)) && v2.getEdges().add(new Edge(v1, weight));
    }

    public boolean AddVertex(Node v) {
        return nodes.add(v);
    }

    public void printGraph() {

        for (Node v: nodes) {
            System.out.print("vertex name: " + v.getName() + ":\n");
            for (Edge e: v.getEdges()) {
                System.out.print("destVertex: " + e.getDestVertex().getName() + ", weight: " + e.getWeight() + "\n");
            }
            System.out.print("\n");
        }
    }
}

class FindGraphProperties{
    private Map<String,Node> uniqueNodesList = new HashMap<>();
    private Graph ourGraph = new Graph();
    private Set<String> stringNode = new HashSet<String>();
    private List<List<Integer>> connectedCompLists;

    public FindGraphProperties(String filepath){
       readEdgeFile(filepath);
      int components= findConnectedComponents();
        System.out.println("_______________________"+components);
        findAllNodesCentrality();
       /*createNodes(this.uniqueNodesList);
       readAndCreateEdges(filepath);*/
    }


    public void readEdgeFile(String path){

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    path));
            String line = reader.readLine();
            while (line != null) {
                String[] nodes = line.split(" ", 2);
                System.out.println("Node "+ nodes[0] + "Node " + nodes[1]);
                // read next line
                Node n1 = null;
                Node n2 = null;
                if(stringNode.contains(nodes[0]))
                {
                    n1 = uniqueNodesList.get(nodes[0]);
                }
                else
                {

                    n1 = new Node(nodes[0]);
                    stringNode.add(nodes[0]);
                    uniqueNodesList.put(nodes[0],n1);
                }
                if(stringNode.contains(nodes[1]))
                {
                    n2 = uniqueNodesList.get(nodes[1]);
                }
                else
                {
                    stringNode.add(nodes[1]);
                    n2 = new Node(nodes[1]);
                    ourGraph.AddVertex(n1);
                    uniqueNodesList.put(nodes[1],n2);
                }
                // n1.getEdges().add(new Edge(n2,1));
                // n2.getEdges().add(new Edge(n1,1));
                createEdges(n2,n1);

                // Adding only unique nodes into Hashset
                //uniqueNodesList.put(nodes[1],n2);
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAndCreateEdges(String path){

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(
                    path));
            String line = reader.readLine();
            while (line != null) {
                List<Node> pairs = new LinkedList<Node>();
                String[] nodes = line.split(" ", 2);
                System.out.println("Node "+ nodes[0] + "Node " + nodes[1]);
                // read next line

                // Adding only unique nodes into Hashset
                String n1 = new String(nodes[0]);
                String n2 = new String(nodes[1]);

                if(uniqueNodesList.containsKey(n1)){
                    Node n11 = uniqueNodesList.get(n1);
                    pairs.add(n11);
                }
                else
                {
                    Node n11 = new Node(n1);
                    uniqueNodesList.put(n1,n11);
                    pairs.add(n11);
                }
                if(uniqueNodesList.containsKey(n2)){

                    Node n12 = uniqueNodesList.get(n2);
                    pairs.add(n12);
                }
                else
                {
                    Node n12 = new Node(n2);
                    uniqueNodesList.put(n1,n12);
                    pairs.add(n12);
                }


                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createNodes(Map<String,Node> nodes)
    {
        for (String s: nodes.keySet())
        {
            Node newNode = new Node(s);
            ourGraph.AddVertex(newNode);
        }
    }

    public void createEdges(Node n1, Node n2)
    {
        // creating a bidirectional edge with default weights
        ourGraph.AddEdge(n1, n2, 1);
    }

    /* DFS
    *
    * Step 1 we need to iterate over hashset which contains all nodes
    * Step 2 Each node contains edge list iterate over the edge list
    * Check 1: maintain all the visited edges if edge is already visited
    * check 2:
    * */

    public void printGraph()
    {
        ourGraph.printGraph();
    }

    public void bfs(Node node, int level, Map<Node, Integer> distances,  Map<Node,Boolean> visitedNodes)
    {
        if(visitedNodes.get(node)) {
            if(distances.get(node)>level){
                distances.put(node, level);
            }
            else return;
        }
        else {
            visitedNodes.put(node, true);
           distances.put(node, level);
        }

        for( Edge e: node.getEdges()){
            Node neighbour=e.getDestVertex();
            bfs(neighbour, level+1, distances, visitedNodes);

        }

        return;
    }
    
	//id   its mesures
    public Map<Integer, List<Double>> findAllNodesCentrality(){

        Map<Node,Boolean> visitedNodes = new HashMap<>();
        Map<Integer, List<Double>> nodeCentrality= new HashMap<>();

        for(String s: uniqueNodesList.keySet()) {
            visitedNodes.put(uniqueNodesList.get(s), false);
            nodeCentrality.put(Integer.parseInt(s), new ArrayList<>());
        }
        int ans=0;
        for(List<Integer> list: connectedCompLists){

            for(Integer node: list){
                Map<Node, Integer> distances= new HashMap<>();
                for(String s: uniqueNodesList.keySet()) {
                    distances.put(uniqueNodesList.get(s), 1000);
                    visitedNodes.put(uniqueNodesList.get(s), false);
                }
                Node curr= uniqueNodesList.get(""+node);


                bfs(curr, 0, distances, visitedNodes);

                for(Node n: distances.keySet()){
                    ans += distances.get(n)==1000?0:distances.get(n);
                }
                double centrality= (double)ans/(double)(uniqueNodesList.size()-1);
                nodeCentrality.get(node).add(centrality);
                ans=0;

            }
        }
        System.out.println(nodeCentrality.toString());
    return nodeCentrality;
    }


    /*
    *
    * Algorithm flow 
    * 
    /**
     * 1. over the entire set of nodes.
     * set of all unique nodes-> populate your map key: node -> value: -1;
     * int counter=0;
     * Create a hashMap node->component number
     * for all nodes{
     * if(map.get(node[i])==-1)
     * {traverseIt(node[i], counter, map);
     *  counter++; keeps track of which connected component is traversed
     *  }
     *  }
     *  traversal(node, counter, map){
     *  if(map.get(node)!=-1) return;
     *      map.put(node, counter);
     *      for(nodeNeighbours){
     *          traversal(neighbour, counter,map);
     *      }
     *      return;
     *  }
     */
    public int findConnectedComponents(){
        int counter=0;
        connectedCompLists =new ArrayList<>();
        //uniqueNodesList
        Map<String, Integer> map = new HashMap<>();
        for(String s: stringNode) map.put(s, -1);

        for(String key: map.keySet()){
            if(map.get(key)==-1){
                List<Integer> n = new ArrayList<>();
                n.add(Integer.parseInt(key));
                map.put(key, counter);
                markComponent(key, map, counter,n);
                System.out.println("#########********######"+n.toString());
                connectedCompLists.add(n);
                counter++;
            }

        }
    return counter;

    }

    /* This method helps group the components
    *
    * Marking visited component.
    * */
   public void markComponent(String key,Map<String, Integer> map, int counter,List<Integer> n){

        Node curr = uniqueNodesList.get(key);
        for(Edge e: curr.getEdges()){
            Node neighbour = e.getDestVertex();
            String name = neighbour.getName();
            if(map.get(name)==-1){
                map.put(name, counter);
                markComponent(name, map, counter,n);
               n.add(Integer.parseInt(name));
            }
        }
        return;
   }
}

public class Main {
    public static void main(String[] args) {

        String filepath = "C:/Users/ashwin/edges.txt";
        FindGraphProperties graphProps = new FindGraphProperties(filepath);
        graphProps.printGraph();
    }
}
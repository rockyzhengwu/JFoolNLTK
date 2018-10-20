package me.midday.lexical;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Edge implements Comparable{
    int start ;
    int end ;
    Double weight;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public  Edge(int start, int end, Double weight)  {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public int compareTo(Object o) {
        Edge otherEdge = (Edge)o;
        if(weight < otherEdge.weight ) {
            return -1;
        }else if (weight == otherEdge.weight) {
            return 0;
        }

        return  1;
    }
}


public class Graph {
    private Map<Integer, List<Edge>> nodes = new HashMap<>();
    private int length;

    public Graph(int length){
        for (int i = 0; i < length; i++){
            nodes.put(i, new ArrayList<>());
            nodes.get(i).add(new Edge(i, i+1, 1.0));
        }
        this.length = length;
    }
    public void addEdge(int start, int end, Double weight){
        Edge edge = new Edge(start, end, weight);
        nodes.get(start).add(edge);
    }

    public void addEdge(Edge edge){
        nodes.get(edge.start).add(edge);
    }

    public List<Pair<Integer, Integer>> extract(){
        List<Pair<Integer, Integer>> cutIndex = new ArrayList<>();

        Map<Integer, Edge> routes = new HashMap<>();
        int edgeCount = this.length;
        routes.put(length-1, new Edge(length-1, length, 0.0));

        for(int i = edgeCount - 2; i > -1 ; i--){
            List<Edge> tmpEdges = nodes.get(i);
            // 所有的出口
            double maxWeight = 0.0;
            Edge maxEdge = null;
            for(Edge edge: tmpEdges){
                double tmpWeight = edge.getWeight() + routes.get(edge.getEnd()).getWeight();
                if(tmpWeight > maxWeight){
                    maxWeight = tmpWeight;
                    maxEdge = edge;
                }
                routes.put(i, maxEdge);
            }
        }

        Edge e = null;

        for (int i = 0; i < edgeCount - 1; i++) {
            e = routes.get(i);
            cutIndex.add(new Pair(e.getStart(), e.getEnd()));
            i = e.getEnd() - 1;
        }
        return cutIndex;
    }

}

package me.lexical;

import javafx.util.Pair;
import me.midday.lexical.Graph;

import java.util.List;

public class GraphDemo {
    public static void main(String[] args){
        Graph graph = new Graph(6);
        graph.addEdge(0, 2, 10.0);
        graph.addEdge(1, 5, 20.0);
        graph.addEdge(3, 5, 50.0);
        graph.addEdge(2, 4, 80.0);
        List<Pair<Integer, Integer>> pairs = graph.extract();
        for (Pair<Integer, Integer> p: pairs) {
            int start = p.getKey();
            int end = p.getValue();
            System.out.println(String.valueOf(start)  + " = " + String.valueOf(end));
        }
    }
}

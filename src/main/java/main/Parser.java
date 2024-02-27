package main;
import cse332.exceptions.NotYetImplementedException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list
     */
    private static ArrayList<Map<Integer, Integer>> data;
    public static ArrayList<Map<Integer, Integer>> parse(int[][] adjMatrix) {
        data = new ArrayList<>();
        for(int i = 0; i < adjMatrix[0].length; i++){
            data.add(i, (new HashMap<>()));
        }
        for(int i = 0; i < adjMatrix[0].length; i++){
            for(int j = 0; j < adjMatrix.length; j++){
                if(adjMatrix[i][j] != Integer.MAX_VALUE){
                    data.get(i).put(j,adjMatrix[i][j]);
                }
            }
        }
        return data;
    }


    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight with incoming edges
     */
    public static ArrayList<Map<Integer, Integer>> parseInverse(int[][] adjMatrix) {
        throw new NotYetImplementedException();
    }

}

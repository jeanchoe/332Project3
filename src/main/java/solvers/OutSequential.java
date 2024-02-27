package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import java.util.ArrayList;
import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;

public class OutSequential implements BellmanFordSolver {
    private int[] firstStore;
    private int[] secondStore;
    private int[] pointVal;

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> data = Parser.parse(adjMatrix);
        firstStore = new int[data.size()];
        secondStore = new int[data.size()];
        pointVal = new int[data.size()];

        for(int i = 0; i < data.size(); i++){
            firstStore[i] = Integer.MAX_VALUE;
            secondStore[i] = Integer.MAX_VALUE;
            pointVal[i] = -1;
        }
        firstStore[source] = 0;
        secondStore[source] = 0;
        for(int i = 0; i < data.size(); i++){
            for(int j = 0; j < data.size(); j++){
                if(secondStore[j] == Integer.MAX_VALUE) continue;
                for(int record : data.get(j).keySet()){
                    int newRecord = data.get(j).get(record);
                    testValue(record, j, newRecord);
                    testValue(record, j, newRecord);
                }
                for(int k = 0; k < data.size(); k++){
                    secondStore[k] = firstStore[k];
                }
            }
        }
        return GraphUtil.getCycle(pointVal);
    }
    private void testValue(int firstData, int i, int secondData){
        if(firstStore[firstData] > secondStore[secondData]){
            firstStore[firstData] = secondStore[secondData] + secondData;
            pointVal[firstData] = i;
        }
    }

}

package solvers;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutSequential implements BellmanFordSolver {
    private int[] firstList;
    private int[] secondList;
    private int[] pointVal;

    @Override
    public List<Integer> solve(int[][] adjMatrix, int source) {
        ArrayList<Map<Integer, Integer>> graph = Parser.parse(adjMatrix);
        initialize(graph.size(), source);

        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.size(); j++) {
                if (secondList[j] == Integer.MAX_VALUE) continue;
                for (Map.Entry<Integer, Integer> edge : graph.get(j).entrySet()) {
                    test(edge.getKey(), j, edge.getValue());
                }
            }
            update(graph.size());
        }

        return GraphUtil.getCycle(pointVal);
    }

    private void initialize(int length, int source) {
        firstList = new int[length];
        secondList = new int[length];
        pointVal = new int[length];

        for (int i = 0; i < length; i++) {
            firstList[i] = Integer.MAX_VALUE;
            secondList[i] = Integer.MAX_VALUE;
            pointVal[i] = -1;
        }
        firstList[source] = 0;
        secondList[source] = 0;
    }

    private void update(int length) {
        for (int i = 0; i < length; i++) {
            secondList[i] = firstList[i];
        }
    }

    private void test(int end, int source, int i) {
        if (firstList[end] > secondList[source] + i) {
            firstList[end] = secondList[source] + i;
            pointVal[end] = source;
        }
    }
}
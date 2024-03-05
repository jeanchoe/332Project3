package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import cse332.graph.GraphUtil;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskBad;

public class OutParallelBad implements BellmanFordSolver {

    int[] firstList;
    int[] secondList;
    int[] pointVal;

    public void solve(int size){
        this.firstList = new int[size];
        this.secondList = new int[size];
        this.pointVal = new int[size];
    }

    public List<Integer> solve(int[][] adjMatrix, int source) {

        ArrayList<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        solve(g.size());
        for(int i = 0; i < g.size(); i++){
            firstList[i] = Integer.MAX_VALUE;
            pointVal[i] = Integer.MIN_VALUE;
        }
        restart(source);

        for(int i = 0; i < g.size(); i++){
            secondList = ArrayCopyTask.copy(firstList);
            RelaxOutTaskBad.parallel(firstList, secondList, pointVal, g);
        }
        return GraphUtil.getCycle(pointVal);
    }

    private void restart(int point){
        firstList[point] = 0;
    }

}

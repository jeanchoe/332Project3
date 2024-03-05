package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxInTask;
import paralleltasks.RelaxOutTaskBad;
import paralleltasks.RelaxOutTaskLock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class InParallel implements BellmanFordSolver {
    int[] firstList;
    int[] secondList;
    int[] pointVal;

    public void solve(int size){
        this.firstList = new int[size];
        this.secondList = new int[size];
        this.pointVal = new int[size];

    }
    public List<Integer> solve(int[][] adjMatrix, int source) {
        ArrayList<Map<Integer, Integer>> g = Parser.parseInverse(adjMatrix);
        solve(g.size());
        for(int i = 0; i < g.size(); i++){
            firstList[i] = Integer.MAX_VALUE;
            resetEdge(i);
        }
        reset(source);
        for(int i = 0; i < g.size(); i++){
            secondList = ArrayCopyTask.copy(firstList);
            RelaxInTask.parallel(firstList, secondList, pointVal, g);
        }
        return GraphUtil.getCycle(pointVal);
    }
    private void reset(int point){
        firstList[point] = 0;
    }

    private void resetEdge(int i ){
        pointVal[i] = Integer.MIN_VALUE;
    }
}
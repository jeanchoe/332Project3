package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskBad;
import paralleltasks.RelaxOutTaskLock;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;


public class OutParallelLock implements BellmanFordSolver {

    int[] firstList;
    int[] secondList;
    int[] pointVal;

    ReentrantLock[] locks;
    public List<Integer> solve(int[][] adjMatrix, int source) {
        ArrayList<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
            locks = new ReentrantLock[adjMatrix[0].length];
            firstList = new int[g.size()];
            secondList = new int[g.size()];
            pointVal = new int[g.size()];

            for(int i = 0; i < g.size(); i++){
                firstList[i] = Integer.MAX_VALUE;
                pointVal[i] = Integer.MIN_VALUE;
                lock(i);
            }
            reset (source);

            for(int i = 0; i < g.size(); i++){
                secondList = ArrayCopyTask.copy(firstList);
                RelaxOutTaskLock.parallel(firstList, secondList, pointVal, g, locks);
            }
            return GraphUtil.getCycle(pointVal);
    }

    private void lock(int i ){
        locks[i] = new ReentrantLock();
    }

    private void reset(int point){
        firstList[point] = 0;
    }
}

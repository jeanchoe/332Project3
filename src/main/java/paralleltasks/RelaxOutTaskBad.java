package paralleltasks;

import cse332.exceptions.NotYetImplementedException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskBad extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private final int lo;
    private final int hi;
    private final int[] firstList;
    private final int[] secondList;
    private final int[] pointVal;

    private final ArrayList<Map<Integer, Integer>> g;

    public RelaxOutTaskBad(int[] firstList, int[] secondList, int[] pointVal, int lo, int hi, ArrayList<Map<Integer, Integer>> g) {
        this.firstList = firstList;
        this.secondList = secondList;
        this.pointVal = pointVal;
        this.lo = lo;
        this.hi = hi;
        this.g = g;
    }

    protected void compute() {
        if (hi - lo > CUTOFF) {

        } else {
            sequential(lo, hi, firstList, secondList, pointVal, g);
            return;
        }

        RelaxOutTaskBad leftSide = new RelaxOutTaskBad(firstList, secondList, pointVal, lo, lo + (hi - lo) / 2, g);
        leftSide.fork();
        RelaxOutTaskBad rightSide = new RelaxOutTaskBad(firstList, secondList, pointVal, lo + (hi - lo) / 2, hi, g);
        rightSide.compute();
        leftSide.join();
    }

    public static void sequential(int lo, int hi, int[] firstList, int[] secondList, int[] pointVal, ArrayList<Map<Integer, Integer>> g) {

        int k = lo;
        do {
            Set<Integer> v = g.get(k).keySet();
            for (int j : v) {
                if (secondList[k] != Integer.MAX_VALUE && firstList[j] > secondList[k] + g.get(k).get(j)) {
                    firstList[j] = secondList[k] + g.get(k).get(j);
                    pointVal[j] = k;
                }
            }
            k++;
        } while(k<hi);

    }

    public static void parallel(int[] firstList, int[] secondList, int[] pointVal,  ArrayList<Map<Integer, Integer>> g) {
        pool.invoke(new RelaxOutTaskBad(firstList, secondList, pointVal, 0, g.size(), g));
    }

}
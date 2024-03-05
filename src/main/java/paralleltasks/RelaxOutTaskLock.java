package paralleltasks;

import cse332.exceptions.NotYetImplementedException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class RelaxOutTaskLock extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private final int bot;
    private final int top;
    private final int[] firstList;
    private final int[] secondList;
    private final int[] pointVal;


    private final ArrayList<Map<Integer, Integer>> g;
    private final ReentrantLock[] locks;

    public RelaxOutTaskLock(int[] firstList, int[] secondList, int[] pointVal, int bot, int top, ArrayList<Map<Integer, Integer>> g, ReentrantLock[] locks) {
        this.firstList = firstList;
        this.secondList = secondList;
        this.pointVal = pointVal;
        this.bot = bot;
        this.top = top;
        this.g = g;
        this.locks = locks;
    }

    protected void compute() {
        if (top - bot > CUTOFF) {

        } else {
            sequential(bot, top, firstList, secondList, pointVal, g,locks);
            return;
        }

        RelaxOutTaskLock leftSide = new RelaxOutTaskLock(firstList, secondList, pointVal, bot, bot + (top - bot) / 2, g, locks);

        RelaxOutTaskLock rightSide = new RelaxOutTaskLock(firstList, secondList, pointVal, bot + (top - bot) / 2, top, g, locks);
        leftSide.fork();
        rightSide.compute();
        leftSide.join();
    }

    public static void sequential(int bot, int top, int[] firstList, int[] secondList, int[] pointVal, ArrayList<Map<Integer, Integer>> g, ReentrantLock[] locks) {

        int k = bot;
        do {
            //Set<Integer> v = g.get(k).keySet();
            for (int j : g.get(k).keySet()) {
                if (secondList[k] != Integer.MAX_VALUE && firstList[j] > secondList[k] + g.get(k).get(j)) {
                    firstList[j] = secondList[k] + g.get(k).get(j);
                    pointVal[j] = k;
                }
            }

            k++;

        } while(k<top);

    }

    public static void parallel(int[] firstList, int[] secondList, int[] pointVal,  ArrayList<Map<Integer, Integer>> g, ReentrantLock[] locks) {
        pool.invoke(new RelaxOutTaskLock(firstList, secondList, pointVal, 0, g.size(), g, locks));
    }
}
package paralleltasks;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskBad extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private final int top;
    private final int bot;
    private final int[] firstList;
    private final int[] secondList;
    private final int[] pointVal;

    private final ArrayList<Map<Integer, Integer>> g;

    public RelaxOutTaskBad(int[] firstList, int[] secondList, int[] pointVal,
                        int top, int bot, ArrayList<Map<Integer, Integer>>g) {
        this.firstList = firstList;
        this.secondList = secondList;
        this.pointVal = pointVal;
        this.top = top;
        this.bot = bot;
        this.g = g;
    }

    protected void compute() {
        if(top - bot > CUTOFF){
        } else {
            sequential(bot, top, firstList, secondList, pointVal, g);
        }
        RelaxOutTaskBad leftSide = new RelaxOutTaskBad(firstList, secondList, pointVal, bot, bot + (top - bot) / 2, g);
        leftSide.fork();
        RelaxOutTaskBad rightSide = new RelaxOutTaskBad(firstList, secondList, pointVal, bot + (top - bot) / 2, top, g);
        rightSide.compute();
        leftSide.join();
    }

    public static void sequential(int bot, int top, int[] firstList, int[] secondList, int[] pointVal, ArrayList<Map<Integer, Integer>>g) {
        int low = bot;
        do {
            Set<Integer> v = g.get(low).keySet();
            for (int i : v) {
                if (secondList[low] != Integer.MAX_VALUE && firstList[i] > secondList[low] + g.get(low).get(i)) {
                    firstList[i] = secondList[low] + g.get(low).get(i);
                    pointVal[i] = low;
                }
            }
            low++;
        } while(low < top);

    }

    public static void parallel(int[] firstList, int[] secondList, int[] pointVal, ArrayList<Map<Integer, Integer>>g) {
        pool.invoke(new RelaxOutTaskBad(firstList, secondList, pointVal, 0, g.size(), g));
    }

}

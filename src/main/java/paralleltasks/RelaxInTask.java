package paralleltasks;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxInTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    final int lo, hi;
    private final int[] firstList, secondList, pointVal;

    private final ArrayList<Map<Integer, Integer>> g;

    public RelaxInTask(int[] firstList, int[] secondList, int[] pointVal, int lo, int hi, ArrayList<Map<Integer, Integer>> g) {
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
            sequential(hi, lo,g, firstList, secondList, pointVal);
            return;
        }



        RelaxInTask left = new RelaxInTask(firstList, secondList, pointVal, lo, lo + (hi - lo) / 2, g);
        RelaxInTask right = new RelaxInTask(firstList, secondList, pointVal, lo + (hi - lo) / 2, hi, g);

        left.fork();
        right.compute();
        left.join();
    }

    public static void sequential(int hi, int lo, ArrayList<Map<Integer, Integer>> g, int[] firstList, int[] secondList, int[] pointVal) {
        int lo1 = lo;
        while (lo1 < hi) {
            for (int i = lo; i < hi; i++) {
                for (int j : g.get(i).keySet()) {
                    if (secondList[j] == Integer.MAX_VALUE || firstList[i] <= secondList[j] + g.get(i).get(j)) {

                    }
                    else {
                        firstList[i] = secondList[j] + g.get(i).get(j);
                        pointVal[i] = j;
                    }
                }
                lo1++;
            }
            while (lo1 < hi) ;

        }
    }

    public static void parallel(int[] firstList, int[] secondList, int[] pointVal,  ArrayList<Map<Integer, Integer>> g) {
        pool.invoke(new RelaxInTask(firstList, secondList, pointVal, 0, g.size(), g));
    }

}
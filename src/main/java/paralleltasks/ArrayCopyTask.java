package paralleltasks;



import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ArrayCopyTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    public static int[] copy(int[] src) {
        int[] dst = new int[src.length];
        pool.invoke(new ArrayCopyTask(src, dst, 0, src.length));
        return dst;
    }

    private final int[] src, dst;
    private final int lo, hi;

    @SuppressWarnings("ManualArrayCopy")
    protected void compute() {

        if (hi - lo > CUTOFF) {
        } else {
            int i = lo;
            do{
                this.dst[i] = this.src[i];
                i++;
            } while(i<hi);
            return;
        }



        ArrayCopyTask left = new ArrayCopyTask(src, dst, lo,   lo + (hi - lo) / 2);
        left.fork();
        ArrayCopyTask right = new ArrayCopyTask(src, dst,   lo + (hi - lo) / 2, hi);
        right.compute();
        left.join();
    }
    public ArrayCopyTask(int[] src, int[] dst, int lo, int hi) {
        this.src = src;
        this.dst = dst;
        this.lo = lo;
        this.hi = hi;
    }


}
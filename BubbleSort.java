import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public final class BubbleSort implements SortAlgorithm {
    @Override
    public String name() {
        return "Bubble Sort";
    }

    @Override
    public void sort(int[] values) {
        for (int end = values.length - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++) {
                if (values[i] > values[i + 1]) {
                    int temp = values[i];
                    values[i] = values[i + 1];
                    values[i + 1] = temp;
                    changed = true;
                }
            }
            if (!changed) {
                return;
            }
        }
    }

    @Override
    public void parallelSort(int[] values, int threads) {
        ParallelSortSupport.parallelChunkSort(values, threads, this::sort);
    }
}

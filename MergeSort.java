import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public final class MergeSort implements SortAlgorithm {
    private static final int SEQUENTIAL_THRESHOLD = 2_048;

    @Override
    public String name() {
        return "Merge Sort";
    }

    @Override
    public void sort(int[] values) {
        if (values.length < 2) {
            return;
        }
        int[] buffer = new int[values.length];
        mergeSort(values, buffer, 0, values.length);
    }

    private void mergeSort(int[] values, int[] buffer, int start, int end) {
        if (end - start < 2) {
            return;
        }
        int middle = (start + end) >>> 1;
        mergeSort(values, buffer, start, middle);
        mergeSort(values, buffer, middle, end);
        merge(values, buffer, start, middle, end);
    }

    private void merge(int[] values, int[] buffer, int start, int middle, int end) {
        int left = start;
        int right = middle;
        int target = start;
        while (left < middle && right < end) {
            buffer[target++] = values[left] <= values[right] ? values[left++] : values[right++];
        }
        while (left < middle) buffer[target++] = values[left++];
        while (right < end) buffer[target++] = values[right++];
        System.arraycopy(buffer, start, values, start, end - start);
    }

    @Override
    public void parallelSort(int[] values, int threads) {
        if (values.length < 2) {
            return;
        }
        ForkJoinPool pool = new ForkJoinPool(Math.max(1, threads));
        try {
            pool.invoke(new MergeTask(values, new int[values.length], 0, values.length));
        } finally {
            pool.shutdown();
        }
    }

    private final class MergeTask extends RecursiveAction {
        private final int[] values;
        private final int[] buffer;
        private final int start;
        private final int end;

        private MergeTask(int[] values, int[] buffer, int start, int end) {
            this.values = values;
            this.buffer = buffer;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start < 2) {
                return;
            }
            if (end - start < SEQUENTIAL_THRESHOLD) {
                mergeSort(values, buffer, start, end);
                return;
            }
            int middle = (start + end) >>> 1;
            invokeAll(new MergeTask(values, buffer, start, middle),
                    new MergeTask(values, buffer, middle, end));
            merge(values, buffer, start, middle, end);
        }
    }
}

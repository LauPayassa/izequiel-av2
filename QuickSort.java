import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public final class QuickSort implements SortAlgorithm {
    private static final int SEQUENTIAL_THRESHOLD = 2_048;

    @Override
    public String name() {
        return "Quick Sort";
    }

    @Override
    public void sort(int[] values) {
        quickSort(values, 0, values.length - 1);
    }

    private void quickSort(int[] values, int low, int high) {
        if (low >= high) {
            return;
        }
        int pivot = partition(values, low, high);
        quickSort(values, low, pivot - 1);
        quickSort(values, pivot + 1, high);
    }

    private int partition(int[] values, int low, int high) {
        int pivotIndex = low + ((high - low) >>> 1);
        swap(values, pivotIndex, high);
        int pivot = values[high];
        int index = low;
        for (int i = low; i < high; i++) {
            if (values[i] <= pivot) {
                swap(values, i, index++);
            }
        }
        swap(values, index, high);
        return index;
    }

    private static void swap(int[] values, int first, int second) {
        int temp = values[first];
        values[first] = values[second];
        values[second] = temp;
    }

    @Override
    public void parallelSort(int[] values, int threads) {
        int parallelism = Math.max(1, Math.min(threads, values.length == 0 ? 1 : values.length));
        ForkJoinPool pool = new ForkJoinPool(parallelism);
        try {
            pool.invoke(new QuickTask(values, 0, values.length - 1));
        } finally {
            pool.shutdown();
        }
    }

    private final class QuickTask extends RecursiveAction {
        private final int[] values;
        private final int low;
        private final int high;

        private QuickTask(int[] values, int low, int high) {
            this.values = values;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (low >= high) {
                return;
            }
            if (high - low < SEQUENTIAL_THRESHOLD) {
                quickSort(values, low, high);
                return;
            }
            int pivot = partition(values, low, high);
            invokeAll(new QuickTask(values, low, pivot - 1),
                    new QuickTask(values, pivot + 1, high));
        }
    }
}

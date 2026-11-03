import java.util.Arrays;

public final class CountingSort implements SortAlgorithm {
    @Override
    public String name() {
        return "Counting Sort";
    }

    @Override
    public void sort(int[] values) {
        if (values.length < 2) {
            return;
        }
        int min = values[0];
        int max = values[0];
        for (int value : values) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        long range = (long) max - min + 1;
        if (range > 10_000_000L) {
            Arrays.sort(values);
            return;
        }
        int[] counts = new int[(int) range];
        for (int value : values) counts[value - min]++;
        int index = 0;
        for (int i = 0; i < counts.length; i++) {
            for (int count = counts[i]; count > 0; count--) values[index++] = i + min;
        }
    }

    @Override
    public void parallelSort(int[] values, int threads) {
        ParallelSortSupport.parallelChunkSort(values, threads, this::sort);
    }
}

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.stream.IntStream;

final class ParallelSortSupport {
    private ParallelSortSupport() {
    }

    static void parallelChunkSort(int[] values, int threads, Consumer<int[]> sorter) {
        if (values.length < 2) {
            return;
        }
        int chunks = Math.max(1, Math.min(threads, values.length));
        ForkJoinPool pool = new ForkJoinPool(chunks);
        try {
            int chunkSize = (values.length + chunks - 1) / chunks;
            int[][] sortedChunks = new int[chunks][];
            pool.submit(() -> IntStream.range(0, chunks).parallel().forEach(index -> {
                int start = index * chunkSize;
                int end = Math.min(values.length, start + chunkSize);
                if (start < end) {
                    sortedChunks[index] = Arrays.copyOfRange(values, start, end);
                    sorter.accept(sortedChunks[index]);
                }
            })).join();
            for (int index = 0; index < chunks; index++) {
                if (sortedChunks[index] == null) {
                    sortedChunks[index] = new int[0];
                }
            }
            mergeChunks(values, sortedChunks);
        } finally {
            pool.shutdown();
        }
    }

    private static void mergeChunks(int[] target, int[][] chunks) {
        int[] positions = new int[chunks.length];
        for (int output = 0; output < target.length; output++) {
            int selected = -1;
            for (int chunk = 0; chunk < chunks.length; chunk++) {
                if (positions[chunk] < chunks[chunk].length
                        && (selected < 0 || chunks[chunk][positions[chunk]] < chunks[selected][positions[selected]])) {
                    selected = chunk;
                }
            }
            target[output] = chunks[selected][positions[selected]++];
        }
    }
}

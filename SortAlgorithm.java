public interface SortAlgorithm {
    String name();

    void sort(int[] values);

    void parallelSort(int[] values, int threads);
}

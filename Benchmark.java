import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public final class Benchmark {
    private static final int[] TAMANHOS = {1_000, 5_000, 10_000};
    private static final int[] THREADS = {1, 2, 4};
    private static final int AMOSTRAS = 5;
    private static final Path OUTPUT = Paths.get("resultados", "benchmark.csv");
    private static final List<SortAlgorithm> ALGORITHMS = List.of(
            new BubbleSort(), new QuickSort(), new MergeSort(), new CountingSort());
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private Benchmark() {
    }

    public static void main(String[] args) throws IOException {
        Files.createDirectories(OUTPUT.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(OUTPUT, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("algoritmo,modo,tamanho,tipo_dados,threads,processadores_disponiveis,amostra,tempo_ns");
            writer.newLine();
            for (int size : TAMANHOS) {
                for (GeradorDados.Tipo type : GeradorDados.Tipo.values()) {
                    for (SortAlgorithm algorithm : ALGORITHMS) {
                        for (int sample = 1; sample <= AMOSTRAS; sample++) {
                            int[] input = GeradorDados.gerar(size, type, 2026L + sample);
                            writeResult(writer, algorithm, "serial", input, type, 1, sample);
                            for (int threadCount : THREADS) {
                                writeResult(writer, algorithm, "paralelo", input, type, threadCount, sample);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Resultados gravados em " + OUTPUT.toAbsolutePath());
    }

    private static void writeResult(BufferedWriter writer, SortAlgorithm algorithm, String mode,
                                    int[] input, GeradorDados.Tipo type, int threads, int sample)
            throws IOException {
        int[] values = Arrays.copyOf(input, input.length);
        long start = System.nanoTime();
        if ("serial".equals(mode)) algorithm.sort(values);
        else algorithm.parallelSort(values, threads);
        long elapsed = System.nanoTime() - start;
        if (!isSorted(values)) throw new IllegalStateException(algorithm.name() + " produziu resultado invalido");
        writer.write(String.format("%s,%s,%d,%s,%d,%d,%d,%d%n",
                algorithm.name(), mode, values.length, type, threads,
                AVAILABLE_PROCESSORS, sample, elapsed));
    }

    private static boolean isSorted(int[] values) {
        for (int i = 1; i < values.length; i++) if (values[i - 1] > values[i]) return false;
        return true;
    }
}

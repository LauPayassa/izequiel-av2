import java.util.Random;

public final class GeradorDados {
    public enum Tipo {
        ALEATORIO, ORDENADO, INVERSAMENTE_ORDENADO, MUITAS_DUPLICATAS
    }

    private GeradorDados() {
    }

    public static int[] gerar(int tamanho, Tipo tipo, long seed) {
        if (tamanho < 0) {
            throw new IllegalArgumentException("O tamanho nao pode ser negativo");
        }
        int[] dados = new int[tamanho];
        Random random = new Random(seed);
        switch (tipo) {
            case ORDENADO -> {
                for (int i = 0; i < tamanho; i++) dados[i] = i;
            }
            case INVERSAMENTE_ORDENADO -> {
                for (int i = 0; i < tamanho; i++) dados[i] = tamanho - i;
            }
            case MUITAS_DUPLICATAS -> {
                for (int i = 0; i < tamanho; i++) dados[i] = random.nextInt(1_001);
            }
            case ALEATORIO -> {
                for (int i = 0; i < tamanho; i++) dados[i] = random.nextInt();
            }
        }
        return dados;
    }
}

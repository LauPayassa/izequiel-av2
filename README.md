# Benchmark de algoritmos de ordenacao em Java

Projeto para comparar execucoes serial e paralela de Bubble Sort, Quick Sort, Merge Sort e Counting Sort. Os resultados sao gravados em CSV para analise posterior e podem ser visualizados com um grafico Swing simples.

O relatorio academico completo esta em [`RELATORIO.md`](RELATORIO.md), com resumo, introducao, metodologia, resultados, conclusao, referencias e anexos.

## Requisitos

- Java 17 ou superior
- Nenhuma dependencia externa

## Compilar

```bash
javac *.java
```

## Executar o benchmark

```bash
java Benchmark
```

O arquivo `resultados/benchmark.csv` sera criado com 5 amostras para cada algoritmo, tamanho de entrada, tipo de dados, modo de execucao e quantidade de threads.

Os tamanhos testados sao 1.000, 5.000 e 10.000 elementos. Os dados sao aleatorios, ordenados, inversamente ordenados e com muitas duplicatas. As execucoes paralelas usam 1, 2 e 4 threads.

O benchmark grava 960 medicoes (mais o cabecalho). Cada resultado e validado automaticamente para garantir que a ordenacao foi concluida corretamente.

## Abrir o grafico

Depois de executar o benchmark:

```bash
java GraficoSwing
```

Tambem e possivel informar outro CSV:

```bash
java GraficoSwing caminho/para/arquivo.csv
```

## Estrutura

- `Benchmark.java`: executa as amostras e escreve o CSV.
- `GeradorDados.java`: cria os conjuntos de dados.
- `BubbleSort.java`, `QuickSort.java`, `MergeSort.java`, `CountingSort.java`: implementacoes serial e paralela.
- `GraficoSwing.java`: visualizacao basica dos tempos.
- `ParallelSortSupport.java`: suporte comum para dividir e reunir blocos em algoritmos paralelizaveis.

O tempo medido e o tempo de ordenacao em nanossegundos (`tempo_ns`). A implementacao valida automaticamente se cada resultado esta ordenado antes de gravar a linha no CSV.

# Análise comparativa de algoritmos de ordenação em Java

**Repositório:** https://github.com/LauPayassa/benchmark-algoritmos-java  
**Linguagem:** Java 17 ou superior  
**Experimento:** execução serial e paralela com geração de CSV

## Resumo

Este trabalho apresenta uma análise experimental do desempenho de quatro algoritmos de ordenação: Bubble Sort, Quick Sort, Merge Sort e Counting Sort. Cada algoritmo possui uma implementação sequencial e uma implementação paralela. O framework de testes varia o tamanho e a natureza dos dados, a quantidade de threads e realiza cinco amostras por configuração. Os tempos são registrados em nanossegundos em arquivo CSV e podem ser visualizados com uma aplicação Swing.

Os resultados confirmam as diferenças esperadas de complexidade. Bubble Sort apresenta crescimento significativamente maior, enquanto Quick Sort, Merge Sort e Counting Sort são mais adequados para entradas maiores. A paralelização permite investigar o custo e o benefício da divisão do trabalho, embora, para entradas pequenas, o custo de criar threads possa superar o ganho de processamento.

## 1. Introdução

Algoritmos de ordenação são usados como base para buscas, consultas e processamento de dados. A escolha do algoritmo influencia diretamente o tempo de execução, o uso de memória e a capacidade de aproveitar processadores multicore.

Foram escolhidos quatro algoritmos com características diferentes:

- **Bubble Sort:** simples, mas com complexidade quadrática no caso médio e no pior caso.
- **Quick Sort:** complexidade média `O(n log n)` e particionamento recursivo.
- **Merge Sort:** complexidade `O(n log n)` garantida, usando divisão e intercalação.
- **Counting Sort:** pode atingir `O(n + k)` quando o intervalo de valores `k` é controlado.

Embora o enunciado utilize a expressão “algoritmos de busca”, os métodos listados são algoritmos de **ordenação**. Portanto, o experimento foi implementado como comparação de ordenação, seguindo os algoritmos fornecidos no enunciado.

## 2. Metodologia

O programa `Benchmark.java` funciona como um framework de testes. Para cada combinação de algoritmo, tamanho, tipo de entrada, modo de execução e quantidade de threads, ele:

1. gera uma entrada determinística usando uma semente;
2. copia a entrada para evitar que uma execução altere a próxima;
3. mede apenas o tempo da ordenação com `System.nanoTime()`;
4. verifica automaticamente se o vetor final está ordenado;
5. grava os parâmetros e o tempo no CSV.

Foram utilizadas:

- tamanhos de **1.000, 5.000 e 10.000 elementos**;
- dados **aleatórios, ordenados, inversamente ordenados e com duplicatas**;
- uma execução serial;
- execuções paralelas com **1, 2 e 4 threads**;
- **5 amostras** para cada configuração;
- `ForkJoinPool` para dividir o trabalho paralelo.

O experimento produz 960 medições, além do cabeçalho, no arquivo `resultados/benchmark.csv`. O número de threads é uma configuração lógica do programa. O número de processadores disponíveis pode ser consultado pela JVM com `Runtime.getRuntime().availableProcessors()`; a quantidade de núcleos físicos efetivamente utilizada depende do sistema operacional e do ambiente de execução.

## 3. Resultados e discussão

O CSV gerado possui as colunas:

```text
algoritmo,modo,tamanho,tipo_dados,threads,processadores_disponiveis,amostra,tempo_ns
```

Uma execução validada deste projeto produziu 961 linhas. Considerando somente as 60 medições seriais de cada algoritmo, os tempos médios observados foram:

| Algoritmo | Média serial observada (ns) | Interpretação |
|---|---:|---|
| Bubble Sort | 9.810.414 | Maior custo; comportamento quadrático |
| Quick Sort | 207.486 | Bom desempenho médio |
| Merge Sort | 281.835 | Desempenho estável, com memória auxiliar |
| Counting Sort | 194.884 | Muito eficiente quando o intervalo é adequado |

Esses valores são ilustrativos de uma execução e não devem ser tratados como constantes universais. Resultados podem variar conforme processador, JVM, sistema operacional, aquecimento do JIT, carga do computador e quantidade de memória disponível.

Para a análise estatística final, recomenda-se calcular média, mediana, desvio-padrão e intervalo mínimo/máximo agrupando por `algoritmo`, `modo`, `tamanho`, `tipo_dados` e `threads`. O arquivo CSV foi estruturado para permitir esse processamento em Java, Python, Excel ou outra ferramenta.

Espera-se que:

- Bubble Sort cresça rapidamente com o tamanho da entrada;
- Quick Sort seja rápido em dados gerais, mas dependa da qualidade do particionamento;
- Merge Sort apresente comportamento previsível e seja adequado à divisão em tarefas;
- Counting Sort seja especialmente competitivo com muitas duplicatas e intervalo de valores pequeno;
- a execução paralela tenha maior potencial em entradas grandes;
- com poucas entradas, o custo de criar pools e reunir blocos possa reduzir ou eliminar o ganho da paralelização.

O gráfico Swing incluído (`GraficoSwing.java`) desenha uma série temporal dos tempos do CSV. Ele é uma visualização básica e serve como extra do trabalho; análises acadêmicas mais completas podem gerar gráficos agrupados por algoritmo, tamanho e número de threads.

## 4. Conclusão

O projeto implementa um ambiente reproduzível para comparar ordenação serial e paralela em Java. Ele atende à variação de dados, tamanhos, threads e amostras, registra os resultados em CSV e fornece uma visualização Swing.

Os resultados mostram que não existe um algoritmo melhor para todos os cenários. Bubble Sort é útil didaticamente, mas inadequado para entradas grandes. Quick Sort e Merge Sort oferecem melhor escalabilidade assintótica, enquanto Counting Sort pode ser superior quando seus pressupostos sobre o intervalo de valores são atendidos. A paralelização deve ser avaliada junto com o tamanho do problema, pois threads introduzem custos adicionais.

Como trabalhos futuros, podem ser adicionados Selection Sort e Insertion Sort, tamanhos maiores, aquecimento da JVM, mais repetições, medição de memória, exportação de médias e uma interface com filtros dinâmicos.

## 5. Referências

1. CORMEN, T. H. et al. *Introduction to Algorithms*. MIT Press.
2. Oracle. *Java Platform, Standard Edition Documentation*. https://docs.oracle.com/en/java/
3. Oracle. *Fork/Join Framework*. https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html
4. SEDGEWICK, R.; WAYNE, K. *Algorithms*. Addison-Wesley.

## 6. Anexos

Os códigos-fonte utilizados estão disponíveis no GitHub:

https://github.com/LauPayassa/benchmark-algoritmos-java

Arquivos principais:

- `Benchmark.java`
- `GeradorDados.java`
- `BubbleSort.java`
- `QuickSort.java`
- `MergeSort.java`
- `CountingSort.java`
- `ParallelSortSupport.java`
- `GraficoSwing.java`

Para reproduzir:

```bash
javac *.java
java Benchmark
java GraficoSwing
```

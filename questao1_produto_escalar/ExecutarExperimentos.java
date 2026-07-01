import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class ExecutarExperimentos {
    private static final int[] TAMANHOS = {10_000, 100_000, 1_000_000, 5_000_000, 10_000_000};
    private static final int[] THREADS = {1, 2, 4, 8, 16};
    private static final int REPETICOES = 5;
    private static final int AQUECIMENTOS = 3;

    public static void main(String[] args) throws IOException, InterruptedException {
        Locale.setDefault(Locale.US);

        String computador = args.length >= 1 ? args[0] : System.getProperty("user.name") + "-" + System.getProperty("os.name").replace(' ', '_');
        Path arquivoSaida = Paths.get("questao1_produto_escalar", "resultados.csv");
        if (!Files.exists(arquivoSaida.getParent())) {
            arquivoSaida = Paths.get("resultados.csv");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(arquivoSaida, StandardCharsets.UTF_8)) {
            writer.write("computador,modo,tamanho_vetor,threads,tempo_ms,resultado,speedup");
            writer.newLine();

            for (int tamanho : TAMANHOS) {
                double[] vetorA = ProdutoEscalarSequencial.criarVetorA(tamanho);
                double[] vetorB = ProdutoEscalarSequencial.criarVetorB(tamanho);

                aquecer(vetorA, vetorB);

                double tempoSequencial = ProdutoEscalarSequencial.medirTempoMs(vetorA, vetorB, REPETICOES);
                double resultadoSequencial = ProdutoEscalarSequencial.produtoSequencial(vetorA, vetorB);

                escreverLinha(writer, computador, "sequencial", tamanho, 1, tempoSequencial, resultadoSequencial, 1.0);
                System.out.printf("Sequencial tamanho=%d tempo=%.4f ms resultado=%.4f%n", tamanho, tempoSequencial, resultadoSequencial);

                for (int threads : THREADS) {
                    double tempoParalelo = ProdutoEscalarParalelo.medirTempoMs(vetorA, vetorB, threads, REPETICOES);
                    double resultadoParalelo = ProdutoEscalarParalelo.produtoParalelo(vetorA, vetorB, threads);
                    double speedup = tempoSequencial / tempoParalelo;

                    escreverLinha(writer, computador, "paralelo", tamanho, threads, tempoParalelo, resultadoParalelo, speedup);
                    System.out.printf("Paralelo tamanho=%d threads=%d tempo=%.4f ms speedup=%.4f%n", tamanho, threads, tempoParalelo, speedup);
                }
            }
        }

        System.out.println("CSV salvo em: " + arquivoSaida.toAbsolutePath());
    }

    private static void aquecer(double[] vetorA, double[] vetorB) throws InterruptedException {
        for (int i = 0; i < AQUECIMENTOS; i++) {
            ProdutoEscalarSequencial.produtoSequencial(vetorA, vetorB);
            ProdutoEscalarParalelo.produtoParalelo(vetorA, vetorB, 2);
        }
    }

    private static void escreverLinha(BufferedWriter writer, String computador, String modo, int tamanho, int threads, double tempoMs, double resultado, double speedup) throws IOException {
        writer.write(String.format(Locale.US, "%s,%s,%d,%d,%.6f,%.6f,%.6f", computador, modo, tamanho, threads, tempoMs, resultado, speedup));
        writer.newLine();
    }
}

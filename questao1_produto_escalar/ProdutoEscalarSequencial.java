public class ProdutoEscalarSequencial {

    static volatile double blackhole;

    public static double produtoSequencial(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Os vetores devem ter o mesmo tamanho.");
        }

        double soma = 0.0;
        for (int i = 0; i < a.length; i++) {
            soma += a[i] * b[i];
        }

        return soma;
    }

    public static long medirTempo(double[] a, double[] b, int repeticoes) {
        long menorTempo = Long.MAX_VALUE;

        for (int i = 0; i < repeticoes; i++) {
            long inicio = System.nanoTime();
            blackhole = produtoSequencial(a, b);
            long fim = System.nanoTime();

            long tempo = fim - inicio;
            if (tempo < menorTempo) {
                menorTempo = tempo;
            }
        }

        return menorTempo;
    }

    public static double medirTempoMs(double[] a, double[] b, int repeticoes) {
        return medirTempo(a, b, repeticoes) / 1_000_000.0;
    }

    public static double[] criarVetorA(int tamanho) {
        double[] a = new double[tamanho];
        for (int i = 0; i < tamanho; i++) {
            a[i] = i % 100;
        }
        return a;
    }

    public static double[] criarVetorB(int tamanho) {
        double[] b = new double[tamanho];
        for (int i = 0; i < tamanho; i++) {
            b[i] = i % 50;
        }
        return b;
    }

    public static void aquecer(double[] a, double[] b, int repeticoes) {
        for (int i = 0; i < repeticoes; i++) {
            blackhole = produtoSequencial(a, b);
        }
    }

    public static void main(String[] args) {
        int tamanho = 1_000_000;
        int repeticoes = 5;

        if (args.length >= 1) {
            tamanho = Integer.parseInt(args[0]);
        }

        if (args.length >= 2) {
            repeticoes = Integer.parseInt(args[1]);
        }

        double[] a = criarVetorA(tamanho);
        double[] b = criarVetorB(tamanho);

        aquecer(a, b, 3);

        double tempoMs = medirTempoMs(a, b, repeticoes);
        double resultado = produtoSequencial(a, b);

        System.out.println("Programa sequencial");
        System.out.println("Tamanho do vetor: " + tamanho);
        System.out.println("Repeticoes: " + repeticoes);
        System.out.println("Tempo sequencial (ms): " + tempoMs);
        System.out.println("Resultado: " + resultado);
    }
}

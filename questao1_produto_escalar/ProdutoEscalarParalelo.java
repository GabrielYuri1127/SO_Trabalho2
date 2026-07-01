public class ProdutoEscalarParalelo {

    static volatile double blackhole;

    static class Trabalhador extends Thread {
        private final double[] a;
        private final double[] b;
        private final int inicio;
        private final int fim;
        private double resultadoParcial;

        public Trabalhador(double[] a, double[] b, int inicio, int fim) {
            this.a = a;
            this.b = b;
            this.inicio = inicio;
            this.fim = fim;
        }

        @Override
        public void run() {
            double soma = 0.0;
            for (int i = inicio; i < fim; i++) {
                soma += a[i] * b[i];
            }
            resultadoParcial = soma;
        }

        public double getResultadoParcial() {
            return resultadoParcial;
        }
    }

    public static double produtoParalelo(double[] a, double[] b, int numThreads) throws InterruptedException {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Os vetores devem ter o mesmo tamanho.");
        }

        int n = a.length;
        if (numThreads <= 0) {
            numThreads = 1;
        }
        if (numThreads > n) {
            numThreads = n;
        }

        Trabalhador[] threads = new Trabalhador[numThreads];
        int tamanhoBloco = n / numThreads;
        int inicio = 0;

        for (int i = 0; i < numThreads; i++) {
            int fim = (i == numThreads - 1) ? n : inicio + tamanhoBloco;
            threads[i] = new Trabalhador(a, b, inicio, fim);
            threads[i].start();
            inicio = fim;
        }

        double somaTotal = 0.0;
        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
            somaTotal += threads[i].getResultadoParcial();
        }

        return somaTotal;
    }

    public static long medirTempo(double[] a, double[] b, int numThreads, int repeticoes) throws InterruptedException {
        long menorTempo = Long.MAX_VALUE;

        for (int i = 0; i < repeticoes; i++) {
            long inicio = System.nanoTime();
            blackhole = produtoParalelo(a, b, numThreads);
            long fim = System.nanoTime();

            long tempo = fim - inicio;
            if (tempo < menorTempo) {
                menorTempo = tempo;
            }
        }

        return menorTempo;
    }

    public static double medirTempoMs(double[] a, double[] b, int numThreads, int repeticoes) throws InterruptedException {
        return medirTempo(a, b, numThreads, repeticoes) / 1_000_000.0;
    }

    public static void aquecer(double[] a, double[] b, int numThreads, int repeticoes) throws InterruptedException {
        for (int i = 0; i < repeticoes; i++) {
            blackhole = produtoParalelo(a, b, numThreads);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int tamanho = 1_000_000;
        int numThreads = 4;
        int repeticoes = 5;

        if (args.length >= 1) {
            tamanho = Integer.parseInt(args[0]);
        }

        if (args.length >= 2) {
            numThreads = Integer.parseInt(args[1]);
        }

        if (args.length >= 3) {
            repeticoes = Integer.parseInt(args[2]);
        }

        double[] a = ProdutoEscalarSequencial.criarVetorA(tamanho);
        double[] b = ProdutoEscalarSequencial.criarVetorB(tamanho);

        aquecer(a, b, numThreads, 3);

        double tempoMs = medirTempoMs(a, b, numThreads, repeticoes);
        double resultado = produtoParalelo(a, b, numThreads);

        System.out.println("Programa paralelo");
        System.out.println("Tamanho do vetor: " + tamanho);
        System.out.println("Threads: " + numThreads);
        System.out.println("Repeticoes: " + repeticoes);
        System.out.println("Tempo paralelo (ms): " + tempoMs);
        System.out.println("Resultado: " + resultado);
    }
}

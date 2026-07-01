import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ClinicaVeterinariaThreads {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    static class Animal implements Comparable<Animal> {
        final int id;
        final String nome;
        final String especie;
        final boolean emergencia;
        final String servico;
        final int duracaoMs;
        final long ordemChegada;
        final boolean fimExpediente;

        Animal(int id, long ordemChegada) {
            this.id = id;
            this.nome = "Animal-" + id;
            this.especie = escolherEspecie(id);
            this.emergencia = id % 7 == 0;
            this.servico = escolherServico(id, emergencia);
            this.duracaoMs = escolherDuracao(servico, emergencia);
            this.ordemChegada = ordemChegada;
            this.fimExpediente = false;
        }

        private Animal() {
            this.id = -1;
            this.nome = "FIM";
            this.especie = "";
            this.emergencia = false;
            this.servico = "";
            this.duracaoMs = 0;
            this.ordemChegada = Long.MAX_VALUE;
            this.fimExpediente = true;
        }

        static Animal fimExpediente() {
            return new Animal();
        }

        @Override
        public int compareTo(Animal outro) {
            if (this.fimExpediente && outro.fimExpediente) {
                return 0;
            }
            if (this.fimExpediente) {
                return 1;
            }
            if (outro.fimExpediente) {
                return -1;
            }
            if (this.emergencia != outro.emergencia) {
                return this.emergencia ? -1 : 1;
            }
            return Long.compare(this.ordemChegada, outro.ordemChegada);
        }
    }

    static class Recepcao extends Thread {
        private final PriorityBlockingQueue<Animal> fila;
        private final int quantidadeAnimais;
        private final int intervaloChegadaMs;
        private final int quantidadeFuncionarios;

        Recepcao(PriorityBlockingQueue<Animal> fila, int quantidadeAnimais, int intervaloChegadaMs, int quantidadeFuncionarios) {
            super("Recepcao");
            this.fila = fila;
            this.quantidadeAnimais = quantidadeAnimais;
            this.intervaloChegadaMs = intervaloChegadaMs;
            this.quantidadeFuncionarios = quantidadeFuncionarios;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= quantidadeAnimais; i++) {
                    Thread.sleep(intervaloChegadaMs);
                    Animal animal = new Animal(i, System.nanoTime());
                    log("Recepcao cadastrou " + resumoAnimal(animal));
                    fila.put(animal);
                    log("Recepcao colocou " + animal.nome + " na fila. Tamanho da fila: " + fila.size());
                }

                for (int i = 0; i < quantidadeFuncionarios; i++) {
                    fila.put(Animal.fimExpediente());
                }
                log("Recepcao encerrou chegadas e avisou os funcionarios.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log("Recepcao interrompida.");
            }
        }
    }

    static class Funcionario extends Thread {
        private final PriorityBlockingQueue<Animal> fila;
        private final AtomicInteger atendimentosConcluidos;

        Funcionario(int numero, PriorityBlockingQueue<Animal> fila, AtomicInteger atendimentosConcluidos) {
            super("Funcionario-" + numero);
            this.fila = fila;
            this.atendimentosConcluidos = atendimentosConcluidos;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Animal animal = fila.take();
                    if (animal.fimExpediente) {
                        log(getName() + " recebeu fim de expediente.");
                        break;
                    }

                    log(getName() + " INICIOU " + animal.servico + " para " + resumoAnimal(animal));
                    if (animal.emergencia) {
                        log(getName() + " priorizou emergencia: veterinario, exames e medicacao imediata.");
                    }

                    Thread.sleep(animal.duracaoMs);
                    int total = atendimentosConcluidos.incrementAndGet();
                    log(getName() + " FINALIZOU " + animal.nome + ". Total concluido: " + total);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log(getName() + " interrompido.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int quantidadeFuncionarios = args.length >= 1 ? Integer.parseInt(args[0]) : 4;
        int quantidadeAnimais = args.length >= 2 ? Integer.parseInt(args[1]) : 20;
        int intervaloChegadaMs = args.length >= 3 ? Integer.parseInt(args[2]) : 150;

        if (quantidadeFuncionarios <= 0) {
            quantidadeFuncionarios = 1;
        }

        PriorityBlockingQueue<Animal> fila = new PriorityBlockingQueue<Animal>();
        AtomicInteger atendimentosConcluidos = new AtomicInteger(0);
        Funcionario[] funcionarios = new Funcionario[quantidadeFuncionarios];

        log("CLINICA COM THREADS INICIADA");
        log("Funcionarios: " + quantidadeFuncionarios + ", animais: " + quantidadeAnimais + ", intervalo: " + intervaloChegadaMs + " ms");

        long inicio = System.nanoTime();
        Recepcao recepcao = new Recepcao(fila, quantidadeAnimais, intervaloChegadaMs, quantidadeFuncionarios);
        recepcao.start();

        for (int i = 0; i < quantidadeFuncionarios; i++) {
            funcionarios[i] = new Funcionario(i + 1, fila, atendimentosConcluidos);
            funcionarios[i].start();
        }

        recepcao.join();
        for (Funcionario funcionario : funcionarios) {
            funcionario.join();
        }
        long fim = System.nanoTime();

        double tempoTotalMs = (fim - inicio) / 1_000_000.0;
        log("CLINICA COM THREADS ENCERRADA. Animais atendidos: " + atendimentosConcluidos.get());
        log(String.format("Tempo total aproximado: %.2f ms", tempoTotalMs));
    }

    private static String escolherEspecie(int id) {
        String[] especies = {"cachorro", "gato", "coelho", "papagaio", "hamster"};
        return especies[(id - 1) % especies.length];
    }

    private static String escolherServico(int id, boolean emergencia) {
        if (emergencia) {
            return "emergencia";
        }

        String[] servicos = {"consulta veterinaria", "vacinacao", "banho e tosa", "exames laboratoriais"};
        return servicos[(id - 1) % servicos.length];
    }

    private static int escolherDuracao(String servico, boolean emergencia) {
        if (emergencia) {
            return 900;
        }
        if (servico.equals("consulta veterinaria")) {
            return 500;
        }
        if (servico.equals("vacinacao")) {
            return 300;
        }
        if (servico.equals("banho e tosa")) {
            return 700;
        }
        return 650;
    }

    private static String resumoAnimal(Animal animal) {
        String prioridade = animal.emergencia ? "PRIORIDADE EMERGENCIA" : "prioridade normal";
        return animal.nome + " (" + animal.especie + ", " + animal.servico + ", " + prioridade + ")";
    }

    private static void log(String mensagem) {
        System.out.println("[" + LocalTime.now().format(FORMATO_HORA) + "] [" + Thread.currentThread().getName() + "] " + mensagem);
    }
}

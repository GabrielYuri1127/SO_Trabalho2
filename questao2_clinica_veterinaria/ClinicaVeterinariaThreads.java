import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ClinicaVeterinariaThreads {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    static class Recepcao extends Thread {
        private final PriorityBlockingQueue<AtendimentoVeterinario> fila;
        private final int quantidadeAnimais;
        private final int intervaloChegadaMs;
        private final int quantidadeFuncionarios;

        Recepcao(PriorityBlockingQueue<AtendimentoVeterinario> fila, int quantidadeAnimais, int intervaloChegadaMs, int quantidadeFuncionarios) {
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
                    AtendimentoVeterinario atendimento = AtendimentoVeterinario.novo(i);
                    log("Recepcao cadastrou " + atendimento.descricaoCompleta());
                    log("Dados do tutor: " + atendimento.dadosTutor());
                    fila.put(atendimento);
                    log("Recepcao colocou " + atendimento.nomePet + " na fila. Tamanho da fila: " + fila.size());
                }

                for (int i = 0; i < quantidadeFuncionarios; i++) {
                    fila.put(AtendimentoVeterinario.fimExpediente());
                }
                log("Recepcao encerrou chegadas e avisou os funcionarios.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log("Recepcao interrompida.");
            }
        }
    }

    static class Funcionario extends Thread {
        private final PriorityBlockingQueue<AtendimentoVeterinario> fila;
        private final AtomicInteger atendimentosConcluidos;

        Funcionario(int numero, PriorityBlockingQueue<AtendimentoVeterinario> fila, AtomicInteger atendimentosConcluidos) {
            super("Funcionario-" + numero);
            this.fila = fila;
            this.atendimentosConcluidos = atendimentosConcluidos;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    AtendimentoVeterinario atendimento = fila.take();
                    if (atendimento.fimExpediente) {
                        log(getName() + " recebeu fim de expediente.");
                        break;
                    }

                    log(getName() + " INICIOU " + atendimento.servico + " para " + atendimento.descricaoCompleta());
                    log("Detalhes: " + atendimento.resumoAtendimento(getName()));
                    if (atendimento.emergencia) {
                        log(getName() + " priorizou emergencia: veterinario, exames e medicacao imediata.");
                    }

                    Thread.sleep(atendimento.duracaoMs);
                    int total = atendimentosConcluidos.incrementAndGet();
                    log(getName() + " FINALIZOU " + atendimento.nomePet + ". Valor: " + atendimento.valorFormatado() + ". Total concluido: " + total);
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

        PriorityBlockingQueue<AtendimentoVeterinario> fila = new PriorityBlockingQueue<AtendimentoVeterinario>();
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

    private static void log(String mensagem) {
        System.out.println("[" + LocalTime.now().format(FORMATO_HORA) + "] [" + Thread.currentThread().getName() + "] " + mensagem);
    }
}

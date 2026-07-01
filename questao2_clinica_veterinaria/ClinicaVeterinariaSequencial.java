import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClinicaVeterinariaSequencial {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    static class Animal {
        final int id;
        final String nome;
        final String especie;
        final boolean emergencia;
        final String servico;
        final int duracaoMs;

        Animal(int id) {
            this.id = id;
            this.nome = "Animal-" + id;
            this.especie = escolherEspecie(id);
            this.emergencia = id % 7 == 0;
            this.servico = escolherServico(id, emergencia);
            this.duracaoMs = escolherDuracao(servico, emergencia);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int quantidadeAnimais = args.length >= 1 ? Integer.parseInt(args[0]) : 12;
        int intervaloChegadaMs = args.length >= 2 ? Integer.parseInt(args[1]) : 200;

        log("CLINICA SEQUENCIAL INICIADA");
        log("Tudo acontece um atendimento por vez, sem uso de threads de funcionarios.");

        long inicio = System.nanoTime();
        for (int i = 1; i <= quantidadeAnimais; i++) {
            Animal animal = new Animal(i);
            simularChegada(animal, intervaloChegadaMs);
            recepcionar(animal);
            atender(animal);
        }
        long fim = System.nanoTime();

        double tempoTotalMs = (fim - inicio) / 1_000_000.0;
        log("CLINICA SEQUENCIAL ENCERRADA. Animais atendidos: " + quantidadeAnimais);
        log(String.format("Tempo total aproximado: %.2f ms", tempoTotalMs));
    }

    private static void simularChegada(Animal animal, int intervaloChegadaMs) throws InterruptedException {
        Thread.sleep(intervaloChegadaMs);
        log("Chegada: " + resumoAnimal(animal));
    }

    private static void recepcionar(Animal animal) throws InterruptedException {
        log("Recepcao iniciou cadastro de " + animal.nome);
        Thread.sleep(80);
        log("Recepcao concluiu cadastro de " + animal.nome + ". Encaminhando para " + animal.servico);
    }

    private static void atender(Animal animal) throws InterruptedException {
        log("Inicio do atendimento veterinario: " + resumoAnimal(animal));
        if (animal.emergencia) {
            log("EMERGENCIA detectada. Atendimento clinico prioritario, exames rapidos e medicacao.");
        } else {
            log("Procedimento: " + animal.servico + " em andamento.");
        }

        Thread.sleep(animal.duracaoMs);
        log("Fim do atendimento: " + animal.nome + " liberado apos " + animal.servico);
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
        String prioridade = animal.emergencia ? " PRIORIDADE EMERGENCIA" : " prioridade normal";
        return animal.nome + " (" + animal.especie + ", " + animal.servico + "," + prioridade + ")";
    }

    private static void log(String mensagem) {
        System.out.println("[" + LocalTime.now().format(FORMATO_HORA) + "] " + mensagem);
    }
}

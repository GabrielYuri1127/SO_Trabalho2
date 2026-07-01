import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClinicaVeterinariaSequencial {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void main(String[] args) throws InterruptedException {
        int quantidadeAnimais = args.length >= 1 ? Integer.parseInt(args[0]) : 12;
        int intervaloChegadaMs = args.length >= 2 ? Integer.parseInt(args[1]) : 200;

        log("CLINICA SEQUENCIAL INICIADA");
        log("MODO SEQUENCIAL: sem threads, fluxo unico equivalente a um unico funcionario atendendo um pet por vez.");

        long inicio = System.nanoTime();
        for (int i = 1; i <= quantidadeAnimais; i++) {
            AtendimentoVeterinario atendimento = AtendimentoVeterinario.novo(i);
            simularChegada(atendimento, intervaloChegadaMs);
            recepcionar(atendimento);
            atender(atendimento);
        }
        long fim = System.nanoTime();

        double tempoTotalMs = (fim - inicio) / 1_000_000.0;
        log("CLINICA SEQUENCIAL ENCERRADA. Animais atendidos: " + quantidadeAnimais);
        log(String.format("Tempo total aproximado: %.2f ms", tempoTotalMs));
    }

    private static void simularChegada(AtendimentoVeterinario atendimento, int intervaloChegadaMs) throws InterruptedException {
        Thread.sleep(intervaloChegadaMs);
        log("Chegada: " + atendimento.descricaoCompleta());
        log("Dados do tutor: " + atendimento.dadosTutor());
    }

    private static void recepcionar(AtendimentoVeterinario atendimento) throws InterruptedException {
        log("Recepcao iniciou cadastro de " + atendimento.nomePet + " para o tutor " + atendimento.tutor);
        Thread.sleep(80);
        log("Recepcao concluiu cadastro de " + atendimento.nomePet + ". Encaminhando para " + atendimento.servico + " (" + atendimento.valorFormatado() + ")");
    }

    private static void atender(AtendimentoVeterinario atendimento) throws InterruptedException {
        String funcionario = "Funcionario unico sequencial";
        log("Inicio do atendimento: " + atendimento.resumoAtendimento(funcionario));
        if (atendimento.emergencia) {
            log("EMERGENCIA detectada. Atendimento clinico prioritario, exames rapidos e medicacao.");
        } else {
            log("Procedimento: " + atendimento.ocorrencia + " em andamento.");
        }

        Thread.sleep(atendimento.duracaoMs);
        log("Fim do atendimento: " + atendimento.nomePet + " liberado apos " + atendimento.servico + ". Valor cobrado: " + atendimento.valorFormatado());
    }

    private static void log(String mensagem) {
        System.out.println("[" + LocalTime.now().format(FORMATO_HORA) + "] " + mensagem);
    }
}

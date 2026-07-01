import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClinicaVeterinariaInterface extends JFrame {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final JTextField campoFuncionarios = new JTextField("4", 6);
    private final JTextField campoAnimais = new JTextField("20", 6);
    private final JTextField campoIntervalo = new JTextField("150", 6);
    private final JButton botaoIniciar = new JButton("Iniciar simulacao");
    private final JButton botaoLimpar = new JButton("Limpar logs");
    private final JTextArea areaLogs = new JTextArea();
    private final DefaultListModel<String> modeloFila = new DefaultListModel<String>();
    private final JList<String> listaFila = new JList<String>(modeloFila);
    private final JProgressBar barraProgresso = new JProgressBar();
    private final JLabel rotuloStatus = new JLabel("Pronta para iniciar.");

    public ClinicaVeterinariaInterface() {
        super("Clinica Veterinaria em Tempo Real");
        configurarJanela();
        configurarEventos();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClinicaVeterinariaInterface().setVisible(true);
            }
        });
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(920, 560));
        setLocationRelativeTo(null);

        areaLogs.setEditable(false);
        areaLogs.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaLogs.setBackground(new Color(250, 250, 250));

        listaFila.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        barraProgresso.setStringPainted(true);

        JPanel painelTopo = criarPainelTopo();
        JPanel painelRodape = criarPainelRodape();

        JScrollPane rolagemLogs = new JScrollPane(areaLogs);
        rolagemLogs.setBorder(BorderFactory.createTitledBorder("Eventos da clinica"));

        JScrollPane rolagemFila = new JScrollPane(listaFila);
        rolagemFila.setPreferredSize(new Dimension(270, 400));
        rolagemFila.setBorder(BorderFactory.createTitledBorder("Fila de atendimentos"));

        JSplitPane divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rolagemLogs, rolagemFila);
        divisor.setResizeWeight(0.72);

        add(painelTopo, BorderLayout.NORTH);
        add(divisor, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
        pack();
    }

    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        painel.add(new JLabel("Funcionarios:"), c);
        c.gridx = 1;
        painel.add(campoFuncionarios, c);

        c.gridx = 2;
        painel.add(new JLabel("Animais:"), c);
        c.gridx = 3;
        painel.add(campoAnimais, c);

        c.gridx = 4;
        painel.add(new JLabel("Intervalo (ms):"), c);
        c.gridx = 5;
        painel.add(campoIntervalo, c);

        c.gridx = 6;
        painel.add(botaoIniciar, c);
        c.gridx = 7;
        painel.add(botaoLimpar, c);

        return painel;
    }

    private JPanel criarPainelRodape() {
        JPanel painel = new JPanel(new BorderLayout(10, 0));
        painel.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        painel.add(rotuloStatus, BorderLayout.WEST);
        painel.add(barraProgresso, BorderLayout.CENTER);
        return painel;
    }

    private void configurarEventos() {
        botaoIniciar.addActionListener(evento -> iniciarSimulacao());
        botaoLimpar.addActionListener(evento -> {
            areaLogs.setText("");
            modeloFila.clear();
            rotuloStatus.setText("Logs limpos.");
            barraProgresso.setValue(0);
        });
    }

    private void iniciarSimulacao() {
        int funcionarios;
        int animais;
        int intervaloMs;

        try {
            funcionarios = lerInteiroPositivo(campoFuncionarios, "funcionarios");
            animais = lerInteiroPositivo(campoAnimais, "animais");
            intervaloMs = lerInteiroPositivo(campoIntervalo, "intervalo");
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(), "Entrada invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        areaLogs.setText("");
        modeloFila.clear();
        barraProgresso.setMinimum(0);
        barraProgresso.setMaximum(animais);
        barraProgresso.setValue(0);
        botaoIniciar.setEnabled(false);
        rotuloStatus.setText("Simulacao em andamento...");

        Thread controle = new Thread(new Simulacao(funcionarios, animais, intervaloMs), "Controle-Simulacao");
        controle.start();
    }

    private int lerInteiroPositivo(JTextField campo, String nomeCampo) {
        try {
            int valor = Integer.parseInt(campo.getText().trim());
            if (valor <= 0) {
                throw new NumberFormatException();
            }
            return valor;
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException("Informe um valor inteiro positivo para " + nomeCampo + ".");
        }
    }

    private void log(String mensagem) {
        final String linha = "[" + LocalTime.now().format(FORMATO_HORA) + "] [" + Thread.currentThread().getName() + "] " + mensagem;
        System.out.println(linha);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                areaLogs.append(linha + System.lineSeparator());
                areaLogs.setCaretPosition(areaLogs.getDocument().getLength());
            }
        });
    }

    private void adicionarNaFila(final Animal animal) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                modeloFila.addElement(animal.descricaoCurta());
            }
        });
    }

    private void removerDaFila(final Animal animal) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                modeloFila.removeElement(animal.descricaoCurta());
            }
        });
    }

    private void atualizarProgresso(final int concluidos, final int total) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                barraProgresso.setValue(concluidos);
                barraProgresso.setString(concluidos + " / " + total + " atendimentos");
            }
        });
    }

    private void finalizarInterface(final int concluidos, final double tempoMs) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                botaoIniciar.setEnabled(true);
                rotuloStatus.setText(String.format("Finalizada: %d atendimentos em %.2f ms.", concluidos, tempoMs));
            }
        });
    }

    private class Simulacao implements Runnable {
        private final int quantidadeFuncionarios;
        private final int quantidadeAnimais;
        private final int intervaloMs;

        Simulacao(int quantidadeFuncionarios, int quantidadeAnimais, int intervaloMs) {
            this.quantidadeFuncionarios = quantidadeFuncionarios;
            this.quantidadeAnimais = quantidadeAnimais;
            this.intervaloMs = intervaloMs;
        }

        @Override
        public void run() {
            PriorityBlockingQueue<Animal> fila = new PriorityBlockingQueue<Animal>();
            AtomicInteger concluidos = new AtomicInteger(0);
            Funcionario[] funcionarios = new Funcionario[quantidadeFuncionarios];

            long inicio = System.nanoTime();
            log("CLINICA COM INTERFACE INICIADA");
            log("Funcionarios: " + quantidadeFuncionarios + ", animais: " + quantidadeAnimais + ", intervalo: " + intervaloMs + " ms");

            Recepcao recepcao = new Recepcao(fila, quantidadeAnimais, intervaloMs, quantidadeFuncionarios);
            recepcao.start();

            for (int i = 0; i < quantidadeFuncionarios; i++) {
                funcionarios[i] = new Funcionario(i + 1, fila, concluidos, quantidadeAnimais);
                funcionarios[i].start();
            }

            try {
                recepcao.join();
                for (Funcionario funcionario : funcionarios) {
                    funcionario.join();
                }
            } catch (InterruptedException erro) {
                Thread.currentThread().interrupt();
                log("Simulacao interrompida.");
            }

            long fim = System.nanoTime();
            double tempoMs = (fim - inicio) / 1_000_000.0;
            log(String.format("CLINICA COM INTERFACE ENCERRADA. Tempo total: %.2f ms", tempoMs));
            finalizarInterface(concluidos.get(), tempoMs);
        }
    }

    private class Recepcao extends Thread {
        private final PriorityBlockingQueue<Animal> fila;
        private final int quantidadeAnimais;
        private final int intervaloMs;
        private final int quantidadeFuncionarios;

        Recepcao(PriorityBlockingQueue<Animal> fila, int quantidadeAnimais, int intervaloMs, int quantidadeFuncionarios) {
            super("Recepcao");
            this.fila = fila;
            this.quantidadeAnimais = quantidadeAnimais;
            this.intervaloMs = intervaloMs;
            this.quantidadeFuncionarios = quantidadeFuncionarios;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= quantidadeAnimais; i++) {
                    Thread.sleep(intervaloMs);
                    Animal animal = new Animal(i, System.nanoTime());
                    fila.put(animal);
                    adicionarNaFila(animal);
                    log("Recepcao cadastrou e colocou na fila: " + animal.descricaoCompleta());
                }

                for (int i = 0; i < quantidadeFuncionarios; i++) {
                    fila.put(Animal.fimExpediente());
                }
                log("Recepcao encerrou as chegadas.");
            } catch (InterruptedException erro) {
                Thread.currentThread().interrupt();
                log("Recepcao interrompida.");
            }
        }
    }

    private class Funcionario extends Thread {
        private final PriorityBlockingQueue<Animal> fila;
        private final AtomicInteger concluidos;
        private final int totalAnimais;

        Funcionario(int numero, PriorityBlockingQueue<Animal> fila, AtomicInteger concluidos, int totalAnimais) {
            super("Funcionario-" + numero);
            this.fila = fila;
            this.concluidos = concluidos;
            this.totalAnimais = totalAnimais;
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

                    removerDaFila(animal);
                    log(getName() + " INICIOU " + animal.servico + ": " + animal.descricaoCompleta());
                    if (animal.emergencia) {
                        log(getName() + " priorizou emergencia com atendimento imediato.");
                    }

                    Thread.sleep(animal.duracaoMs);
                    int total = concluidos.incrementAndGet();
                    atualizarProgresso(total, totalAnimais);
                    log(getName() + " FINALIZOU " + animal.nome + ". Total concluido: " + total);
                }
            } catch (InterruptedException erro) {
                Thread.currentThread().interrupt();
                log(getName() + " interrompido.");
            }
        }
    }

    private static class Animal implements Comparable<Animal> {
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

        String descricaoCurta() {
            String prioridade = emergencia ? "EMERGENCIA" : "normal";
            return nome + " - " + servico + " - " + prioridade;
        }

        String descricaoCompleta() {
            String prioridade = emergencia ? "PRIORIDADE EMERGENCIA" : "prioridade normal";
            return nome + " (" + especie + ", " + servico + ", " + prioridade + ")";
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
    }
}

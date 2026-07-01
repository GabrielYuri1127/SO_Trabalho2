import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ClinicaVeterinariaInterface extends JFrame {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FORMATO_ARQUIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static final Color FUNDO = new Color(239, 244, 248);
    private static final Color CARD = Color.WHITE;
    private static final Color PRIMARIA = new Color(22, 93, 97);
    private static final Color PRIMARIA_ESCURO = new Color(14, 63, 67);
    private static final Color TEXTO = new Color(33, 43, 54);
    private static final Color TEXTO_SUAVE = new Color(92, 108, 125);
    private static final Color LINHA = new Color(219, 228, 235);
    private static final Color ALERTA = new Color(196, 57, 57);
    private static final Color SUCESSO = new Color(35, 126, 86);

    private final JTextField campoFuncionarios = new JTextField("4", 6);
    private final JTextField campoAnimais = new JTextField("20", 6);
    private final JTextField campoIntervalo = new JTextField("150", 6);
    private final JButton botaoIniciar = new JButton("Iniciar atendimento");
    private final JButton botaoLimpar = new JButton("Limpar painel");
    private final JTextArea areaLogs = new JTextArea();
    private final JProgressBar barraProgresso = new JProgressBar();
    private final JButton botaoSalvarHistorico = new JButton("Salvar historico CSV");
    private final JButton botaoSalvarRelatorio = new JButton("Salvar relatorio TXT");

    private final JLabel rotuloStatus = new JLabel("Pronta para iniciar");
    private final JLabel cardFila = new JLabel("0");
    private final JLabel cardConcluidos = new JLabel("0");
    private final JLabel cardEmergencias = new JLabel("0");
    private final JLabel cardTempo = new JLabel("0,00 s");
    private final JLabel cardReceita = new JLabel("R$ 0,00");

    private final DefaultTableModel modeloFila = criarModelo(new String[]{"Pet", "Tutor", "Servico", "Ocorrencia", "Valor", "Prioridade"});
    private final DefaultTableModel modeloEquipe = criarModelo(new String[]{"Funcionario", "Estado", "Pet", "Tutor", "Servico", "Valor"});
    private final DefaultTableModel modeloHistorico = criarModelo(new String[]{"Horario", "Funcionario", "Pet", "Tutor", "Telefone", "Bairro", "Servico", "Ocorrencia", "Valor"});
    private final JTable tabelaFila = new JTable(modeloFila);
    private final JTable tabelaEquipe = new JTable(modeloEquipe);
    private final JTable tabelaHistorico = new JTable(modeloHistorico);

    private Timer timerTempo;
    private long inicioSimulacao;
    private int emergenciasRegistradas;
    private double valorTotalHistorico;
    private final Set<String> animaisRetiradosAntesDeExibir = new HashSet<String>();

    public ClinicaVeterinariaInterface() {
        super("Clinica Veterinaria em Tempo Real");
        aplicarAparencia();
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

    private void aplicarAparencia() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
            // Mantem o visual padrao se Nimbus nao estiver disponivel.
        }
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 720));
        setPreferredSize(new Dimension(1240, 760));
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(18, 18));
        raiz.setBackground(FUNDO);
        raiz.setBorder(new EmptyBorder(18, 18, 18, 18));

        raiz.add(criarCabecalho(), BorderLayout.NORTH);
        raiz.add(criarConteudo(), BorderLayout.CENTER);
        raiz.add(criarRodape(), BorderLayout.SOUTH);

        setContentPane(raiz);
        pack();
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout(16, 0));
        painel.setBackground(PRIMARIA);
        painel.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("Clinica Veterinaria em Tempo Real");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

        JLabel subtitulo = new JLabel("VERSAO COM THREADS: recepcao e varios funcionarios simultaneos; sem threads = um unico funcionario em fluxo sequencial");
        subtitulo.setForeground(new Color(213, 236, 234));
        subtitulo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        rotuloStatus.setOpaque(true);
        rotuloStatus.setForeground(PRIMARIA_ESCURO);
        rotuloStatus.setBackground(new Color(228, 247, 243));
        rotuloStatus.setHorizontalAlignment(SwingConstants.CENTER);
        rotuloStatus.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        rotuloStatus.setBorder(new EmptyBorder(8, 14, 8, 14));

        painel.add(textos, BorderLayout.WEST);
        painel.add(rotuloStatus, BorderLayout.EAST);
        return painel;
    }

    private JPanel criarConteudo() {
        JPanel painel = new JPanel(new BorderLayout(16, 16));
        painel.setOpaque(false);

        JPanel topo = new JPanel(new BorderLayout(16, 16));
        topo.setOpaque(false);
        topo.add(criarPainelControles(), BorderLayout.WEST);
        topo.add(criarPainelIndicadores(), BorderLayout.CENTER);
        topo.add(criarPainelPet(), BorderLayout.EAST);

        JSplitPane divisorPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, criarPainelLogs(), criarPainelOperacao());
        divisorPrincipal.setResizeWeight(0.56);
        divisorPrincipal.setBorder(null);
        divisorPrincipal.setOpaque(false);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(divisorPrincipal, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelControles() {
        JPanel painel = criarCard("Parametros da simulacao com threads");
        painel.setPreferredSize(new Dimension(430, 170));

        JPanel conteudo = new JPanel(new GridBagLayout());
        conteudo.setOpaque(false);

        estilizarCampo(campoFuncionarios);
        estilizarCampo(campoAnimais);
        estilizarCampo(campoIntervalo);
        estilizarBotaoPrimario(botaoIniciar);
        estilizarBotaoSecundario(botaoLimpar);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        adicionarCampo(conteudo, c, 0, "Funcionarios", campoFuncionarios);
        adicionarCampo(conteudo, c, 1, "Animais", campoAnimais);
        adicionarCampo(conteudo, c, 2, "Intervalo (ms)", campoIntervalo);

        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 2;
        conteudo.add(botaoIniciar, c);

        c.gridx = 2;
        c.gridwidth = 1;
        conteudo.add(botaoLimpar, c);

        painel.add(conteudo, BorderLayout.CENTER);

        return painel;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints c, int coluna, String rotulo, JTextField campo) {
        JLabel label = new JLabel(rotulo);
        label.setForeground(TEXTO_SUAVE);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        c.gridy = 0;
        c.gridx = coluna;
        c.weightx = 1.0;
        painel.add(label, c);

        c.gridy = 1;
        painel.add(campo, c);
    }

    private JPanel criarPainelIndicadores() {
        JPanel painel = new JPanel(new GridLayout(1, 5, 12, 0));
        painel.setOpaque(false);
        painel.add(criarIndicador("Na fila", cardFila, "Atendimentos aguardando", new Color(39, 99, 164)));
        painel.add(criarIndicador("Concluidos", cardConcluidos, "Total finalizado", SUCESSO));
        painel.add(criarIndicador("Emergencias", cardEmergencias, "Prioridade automatica", ALERTA));
        painel.add(criarIndicador("Receita", cardReceita, "Valor simulado", new Color(132, 92, 22)));
        painel.add(criarIndicador("Tempo", cardTempo, "Duracao da simulacao", PRIMARIA));
        return painel;
    }

    private JPanel criarIndicador(String titulo, JLabel valor, String detalhe, Color cor) {
        JPanel painel = criarCard(null);
        painel.setLayout(new BorderLayout(8, 8));
        painel.setPreferredSize(new Dimension(150, 170));

        JLabel rotulo = new JLabel(titulo);
        rotulo.setForeground(TEXTO_SUAVE);
        rotulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        valor.setForeground(cor);
        valor.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));

        JLabel detalheLabel = new JLabel(detalhe);
        detalheLabel.setForeground(TEXTO_SUAVE);
        detalheLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        painel.add(rotulo, BorderLayout.NORTH);
        painel.add(valor, BorderLayout.CENTER);
        painel.add(detalheLabel, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelPet() {
        JPanel painel = criarCard("Movimento pet");
        painel.setPreferredSize(new Dimension(180, 170));
        painel.add(new PainelPetAnimado(), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelLogs() {
        JPanel painel = criarCard("Eventos em tempo real");

        areaLogs.setEditable(false);
        areaLogs.setLineWrap(true);
        areaLogs.setWrapStyleWord(true);
        areaLogs.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaLogs.setForeground(new Color(36, 44, 54));
        areaLogs.setBackground(new Color(249, 251, 253));
        areaLogs.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane rolagem = new JScrollPane(areaLogs);
        rolagem.setBorder(BorderFactory.createLineBorder(LINHA));
        painel.add(rolagem, BorderLayout.CENTER);

        return painel;
    }

    private JTabbedPane criarPainelOperacao() {
        JTabbedPane abas = new JTabbedPane();

        JPanel fila = criarCard("Fila prioritaria");
        configurarTabela(tabelaFila);
        fila.add(new JScrollPane(tabelaFila), BorderLayout.CENTER);

        JPanel equipe = criarCard("Equipe em atendimento");
        configurarTabela(tabelaEquipe);
        equipe.add(new JScrollPane(tabelaEquipe), BorderLayout.CENTER);

        JPanel historico = criarCard("Historico de atendimentos");
        configurarTabela(tabelaHistorico);
        historico.add(new JScrollPane(tabelaHistorico), BorderLayout.CENTER);

        abas.addTab("Fila", fila);
        abas.addTab("Equipe", equipe);
        abas.addTab("Historico", historico);
        return abas;
    }

    private JPanel criarRodape() {
        JPanel painel = criarCard(null);
        painel.setLayout(new BorderLayout(12, 0));
        painel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel label = new JLabel("Progresso geral");
        label.setForeground(TEXTO_SUAVE);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        barraProgresso.setStringPainted(true);
        barraProgresso.setString("0 / 0 atendimentos");
        barraProgresso.setForeground(PRIMARIA);
        barraProgresso.setBackground(new Color(224, 233, 240));
        barraProgresso.setPreferredSize(new Dimension(300, 24));

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);
        estilizarBotaoSecundario(botaoSalvarHistorico);
        estilizarBotaoSecundario(botaoSalvarRelatorio);
        botaoSalvarHistorico.setEnabled(false);
        botaoSalvarRelatorio.setEnabled(false);
        acoes.add(botaoSalvarHistorico);
        acoes.add(botaoSalvarRelatorio);

        painel.add(label, BorderLayout.WEST);
        painel.add(barraProgresso, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.EAST);
        return painel;
    }

    private JPanel criarCard(String titulo) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(CARD);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINHA),
                new EmptyBorder(14, 16, 14, 16)));

        if (titulo != null) {
            JLabel label = new JLabel(titulo);
            label.setForeground(TEXTO);
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            label.setBorder(new EmptyBorder(0, 0, 12, 0));
            painel.add(label, BorderLayout.NORTH);
        }
        return painel;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINHA),
                new EmptyBorder(8, 10, 8, 10)));
    }

    private void estilizarBotaoPrimario(JButton botao) {
        botao.setForeground(Color.WHITE);
        botao.setBackground(PRIMARIA);
        botao.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        botao.setFocusPainted(false);
        botao.setBorder(new EmptyBorder(10, 14, 10, 14));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setForeground(PRIMARIA_ESCURO);
        botao.setBackground(new Color(226, 239, 239));
        botao.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        botao.setFocusPainted(false);
        botao.setBorder(new EmptyBorder(10, 14, 10, 14));
    }

    private void configurarTabela(JTable tabela) {
        tabela.setRowHeight(30);
        tabela.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        tabela.setForeground(TEXTO);
        tabela.setGridColor(new Color(230, 236, 241));
        tabela.setShowVerticalLines(false);
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionBackground(new Color(219, 238, 236));
        tabela.setSelectionForeground(TEXTO);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        cabecalho.setForeground(TEXTO_SUAVE);
        cabecalho.setBackground(new Color(245, 248, 251));
        cabecalho.setReorderingAllowed(false);

        tabela.setDefaultRenderer(Object.class, new RendererTabela());
    }

    private DefaultTableModel criarModelo(String[] colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void configurarEventos() {
        botaoIniciar.addActionListener(evento -> iniciarSimulacao());
        botaoLimpar.addActionListener(evento -> limparPainel());
        botaoSalvarHistorico.addActionListener(evento -> salvarHistoricoCsv());
        botaoSalvarRelatorio.addActionListener(evento -> salvarRelatorioTxt());
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

        prepararPainel(funcionarios, animais);
        Thread controle = new Thread(new Simulacao(funcionarios, animais, intervaloMs), "Controle-Simulacao");
        controle.start();
    }

    private void prepararPainel(int funcionarios, int animais) {
        areaLogs.setText("");
        modeloFila.setRowCount(0);
        modeloEquipe.setRowCount(0);
        modeloHistorico.setRowCount(0);
        animaisRetiradosAntesDeExibir.clear();
        emergenciasRegistradas = 0;
        valorTotalHistorico = 0.0;

        for (int i = 1; i <= funcionarios; i++) {
            modeloEquipe.addRow(new Object[]{"Funcionario-" + i, "Aguardando", "-", "-", "-", "-"});
        }

        cardFila.setText("0");
        cardConcluidos.setText("0");
        cardEmergencias.setText("0");
        cardTempo.setText("0,00 s");
        cardReceita.setText("R$ 0,00");

        barraProgresso.setMinimum(0);
        barraProgresso.setMaximum(animais);
        barraProgresso.setValue(0);
        barraProgresso.setString("0 / " + animais + " atendimentos");

        inicioSimulacao = System.nanoTime();
        iniciarRelogio();
        alterarControles(false);
        botaoSalvarHistorico.setEnabled(false);
        botaoSalvarRelatorio.setEnabled(false);
        atualizarStatus("Simulacao em andamento", new Color(255, 246, 214), new Color(119, 82, 0));
    }

    private void limparPainel() {
        if (!botaoIniciar.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Aguarde a simulacao atual terminar antes de limpar.", "Simulacao em andamento", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        areaLogs.setText("");
        modeloFila.setRowCount(0);
        modeloEquipe.setRowCount(0);
        modeloHistorico.setRowCount(0);
        valorTotalHistorico = 0.0;
        cardFila.setText("0");
        cardConcluidos.setText("0");
        cardEmergencias.setText("0");
        cardTempo.setText("0,00 s");
        cardReceita.setText("R$ 0,00");
        barraProgresso.setValue(0);
        barraProgresso.setString("0 / 0 atendimentos");
        botaoSalvarHistorico.setEnabled(false);
        botaoSalvarRelatorio.setEnabled(false);
        atualizarStatus("Pronta para iniciar", new Color(228, 247, 243), PRIMARIA_ESCURO);
    }

    private void alterarControles(boolean habilitado) {
        botaoIniciar.setEnabled(habilitado);
        campoFuncionarios.setEnabled(habilitado);
        campoAnimais.setEnabled(habilitado);
        campoIntervalo.setEnabled(habilitado);
    }

    private void iniciarRelogio() {
        if (timerTempo != null) {
            timerTempo.stop();
        }
        timerTempo = new Timer(250, evento -> atualizarTempo());
        timerTempo.start();
    }

    private void atualizarTempo() {
        double segundos = (System.nanoTime() - inicioSimulacao) / 1_000_000_000.0;
        cardTempo.setText(String.format("%.2f s", segundos));
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

    private void adicionarNaFila(final AtendimentoVeterinario atendimento) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (atendimento.emergencia) {
                    emergenciasRegistradas++;
                    cardEmergencias.setText(String.valueOf(emergenciasRegistradas));
                }

                if (animaisRetiradosAntesDeExibir.remove(atendimento.nomePet)) {
                    cardFila.setText(String.valueOf(modeloFila.getRowCount()));
                    return;
                }

                modeloFila.addRow(new Object[]{
                        atendimento.nomePet,
                        atendimento.tutor,
                        atendimento.servico,
                        atendimento.ocorrencia,
                        atendimento.valorFormatado(),
                        atendimento.prioridadeTexto()
                });
                cardFila.setText(String.valueOf(modeloFila.getRowCount()));
            }
        });
    }

    private void removerDaFila(final AtendimentoVeterinario atendimento) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boolean removido = false;
                for (int i = 0; i < modeloFila.getRowCount(); i++) {
                    if (atendimento.nomePet.equals(modeloFila.getValueAt(i, 0))) {
                        modeloFila.removeRow(i);
                        removido = true;
                        break;
                    }
                }
                if (!removido) {
                    animaisRetiradosAntesDeExibir.add(atendimento.nomePet);
                }
                cardFila.setText(String.valueOf(modeloFila.getRowCount()));
            }
        });
    }

    private void atualizarFuncionario(final int numero, final String estado, final String pet, final String tutor, final String servico, final String valor) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int linha = numero - 1;
                if (linha >= 0 && linha < modeloEquipe.getRowCount()) {
                    modeloEquipe.setValueAt(estado, linha, 1);
                    modeloEquipe.setValueAt(pet, linha, 2);
                    modeloEquipe.setValueAt(tutor, linha, 3);
                    modeloEquipe.setValueAt(servico, linha, 4);
                    modeloEquipe.setValueAt(valor, linha, 5);
                }
            }
        });
    }

    private void atualizarProgresso(final int concluidos, final int total) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                barraProgresso.setValue(concluidos);
                barraProgresso.setString(concluidos + " / " + total + " atendimentos");
                cardConcluidos.setText(String.valueOf(concluidos));
            }
        });
    }

    private void registrarHistorico(final AtendimentoVeterinario atendimento, final String funcionario) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                modeloHistorico.addRow(new Object[]{
                        LocalTime.now().format(FORMATO_HORA),
                        funcionario,
                        atendimento.nomePet,
                        atendimento.tutor,
                        atendimento.telefoneTutor,
                        atendimento.bairroTutor,
                        atendimento.servico,
                        atendimento.ocorrencia,
                        atendimento.valorFormatado()
                });
                valorTotalHistorico += atendimento.valor;
                cardReceita.setText(String.format("R$ %.2f", valorTotalHistorico));
            }
        });
    }

    private void finalizarInterface(final int concluidos, final double tempoMs) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (timerTempo != null) {
                    timerTempo.stop();
                }
                modeloFila.setRowCount(0);
                cardFila.setText("0");
                alterarControles(true);
                botaoSalvarHistorico.setEnabled(modeloHistorico.getRowCount() > 0);
                botaoSalvarRelatorio.setEnabled(modeloHistorico.getRowCount() > 0);
                atualizarTempo();
                atualizarStatus("Finalizada: " + concluidos + " atendimentos", new Color(229, 247, 236), SUCESSO);
                log(String.format("Resumo final exibido na interface. Tempo total: %.2f ms", tempoMs));
            }
        });
    }

    private void salvarHistoricoCsv() {
        if (modeloHistorico.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nao ha historico para salvar.", "Historico vazio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File arquivo = escolherArquivo("historico_atendimentos_" + LocalDateTime.now().format(FORMATO_ARQUIVO) + ".csv");
        if (arquivo == null) {
            return;
        }

        try {
            salvarTabelaComoCsv(arquivo);
            JOptionPane.showMessageDialog(this, "Historico salvo em:\n" + arquivo.getAbsolutePath(), "Historico salvo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException erro) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar historico: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarRelatorioTxt() {
        if (modeloHistorico.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nao ha atendimentos para gerar relatorio.", "Historico vazio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File arquivo = escolherArquivo("relatorio_atendimentos_" + LocalDateTime.now().format(FORMATO_ARQUIVO) + ".txt");
        if (arquivo == null) {
            return;
        }

        try {
            salvarRelatorioTexto(arquivo);
            JOptionPane.showMessageDialog(this, "Relatorio salvo em:\n" + arquivo.getAbsolutePath(), "Relatorio salvo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException erro) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar relatorio: " + erro.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private File escolherArquivo(String nomePadrao) {
        JFileChooser seletor = new JFileChooser();
        seletor.setSelectedFile(new File(nomePadrao));
        int resultado = seletor.showSaveDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return seletor.getSelectedFile();
    }

    private void salvarTabelaComoCsv(File arquivo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo));
        try {
            for (int coluna = 0; coluna < modeloHistorico.getColumnCount(); coluna++) {
                if (coluna > 0) {
                    writer.write(",");
                }
                writer.write(escaparCsv(modeloHistorico.getColumnName(coluna)));
            }
            writer.newLine();

            for (int linha = 0; linha < modeloHistorico.getRowCount(); linha++) {
                for (int coluna = 0; coluna < modeloHistorico.getColumnCount(); coluna++) {
                    if (coluna > 0) {
                        writer.write(",");
                    }
                    writer.write(escaparCsv(String.valueOf(modeloHistorico.getValueAt(linha, coluna))));
                }
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private void salvarRelatorioTexto(File arquivo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo));
        try {
            writer.write("Relatorio de Atendimentos - Clinica Veterinaria");
            writer.newLine();
            writer.write("Gerado em: " + LocalDateTime.now());
            writer.newLine();
            writer.write("Atendimentos concluidos: " + modeloHistorico.getRowCount());
            writer.newLine();
            writer.write(String.format("Receita simulada: R$ %.2f", valorTotalHistorico));
            writer.newLine();
            writer.newLine();

            for (int linha = 0; linha < modeloHistorico.getRowCount(); linha++) {
                writer.write("Atendimento " + (linha + 1));
                writer.newLine();
                for (int coluna = 0; coluna < modeloHistorico.getColumnCount(); coluna++) {
                    writer.write(modeloHistorico.getColumnName(coluna) + ": " + modeloHistorico.getValueAt(linha, coluna));
                    writer.newLine();
                }
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private String escaparCsv(String texto) {
        String normalizado = texto == null ? "" : texto;
        return "\"" + normalizado.replace("\"", "\"\"") + "\"";
    }

    private void atualizarStatus(String texto, Color fundo, Color corTexto) {
        rotuloStatus.setText(texto);
        rotuloStatus.setBackground(fundo);
        rotuloStatus.setForeground(corTexto);
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
            PriorityBlockingQueue<AtendimentoVeterinario> fila = new PriorityBlockingQueue<AtendimentoVeterinario>();
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
        private final PriorityBlockingQueue<AtendimentoVeterinario> fila;
        private final int quantidadeAnimais;
        private final int intervaloMs;
        private final int quantidadeFuncionarios;

        Recepcao(PriorityBlockingQueue<AtendimentoVeterinario> fila, int quantidadeAnimais, int intervaloMs, int quantidadeFuncionarios) {
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
                    AtendimentoVeterinario atendimento = AtendimentoVeterinario.novo(i);
                    fila.put(atendimento);
                    adicionarNaFila(atendimento);
                    log("Recepcao cadastrou e colocou na fila: " + atendimento.descricaoCompleta());
                    log("Tutor: " + atendimento.dadosTutor() + " | Ocorrencia: " + atendimento.ocorrencia);
                }

                for (int i = 0; i < quantidadeFuncionarios; i++) {
                    fila.put(AtendimentoVeterinario.fimExpediente());
                }
                log("Recepcao encerrou as chegadas.");
            } catch (InterruptedException erro) {
                Thread.currentThread().interrupt();
                log("Recepcao interrompida.");
            }
        }
    }

    private class Funcionario extends Thread {
        private final int numero;
        private final PriorityBlockingQueue<AtendimentoVeterinario> fila;
        private final AtomicInteger concluidos;
        private final int totalAnimais;

        Funcionario(int numero, PriorityBlockingQueue<AtendimentoVeterinario> fila, AtomicInteger concluidos, int totalAnimais) {
            super("Funcionario-" + numero);
            this.numero = numero;
            this.fila = fila;
            this.concluidos = concluidos;
            this.totalAnimais = totalAnimais;
        }

        @Override
        public void run() {
            try {
                atualizarFuncionario(numero, "Aguardando", "-", "-", "-", "-");
                while (true) {
                    AtendimentoVeterinario atendimento = fila.take();
                    if (atendimento.fimExpediente) {
                        atualizarFuncionario(numero, "Encerrado", "-", "-", "-", "-");
                        log(getName() + " recebeu fim de expediente.");
                        break;
                    }

                    removerDaFila(atendimento);
                    atualizarFuncionario(
                            numero,
                            atendimento.emergencia ? "Emergencia" : "Atendendo",
                            atendimento.nomePet,
                            atendimento.tutor,
                            atendimento.servico,
                            atendimento.valorFormatado());
                    log(getName() + " INICIOU " + atendimento.servico + ": " + atendimento.descricaoCompleta());
                    log("Detalhes: " + atendimento.resumoAtendimento(getName()));
                    if (atendimento.emergencia) {
                        log(getName() + " priorizou emergencia com atendimento imediato.");
                    }

                    Thread.sleep(atendimento.duracaoMs);
                    int total = concluidos.incrementAndGet();
                    registrarHistorico(atendimento, getName());
                    atualizarProgresso(total, totalAnimais);
                    atualizarFuncionario(numero, "Aguardando", "-", "-", "-", "-");
                    log(getName() + " FINALIZOU " + atendimento.nomePet + ". Valor: " + atendimento.valorFormatado() + ". Total concluido: " + total);
                }
            } catch (InterruptedException erro) {
                Thread.currentThread().interrupt();
                atualizarFuncionario(numero, "Interrompido", "-", "-", "-", "-");
                log(getName() + " interrompido.");
            }
        }
    }

    private class RendererTabela extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component componente = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            componente.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
            setBorder(new EmptyBorder(4, 8, 4, 8));

            if (!isSelected) {
                componente.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 251, 253));
                componente.setForeground(TEXTO);
            }

            String texto = value == null ? "" : String.valueOf(value);
            if ("EMERGENCIA".equals(texto) || "Emergencia".equals(texto)) {
                componente.setForeground(ALERTA);
                componente.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
            } else if ("Encerrado".equals(texto) || "Concluido".equals(texto)) {
                componente.setForeground(SUCESSO);
            }

            return componente;
        }
    }

    private class PainelPetAnimado extends JPanel {
        private int passo;

        PainelPetAnimado() {
            setOpaque(false);
            Timer timer = new Timer(180, evento -> {
                passo = (passo + 1) % 40;
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int largura = getWidth();
            int altura = getHeight();
            g2.setColor(new Color(241, 248, 247));
            g2.fillRoundRect(8, 8, largura - 16, altura - 16, 18, 18);

            int x = 22 + (passo % 18);
            int y = altura / 2 + 8;
            desenharPet(g2, x, y);
            desenharPata(g2, largura - 58, 38, 0.85);
            desenharPata(g2, largura - 94, 76, 0.65);
            desenharPata(g2, largura - 45, 108, 0.55);

            g2.setColor(TEXTO_SUAVE);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            g2.drawString("Pets chegando", 22, 28);
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            g2.drawString("fila e equipe em tempo real", 22, 44);
            g2.dispose();
        }

        private void desenharPet(Graphics2D g2, int x, int y) {
            g2.setColor(new Color(98, 134, 108));
            g2.fillOval(x + 18, y - 28, 62, 34);
            g2.fillOval(x + 66, y - 42, 28, 28);
            g2.fillOval(x + 70, y - 55, 10, 18);
            g2.fillOval(x + 84, y - 55, 10, 18);
            g2.fillRect(x + 28, y, 10, 18);
            g2.fillRect(x + 62, y, 10, 18);
            g2.setColor(new Color(65, 91, 73));
            g2.fillOval(x + 86, y - 32, 4, 4);
            g2.drawArc(x + 4, y - 26, 24, 18, 20, 130);
        }

        private void desenharPata(Graphics2D g2, int x, int y, double escala) {
            int s = (int) (10 * escala);
            g2.setColor(new Color(193, 215, 207));
            g2.fillOval(x, y, s + 7, s + 5);
            g2.fillOval(x - s, y - s, s, s);
            g2.fillOval(x + 2, y - s - 4, s, s);
            g2.fillOval(x + s + 3, y - s, s, s);
        }
    }
}

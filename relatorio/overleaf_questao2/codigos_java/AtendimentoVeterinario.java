public class AtendimentoVeterinario implements Comparable<AtendimentoVeterinario> {
    final int id;
    final String nomePet;
    final String especie;
    final String raca;
    final String sexo;
    final String tutor;
    final String telefoneTutor;
    final String bairroTutor;
    final boolean emergencia;
    final String servico;
    final String ocorrencia;
    final double valor;
    final int duracaoMs;
    final long ordemChegada;
    final boolean fimExpediente;

    AtendimentoVeterinario(int id, long ordemChegada) {
        this.id = id;
        this.nomePet = escolher(NOMES_PETS, id);
        this.especie = escolher(ESPECIES, id);
        this.raca = escolher(RACAS, id);
        this.sexo = escolher(SEXOS, id);
        this.tutor = escolher(TUTORES, id);
        this.telefoneTutor = escolher(TELEFONES, id);
        this.bairroTutor = escolher(BAIRROS, id);
        this.emergencia = id % 7 == 0;
        this.servico = escolherServico(id, emergencia);
        this.ocorrencia = escolherOcorrencia(servico, id, emergencia);
        this.valor = calcularValor(servico, id, emergencia);
        this.duracaoMs = escolherDuracao(servico, emergencia);
        this.ordemChegada = ordemChegada;
        this.fimExpediente = false;
    }

    private AtendimentoVeterinario() {
        this.id = -1;
        this.nomePet = "FIM";
        this.especie = "";
        this.raca = "";
        this.sexo = "";
        this.tutor = "";
        this.telefoneTutor = "";
        this.bairroTutor = "";
        this.emergencia = false;
        this.servico = "";
        this.ocorrencia = "";
        this.valor = 0.0;
        this.duracaoMs = 0;
        this.ordemChegada = Long.MAX_VALUE;
        this.fimExpediente = true;
    }

    static AtendimentoVeterinario novo(int id) {
        return new AtendimentoVeterinario(id, System.nanoTime());
    }

    static AtendimentoVeterinario fimExpediente() {
        return new AtendimentoVeterinario();
    }

    @Override
    public int compareTo(AtendimentoVeterinario outro) {
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

    String prioridadeTexto() {
        return emergencia ? "EMERGENCIA" : "Normal";
    }

    boolean femea() {
        return "Femea".equals(sexo);
    }

    String perfilPet() {
        return especie + " | " + raca + " | " + sexo;
    }

    String valorFormatado() {
        return String.format("R$ %.2f", valor);
    }

    String dadosTutor() {
        return tutor + " | " + telefoneTutor + " | " + bairroTutor;
    }

    String descricaoCompleta() {
        String prioridade = emergencia ? "PRIORIDADE EMERGENCIA" : "prioridade normal";
        return nomePet + " (" + perfilPet() + ", tutor: " + tutor + ", " + servico + ", " + prioridade + ", " + valorFormatado() + ")";
    }

    String resumoAtendimento(String funcionario) {
        return "Funcionario: " + funcionario
                + " | Pet: " + nomePet
                + " | Perfil: " + perfilPet()
                + " | Tutor: " + dadosTutor()
                + " | Atendimento: " + ocorrencia
                + " | Valor: " + valorFormatado();
    }

    private static String escolher(String[] valores, int id) {
        return valores[(id - 1) % valores.length];
    }

    private static String escolherServico(int id, boolean emergencia) {
        if (emergencia) {
            return "emergencia";
        }

        String[] servicos = {"consulta veterinaria", "vacinacao", "banho e tosa", "exames laboratoriais"};
        return escolher(servicos, id);
    }

    private static String escolherOcorrencia(String servico, int id, boolean emergencia) {
        if (emergencia) {
            String[] ocorrencias = {
                    "queda com suspeita de fratura; analgesia, raio-x e observacao",
                    "engasgo e dificuldade respiratoria; estabilizacao e medicacao",
                    "intoxicacao alimentar; fluidoterapia e exames rapidos"
            };
            return escolher(ocorrencias, id);
        }
        if (servico.equals("consulta veterinaria")) {
            String[] ocorrencias = {
                    "check-up geral, avaliacao de peso e prescricao preventiva",
                    "avaliacao de coceira persistente e orientacao dermatologica",
                    "retorno clinico para acompanhar recuperacao"
            };
            return escolher(ocorrencias, id);
        }
        if (servico.equals("vacinacao")) {
            String[] ocorrencias = {
                    "aplicacao de vacina anual e atualizacao da carteirinha",
                    "dose de reforco e orientacao ao tutor",
                    "vacina multipla com observacao pos-aplicacao"
            };
            return escolher(ocorrencias, id);
        }
        if (servico.equals("banho e tosa")) {
            String[] ocorrencias = {
                    "banho completo, tosa higienica e corte de unhas",
                    "banho antipulgas e desembolo de pelagem",
                    "tosa completa e limpeza auricular"
            };
            return escolher(ocorrencias, id);
        }

        String[] ocorrencias = {
                "coleta de sangue e hemograma completo",
                "ultrassom abdominal e avaliacao clinica",
                "exame de pele e analise laboratorial"
        };
        return escolher(ocorrencias, id);
    }

    private static double calcularValor(String servico, int id, boolean emergencia) {
        double variacao = (id % 4) * 12.50;
        if (emergencia) {
            return 420.00 + variacao;
        }
        if (servico.equals("consulta veterinaria")) {
            return 130.00 + variacao;
        }
        if (servico.equals("vacinacao")) {
            return 95.00 + variacao;
        }
        if (servico.equals("banho e tosa")) {
            return 85.00 + variacao;
        }
        return 180.00 + variacao;
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

    private static final String[] NOMES_PETS = {
            "Thor", "Mel", "Luna", "Bob", "Nina", "Toby", "Maya", "Simba", "Pipoca", "Amora",
            "Billy", "Jade", "Fred", "Belinha", "Theo", "Mila", "Chico", "Sol", "Rex", "Kiara"
    };

    private static final String[] ESPECIES = {
            "cachorro", "gato", "coelho", "papagaio", "hamster",
            "cachorro", "gato", "cachorro", "coelho", "gato",
            "cachorro", "gato", "cachorro", "gato", "cachorro",
            "gato", "papagaio", "coelho", "cachorro", "gato"
    };

    private static final String[] RACAS = {
            "Labrador", "Siamese", "Mini Lion", "Papagaio-verdadeiro", "Sirio",
            "Beagle", "Persa", "Vira-lata", "Angora", "Maine Coon",
            "Poodle", "SRD", "Golden Retriever", "Ragdoll", "Shih-tzu",
            "British Shorthair", "Calopsita", "Holandes", "Pastor Alemao", "Sphynx"
    };

    private static final String[] SEXOS = {
            "Macho", "Femea", "Femea", "Macho", "Femea",
            "Macho", "Femea", "Macho", "Femea", "Femea",
            "Macho", "Femea", "Macho", "Femea", "Macho",
            "Femea", "Macho", "Femea", "Macho", "Femea"
    };

    private static final String[] TUTORES = {
            "Ana Souza", "Carlos Lima", "Mariana Costa", "Rafael Pereira", "Juliana Rocha",
            "Paulo Nascimento", "Fernanda Alves", "Bruno Martins", "Camila Ribeiro", "Lucas Oliveira",
            "Patricia Mendes", "Diego Carvalho", "Larissa Gomes", "Renato Castro", "Aline Barbosa",
            "Eduardo Silva", "Priscila Teixeira", "Mateus Andrade", "Sofia Monteiro", "Gustavo Freitas"
    };

    private static final String[] TELEFONES = {
            "(92) 98411-2301", "(92) 99120-4472", "(92) 98244-1098", "(92) 99301-7710",
            "(92) 98155-6203", "(92) 99233-4511", "(92) 98500-1187", "(92) 99412-3309",
            "(92) 98876-9021", "(92) 98770-1402", "(92) 99345-2210", "(92) 98231-0098",
            "(92) 99111-5308", "(92) 98642-7719", "(92) 98400-6591", "(92) 99512-8820",
            "(92) 98199-3407", "(92) 99288-6714", "(92) 98573-0205", "(92) 98741-3906"
    };

    private static final String[] BAIRROS = {
            "Centro", "Adrianopolis", "Flores", "Ponta Negra", "Cidade Nova",
            "Aleixo", "Parque Dez", "Coroado", "Dom Pedro", "Chapada"
    };
}

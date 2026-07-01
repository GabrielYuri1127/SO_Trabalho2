import csv
import sys
from collections import defaultdict
from pathlib import Path

try:
    import matplotlib.pyplot as plt
except ModuleNotFoundError:
    print("Erro: o pacote matplotlib nao esta instalado neste Python.")
    print("Instale com:")
    print("  python -m pip install -r requirements.txt")
    print("ou:")
    print("  python -m pip install matplotlib")
    raise SystemExit(1)


def ler_csv(caminho_csv):
    with open(caminho_csv, newline="", encoding="utf-8") as arquivo:
        return list(csv.DictReader(arquivo))


def gerar_grafico_tempos(linhas, pasta_saida):
    dados = defaultdict(list)

    for linha in linhas:
        if linha["modo"] == "sequencial":
            chave = f"{linha['computador']} - sequencial"
        else:
            chave = f"{linha['computador']} - paralelo {linha['threads']} threads"

        dados[chave].append((int(linha["tamanho_vetor"]), float(linha["tempo_ms"])))

    plt.figure(figsize=(11, 6))
    for chave, valores in sorted(dados.items()):
        valores.sort()
        tamanhos = [valor[0] for valor in valores]
        tempos = [valor[1] for valor in valores]
        plt.plot(tamanhos, tempos, marker="o", label=chave)

    plt.title("Tempo de execucao por tamanho do vetor")
    plt.xlabel("Tamanho do vetor")
    plt.ylabel("Tempo (ms)")
    plt.xscale("log")
    plt.grid(True, which="both", linestyle="--", alpha=0.4)
    plt.legend(fontsize=8)
    plt.tight_layout()
    plt.savefig(pasta_saida / "grafico_tempos.png", dpi=150)


def gerar_grafico_speedup(linhas, pasta_saida):
    dados = defaultdict(list)

    for linha in linhas:
        if linha["modo"] != "paralelo":
            continue

        chave = f"{linha['computador']} - {linha['threads']} threads"
        dados[chave].append((int(linha["tamanho_vetor"]), float(linha["speedup"])))

    plt.figure(figsize=(11, 6))
    for chave, valores in sorted(dados.items()):
        valores.sort()
        tamanhos = [valor[0] for valor in valores]
        speedups = [valor[1] for valor in valores]
        plt.plot(tamanhos, speedups, marker="o", label=chave)

    plt.title("Speedup por tamanho do vetor")
    plt.xlabel("Tamanho do vetor")
    plt.ylabel("Speedup")
    plt.xscale("log")
    plt.grid(True, which="both", linestyle="--", alpha=0.4)
    plt.legend(fontsize=8)
    plt.tight_layout()
    plt.savefig(pasta_saida / "grafico_speedup.png", dpi=150)


def main():
    caminho_csv = Path(sys.argv[1]) if len(sys.argv) >= 2 else Path("questao1_produto_escalar/resultados.csv")

    if not caminho_csv.exists():
        raise SystemExit(f"CSV nao encontrado: {caminho_csv}")

    linhas = ler_csv(caminho_csv)
    if not linhas:
        raise SystemExit("CSV vazio.")

    pasta_saida = caminho_csv.parent
    gerar_grafico_tempos(linhas, pasta_saida)
    gerar_grafico_speedup(linhas, pasta_saida)

    print(f"Graficos gerados em: {pasta_saida.resolve()}")


if __name__ == "__main__":
    main()

package br.com.mvbos.lgj;

public class JogadorInfo {
    private String nome;
    private int pontuacao;

    public JogadorInfo(String nome, int pontuacao) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }
}
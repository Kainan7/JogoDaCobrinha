package br.com.mvbos.lgj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ranking {
    private JFrame frame;
    private JTextArea rankingTextArea;
    private List<JogadorInfo> jogadores;

    public Ranking() {
        // Inicializa a lista de jogadores
        jogadores = new ArrayList<>();
    }

    public void exibirJanelaRanking() {
        if (frame == null) {
            criarJanelaRanking();
            configurarKeyBindings();
        }

        frame.setVisible(true);
    }

    private void criarJanelaRanking() {
        frame = new JFrame("Ranking");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        frame.setContentPane(panel);

        rankingTextArea = new JTextArea();
        rankingTextArea.setEditable(false);
        rankingTextArea.setForeground(Color.WHITE);
        rankingTextArea.setBackground(Color.BLACK);
        panel.add(rankingTextArea);

        JLabel messageLabel = new JLabel("Apertar 'ESC' para retornar ao Jogo");
        messageLabel.setForeground(Color.WHITE);
        panel.add(messageLabel, BorderLayout.SOUTH);
    }

    private void configurarKeyBindings() {
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = frame.getRootPane().getInputMap(condition);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        // Adicione KeyBindings para a tecla ESCAPE (VK_ESCAPE)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapeAction");
        actionMap.put("escapeAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                // Volta ao jogo
            }
        });
    }

    public void atualizarRanking() {
        Collections.sort(jogadores, Comparator.comparingInt(JogadorInfo::getPontuacao).reversed());

        StringBuilder rankingText = new StringBuilder();
        rankingText.append("RANKING\n\n");

        for (int i = 0; i < Math.min(jogadores.size(), 10); i++) {
            JogadorInfo jogador = jogadores.get(i);
            rankingText.append(String.format("%d. %s --------------------------------------- %d\n", i + 1, jogador.getNome(), jogador.getPontuacao()));
        }

        rankingTextArea.setText(rankingText.toString());
    }

    public void adicionarJogadorAoRanking(JogadorInfo jogador) {
        jogadores.add(jogador);
        salvarJogador(); // Chama o método para salvar o jogador após adicioná-lo
    }

    public void salvarJogador() {
        // Salva a lista de jogadores em um arquivo de texto
        Path directoryPath = Paths.get("C:\\Users\\Public\\Documents\\Jogadores");

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path filePath = Paths.get("C:\\Users\\Public\\Documents\\Jogadores\\Jogadores.txt");

        try (BufferedWriter bw = Files.newBufferedWriter(filePath)) {
            bw.write("RANKING \uD83D\uDC51 ");
            bw.newLine();

            // Inverta a ordem ao escrever no arquivo
            List<JogadorInfo> jogadoresOrdenados = new ArrayList<>(jogadores);
            jogadoresOrdenados.sort(Comparator.comparingInt(JogadorInfo::getPontuacao).reversed());

            for (JogadorInfo jogadorInfo : jogadoresOrdenados) {
                bw.write("Nome: " + jogadorInfo.getNome() + " ---------------------------------------" + " Pontuação: " + jogadorInfo.getPontuacao());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("ERRO AO SALVAR JOGADOR " + e.getMessage());
        }
    }
}
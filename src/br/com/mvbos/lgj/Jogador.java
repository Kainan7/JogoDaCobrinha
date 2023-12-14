package br.com.mvbos.lgj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Jogador {
    private JFrame frame;
    private JTextField textField;
    private JLabel rotulo;
    private JogoCenario jogoCenario;

    public Jogador(JogoCenario jogoCenario) {
        this.jogoCenario = jogoCenario;
    }

    // Método para exibir a janela de entrada do jogador
    public void exibirJanelaJogador(int pontuacao) {
        frame = new JFrame("Ranking da Cobrinha");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        frame.setContentPane(panel);

        rotulo = new JLabel("Nome: ");
        rotulo.setForeground(Color.WHITE);
        panel.add(rotulo);

        textField = new JTextField(40);

        // Adiciona um KeyListener para detectar a tecla "Enter" no campo de texto
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            // Método chamado quando uma tecla é pressionada e solta
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Chama o método adicionarAoRanking() quando a tecla "Enter" é pressionada
                    try {
                        adicionarAoRanking();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { // Método chamado quando uma tecla é solta
            }
        });

        panel.add(textField);

        JButton adicionarButton = new JButton("Adicionar ao Ranking");
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Método chamado quando o botão "Adicionar ao Ranking" é clicado
                try {
                    adicionarAoRanking();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        panel.add(adicionarButton);

        // Cria um novo GroupLayout para o painel
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        // Ativa a criação automática de espaçamentos entre os componentes e seus contêineres
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Cria um grupo sequencial horizontal (hGroup) para organizar os componentes horizontalmente
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(rotulo) //adiciona rotulo
                .addComponent(textField) // adiciona campo de texto
                .addComponent(adicionarButton)); // adiciona botao
        layout.setHorizontalGroup(hGroup); //define o grupo sequencial

        // Cria um grupo sequencial vertical (vGroup) para organizar os componentes verticalmente
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(rotulo));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(textField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(adicionarButton));
        layout.setVerticalGroup(vGroup);

        // Ajusta o tamanho da janela automaticamente com base no layout
        frame.pack();
        frame.setVisible(true);
    }

    private void adicionarAoRanking() throws Exception {
        String nome = textField.getText();
        if (!nome.isEmpty()) { // Adiciona o jogador ao ranking
            jogoCenario.adicionarJogadorAoRanking(new JogadorInfo(nome, jogoCenario.getPONTUACAO()));

            // Exibe e atualiza o ranking
            Jogo.ranking.exibirJanelaRanking();
            Jogo.ranking.atualizarRanking();

            // Exibe uma mensagem de sucesso
            JOptionPane.showMessageDialog(frame, "Pontuação adicionada ao ranking!");
            frame.dispose();

            // Adiciona o jogador ao ranking novamente
            JogadorInfo novoJogador = new JogadorInfo(nome, jogoCenario.getPONTUACAO());
            Jogo.ranking.adicionarJogadorAoRanking(novoJogador);
            Jogo.ranking.atualizarRanking();
            Jogo.ranking.exibirJanelaRanking();
        }
    }
}

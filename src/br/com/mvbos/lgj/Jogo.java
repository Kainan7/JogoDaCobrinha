package br.com.mvbos.lgj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.com.mvbos.lgj.base.CenarioPadrao;
import br.com.mvbos.lgj.base.Texto;

public class Jogo extends JFrame {
	public static Ranking ranking = new Ranking();
	private long pauseTime = 0;
	private Texto textoTempo = new Texto(new Font("Tahoma",Font.PLAIN, 20));
	private long tempoDecorrido = 0;
	private boolean jogoIniciado = false;
	private boolean mostrarTempo = false;
	private static final long serialVersionUID = 1L;
	private static final int FPS = 1000 / 20;
	private static final int JANELA_ALTURA = 720;
	private static final int JANELA_LARGURA = 720;

	private JPanel tela;

	private Graphics2D g2d;

	private BufferedImage buffer;

	private CenarioPadrao cenario;


	// método pra exibir a janela do jogador ao final do jogo
	private void exibirJanelaJogador() {
		if (cenario instanceof JogoCenario) {
			Jogador jogador = new Jogador((JogoCenario) cenario);
			jogador.exibirJanelaJogador((int) tempoDecorrido);
		}
	}

	public static final Texto textoPausa = new Texto(new Font("Tahoma", Font.PLAIN, 20));

	public enum Tecla {
		CIMA, BAIXO, ESQUERDA, DIREITA, BA, BB
	}

	// Array de controle para rastrear o estado das teclas
	public static boolean[] controleTecla = new boolean[Tecla.values().length];

	public static void liberaTeclas() {
		for (int i = 0; i < controleTecla.length; i++) {
			controleTecla[i] = false;
		}
	}

	private void setaTecla(int tecla, boolean pressionada) {
		switch (tecla) {
			case KeyEvent.VK_UP:
				controleTecla[Tecla.CIMA.ordinal()] = pressionada;
				break;
			case KeyEvent.VK_DOWN:
				controleTecla[Tecla.BAIXO.ordinal()] = pressionada;
				break;
			case KeyEvent.VK_LEFT:
				controleTecla[Tecla.ESQUERDA.ordinal()] = pressionada;
				break;
			case KeyEvent.VK_RIGHT:
				controleTecla[Tecla.DIREITA.ordinal()] = pressionada;
				break;

			case KeyEvent.VK_ESCAPE:
				controleTecla[Tecla.BB.ordinal()] = pressionada;
				break;

			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_ENTER:
				controleTecla[Tecla.BA.ordinal()] = pressionada;
		}
	}

	public static int nivel;

	public static int velocidade;

	public static boolean pausado;

	public Jogo() {
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				setaTecla(e.getKeyCode(), false);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				setaTecla(e.getKeyCode(), true);
			}
		});

		buffer = new BufferedImage(JANELA_LARGURA, JANELA_ALTURA, BufferedImage.TYPE_INT_RGB);

		g2d = buffer.createGraphics();

		tela = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(buffer, 0, 0, null);

			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(JANELA_LARGURA, JANELA_ALTURA);
			}

			@Override
			public Dimension getMinimumSize() {
				return getPreferredSize();
			}
		};

		getContentPane().add(tela);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		pack();
		setVisible(true);
		tela.repaint();
	}

	private void carregarJogo() {
		cenario = new InicioCenario(tela.getWidth(), tela.getHeight());
		// cenario = new JogoCenario(canvas.getWidth(), canvas.getHeight());
		// cenario = new JogoCenarioDoRusso(canvas.getWidth(),
		// canvas.getHeight());
		cenario.carregar();
	}

	public void iniciarJogo() {
		long startTime = 0;
		long duration = 60 * 1000; // 1 minuto
		long prxAtualizacao = 0;
		boolean foiPausado = false;
		long tempoPausado = 0;

		while (true) {
			try {
				// verificar se a tecla de inicio foi pressionada
				if (controleTecla[Tecla.BA.ordinal()] && !jogoIniciado) {
					// Inicia o tempo quando aperta enter
					startTime = System.currentTimeMillis() - tempoPausado;
					jogoIniciado = true;
					mostrarTempo = true; // Define como true quando o jogo é iniciado
				}
				g2d.setColor(Color.RED); // Cor da fonte

				// atualiza o tempo decorrido
				if (mostrarTempo) {
					if (!Jogo.pausado || foiPausado) {
						if (!Jogo.pausado) {
							tempoDecorrido = (System.currentTimeMillis() - startTime - tempoPausado) / 1000;
						}
					}

					// Lógica para pausar o tempo
					if (Jogo.pausado && !foiPausado) {
						pauseTime = System.currentTimeMillis();
						foiPausado = true;
					} else if (!Jogo.pausado && foiPausado) {
						tempoPausado += (System.currentTimeMillis() - pauseTime);
						foiPausado = false;
					}

					// Adiciona esta lógica para verificar se o tempo atingiu zero
					if (tempoDecorrido > duration / 1000) {
						String textoTempoEsgotado = "Tempo Esgotado! Você Perdeu.";
						int larguraTexto = g2d.getFontMetrics().stringWidth(textoTempoEsgotado);
						int alturaTexto = g2d.getFontMetrics().getHeight();
						int xTexto = (JANELA_LARGURA - larguraTexto) / 2;
						int yTexto = (JANELA_ALTURA - alturaTexto) / 2;
						yTexto += 30;

						g2d.setColor(Color.YELLOW);
						g2d.drawString(textoTempoEsgotado, xTexto, yTexto);
						tela.repaint();

						exibirJanelaJogador();

						// Aguarda a tecla ESC ser pressionada para voltar ao menu
						while (true) {
							if (controleTecla[Tecla.BB.ordinal()]) {
								// Volta para o InicioCenario (menu)
								cenario.descarregar();
								cenario = new InicioCenario(tela.getWidth(), tela.getHeight());
								cenario.carregar();

								// Reinicia as variáveis de controle
								tempoDecorrido = 0;
								jogoIniciado = false;
								mostrarTempo = false;
								Jogo.pausado = false;
								pauseTime = 0;

								// Exibe a janela do jogador
								exibirJanelaJogador();

								liberaTeclas();
								break; // Sai do loop quando a tecla ESC é pressionada
							}

							// Aguarda um curto período de tempo para evitar um consumo excessivo de recursos
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						continue; // Continue para a próxima iteração do loop.
					} else {
						g2d.setColor(Color.RED);
						textoTempo.desenha(g2d, "Tempo: " + Math.max(0, duration / 1000 - tempoDecorrido) + "s", 20, 20);
					}
				}

				if (controleTecla[Tecla.BB.ordinal()]) {
					// Reinicia a contagem quando pressiona 'esc'
					if (!jogoIniciado) {
						startTime = System.currentTimeMillis();
						tempoPausado = 0;
					} else {
						if (Jogo.pausado) {
							pauseTime = System.currentTimeMillis();
						} else {
							tempoPausado += (System.currentTimeMillis() - pauseTime);
						}
					}

					// Reinicia as variáveis
					tempoDecorrido = 0;
					jogoIniciado = false;
					mostrarTempo = false;
					Jogo.pausado = !Jogo.pausado;
				}

				if (cenario instanceof JogoCenario && (((JogoCenario) cenario).getEstado() == JogoCenario.Estado.GANHOU
						|| ((JogoCenario) cenario).getEstado() == JogoCenario.Estado.PERDEU)) {

					mostrarTempo = false;

					if (controleTecla[Tecla.BB.ordinal()]) {
						// Volta para o InicioCenario (menu)
						cenario.descarregar();
						cenario = new InicioCenario(tela.getWidth(), tela.getHeight());
						cenario.carregar();

						// Reinicia as variáveis de controle
						tempoDecorrido = 0;
						jogoIniciado = false;
						mostrarTempo = false;
						Jogo.pausado = false;
						pauseTime = 0;

						// Exibe a janela do jogador
						exibirJanelaJogador();

						liberaTeclas();
						continue; // Continue para a próxima iteração do loop.
					}
				}

				if (System.currentTimeMillis() >= prxAtualizacao) {
					g2d.setColor(Color.BLACK);
					g2d.fillRect(0, 0, JANELA_LARGURA, JANELA_ALTURA);

					if (controleTecla[Tecla.BA.ordinal()]) {
						// Pressionou espaço ou enter
						if (cenario instanceof InicioCenario) {
							cenario.descarregar();
							cenario = null;

							if (Jogo.nivel < Nivel.niveis.length) {
								cenario = new JogoCenario(tela.getWidth(), tela.getHeight());
							}

							cenario.carregar();
						} else {
							Jogo.pausado = !Jogo.pausado;
						}

						liberaTeclas();
					} else if (controleTecla[Tecla.BB.ordinal()]) {
						// Pressionou ESQ
						if (!(cenario instanceof InicioCenario)) {
							cenario.descarregar();
							cenario = null;
							cenario = new InicioCenario(tela.getWidth(), tela.getHeight());
							cenario.carregar();
						}

						liberaTeclas();
					}

					if (cenario == null) {
						g2d.setColor(Color.WHITE);
						g2d.drawString("Carregando...", 20, 20);
					} else {
						if (!Jogo.pausado) {
							cenario.atualizar();
						}

						cenario.desenhar(g2d);
					}

					tela.repaint();
					prxAtualizacao = System.currentTimeMillis() + FPS;
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Jogo jogo = new Jogo();
		jogo.carregarJogo();
		jogo.iniciarJogo();

	}
}

package br.com.mvbos.lgj;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.*;

import br.com.mvbos.lgj.base.CenarioPadrao;
import br.com.mvbos.lgj.base.Elemento;
import br.com.mvbos.lgj.base.Texto;
import br.com.mvbos.lgj.base.Util;

import javax.swing.*;


public class JogoCenario extends CenarioPadrao {

	enum Estado {
		JOGANDO, GANHOU, PERDEU
	}

	private boolean janelaExibida = false;
	private boolean distribuicaoFeita = false;
	private int vidas = 5;

	private static final int _LARG = 40;

	private static final int RASTRO_INICIAL = 5;

	private int dx, dy;

	private boolean moveu;

	private int temporizador = 0;

	private int contadorRastro = RASTRO_INICIAL;

	private Elemento[] fruta = new Elemento[15];
	private Elemento[] objeto = new Elemento[10];
	private Elemento serpente;
	private Elemento[] nivel;
	private Elemento[] rastros;

	private Texto texto = new Texto(new Font("Arial", Font.PLAIN, 25));

	private Random rand = new Random();

	// Frutas para finalizar o level
	private int dificuldade = 15;

	private int contadorNivel = 0;

	private Estado estado = Estado.JOGANDO;

	private int PONTUACAO = 0;
	String SCORE = "PONTUAÇÃO: " + PONTUACAO;
	Font SCORE_FONT = new Font("Consolas", Font.BOLD, 25);

	public int getPONTUACAO() {
		return PONTUACAO;
	}

	public Estado getEstado() {
		return estado;
	}

	public JogoCenario(int largura, int altura) {
		super(largura, altura);
	}

	public void adicionarJogadorAoRanking(JogadorInfo jogador) {
		// Adicione a lógica para adicionar o jogador ao ranking
		// Pode ser algo como ranking.adicionarJogador(jogador);
	}

	@Override
	public void carregar() {


		// define direcao inicial
		int Roleta = rand.nextInt(2);
		if(Roleta == 0){
			dx = rand.nextInt(2);
			if(dx == 0){
				dx = -1;
			}
		}else {
			dy = rand.nextInt(2);
			if(dy == 0){
				dy = -1;
			}
		}

		rastros = new Elemento[dificuldade + RASTRO_INICIAL];

		for (int i = 0; i < 15; i++) {
			fruta[i] = new Elemento(0, 0, _LARG, _LARG);
			fruta[i].setCor(Color.RED);
		}
		for (int i = 0; i < 10; i++) {
			objeto[i] = new Elemento(0, 0, _LARG, _LARG);
			objeto[i].setCor(Color.LIGHT_GRAY);
		}

		int CDX;
		int CDY;
		int cordX = rand.nextInt(720);
		int cordY = rand.nextInt(720);
		if(cordX % 40 != 0){
			CDX = cordX - (cordX % 40);
		}else {
			CDX = cordX;
		}
		if(cordY % 40 != 0){
			CDY = cordY - (cordY % 40);
		}else {
			CDY = cordY;
		}
		serpente = new Elemento(CDX, CDY, _LARG, _LARG);
		serpente.setAtivo(true);
		serpente.setCor(Color.YELLOW);
		serpente.setVel(Jogo.velocidade);

		for (int i = 0; i < rastros.length; i++) {
			rastros[i] = new Elemento(serpente.getPx(), serpente.getPy(), _LARG, _LARG);
			rastros[i].setCor(Color.GREEN);
			rastros[i].setAtivo(true);
		}

		char[][] nivelSelecionado = Nivel.niveis[Jogo.nivel];
		nivel = new Elemento[nivelSelecionado.length * 2];

// Distribuir obstáculos e comidas apenas se ainda não foi feito
		if (!distribuicaoFeita && Jogo.controleTecla[Jogo.Tecla.BA.ordinal()]) {
			for (int i = 0; i < 10; i++) {
				distribuirElementoAleatoriamente(objeto[i], objeto);
			}

			for (int i = 0; i < 15; i++) {
				distribuirElementoAleatoriamente(fruta[i], fruta);
			}
			distribuicaoFeita = true; // Marcamos que a distribuição já foi feita
		}
	}

	private void distribuirElementoAleatoriamente(Elemento elemento, Elemento[] elementos) {
		Random rand = new Random();

		while (true) {
			int x = rand.nextInt(largura / _LARG) * _LARG;
			int y = rand.nextInt(altura / _LARG) * _LARG;

			// Garantir que as coordenadas estejam dentro dos limites da tela
			x = Math.min(Math.max(x, 0), largura - _LARG);
			y = Math.min(Math.max(y, 0), altura - _LARG);

			boolean posicaoLivre = true;

			// Verificar colisão com outros elementos
			for (Elemento e : elementos) {
				if (e.isAtivo() && Util.colide(elemento, e)) {
					posicaoLivre = false;
					break;
				}
			}

			// Verificar colisão com a serpente
			if (Util.colide(elemento, serpente)) {
				posicaoLivre = false;
			}

			if (posicaoLivre) {
				elemento.setPx(x);
				elemento.setPy(y);
				elemento.setAtivo(true);
				break;
			}
		}
	}

	@Override
	public void descarregar() {
		fruta = null;
		objeto = null;
		rastros = null;
		serpente = null;
	}

	@Override
	public void atualizar() {


		if (estado != Estado.JOGANDO) {
			return;
		}

		if (!moveu) {
			if (dy != 0) {
				if (Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()]) {
					dx = -1;

				} else if (Jogo.controleTecla[Jogo.Tecla.DIREITA.ordinal()]) {
					dx = 1;
				}

				if (dx != 0) {
					dy = 0;
					moveu = true;
				}

			} else if (dx != 0) {
				if (Jogo.controleTecla[Jogo.Tecla.CIMA.ordinal()]) {
					dy = -1;
				} else if (Jogo.controleTecla[Jogo.Tecla.BAIXO.ordinal()]) {
					dy = 1;
				}

				if (dy != 0) {
					dx = 0;
					moveu = true;
				}

			}
		}


		if (temporizador >= 20) {
			temporizador = 0;
			moveu = false;

			int x = serpente.getPx();
			int y = serpente.getPy();

			serpente.setPx(serpente.getPx() + (_LARG) * dx);
			serpente.setPy(serpente.getPy() + (_LARG) * dy);

			if (Util.SYC(serpente, largura, altura)) {
				serpente.setPx(serpente.getPx() + _LARG * dx);
				serpente.setPy(720);
				serpente.setAtivo(true);
				estado = Estado.JOGANDO;

			} else if (Util.SYB(serpente, largura, altura)) {
				serpente.setPx(serpente.getPx() + _LARG * dx);
				serpente.setPy(0);
				serpente.setAtivo(true);
				estado = Estado.JOGANDO;

			} else if (Util.SXE(serpente, largura, altura)) {
				serpente.setPx(720);
				serpente.setPy(serpente.getPy() + _LARG * dy);
				serpente.setAtivo(true);
				estado = Estado.JOGANDO;

			}else if (Util.SXD(serpente, largura, altura)) {
				serpente.setPx(0);
				serpente.setPy(serpente.getPy() + _LARG * dy);
				serpente.setAtivo(true);
				estado = Estado.JOGANDO;

			}else {
				// colisao com objeto (Serpente->objeto)
				for (int i = 0; i < 10; i++) {

					if (Util.colide(serpente, objeto[i])) {
						serpente.setAtivo(true);
						estado = Estado.JOGANDO;
						vidas--;
						PONTUACAO = PONTUACAO - 10;
						objeto[i].setAtivo(false);
						if(vidas == 0){
							serpente.setAtivo(false);
							estado = Estado.PERDEU;
						}
						return;
					}
				}
				for (int i = 0; i < 10; i++) {
					if (Util.colide(serpente, objeto[i])) {
						serpente.setAtivo(true);
						estado = Estado.JOGANDO;
						vidas--; // perde uma vida ao colidir com obstaculo
						objeto[i].setAtivo(false); // remove o obstaculo que causou a colisao
						if (vidas <= 0) {
							estado = Estado.PERDEU;
						}
						// Remover obstáculo do array nivel
						Elemento[] novoNivel = new Elemento[nivel.length - 1];
						int novoIndice = 0;

						for (int j = 0; j < nivel.length; j++) {
							if (j != i) {
								novoNivel[novoIndice++] = objeto[j];
							}
						}

						nivel = novoNivel;
						resetCenario();
						break;
					}
				}
				// colisao com o rastro (Serpente->rastro)
				for (int i = 0; i < contadorRastro; i++) {
					if (Util.colide(serpente, rastros[i])) {
						serpente.setAtivo(true);
						estado = Estado.JOGANDO;
						vidas--;
						PONTUACAO = PONTUACAO - 10;
						if(vidas==0){
							serpente.setAtivo(false);
							estado = Estado.PERDEU;
						}
						return;
					}
				}
			}

			for (int i = 0; i < 15; i++) {
				if (Util.colide(fruta[i], serpente)) {
					// Adiciona uma pausa
					temporizador = -10;
					contadorRastro++;
					PONTUACAO = PONTUACAO + 10;
					fruta[i].setAtivo(false);
					int frutasResto = rastros.length - contadorRastro;
					if (contadorRastro == rastros.length) {
						serpente.setAtivo(false);
						estado = Estado.GANHOU;
					}
					if (frutasResto % 3 == 0) {
						vidas++;
					}
				}
			}

			for (int i = 0; i < contadorRastro; i++) {
				Elemento rastro = rastros[i];
				int tx = rastro.getPx();
				int ty = rastro.getPy();

				rastro.setPx(x);
				rastro.setPy(y);

				x = tx;
				y = ty;
			}

		} else {
			temporizador += serpente.getVel();
		}

		// Adicionando Objetos
		for (int i = 0; i < 10; i++) {
			int x = rand.nextInt(largura / _LARG);
			int y = rand.nextInt(altura / _LARG);


			// colisao com serpente
			if (Util.colide(objeto[i], serpente)) {
				objeto[i].setAtivo(false);
				return;
			}
			// colisao com rastro
			for (int n = 0; n < 10; n++) {
				if (Util.colide(objeto[n], rastros[n])) {
					objeto[n].setAtivo(false);
					return;
				}
			}
			// colisao com objeto
			for (int n = 0; n < contadorNivel; n++) {
				if (Util.colide(objeto[n], objeto[n])) {
					objeto[n].setAtivo(false);
					return;
				}
			}
			// colisao com cenario
			while (vidas > 0) {
				boolean colidiu = false;

				for (int j = 0; j < contadorNivel; j++) {
					if (Util.colide(serpente, objeto[j])) {
						serpente.setAtivo(false);
						vidas--; // perde uma vida ao colidir com obstaculo

						objeto[j].setAtivo(false); // remove o obstaculo que causou a colisao
						colidiu = true;

						if (vidas <= 0) {
							estado = Estado.PERDEU;
						}

						// Remover obstáculo do array nivel
						Elemento[] novoNivel = new Elemento[nivel.length - 1];
						int novoIndice = 0;

						for (int h = 0; h < nivel.length; h++) {
							if (h != j) {
								novoNivel[novoIndice++] = nivel[h];
							}
						}

						nivel = novoNivel;

						break;
					}
				}

				if (!colidiu) {
					// Se não houve colisão, podemos sair do loop
					break;
				}
			}
		}

		// Adicionando frutas
		for (int n = 0; n < 15; n++){
			int x = rand.nextInt(largura / _LARG);
			int y = rand.nextInt(altura / _LARG);


			// colisao com a serpente
			if (Util.colide(fruta[n], serpente)) {
				fruta[n].setAtivo(false);
				return;
			}

			// colisao com rastro
			for (int i = 0; i < contadorRastro; i++) {
				if (Util.colide(fruta[n], rastros[i])) {
					fruta[n].setAtivo(false);
					return;
				}
			}

			// colisao com objeto
			for (int i = 0; i < contadorNivel; i++) {
				if (Util.colide(fruta[i], objeto[i])) {
					fruta[i].setAtivo(false);
					return;
				}
			}
		}
	}

	@Override
	public void desenhar(Graphics2D g) {
		//desenha PV
		if (estado == Estado.JOGANDO) {
			desenhapontos(g);
		}

		//desenha fruta
		for (int i = 0; i < 15; i++) {
			if (fruta[i].isAtivo()) {
				fruta[i].desenha(g);
			}
		}

		// desenha objeto
		for (int i = 0; i < 10; i++) {
			if (objeto[i].isAtivo()) {
				objeto[i].desenha(g);
			}
		}

		for (Elemento e : nivel) {
			if (e == null)
				break;

			e.desenha(g);
		}

		for (int i = 0; i < contadorRastro; i++) {
			rastros[i].desenha(g);
		}

		serpente.desenha(g);

		texto.desenha(g, String.valueOf(rastros.length - contadorRastro), largura - 35, 705);
		texto.desenha(g, "Vidas: " + vidas, largura - 150, 25);

		if (estado != Estado.JOGANDO) {

			if (estado == Estado.GANHOU)
				texto.desenha(g, "VENCEDOR!", 360, 360);
			else
				texto.desenha(g, "DERROTA!", 360, 360);
		}

		if (estado != Estado.JOGANDO && !janelaExibida) {
			if (estado == Estado.GANHOU || estado == Estado.PERDEU) {
				// Chama o método para exibir a janela de ranking
				Jogador jogador = new Jogador(this);
				jogador.exibirJanelaJogador(PONTUACAO);

				// Atualiza a variável para indicar que a janela foi exibida
				janelaExibida = true;
			}
		}

		if (Jogo.pausado)
			Jogo.textoPausa.desenha(g, "Pause", largura / 2 - Jogo.textoPausa.getFonte().getSize(), altura / 2);
	}

	public void desenhapontos(Graphics g) {
		SCORE = "PONTUACAO: " + PONTUACAO;
		g.setColor(Color.WHITE);
		g.setFont(SCORE_FONT);
		g.drawString(SCORE, 490, 705);
	}

	public void resetCenario() {
		estado = Estado.JOGANDO;
		moveu = false;
		temporizador = 0;
		contadorRastro = RASTRO_INICIAL;
		vidas = 5;

		serpente.setPx(0);
		serpente.setPy(0);
		serpente.setAtivo(true);
		serpente.setVel(Jogo.velocidade);


		for (int i = 0; i < rastros.length; i++) {
			rastros[i].setPx(serpente.getPx());
			rastros[i].setPy(serpente.getPy());
			rastros[i].setAtivo(true);
		}

		char[][] nivelSelecionado = Nivel.niveis[Jogo.nivel];
		contadorNivel = 0;


		for (int linha = 0; linha < nivelSelecionado.length; linha++) {
			for (int coluna = 1; coluna < nivelSelecionado[0].length; coluna++) {
				if (nivelSelecionado[linha][coluna] != ' ') {
					Elemento e = new Elemento();
					e.setAtivo(true);
					e.setCor(Color.LIGHT_GRAY);

					e.setPx(_LARG * coluna);
					e.setPy(_LARG * linha);

					e.setAltura(_LARG);
					e.setLargura(_LARG);

					nivel[contadorNivel++] = e;

				}
			}
		}
	}
}
package br.com.mvbos.lgj;

import java.awt.Graphics2D;
import java.util.Arrays;

import br.com.mvbos.lgj.base.CenarioPadrao;
import br.com.mvbos.lgj.base.Menu;
import br.com.mvbos.lgj.base.Util;

public class InicioCenario extends CenarioPadrao {

	public InicioCenario(int largura, int altura) {
		super(largura, altura);
	}

	private Menu menuJogo;

	private Menu menuVelInicial;

	@Override
	public void carregar() {

		menuJogo = new Menu("Fase");

		String[] opcoes = new String[Nivel.niveis.length + 1];

		Arrays.fill(opcoes, "Nivel Randomizado");

		opcoes[opcoes.length - 1] = "Do Russo";

		menuJogo.addOpcoes(opcoes);

		menuVelInicial = new Menu("Vel.");
		menuVelInicial.addOpcoes("Normal", "R�pido", "Lento");

		Util.centraliza(menuJogo, largura, altura);
		Util.centraliza(menuVelInicial, largura, altura);

		menuVelInicial.setPy(menuJogo.getPy() + menuJogo.getAltura());

		menuJogo.setAtivo(true);
		menuJogo.setSelecionado(true);
		menuVelInicial.setAtivo(true);
	}

	@Override
	public void descarregar() {
		Jogo.nivel = menuJogo.getOpcaoId();

		switch (menuVelInicial.getOpcaoId()) {
			case 0:
				Jogo.velocidade = 5;
				break;
			case 1:
				Jogo.velocidade = 20;
				break;
			case 2:
				Jogo.velocidade = 5;
		}

	}

	@Override
	public void atualizar() {
		if (Jogo.controleTecla[Jogo.Tecla.CIMA.ordinal()] || Jogo.controleTecla[Jogo.Tecla.BAIXO.ordinal()]) {
			if (menuJogo.isSelecionado()) {
				menuJogo.setSelecionado(false);
				menuVelInicial.setSelecionado(true);

			} else {
				menuJogo.setSelecionado(true);
				menuVelInicial.setSelecionado(false);
			}

		} else if (Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()] || Jogo.controleTecla[Jogo.Tecla.DIREITA.ordinal()]) {
			menuJogo.setTrocaOpcao(Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()]);
			menuVelInicial.setTrocaOpcao(Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()]);

		}

		Jogo.liberaTeclas();

	}

	@Override
	public void desenhar(Graphics2D g) {
		menuJogo.desenha(g);
		menuVelInicial.desenha(g);
	}

}

## Projeto do Jogo da Cobrinha (Snake Game) com Modificações

## Este projeto é uma implementação do clássico jogo da cobrinha, com adições e modificações feitas com base nos códigos desenvolvidos por Marcus Vinicius Becker, além de incluir desafios propostos no livro "Ascencio - Fundamentos da Programação de Computadores - Algoritmos, Pascal, C C++ e Java - 3a ed". O projeto abrange três partes principais: Jogabilidade, Ranking e Fluxo de Dados.

## Parte I - Jogabilidade:

<ol type="I"> <!--1 A a I i-->
<li> Distribuição de Obstáculos e Comidas:
<ul type="square">
<li>Dez obstáculos e quinze comidas são distribuídos aleatoriamente na tela do jogo.
<li>A direção inicial da cobrinha é gerada aleatoriamente entre cima, baixo, esquerda e direita.
</ul>
<li> Tempo de Duração do Jogo:

<ul type="square">
<li>
Um tempo pré-determinado limita a duração do jogo.
<li>O jogador inicia o jogo com cinco vidas.
</ul>

<li> Movimento da Cobrinha:

<ul type="square">
<li>
A cobrinha move-se em uma direção até que o jogador pressione uma das teclas de direção (←↑→↓) para alterar sua trajetória.
<li>
Restrições são aplicadas para evitar que a cobrinha passe por cima de si mesma.
</ul>

<li> Limites da Janela:
<ul type="square">
<li>
A cobrinha não pode ultrapassar os limites da janela.
<li>
Se a cobrinha atinge a borda, ela reaparece do lado oposto, mantendo a trajetória.
</ul>

<li>Comida e Obstáculo:
<ul type="square">
<li>
Quando a cabeça da cobrinha atinge uma comida, ela é ingerida, e a comida desaparece.
<li>
A cada três comidas ingeridas, o jogador ganha uma vida.
<li>
Ao atingir um obstáculo, a cobrinha perde uma vida.
</ul>

<li>Fim do Jogo:
<ul type="square">
<li>
O jogo termina quando a cobrinha come todas as comidas (vitória), fica sem vidas ou quando o tempo pré-determinado se esgota.
</ul>

## Parte II - Ranking:
<li>
Atualização de Pontuação:
<ul type="square">
<li>
A pontuação do jogador é atualizada e permanece visível durante toda a partida.
</ul>

<li>
Inclusão no Ranking:
<ul type="square">
<li>
Ao final de cada partida, o jogador informa seu nome para inclusão no ranking.
</ul>

<li>
Classificação do Ranking:
<ul type="square">
<li>
O ranking é classificado em ordem decrescente de pontuação.
<li>
São exibidas as dez maiores pontuações com os respectivos nomes dos jogadores.
</ul>

<li>
Implementação por Coleção:
<ul type="square">
<li>
O ranking é implementado por meio de uma coleção que armazena os dados dos jogadores.
</ul>

## Parte III - Fluxo de Dados:
<li>
Persistência de Dados:
<ul type="square">
<li>
O ranking é mantido mesmo após a finalização do programa, utilizando persistência de dados.
<li>
Leitura e gravação de arquivos (formato TXT) são implementadas para armazenar os dados do ranking.
</ul>


<li>
Inicialização do Programa:
<ul type="square">
<li>
Ao iniciar o programa, os dados do ranking (nome e pontuação dos jogadores) são lidos de um arquivo de texto.
</ul>

<li>
Atualização do Ranking:
<ul type="square">
<li>
Após a atualização do ranking na coleção, os dados dos jogadores são gravados de volta no mesmo arquivo.
</ul>


## Este projeto não apenas oferece uma implementação do jogo da cobrinha, mas também incorpora desafios adicionais, como a inclusão de um sistema de ranking persistente, proporcionando uma experiência mais completa e envolvente para o jogador.

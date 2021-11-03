package xadrez.pecas;

import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;

public class Rei extends PecaDeXadrez{

	// incluindo uma dependencia de PartidaDeXadrez da camada Xadrez, para implementar 
	// a jogada especial: roque. Sem essa dependencia, o rei nao consegue usar os metodos 
	// de PartidaDeXadrez pois estao em camadas diferentes
	private PartidaDeXadrez partida;
	
	// ----------------------------------------------------------------------------------------------------------
	
	public Rei(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partida) {
		super(tabuleiro, cor);
		this.partida= partida; // associação entre objetos
	}

	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "K"; // K ao inves de R pq tambem tem a rainha
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// metodo auxiliar para movimentosPossiveis (do rei)
	// o rei pode se mover para determinada posicao? é o que esse metodo responde
	private boolean podeMover(Posicao pos) {
		
		// downcasting de peca para pecadexadrez. Pega a posicao de destino e converte
		// para pecadexadrez
		PecaDeXadrez p= (PecaDeXadrez)getTabuleiro().peca(pos);
		
		// verificar se a peçadexadrez 'p' é nula (se a posicao no tabuleiro esta desocupada)
		// ou se tem peça de oponente
		return p == null || p.getCor() != getCor();
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo auxiliar para ajudar a testar a condicao de roque
	// a torre esta apta para o roque quando nao tiver sido movida (movCont == 0)
	private boolean testaRoqueTorre(Posicao pos) {
		
		// "p" recebe a peça que esta na posicao informada no parametro 
		PecaDeXadrez p= (PecaDeXadrez)getTabuleiro().peca(pos);
		
		// retorna o resultado da condição: a posição informada não é nula E essa peça é uma torre
		// E a cor dessa torre é igual a cor do rei E se essa torre nao se moveu ainda
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getMovCont() == 0;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// o rei so pode mover uma casa por vez, mas pode ser em qualquer direçao (horizontal, vertical e diagonais)
	@Override
	public boolean[][] movimentosPossiveis() {
		// criando uma matriz booleana do mesmo tamanho do tabuleiro
		// por padrão, as posições tem valor falso
		boolean[][] mat= new boolean[getTabuleiro().getLinhas()][getTabuleiro().getCols()];
		
		// criando uma posicao auxiliar para ter um valor de partida
		Posicao p= new Posicao(0, 0);
		
		// verificar para cima
		
		// "p" tem novos valores de posição "setados", com linha - 1 para subir a matriz
		p.setValores(posicao.getLinha() - 1, posicao.getCol());
		
		// agora testa se é possivel se mover para essa posicao
		// testa se ela existe e chama podeMover para fazer seus testes
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar para baixo
		
		// 'p' pega a linha e coluna do rei, que chama essa funcao, e vai testando os movimentos possiveis
		// pega a posicao da peça que quer mover e soma "1" na linha, para descer na matriz
		p.setValores(posicao.getLinha() + 1, posicao.getCol());
		
		// agora testa se é possivel se mover para essa posicao
		// testa se ela existe e chama podeMover para fazer seus testes
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar para esquerda
		
		// 'p' pega a linha e coluna do rei, que chama essa funcao, e vai testando os movimentos possiveis
		// pega a posicao da peça que quer mover e substrai "1" na coluna, para caminhar a esquerda na matriz
		p.setValores(posicao.getLinha(), posicao.getCol() - 1);
		
		// agora testa se é possivel se mover para essa posicao
		// testa se ela existe e chama podeMover para fazer seus testes
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar para direita
		
		// 'p' pega a linha e coluna do rei, que chama essa funcao, e vai testando os movimentos possiveis
		// pega a posicao da peça que quer mover e soma "1" na coluna, para caminhar a direita na matriz
		p.setValores(posicao.getLinha(), posicao.getCol() + 1);
		
		// agora testa se é possivel se mover para essa posicao
		// testa se ela existe e chama podeMover para fazer seus testes
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar diagonal cima/esquerda (noroeste)
		
		p.setValores(posicao.getLinha() - 1, posicao.getCol() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar diagonal cima/direita (nordeste)
		
		p.setValores(posicao.getLinha() - 1, posicao.getCol() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar diagonal baixo/esquerda (sudoeste)
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar diagonal baixo/direita (sudeste)
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// movimento especial roque (castling)
		// o rei move 2 casas para esquerda e a torre da esquerda se move 3 casas para direita
		// OU
		// o rei move 2 casas para direita e a torre da direita se move 2 casas para esquerda
		// roque pequeno (roque do lado do rei): movimenta 2 casas para direita
		// roque grande (roque do lado da rainha): movimenta 2 casas para esquerda
		// requisitos: essas posições devem estar vazias para realizar o movimento, o rei
		// e as torres não foram movidas (movCont == 0) e o rei nao esta em cheque
		
		if(getMovCont() == 0 && !partida.getCheque()) {
			
			// roque pequeno
			
			// 1- pega a posicao aonde esta a torre ao lado direito do rei
			Posicao posTorre1= new Posicao(posicao.getLinha(), posicao.getCol() + 3);
			
			// 2- verifica se tem uma torre la e se não foi movida antes
			if(testaRoqueTorre(posTorre1)) {
				
				// agora testa se as 2 casas a direita do rei estão vazias
				// 3- pega essas 2 posicões e faz o teste
				Posicao p1= new Posicao(posicao.getLinha(), posicao.getCol() + 1);
				Posicao p2= new Posicao(posicao.getLinha(), posicao.getCol() + 2);
				
				if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
					mat[posicao.getLinha()][posicao.getCol() + 2]= true; // o rei pode fazer o movimento para a segunda casa a direita
				}
			}
			
			// roque grande
			
			// 1- pega a posicao aonde esta a torre ao lado esquerdo do rei
			Posicao posTorre2= new Posicao(posicao.getLinha(), posicao.getCol() - 4);
			
			// 2- verifica se tem uma torre la e se não foi movida antes
			if(testaRoqueTorre(posTorre2)) {
				
				// agora testa se as 3 casas a esquerda do rei estão vazias
				// 3- pega essas 3 posicões e faz o teste
				Posicao p1= new Posicao(posicao.getLinha(), posicao.getCol() - 1);
				Posicao p2= new Posicao(posicao.getLinha(), posicao.getCol() - 2);
				Posicao p3= new Posicao(posicao.getLinha(), posicao.getCol() - 3);
				
				if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
					mat[posicao.getLinha()][posicao.getCol() - 2]= true; // o rei pode fazer o movimento para a segunda casa a direita
				}
			}
		}
		
		return mat;
	}
}

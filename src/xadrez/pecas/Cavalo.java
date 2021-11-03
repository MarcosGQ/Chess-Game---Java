package xadrez.pecas;

import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDeXadrez;

public class Cavalo extends PecaDeXadrez{

	public Cavalo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "C"; 
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// metodo auxiliar para movimentosPossiveis (do cavalo)
	// o cavalo pode se mover para determinada posicao? é o que esse metodo responde
	private boolean podeMover(Posicao pos) {
		
		// downcasting de peca para pecadexadrez. Pega a posicao de destino e converte
		// para pecadexadrez
		PecaDeXadrez p= (PecaDeXadrez)getTabuleiro().peca(pos);
		
		// verificar se a peçadexadrez 'p' é nula (se a posicao no tabuleiro esta desocupada)
		// ou se tem peça de oponente
		return p == null || p.getCor() != getCor();
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// o cavalo só movimenta na horizontal ou vertical e sempre em forma de "L": 
	// caminha duas casas para qualquer direção e uma casa para esquerda ou direita
	// alem disso, ele pode "passar por cima das peças para chegar no destino
	// mas para capturar peças adversarias deve esta deve ficar na ultima casa que forma
	// a ponta do "L"
	@Override
	public boolean[][] movimentosPossiveis() {
		// criando uma matriz booleana do mesmo tamanho do tabuleiro
		// por padrão, as posições tem valor falso
		boolean[][] mat= new boolean[getTabuleiro().getLinhas()][getTabuleiro().getCols()];
		
		// criando uma posicao auxiliar para ter um valor de partida
		Posicao p= new Posicao(0, 0);
		
		// verificar para 2x cima e direita

		p.setValores(posicao.getLinha() - 2, posicao.getCol() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar para 2x cima e esquerda
		
		p.setValores(posicao.getLinha() - 2, posicao.getCol() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar para 2x direita e cima

		p.setValores(posicao.getLinha() - 1, posicao.getCol() + 2);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar para 2x direita e baixo
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() + 2);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar 2x baixo e direita
		
		p.setValores(posicao.getLinha() + 2, posicao.getCol() + 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar 2x baixo e esquerda
		
		p.setValores(posicao.getLinha() + 2, posicao.getCol() - 1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar para 2x esquerda e cima
		
		p.setValores(posicao.getLinha() - 1, posicao.getCol() - 2);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		// verificar 2x esquerda e baixo
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() - 2);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getCol()]= true;
		}
		
		return mat;
	}

}

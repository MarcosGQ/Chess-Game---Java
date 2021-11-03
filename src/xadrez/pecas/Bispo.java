package xadrez.pecas;

import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDeXadrez;

public class Bispo extends PecaDeXadrez{

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		// TODO Auto-generated constructor stub
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// estou alterando a impressão dessa peça para apenas "B"
	// na hora de imprimir o tabuleiro, ela será reconhecida por essa letra
	// "Override" vai sobrescrever o metodo "toString"
	@Override
	public String toString() {
		return "B";
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// o bispo se movimenta apenas nas diagonais, o tanto que quiser (assim como a torre)
	@Override
	public boolean[][] movimentosPossiveis() {
		// criando uma matriz booleana do mesmo tamanho do tabuleiro
		// por padrão, as posições tem valor falso
		boolean[][] mat= new boolean[getTabuleiro().getLinhas()][getTabuleiro().getCols()];
		
		// criando uma posicao auxiliar para ter um valor de partida
		Posicao p= new Posicao(0, 0);
		
		// verificar para cima e esquerda (noroeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() - 1, posicao.getCol() - 1);
		
		// agora verifica se esse movimento é possivel conforme a condição do while
		// enquanto a posicao "p" existir e nao tiver uma peça nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() - 1, p.getCol() - 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma peça, verifica se é de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para cima e direita (nordeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() - 1, posicao.getCol() + 1);
		
		// agora verifica se esse movimento é possivel conforme a condição do while
		// enquanto a posicao "p" existir e nao tiver uma peça nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() - 1, p.getCol() + 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma peça, verifica se é de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para baixo e esquerda (sudoeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() - 1);
		
		// agora verifica se esse movimento é possivel conforme a condição do while
		// enquanto a posicao "p" existir e nao tiver uma peça nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() + 1, p.getCol() - 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma peça, verifica se é de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para baixo e direita (sudeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() + 1);
		
		// agora verifica se esse movimento é possivel conforme a condição do while
		// enquanto a posicao "p" existir e nao tiver uma peça nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() + 1, p.getCol() + 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma peça, verifica se é de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		return mat;
	}

}

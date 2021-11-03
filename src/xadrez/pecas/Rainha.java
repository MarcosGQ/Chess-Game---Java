package xadrez.pecas;

import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDeXadrez;

public class Rainha extends PecaDeXadrez{

	public Rainha(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		// TODO Auto-generated constructor stub
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "Q";
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// os movimentos da rainha � uma mistura de torre e bispo
	// ela se movimenta para horizontal, vertical e diagonais
	// pode andar o tanto que quiser ou at� capturar uma pe�a adversaria
	@Override
	public boolean[][] movimentosPossiveis() {
		// criando uma matriz booleana do mesmo tamanho do tabuleiro
		// por padr�o, as posi��es tem valor falso
		boolean[][] mat= new boolean[getTabuleiro().getLinhas()][getTabuleiro().getCols()];
		
		// criando uma posicao auxiliar para ter um valor de partida
		Posicao p= new Posicao(0, 0);
		
		// verificar para cima -----------------------------------------------------
		
		// "p" tem novos valores de posi��o "setados", com linha - 1 para subir a matriz
		p.setValores(posicao.getLinha() - 1, posicao.getCol());
		
		// agora verifica essa "subida" conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao (posicao vaga)
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setLinha(p.getLinha() - 1); // sobe mais uma vez
		}
		
		// se a subida do while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para baixo -----------------------------------------------------
		
		// pega a posicao da pe�a que quer mover e soma "1" na linha, para descer
		p.setValores(posicao.getLinha() + 1, posicao.getCol());
		
		// agora verifica essa "descida" conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao (posicao vaga)
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setLinha(p.getLinha() + 1); // desce mais uma vez
		}
		
		// se a subida do while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para esquerda -----------------------------------------------------
		
		// pega a posicao da pe�a que quer mover e substrai "1" na coluna, para caminhar � esquerda
		p.setValores(posicao.getLinha(), posicao.getCol() - 1);
		
		// agora verifica essa "caminhada" conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao (posicao vaga)
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setCol(p.getCol() - 1); // caminha mais uma vez
		}
		
		// se a subida do while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para direita -----------------------------------------------------
		
		// pega a posicao da pe�a que quer mover e soma "1" na coluna, para caminhar � direita
		p.setValores(posicao.getLinha(), posicao.getCol() + 1);
		
		// agora verifica essa "caminhada" conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao (posicao vaga)
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setCol(p.getCol() + 1); // caminha mais uma vez
		}
		
		// se a subida do while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para cima e esquerda (noroeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() - 1, posicao.getCol() - 1);
		
		// agora verifica se esse movimento � possivel conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() - 1, p.getCol() - 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para cima e direita (nordeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() - 1, posicao.getCol() + 1);
		
		// agora verifica se esse movimento � possivel conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() - 1, p.getCol() + 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para baixo e esquerda (sudoeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() - 1);
		
		// agora verifica se esse movimento � possivel conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() + 1, p.getCol() - 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		// verificar para baixo e direita (sudeste) -----------------------------------------------------
		
		p.setValores(posicao.getLinha() + 1, posicao.getCol() + 1);
		
		// agora verifica se esse movimento � possivel conforme a condi��o do while
		// enquanto a posicao "p" existir e nao tiver uma pe�a nessa posicao
		while(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
			p.setValores(p.getLinha() + 1, p.getCol() + 1); // movimenta mais uma vez
		}
		
		// se o movimento no while termina porque encontrou uma pe�a, verifica se � de oponente
		if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
			mat[p.getLinha()][p.getCol()]= true; // essa posicao fica verdadeira
		}
		
		return mat;
	}
}

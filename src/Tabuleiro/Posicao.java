package Tabuleiro; // representa o tabuleiro do jogo

// Posicao é uma classe da camada de tabuleiro
// ela vai representar uma posicao no tabuleiro
// a posicao de cada peça

public class Posicao {
	
	// atributos
	private int linha;
	private int col;
	
	// construtor
	public Posicao(int linha, int col) {

		this.linha = linha;
		this.col = col;
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// gets e sets
	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para atualizar os valores de uma posicao
	public void setValores(int li, int co) {
		linha= li;
		col= co;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// imprimir a posicao na tela
	// "Override" vai sobrescrever o metodo "toString"
	@Override
	public String toString() {
		return linha + ", " + col;
	}
	
}

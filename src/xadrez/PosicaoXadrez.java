package xadrez;

import Tabuleiro.Posicao;

// essa classe vai movimentar as pe�as no tabuleiro, seguindo a ordem
// expressa no tabuleiro impresso na tela, com as coordenadas de a1 a h8
public class PosicaoXadrez {

	// atributos
	private char col;
	private int lin;
	
	// ----------------------------------------------------------------------------------------------------------
	
	// construtor
	public PosicaoXadrez(char col, int lin) {
		// tratamento de erro
		if(col < 'a' || col > 'h' || lin < 1 || lin > 8) {
			throw new ExcessaoXadrez("Erro: coordenadas erradas. Deve-se escolher entre a1 ate h8");
		}
		this.col= col;
		this.lin= lin;
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// metodos get. N�o ha metodos set pq nao quero permitir alterar a posicao
	// das pe�as fora da funcao especifica para isso
	public char getCol() {
		return col;
	}

	public int getLin() {
		return lin;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// o tabuleiro � uma matriz bidimensional
	// cada linha � igual a: 8 - posicao_da_pe�a_na_linha
	// exemplo: quero mover uma pe�a para linha 7, logo: 8-7= 1
	// a pe�a � movida para a (linha 1 na matriz) 
	// e (linha 7 no tabuleiro que esta impresso na tela)
	
	// quanto as colunas:
	// a= 0, b= 1, c= 2, d= 3, ..., h= 7
	// se fizer:
	// 'a' - 'a' = 0
	// 'b' - 'a' = 1
	// 'c' - 'a' = 2
	// 'h' - 'a' = 7
	// logo, a formula � igual a: posicao_da_pe�a_na_coluna - 'a'
	
	// esse metodo retorna a posicao da pe�a na matriz
	protected Posicao retornaPeca() {
		return new Posicao(8 - lin, col - 'a');
	}
	
	// esse metodo pega uma posicao de matriz e converte para posicao do tabuleiro de xadrez
	protected static PosicaoXadrez convertePosicao(Posicao pos) {
		return new PosicaoXadrez((char)('a' + pos.getCol()), 8 - pos.getLinha());
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "" + col + lin; // esse "" for�a o compilador a entender que isso � uma concatena��o de strings
	}
}

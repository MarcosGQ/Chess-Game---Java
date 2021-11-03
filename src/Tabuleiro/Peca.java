package Tabuleiro;

// apesar do nome da classe, ela não representa as peças do jogo, mas
// cada posição do tabuleiro, cada "peça" é uma posicao na matriz
public abstract class Peca {
		
	// "protected" porque essa posicao nao é a posicao da peça
	// no tabuleiro, mas sim uma posicao simples em matriz
	// e por isso eu nao quero que ela seja visivel na camada 
	// de xadrez
	protected Posicao posicao;
	
	// é preciso associar a peça ao tabuleiro
	private Tabuleiro tabuleiro;

	// ----------------------------------------------------------------------------------------------------------
	
	// construtor sem insercao de valor para o atributo posicao
	// pq a posicao de uma peça recem criada é nula 
	// ela nao foi posta no tabuleiro ainda
	public Peca(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		posicao= null;
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// sem o metodo "set" pq nao quero permitir que o tabuleiro
	// seja alterado (nao pode criar novo tabuleiro com jogo em curso)
	// "protected" pq somente classes dentro do pacote Tabuleiro
	// e subclasses (de peças) é que poderão acessar o tabuleiro
	// nao quero que o tabuleiro seja acessado pela camada "xadrez"
	protected Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// metodo abstrato nao possui corpo
	public abstract boolean[][] movimentosPossiveis();
		
	public boolean movimentoPossivel(Posicao pos) {
		// temos aqui um metodo concreto usando um metodo abstrato
		// movimentoPossivel é um metodo concreto porque ele esta 
		// chamando uma possivel implementacao de alguma subclasse concreta da
		// classe Peca. (fazendo um gancho com uma subclasse)
		return movimentosPossiveis()[pos.getLinha()][pos.getCol()];
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// verifica se ha movimentos possiveis no tabuleiro, para determinada peça
	// leva em consideração movimento generico de peça generica, nao considera
	// as peças especiais do xadrez
	public boolean existeMovimentoPossivel() {
		// matriz booleana que vai receber a chamada do metodo abstrato
		boolean[][] mat= movimentosPossiveis();
		
		// percorrer essa matriz para saber se tem alguma posicao verdadeira
		// (possivel de mover uma peça)
		for(int i= 0; i<mat.length; i++) {
			for(int j= 0; j<mat.length; j++) {
				if(mat[i][j]) { // se essa posicao for verdade
					return true; // entao ha movimento possivel
				}
					
			}
		}
		return false; // senao, nao há movimentos possiveis
	}
	
}

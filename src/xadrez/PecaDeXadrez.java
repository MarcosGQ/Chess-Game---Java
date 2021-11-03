package xadrez;

import Tabuleiro.Peca;
import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;

// PecaDeXadrez é subclasse de Peca
public abstract class PecaDeXadrez extends Peca{
	
	// atributo
	private Cor cor;
	private int movCont; // valores inteiros, por padrão, iniciam em zero
						 // movCont faz a contagem de movimentos de uma peça
	
	// ----------------------------------------------------------------------------------------------------------
	
	// construtor
	public PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro); // o parametro tabuleiro repassa sua chamada para o construtor da super classe Peca
		this.cor = cor;
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// metodo get. Não há set pq nao quero que a cor de uma peça possa ser modificada durante o jogo 
	public Cor getCor() {
		return cor;
	}

	public int getMovCont() {
		return movCont;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	public void aumentaMovCont() {
		movCont++;
	}
	
	public void diminuiMovCont() {
		movCont--;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// preciso da posicao da peça no tabuleiro, mas o atributo posicao da classe Peca
	// que referencia a sua posicao na matriz, é "protected", entao, pra capturar essa
	// posicao, eu pego a posição da PecaDeXadrez
	public PosicaoXadrez getPosicaoXadrez() {
		//usa o metodo convertePosicao em "posicao", que foi herdado de Peca
		// a chamada do metodo e dessa forma pq ele é estatico e protegido
		return PosicaoXadrez.convertePosicao(posicao); 
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// acessivel somente pelas classes da mesma camada e por subclasses
	protected boolean temPecaDeOponente(Posicao pos) {
		// 'p' recebe a peça que estiver na posicao dada no parametro
		// feito um downcasting para transformar a Peca em PecaDeXadrez
		PecaDeXadrez p= (PecaDeXadrez)getTabuleiro().peca(pos);
		
		// se 'p' é diferente de nulo (se ha peça na posicao de destino)
		// e se é diferente da cor da peça que quero mover (que sai da posicao de origem)
		return p != null && p.getCor() != cor;
	}
	
}

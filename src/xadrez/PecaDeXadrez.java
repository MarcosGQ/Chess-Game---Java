package xadrez;

import Tabuleiro.Peca;
import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;

// PecaDeXadrez � subclasse de Peca
public abstract class PecaDeXadrez extends Peca{
	
	// atributo
	private Cor cor;
	private int movCont; // valores inteiros, por padr�o, iniciam em zero
						 // movCont faz a contagem de movimentos de uma pe�a
	
	// ----------------------------------------------------------------------------------------------------------
	
	// construtor
	public PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro); // o parametro tabuleiro repassa sua chamada para o construtor da super classe Peca
		this.cor = cor;
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// metodo get. N�o h� set pq nao quero que a cor de uma pe�a possa ser modificada durante o jogo 
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
	
	// preciso da posicao da pe�a no tabuleiro, mas o atributo posicao da classe Peca
	// que referencia a sua posicao na matriz, � "protected", entao, pra capturar essa
	// posicao, eu pego a posi��o da PecaDeXadrez
	public PosicaoXadrez getPosicaoXadrez() {
		//usa o metodo convertePosicao em "posicao", que foi herdado de Peca
		// a chamada do metodo e dessa forma pq ele � estatico e protegido
		return PosicaoXadrez.convertePosicao(posicao); 
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// acessivel somente pelas classes da mesma camada e por subclasses
	protected boolean temPecaDeOponente(Posicao pos) {
		// 'p' recebe a pe�a que estiver na posicao dada no parametro
		// feito um downcasting para transformar a Peca em PecaDeXadrez
		PecaDeXadrez p= (PecaDeXadrez)getTabuleiro().peca(pos);
		
		// se 'p' � diferente de nulo (se ha pe�a na posicao de destino)
		// e se � diferente da cor da pe�a que quero mover (que sai da posicao de origem)
		return p != null && p.getCor() != cor;
	}
	
}

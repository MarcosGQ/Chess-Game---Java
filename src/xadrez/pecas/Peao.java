package xadrez.pecas;

import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;
import xadrez.PecaDeXadrez;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;

public class Peao extends PecaDeXadrez {
	
	// incluindo uma dependencia de PartidaDeXadrez da camada Xadrez, para implementar 
	// a jogada especial: en passant. Sem essa dependencia, o rei nao consegue usar os 
	// metodos de PartidaDeXadrez pois estao em camadas diferentes
	private PartidaDeXadrez partida;	
		
	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partida) {
		super(tabuleiro, cor);
		this.partida= partida; // associação entre objetos
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "P";
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// logica de movimentos possiveis de um peão
	// o peão só pode mover em linha reta, em direção ao oponente, e uma casa por vez
	// se tiver contagem de movimentos = 0 (movCont= 0), ou seja, se for a 1ª vez movendo
	// um peão, podera move lo 2 casas a frente
	// se houver uma peça adversaria na diagonal do peão, a uma casa de distancia, ele poderá
	// se mover na diagonal para capturar essa peça
	@Override
	public boolean[][] movimentosPossiveis(){
		
		// criando uma matriz booleana do mesmo tamanho do tabuleiro
		// por padrão, as posições tem valor falso
		boolean[][] mat= new boolean[getTabuleiro().getLinhas()][getTabuleiro().getCols()];
		
		// criando uma posicao auxiliar para ter um valor de partida
		Posicao p= new Posicao(0, 0);
		
		// peões brancos se movem para cima na matriz
		if(getCor() == Cor.branco) {
			
			// sobe uma casa -----------------------------------------------------
			
			p.setValores(posicao.getLinha() - 1, posicao.getCol());
			
			// agora testa se essa posicao existe e se nao tem nenhuma peça la ou se tem peça mas é de adversario
			if(getTabuleiro().posicaoExiste(p) && (!getTabuleiro().temUmaPeca(p) || temPecaDeOponente(p))) {
				mat[p.getLinha()][p.getCol()]= true; // marca essa posicao na matriz boleana como movimento possivel
			}
			
			// sobe 2 casas -----------------------------------------------------
			
			// para ir até a segunda casa, tem que passar pela primeira, e por isso, precisa verificar se pode fazer isso
			Posicao p2= new Posicao(posicao.getLinha() - 1, posicao.getCol());
			// agora verifica se pode ir pra segunda casa
			p.setValores(posicao.getLinha() - 2, posicao.getCol());
			
			// testa se as posicoes "p" e "p2" existem, se nao tem peça la e se movCont é igual a 0
			if((getTabuleiro().posicaoExiste(p) && (!getTabuleiro().temUmaPeca(p) || temPecaDeOponente(p)) && getTabuleiro().posicaoExiste(p2) && (!getTabuleiro().temUmaPeca(p2) || temPecaDeOponente(p2)) && getMovCont() == 0 ) ) {
				mat[p.getLinha()][p.getCol()]= true; // marca a segunda casa como "true"
			}
			
			// sobe para diagonal esquerda (noroeste) -----------------------------------------------------
			
			p.setValores(posicao.getLinha() - 1, posicao.getCol() - 1);
			if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
				mat[p.getLinha()][p.getCol()]= true; 
			}
			
			// sobe para diagonal direita (nordeste) -----------------------------------------------------
			
			p.setValores(posicao.getLinha() - 1, posicao.getCol() + 1);
			if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
				mat[p.getLinha()][p.getCol()]= true; 
			}
			
			// movimento especial en passant -----------------------------------------------------
			// quando um peão adversario move pela primeira vez e caminha 2 casas, no 
			// proximo, se algum peão seu puder se mover na diagonal e ficar a frente
			// daquele peão, ele será capturado
			
			// para um peão branco executar o en passant, ele precisa estar na linha 3 da
			// matriz, pois essa é a linha que o peão preto estara se mover 2 casas
			if(posicao.getLinha() == 3) {
				
				// verificar se tem um peão preto do lado esquerdo do peão branco 
				Posicao esquerda= new Posicao(posicao.getLinha(), posicao.getCol() - 1);
				
				// a posicao aonde esta "esquerda" é uma posicao valida na matriz? E
				// tem uma peça de oponente nessa posicao? E
				// essa peça é a mesma que está referenciada na variavel "enPassant"?
				if(getTabuleiro().posicaoExiste(esquerda) && temPecaDeOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getEnPassant()) {
					
					// o peão branco não ocupa o espaço da peça adversaria, mas uma linha
					// acima dele
					mat[esquerda.getLinha() - 1][esquerda.getCol()]= true;
				}
				
				// verificar se tem um peão preto do lado direito do peão branco 
				Posicao direita= new Posicao(posicao.getLinha(), posicao.getCol() + 1);
				
				if(getTabuleiro().posicaoExiste(direita) && temPecaDeOponente(direita) && getTabuleiro().peca(direita) == partida.getEnPassant()) {
					mat[direita.getLinha() - 1][direita.getCol()]= true;
				}
			}
		}
		else { // peões pretos se movem para baixo na matriz
			
			// desce uma casa -----------------------------------------------------
			
			p.setValores(posicao.getLinha() + 1, posicao.getCol());
			// agora testa se essa posicao existe e se nao tem nenhuma peça la
			if(getTabuleiro().posicaoExiste(p) && (!getTabuleiro().temUmaPeca(p) || temPecaDeOponente(p))) {
				mat[p.getLinha()][p.getCol()]= true; // marca essa posicao na matriz boleana como movimento possivel
			}
			
			// desce 2 casas -----------------------------------------------------
			
			p.setValores(posicao.getLinha() + 2, posicao.getCol());
			// mas é preciso verificar se a primeira casa a frente do peão esta disponivel
			// para entao tentar mover-se para a segunda (ver se é possivel fazer isso)
			Posicao p2= new Posicao(posicao.getLinha() + 1, posicao.getCol());	
			// testa se as posicoes "p" e "p2" existem, se nao tem peça la e se movCont é igual a 0
			if(getTabuleiro().posicaoExiste(p) && (!getTabuleiro().temUmaPeca(p) || temPecaDeOponente(p)) && getTabuleiro().posicaoExiste(p2) && (!getTabuleiro().temUmaPeca(p2) || temPecaDeOponente(p2)) && getMovCont() == 0) {
				mat[p.getLinha()][p.getCol()]= true; // marca a segunda casa como "true"
			}
			
			// desce para diagonal esquerda (sudoeste) -----------------------------------------------------
			
			p.setValores(posicao.getLinha() + 1, posicao.getCol() - 1);
			if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
				mat[p.getLinha()][p.getCol()]= true; 
			}
			
			// desce para diagonal direita (sudeste) -----------------------------------------------------
			
			p.setValores(posicao.getLinha() + 1, posicao.getCol() + 1);
			if(getTabuleiro().posicaoExiste(p) && temPecaDeOponente(p)) {
				mat[p.getLinha()][p.getCol()]= true; 
			}
			
			// movimento especial en passant -----------------------------------------------------
			
			// para um peão preto executar o en passant, ele precisa estar na linha 4 da
			// matriz, pois essa é a linha que o peão branco estara se mover 2 casas
			if(posicao.getLinha() == 4) {
				
				// verificar se tem um peão branco do lado esquerdo do peão preto 
				Posicao esquerda= new Posicao(posicao.getLinha(), posicao.getCol() - 1);
				
				if(getTabuleiro().posicaoExiste(esquerda) && temPecaDeOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getEnPassant()) {
					mat[esquerda.getLinha() + 1][esquerda.getCol()]= true;
				}
				
				// verificar se tem um peão branco do lado direito do peão preto 
				Posicao direita= new Posicao(posicao.getLinha(), posicao.getCol() + 1);
				
				if(getTabuleiro().posicaoExiste(direita) && temPecaDeOponente(direita) && getTabuleiro().peca(direita) == partida.getEnPassant()) {
					mat[direita.getLinha() + 1][direita.getCol()]= true;
				}
			}
		}
							
		return mat;
	}
}

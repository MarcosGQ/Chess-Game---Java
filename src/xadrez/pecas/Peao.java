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
		this.partida= partida; // associa��o entre objetos
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "P";
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// logica de movimentos possiveis de um pe�o
	// o pe�o s� pode mover em linha reta, em dire��o ao oponente, e uma casa por vez
	// se tiver contagem de movimentos = 0 (movCont= 0), ou seja, se for a 1� vez movendo
	// um pe�o, podera move lo 2 casas a frente
	// se houver uma pe�a adversaria na diagonal do pe�o, a uma casa de distancia, ele poder�
	// se mover na diagonal para capturar essa pe�a
	@Override
	public boolean[][] movimentosPossiveis(){
		
		// criando uma matriz booleana do mesmo tamanho do tabuleiro
		// por padr�o, as posi��es tem valor falso
		boolean[][] mat= new boolean[getTabuleiro().getLinhas()][getTabuleiro().getCols()];
		
		// criando uma posicao auxiliar para ter um valor de partida
		Posicao p= new Posicao(0, 0);
		
		// pe�es brancos se movem para cima na matriz
		if(getCor() == Cor.branco) {
			
			// sobe uma casa -----------------------------------------------------
			
			p.setValores(posicao.getLinha() - 1, posicao.getCol());
			
			// agora testa se essa posicao existe e se nao tem nenhuma pe�a la ou se tem pe�a mas � de adversario
			if(getTabuleiro().posicaoExiste(p) && (!getTabuleiro().temUmaPeca(p) || temPecaDeOponente(p))) {
				mat[p.getLinha()][p.getCol()]= true; // marca essa posicao na matriz boleana como movimento possivel
			}
			
			// sobe 2 casas -----------------------------------------------------
			
			// para ir at� a segunda casa, tem que passar pela primeira, e por isso, precisa verificar se pode fazer isso
			Posicao p2= new Posicao(posicao.getLinha() - 1, posicao.getCol());
			// agora verifica se pode ir pra segunda casa
			p.setValores(posicao.getLinha() - 2, posicao.getCol());
			
			// testa se as posicoes "p" e "p2" existem, se nao tem pe�a la e se movCont � igual a 0
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
			// quando um pe�o adversario move pela primeira vez e caminha 2 casas, no 
			// proximo, se algum pe�o seu puder se mover na diagonal e ficar a frente
			// daquele pe�o, ele ser� capturado
			
			// para um pe�o branco executar o en passant, ele precisa estar na linha 3 da
			// matriz, pois essa � a linha que o pe�o preto estara se mover 2 casas
			if(posicao.getLinha() == 3) {
				
				// verificar se tem um pe�o preto do lado esquerdo do pe�o branco 
				Posicao esquerda= new Posicao(posicao.getLinha(), posicao.getCol() - 1);
				
				// a posicao aonde esta "esquerda" � uma posicao valida na matriz? E
				// tem uma pe�a de oponente nessa posicao? E
				// essa pe�a � a mesma que est� referenciada na variavel "enPassant"?
				if(getTabuleiro().posicaoExiste(esquerda) && temPecaDeOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getEnPassant()) {
					
					// o pe�o branco n�o ocupa o espa�o da pe�a adversaria, mas uma linha
					// acima dele
					mat[esquerda.getLinha() - 1][esquerda.getCol()]= true;
				}
				
				// verificar se tem um pe�o preto do lado direito do pe�o branco 
				Posicao direita= new Posicao(posicao.getLinha(), posicao.getCol() + 1);
				
				if(getTabuleiro().posicaoExiste(direita) && temPecaDeOponente(direita) && getTabuleiro().peca(direita) == partida.getEnPassant()) {
					mat[direita.getLinha() - 1][direita.getCol()]= true;
				}
			}
		}
		else { // pe�es pretos se movem para baixo na matriz
			
			// desce uma casa -----------------------------------------------------
			
			p.setValores(posicao.getLinha() + 1, posicao.getCol());
			// agora testa se essa posicao existe e se nao tem nenhuma pe�a la
			if(getTabuleiro().posicaoExiste(p) && (!getTabuleiro().temUmaPeca(p) || temPecaDeOponente(p))) {
				mat[p.getLinha()][p.getCol()]= true; // marca essa posicao na matriz boleana como movimento possivel
			}
			
			// desce 2 casas -----------------------------------------------------
			
			p.setValores(posicao.getLinha() + 2, posicao.getCol());
			// mas � preciso verificar se a primeira casa a frente do pe�o esta disponivel
			// para entao tentar mover-se para a segunda (ver se � possivel fazer isso)
			Posicao p2= new Posicao(posicao.getLinha() + 1, posicao.getCol());	
			// testa se as posicoes "p" e "p2" existem, se nao tem pe�a la e se movCont � igual a 0
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
			
			// para um pe�o preto executar o en passant, ele precisa estar na linha 4 da
			// matriz, pois essa � a linha que o pe�o branco estara se mover 2 casas
			if(posicao.getLinha() == 4) {
				
				// verificar se tem um pe�o branco do lado esquerdo do pe�o preto 
				Posicao esquerda= new Posicao(posicao.getLinha(), posicao.getCol() - 1);
				
				if(getTabuleiro().posicaoExiste(esquerda) && temPecaDeOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getEnPassant()) {
					mat[esquerda.getLinha() + 1][esquerda.getCol()]= true;
				}
				
				// verificar se tem um pe�o branco do lado direito do pe�o preto 
				Posicao direita= new Posicao(posicao.getLinha(), posicao.getCol() + 1);
				
				if(getTabuleiro().posicaoExiste(direita) && temPecaDeOponente(direita) && getTabuleiro().peca(direita) == partida.getEnPassant()) {
					mat[direita.getLinha() + 1][direita.getCol()]= true;
				}
			}
		}
							
		return mat;
	}
}

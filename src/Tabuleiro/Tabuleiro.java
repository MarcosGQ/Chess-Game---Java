package Tabuleiro;

public class Tabuleiro {

	// atributos
	private int linhas; // total de linhas do tabuleiro
	private int cols; // total de colunas
	private Peca[][] pecas; // matriz de peças que representa o tabuleiro
	
	// ----------------------------------------------------------------------------------------------------------
	
	// construtor
	public Tabuleiro(int linhas, int cols) {
		if(linhas<1 || cols <1) {
			throw new Excessao("Erro ao criar o tabuleiro: e preciso que tenha pelo menos 1 linha e 1 coluna");
		}
		
		this.linhas = linhas;
		this.cols = cols;
		pecas= new Peca[cols][linhas]; // cria a matriz e seu tamanho
	}

	// ----------------------------------------------------------------------------------------------------------
	
	// gets de linhas e colunas
	// sem "set" para linhas e colunas para nao poder alterar esses valores durante a partida
	// sem get e set para Peças pq essas serão tratadas por outros metodos
	// Tabuleiro nao retorna a matriz inteira, mas apenas uma peça por vez
	public int getLinhas() {
		return linhas;
	}

	public int getCols() {
		return cols;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// retorna a matriz pecas na posicao inserida no parametro
	public Peca peca(int li, int co) {
		if(!posicaoExiste(li, co)) { // tratamento de erro, checa se a posicao é valida
			throw new Excessao("Erro: essa posicao nao pertence ao tabuleiro");
		}
		return pecas[li][co];
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// sobrecarga do metodo acima, alterando seu retorno de funcao
	public Peca peca(Posicao posicao) {
		if(!posicaoExiste(posicao)) { // tratamento de erro, checa se a posicao é valida
			throw new Excessao("Erro: essa posicao nao pertence ao tabuleiro");
		}
		return pecas[posicao.getLinha()][posicao.getCol()];
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para colocar peca no tabuleiro
	public void colocaPeca(Peca peca, Posicao pos) {
		// primeiro teste se ja existe uma peça nessa posicao
		if(temUmaPeca(pos)) {
			throw new Excessao("Erro: tem uma peca nessa posicao");
		}
		
		// ir na matriz de peças do tabuleiro e inserir a peça que veio no argumento
		pecas[pos.getLinha()][pos.getCol()]= peca;
		
		// alterar a posicao dessa peça, que não é mais "null"
		// tenho acesso livre a posicao do objeto peca da classe Peca, pois apesar
		// de ser atributo protected, esta na mesma camada da classe tabuleiro
		peca.posicao= pos;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para remover uma peça do tabuleiro
	public Peca removePeca(Posicao pos) {
		// tratamento de erro
		if(!posicaoExiste(pos)) {
			throw new Excessao("Erro: essa posicao nao pertence ao tabuleiro");
		}
		
		// a posicao existe, mas tem peça la?
		if(peca(pos) == null ) {
			return null; 
		}
		
		Peca aux= peca(pos); // passa a peça para aux
		aux.posicao= null; // fala que aux representa nulo (nada)
		pecas[pos.getLinha()][pos.getCol()]= null; // essa posicao do tabuleiro recebe nulo
		return aux; // retorna a peça removida
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo auxiliar para posicaoExiste(Posicao pos)
	// as vezes é mais facil testar por linha e coluna do que por "Posicao"
	public boolean posicaoExiste(int li, int co) {
		return li >= 0 && li < linhas && co >= 0 && co < cols; // retorna o valor booleano desse teste condicional
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para testar se a posicao declarada pelo usuario existe
	public boolean posicaoExiste(Posicao pos) {
		return posicaoExiste(pos.getLinha(), pos.getCol());
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// se essa peça (posicao do tabuleiro) for diferente de "null", tem uma peça de xadrez lá
	public boolean temUmaPeca(Posicao pos) {
		if(!posicaoExiste(pos)) { // tratamento de erro, checa se a posicao é valida
			throw new Excessao("Erro: essa posicao nao pertence ao tabuleiro");
		}
		return peca(pos) != null; // retorna o valor booleano desse teste condicional
	}
	
	
}

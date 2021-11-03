package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Tabuleiro.Peca;
import Tabuleiro.Posicao;
import Tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

// essa classe possui as regras do jogo de xadrez
public class PartidaDeXadrez {

	// atributos
	private int turno; 
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro; // uma partida precisa de um tabuleiro
	private boolean cheque; // por padrão, inicia em falso (entao nao precisa inicia la no construtor)
	private boolean chequeMate;
	private PecaDeXadrez enPassant; // por padrão, inicia com valor nulo (null)
	private PecaDeXadrez promocao; // transformar um peao em cavalo ou bispo ou torre ou rainha
	// ----------------------------------------------------------------------------------------------------------
	
	// criando a lista no atributo ao inves de no construtor para instancia la
	// logo que a partida for inicializada
	private List<Peca> pecasNoTabuleiro= new ArrayList<>(); // lista das peças no tabuleiro
	private List<Peca> pecasCapturadas= new ArrayList<>(); // lista de peças derrotadas
	
	// ----------------------------------------------------------------------------------------------------------
	
	// construtor
	public PartidaDeXadrez() {
		tabuleiro= new Tabuleiro(8, 8); // tamanho especifico respeitando o tamanho de um tabuleiro real de xadrez
		turno= 1; // primeiro turno da partida
		jogadorAtual= Cor.branco; // nas regras do xadrez, o branco começa o jogo
		cheque= false;
	
		inicioDePartida();
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodos gets sem os sets porque nao quero que esses atributos possam ser alterados no meio da partida
	public int getTurno() {
		return turno;
	}
	
	public Cor getJogadorAtual() {
		return jogadorAtual;
	}
	
	public boolean getCheque() {
		return cheque;
	}
	
	public boolean getChequeMate() {
		return chequeMate;
	}
	
	public PecaDeXadrez getEnPassant() {
		return enPassant;
	}
	
	public PecaDeXadrez getPromocao() {
		return promocao;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// retorna a matriz de peças de xadrez correspondentes a uma partida.
	// Como o desenvolvimento é em camadas, e eu nao quero que a Interface
	// veja os objetos da classe Peca, mas as da classe PecaDeXadrez, a classe
	// Interface so conhece a camada de xadrez e nao a de tabuleiro
	public PecaDeXadrez[][] getPecas(){
		
		// cria uma variavel auxiliar temporaria
		PecaDeXadrez[][] mat= new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getCols()];
		
		// percorre a matriz de peças do tabuleiro, chamada tabuleiro (atributo)
		for(int i= 0; i<tabuleiro.getLinhas(); i++) { // percorre as linhas
			for(int j= 0; j<tabuleiro.getCols(); j++) { // percorre as colunas
				
				// a matriz mat na camada de xadrez recebe nessa posicao, 
				// o que esta na mesma posicao no tabuleiro na camada Tabuleiro
				// Por isso a operação é feita por "downcasting" (objeto se passa como se fosse um subtipo dele)
				// ou seja, "mat" interpreta que esta recebendo uma "PecaDeXadrez" e nao uma "Peca"
				mat[i][j]= (PecaDeXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo que vai imprimir os movimentos possiveis a partir de uma posicao de origem
	// ou seja, quando escolher uma peça para mover, o jogo vai mostrar os movimentos
	// possiveis para essa peça
	public boolean[][] movimentosPossiveis(PosicaoXadrez posRaiz){
		
		// converte a posicao de xadres (linhas 8,7,6,... e colunas a, b, c...) 
		// para posicao de matriz (linhas 0, 1, 2... e colunas 0, 1, 2, ...). Ir para PosicaoXadrez linha 55
		Posicao pos= posRaiz.retornaPeca();
		
		// valida se ha peça nessa posicao de origem. Ir para linha 255
		validarPosicaoRaiz(pos);
		
		// chama "movimentosPossiveis" da peça, que pode ser qualquer uma do xadrez
		// esse método varre a matriz a partir da posição de origem da peça a procura
		// por movimentos possiveis, dentro da regra dessa peça
		return tabuleiro.peca(pos).movimentosPossiveis();
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para mover a peça e retornar uma posicao capturada, se for o caso
	// esse é o metodo que faz uma jogada/um movimento de peça a cada vez de jogador
	public PecaDeXadrez facaMovimentoXadrez(PosicaoXadrez pos_raiz, PosicaoXadrez pos_alvo) {
		// 1º - converte as posicoes de PosicaoXadrez para posicoes da matriz
		Posicao raiz= pos_raiz.retornaPeca();
		Posicao alvo= pos_alvo.retornaPeca();
		
		// 2º - validar se há peça para mover
		validarPosicaoRaiz(raiz);
		
		// 3º - validar se pode mover a peça para posição de destino
		validarPosicaoAlvo(raiz, alvo);
		
		// 4º - // movimenta a peça de um ponto ao outro capturando uma peça adversaria, se houver
		Peca capturada= facaMovimento(raiz, alvo); 
		
		// 4.1 - testa se esse movimento pos o proprio jogador em cheque (nao pode)
		if(testaCheque(jogadorAtual)) {
			desfazerMovimento(raiz, alvo, capturada);
			throw new ExcessaoXadrez("Erro: voce nao pode se por em cheque");
		}
		
		// "pecaMovida" é a que saiu da posição de origem para a de destino
		// pega a referencia dessa peça e passa para "pecaMovida"
		PecaDeXadrez pecaMovida= (PecaDeXadrez)tabuleiro.peca(alvo);
		
		// 4.2- tratamento da jogada especial promoção: quando um peão alcança a ultima
		// linha do tabuleiro do lado adversario, ele é promovido e se transforma em outra
		// peça: cavalo ou bispo ou torre ou rainha. Como essa transformação pode permitir
		// novos movimentos, esses podem colocar o rei adversario em cheque, e por isso,
		// esse tratamento é feito antes do tratamento de cheque
		
		promocao= null; // sempre inicia nulo antes de realizar o tratamento
		
		if(pecaMovida instanceof Peao) { // se a peça movimentada é um peão
			
			// se esse peão é branco e foi parar na linha 0 da matriz ou se é preto e foi parar na linha 7
			if((pecaMovida.getCor() == Cor.branco && alvo.getLinha() == 0) || (pecaMovida.getCor() == Cor.preto && alvo.getLinha() == 7)) {
				
				// "promocao" recebe a instancia do peçao movido
				promocao= (PecaDeXadrez)tabuleiro.peca(alvo);
				
				// chama o metodo de promocao para promover o peão. Por padrão (e pra nao
				// deixar o parametro vazio), o peão sera trocado por uma rainha, mas na
				// verdade, a aplicação vai perguntar para o usuario qual peça ele quer
				// que o peão se transforme
				promocao= trocaPecaPromovida("Q");
			}
		}
		
		// 4.3 - testa se esse movimento pos o oponente em cheque
		// usa "testaCheque" no rei do jogador adversário e verifica:
		// se testaCheque for verdadeiro, retorna verdadeiro, senao, retorna falso
		cheque= (testaCheque(oponente(jogadorAtual))) ? true : false;
		
		// 5º - verifica se o movimento do jogador atual pos o oponente em chequemate OU
		// altera os dados para o proximo turno: incrementa "turno" e muda a vez do jogador
		if(testaChequeMate(oponente(jogadorAtual))) {
			chequeMate= true;
		}
		else {
			proximoTurno();
		}
		
		// 6- verifica se a peça movida era um peão que moveu 2 casas (para fazer o 
		// movimento especial en passant)
		
		if(pecaMovida instanceof Peao && (alvo.getLinha() == raiz.getLinha() - 2 || alvo.getLinha() == raiz.getLinha() + 2)) {
			enPassant= pecaMovida;
		}
		else {
			enPassant= null;
		}
		
		// faz downcasting para converter a variavel "capturada", de Peca para PecaXadrez
		return (PecaDeXadrez)capturada;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// esse metodo troca um peão promovido por um cavalo ou bispo ou torre ou rainha
	public PecaDeXadrez trocaPecaPromovida(String tipo) {
		// tratamento de erro
		if(promocao == null) {
			throw new IllegalStateException("Erro: nao ha peca a ser promovida");
		}
		
		// verifica se a letra recebida no parametro é valida
		// para comparar 2 Strings usa a operaçao "equals" porque a String é um tipo classe e não primitivo
		/*
		if(!tipo.equals("C") && !tipo.equals("B") && !tipo.equals("T") && !tipo.equals("Q")) {
			throw new InvalidParameterException("Erro: letra errada. Digite C ou B ou T ou Q");
		}
		*/
		
		// pega a posicao da peça promovida
		Posicao pos= promocao.getPosicaoXadrez().retornaPeca();
		// "p" recebe esse peão e ele é removido do tabuleiro
		Peca p= tabuleiro.removePeca(pos);
		// tambem remove ele da lista de peças no tabuleiro
		pecasNoTabuleiro.remove(p);
		
		// instanciar uma nova peça a escolha do usuario
		PecaDeXadrez novaP= novaPeca(tipo, promocao.getCor());
		// coloca ela na posicao da peça promovida
		tabuleiro.colocaPeca(novaP, pos);
		// coloca essa nova peça na lista de peças no tabuleiro
		pecasNoTabuleiro.add(novaP);
		
		// testando novamente a condicao de cheque para anular o falso cheque que pode
		// acontecer devido a criação da rainha por padrão no metodo facaMovimentoXadrez
		// por exemplo, quando um peão se torna um bispo, o jogo acusava situação de
		// cheque se esse bispo estava ao lado do rei adversario. Esse cheque nao poderia
		// acontecer porque o bispo nao se move para os lados, apenas nas diagonais
		cheque= testaCheque(jogadorAtual) ? true : false;
		
		// tem que testar se houve cheque mate logo apos transformar o peão
		chequeMate= testaChequeMate(jogadorAtual) ? true : false;
		
		return novaP;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo auxiliar para "trocaPecaPromovida", para permitir criar uma nova peça a partir
	// da letra escolhida pelo usuario
	private PecaDeXadrez novaPeca(String tipo, Cor cor) {
		if(tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if(tipo.equals("B")) return new Bispo(tabuleiro, cor);
		if(tipo.equals("T")) return new Torre(tabuleiro, cor);
		return new Rainha(tabuleiro, cor); // se falhar os 3 "if' acima, cai nesse return
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo de validar se há peça para mover
	private void validarPosicaoRaiz(Posicao pos) {
		
		// 1 - verifica se tem peça pra movimentar
		// se nao tiver peça, mostre mensagem de erro. Ir para Tabuleiro linha 113
		if(!tabuleiro.temUmaPeca(pos)) {
			throw new ExcessaoXadrez("Erro: nao ha peca na posicao de origem");
		}
		
		// 2 - verifica se a peça que quer movimentar pertence ao jogador desse turno
		// jogador e peça de origem precisam ter a mesma cor
		// getCor é propriedade da classe PecaDeXadrez, entao, para tornar essa comparação
		// possivel, é preciso fazer downcasting em tabuleiro.peca para PecaDeXadrez
		if(jogadorAtual != ((PecaDeXadrez)tabuleiro.peca(pos)).getCor()) {
			throw new ExcessaoXadrez("Erro: a peca escolhida nao e sua!");
		}
		
		// 3 - agora testa se há movimentos possiveis para essa peça
		// se nao tiver movimento possivel, mostre mensagem de erro. Ir para Peça linha 55
		if(!tabuleiro.peca(pos).existeMovimentoPossivel()) {
			throw new ExcessaoXadrez("Erro: nao ha movimentos possiveis para essa peca");
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// validar se pode mover a peça para posição de destino
	private void validarPosicaoAlvo(Posicao raiz, Posicao alvo) {
		// para saber se a posicao de destino é valida, em relacao
		// a posicao de origem, basta saber se há movimento possivel
		// a partir da posicao de origem
		
		// se em relaçao a peça de origem, a posicao de destino nao é movimento possivel
		if(!tabuleiro.peca(raiz).movimentoPossivel(alvo)) {
			throw new ExcessaoXadrez("Erro: a peca escolhida nao pode ser movida para a posicao de destino");
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	private Peca facaMovimento(Posicao raiz, Posicao alvo) {
		// 1- remove a peça que quer mover da posição de origem
		
		// depois incrementa o "movCont", MAS o metodo aumentaMovCont pertence a classe 
		// PecaDeXadrez por isso "p" é objeto de PecaDeXadrez e nao de Peca, MAS, esse 
		// metodo, facaMovimento, faz o movimento de qualquer peça, entao deve mover uma 
		// peça generica, que deveria ser da classe Peca. Pra resolver esse problema, crio 
		// a variavel "p" fazendo casting para PecaDeXadrez e depois fazendo um downcasting
		// tambem para PecaDeXadrez ao chamar o metodo removePeca da classe Tabuleiro, que 
		// pertence a mesma camada que Peca
		PecaDeXadrez p= (PecaDeXadrez)tabuleiro.removePeca(raiz);
		
		// 2- remove uma possível peça na posição de destino
		
		// isso porque, no xadrez, se voce movimentar uma peça para ocupar o lugar de outra,
		// do oponente, quer dizer que voce eliminou aquela peça e a sua fica no lugar
		Peca pecaCapturada= tabuleiro.removePeca(alvo);
		
		// 3- testar se a peça capturada é diferente de nulo (se realmente pegou uma peça)
		if(pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada); // retira ela da lista de peças no tabuleiro
			pecasCapturadas.add(pecaCapturada); // e insere na lista de peças capturadas
		}
		
		// 4- colocar a peca que foi retirada da posicao de origem na posicao de destino
		tabuleiro.colocaPeca(p, alvo);
		
		p.aumentaMovCont(); // 5- incrementa o movCont dessa peça que foi movida
		
		// movimento especial roque (castling)
		
		// roque pequeno (rei 2 casas pra direita e torre da direita 2 casas para esquerda)
		
		// se a peça "p" que foi movida da posicao de origem for uma instancia de Rei E
		// a posicao da coluna de destino for igual a da coluna de origem + 2 ( 2 casas
		// a direita do rei)
		if(p instanceof Rei && alvo.getCol() == raiz.getCol() + 2) {
			
			// pega a posicao aonde deve estar a torre ao lado direito do rei 
			Posicao raizTorre= new Posicao(raiz.getLinha(), raiz.getCol() + 3);
			// pega a posicao 2 casas a esquerda dessa torre (vai mover pra essa posicao)
			Posicao alvoTorre= new Posicao(raiz.getLinha(), raiz.getCol() + 1);
			
			// retira a torre da posicao de origem
			PecaDeXadrez torre= (PecaDeXadrez)tabuleiro.removePeca(raizTorre);
			
			tabuleiro.colocaPeca(torre, alvoTorre); // insere na posicao de destino
			
			torre.aumentaMovCont(); // incrementa seu movCont
		}
		
		// roque grande (rei 2 casas pra esquerda e torre da esquerda 3 casas pra direita)
		
		// se a peça "p" que foi movida da posicao de origem for uma instancia de Rei E
		// a posicao da coluna de destino for igual a da coluna de origem - 2 ( 2 casas
		// a esquerda do rei)
		if(p instanceof Rei && alvo.getCol() == raiz.getCol() - 2) {
			
			// pega a posicao aonde deve estar a torre ao lado esquerdo do rei 
			Posicao raizTorre= new Posicao(raiz.getLinha(), raiz.getCol() - 4);
			// pega a posicao 3 casas a direita dessa torre (vai mover pra essa posicao)
			Posicao alvoTorre= new Posicao(raiz.getLinha(), raiz.getCol() - 1);
			
			// retira a torre da posicao de origem
			PecaDeXadrez torre= (PecaDeXadrez)tabuleiro.removePeca(raizTorre);
			
			tabuleiro.colocaPeca(torre, alvoTorre); // insere na posicao de destino
			
			torre.aumentaMovCont(); // incrementa seu movCont
		}
		
		// tratamento de movimento en passant: as capturas "normais" acontecem com o peça
		// dominante ocupando o espaço aonde estava a peça capturada, mas no en passant
		// isso nao ocorre
		
		// 1- verifica se a peça movimentada é um peão
		if(p instanceof Peao) {
			
			// teste para saber se fez en passant: o peão só pode andar na diagonal quando
			// faz uma captura normal, mas no en passant, faz o movimento sem capturar peça
			// na posicao de destino
			// 2- o peão mudou de coluna (andou na diagonal)? E não capturou nenhuma peça? 
			if(raiz.getCol() != alvo.getCol() && pecaCapturada == null) {
				
				// 3- pegar a posicao do peão adversario capturado no en passant
				Posicao posPeao;
				// se o peão é branco, o peão capturado esta abaixo dele
				if(p.getCor() == Cor.branco) {
					posPeao= new Posicao(alvo.getLinha() + 1, alvo.getCol());
				}
				else { // se o peão é preto, o peão capturado esta acima dele
					posPeao= new Posicao(alvo.getLinha() - 1, alvo.getCol());
				}
				
				pecaCapturada= tabuleiro.removePeca(posPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada); 
			}
		}
		
		return pecaCapturada; // pode retornar null ou a peça capturada
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para desfazer o movimento de peça para evitar que o usuario ponha-se em
	// cheque, pois isso é proibido no jogo de xadrez
	private void desfazerMovimento(Posicao raiz, Posicao alvo, Peca pecaCapturada) {
		
		// retira a peça que moveu para a posicao de destino
		// depois incrementa o "movCont", MAS o metodo aumentaMovCont pertence a classe PecaDeXadrez
		// por isso "p" é objeto de PecaDeXadrez e nao de Peca, MAS, esse metodo, facaMovimento,
		// faz o movimento de qualquer peça, entao deve mover uma peça generica, que deveria ser
		// da classe Peca. Pra resolver esse problema, crio a variavel "p" fazendo casting 
		// para PecaDeXadrez e depois fazendo um downcasting tambem para PecaDeXadrez ao chamar
		// o metodo removePeca da classe Tabuleiro, que pertence a mesma camada que Peca
		PecaDeXadrez p= (PecaDeXadrez)tabuleiro.removePeca(alvo);
		p.diminuiMovCont();
		
		// e volta ela pra posição de origem (desfazendo o movimento)
		tabuleiro.colocaPeca(p, raiz);
		
		// e se no movimento de por-se em cheque, uma peça adversaria foi capturada?
		if(pecaCapturada != null) {
			// retorna ela pra sua posicao, que é a de destino
			tabuleiro.colocaPeca(pecaCapturada, alvo);
			// remove ela da lista de peças capturadas
			pecasCapturadas.remove(pecaCapturada);
			// e insere na lista de peças que estao no tabuleiro
			pecasNoTabuleiro.add(pecaCapturada);
		}
		
		// desfazer o movimento especial roque (castling)
		
		// roque pequeno
		
		if(p instanceof Rei && alvo.getCol() == raiz.getCol() + 2) {
			
			Posicao raizTorre= new Posicao(raiz.getLinha(), raiz.getCol() + 3);

			Posicao alvoTorre= new Posicao(raiz.getLinha(), raiz.getCol() + 1);
			
			// retira a torre da posicao de destino, pois esta desfazendo o movimento
			PecaDeXadrez torre= (PecaDeXadrez)tabuleiro.removePeca(alvoTorre);
			
			tabuleiro.colocaPeca(torre, raizTorre); // devolve para posicao de origem
			
			torre.diminuiMovCont(); // reduz seu movCont
		}
		
		// roque grande
		
		if(p instanceof Rei && alvo.getCol() == raiz.getCol() - 2) {
			
			Posicao raizTorre= new Posicao(raiz.getLinha(), raiz.getCol() - 4);
			
			Posicao alvoTorre= new Posicao(raiz.getLinha(), raiz.getCol() - 1);
			
			// retira a torre da posicao de destino, pois esta desfazendo o movimento
			PecaDeXadrez torre= (PecaDeXadrez)tabuleiro.removePeca(alvoTorre);
			
			tabuleiro.colocaPeca(torre, raizTorre); // devolve para posicao de destino
			
			torre.diminuiMovCont(); // reduz seu movCont
		}
		
		// tratamento de desfazer o movimento en passant
		
		// 1- verifica se a peça movimentada é um peão
		if(p instanceof Peao) {
			
			// 2- o peão mudou de coluna (andou na diagonal)? E capturou um peão vulneravel ao en passant? 
			if(raiz.getCol() != alvo.getCol() && pecaCapturada == enPassant) {
				
				// esse metodo, na linha 417, na estrutura: "if(pecaCapturada != null)"
				// devolve a peça capturada para posicao de destino, mas isso nao da certo
				// para o en passant, pois para a peça capturada, a posicao de destino nao
				// e sua posicao de origem, mas a posicao que a peça que a captura iria
				// Portanto, esse "if" nao funciona para o en passant. Soluçao:
				// deixar a peça capturada pelo en passant ser movida para posicao de 
				// destino, e manualmente coloca la em sua posicao correta
				
				PecaDeXadrez peao= (PecaDeXadrez)tabuleiro.removePeca(alvo);
				
				// 3- pegar a posicao do peão adversario capturado no en passant
				Posicao posPeao;
				// se o peão capturado é preto, ele e o peão branco retornam para linha 3 da matriz
				if(p.getCor() == Cor.branco) {
					posPeao= new Posicao(3, alvo.getCol());
				}
				else { // se o peão capturado é branco, ele e o peão preto retornam para linha 4 da matriz
					posPeao= new Posicao(4, alvo.getCol());
				}
				
				// coloca manualmente o peão capturado na sua posição de origem, decidido
				// pelo "if else" acima
				tabuleiro.colocaPeca(peao, posPeao); 
			}
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo que passa para o proximo turno da partida (troca da vez dos jogadores)
	private void proximoTurno() {
		// incrementa o numero do turno
		turno++;
		// muda o jogador usando expressão condicional ternária
		// se o "jogadorAtual" for "branco", então muda para "preto", caso contrario, ele sera "branco"
		jogadorAtual= (jogadorAtual == Cor.branco) ? Cor.preto : Cor.branco;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo que retorna a cor do oponente
	private Cor oponente(Cor cor) {
		// outra expressao condicional ternaria
		// se a cor atual for branco, entao retorna a cor preto, senao, retorna branco
		return (cor == Cor.branco) ? Cor.preto : Cor.branco;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo que localiza o rei de uma determinada cor no tabuleiro
	private PecaDeXadrez rei(Cor cor) {
		
		// procura na lista de peças em jogo, filtrando ela, para mandar para "lista" apenas as peças da cor passada por parametro
		// "pecasNoTabuleiro" é uma lista de "Peca", que nao possui o atributo "cor", entao é feito downcasting 
		// de Peca para PecaDeXadrez para fazer essa filtragem por cor
		// "filter(x->((PecaDeXadrez)x).getCor() == cor)" = procurar por toda Peca 'x' tal que a cor dessa Peca seja igual a "cor"
		List<Peca> lista= pecasNoTabuleiro.stream().filter(x->((PecaDeXadrez)x).getCor() == cor).collect(Collectors.toList());
		
		// para cada Peca "p" na "lista"
		for(Peca p : lista) {
			// se essa Peca "p" for uma instancia da classe Rei (então essa peça é um rei)
			if(p instanceof Rei) {
				return (PecaDeXadrez)p;
			}
		}
		// se essa excessao acontecer, é porque tem erro na implmentação do jogo
		// preciso por uma excessao ou retorno de algum objeto de PecadeXadrez, senao, o compilador
		// acusa erro, pois sempre há a chance desse "for" nao retornar nada
		throw new IllegalStateException("Erro: nao existe um rei " + cor + " no tabuleiro");
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// esse metodo varre o tabuleiro e para cada peça adversaria, testa se ela pode se mover
	// e capturar o rei. Pega uma cor como parametro para saber a quem pertence o rei
	private boolean testaCheque(Cor cor) {
		
		// pega a posicao do rei buscando ele no tabuleiro pelo metodo "rei(Cor cor)"
		// depois pega sua posição no tabuleiro com "getPosicaoXadrez()"
		// e por fim, converte para posição de matriz com "retornaPeca()"
		Posicao posRei= rei(cor).getPosicaoXadrez().retornaPeca();
		
		// monta uma lista com todas as peças do oponente que estão no tabuleiro
		// esse processo de filtragem chama o metodo oponente para retornar sua cor
		// que é diferente da cor enviada para o parametro dessa funcao
		List<Peca> pecasDoOponente= pecasNoTabuleiro.stream().filter(x->((PecaDeXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
		
		// agora é testar cada uma dessas peças para saber se alguma é capaz de por o rei em cheque
		for(Peca p : pecasDoOponente) {
			boolean[][] mat= p.movimentosPossiveis(); // retorna uma matriz com todos os movimentos possiveis dessa peça
			
			// se a posicao do rei estiver marcada como verdadeiro nessa matriz, é porque ele estará em cheque
			if(mat[posRei.getLinha()][posRei.getCol()]) {
				return true;
			}
		}
		// caso contrario, ele nao esta em cheque
		return false;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// o rei esta em cheque mate quando ele esta em cheque e não há nenhum movimento
	// possivel que o retire desse cheque
	private boolean testaChequeMate(Cor cor) {
		
		// se o rei dessa cor nao estiver em cheque, nao precisa testar se ha cheque mate
		if(!testaCheque(cor)) {
			return false;
		}
		
		// procura na lista de peças em jogo, filtrando ela, para mandar para "lista" apenas as peças da cor passada por parametro
		// "pecasNoTabuleiro" é uma lista de "Peca", que nao possui o atributo "cor", entao é feito downcasting 
		// de Peca para PecaDeXadrez para fazer essa filtragem por cor
		// "filter(x->((PecaDeXadrez)x).getCor() == cor)" = procurar por toda Peca 'x' tal que a cor dessa Peca seja igual a "cor"
		List<Peca> lista= pecasNoTabuleiro.stream().filter(x->((PecaDeXadrez)x).getCor() == cor).collect(Collectors.toList());
		
		// procura nessa lista se há alguma peça que retira o rei do cheque
		for(Peca p : lista) { 
			
			boolean[][] mat= p.movimentosPossiveis(); // retorna uma matriz com todos os movimentos possiveis dessa peça
			
			for(int i= 0; i<tabuleiro.getLinhas(); i++) {
				for(int j= 0; j<tabuleiro.getCols(); j++) {
					
					// para cada elemento dessa matriz, testo se essa posicão é um movimento possivel
					if(mat[i][j]) {
						// e ainda testa se esse movimento tira o rei do cheque, para isso
						// passa a peça "p" para essa posicao na matriz, que é um movimento possivel
						// e depois testa se o rei ainda esta em cheque
						
						// "p" é do tipo Peca e sua "posicao" é atributo protegido
						// para pegar a posicao de "p" faz um downcasting de Peca para PecaDeXadrez
						// e chama getPosicaoXadrez que retorna a posicao no tabuleiro e depois
						// retornaPeca converte isso para posicao na matriz
						Posicao raiz= ((PecaDeXadrez)p).getPosicaoXadrez().retornaPeca();
						Posicao destino= new Posicao(i, j);
						
						// agora vamos mover "p" de sua posição raiz para o destino, que é aquela posição no "if"
						Peca pecaCapturada= facaMovimento(raiz, destino);
						
						// agora vamos testar se o rei ainda esta em cheque apos o movimento daquela peça
						boolean testacheque= testaCheque(cor);
						
						// vamos desfazer o movimento daquela peça para nao atrapalhar o jogo dos usuarios
						desfazerMovimento(raiz, destino, pecaCapturada);
						
						// verificando o valor de "testacheque": se nao esta em cheque, é porque aquele movimento tirou o rei do cheque
						if(!testacheque) {
							return false;
						}
					}
				}
			}
		}
		
		// se não há peça que retira o rei do cheque, entao, chequemate
		return true;
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	private void colocaNovaPeca(char col, int lin, PecaDeXadrez peca) {
		// chama o tabuleiro e seu metodo de inserir peça, e como posicao
		// chama um objeto da classe PosicaoXadrez, para inserir essa posicao
		// com seu construtor, e convertendo essa posicao com o metodo
		// retornaPeca para que a peca fique na posicao correta do tabuleiro
		// impresso e ao mesmo tempo, na posicao correta da matriz
		tabuleiro.colocaPeca(peca, new PosicaoXadrez(col, lin).retornaPeca());
		
		// depois de colocar a peça no tabuleiro, poe tambem na lista
		pecasNoTabuleiro.add(peca);
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo que inicia a partida, colocando as peças no tabuleiro
	private void inicioDePartida() {
		
		colocaNovaPeca('a', 1, new Torre(tabuleiro, Cor.branco));
		colocaNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.branco));
		colocaNovaPeca('c', 1, new Bispo(tabuleiro, Cor.branco));
		colocaNovaPeca('d', 1, new Rainha(tabuleiro, Cor.branco));
		colocaNovaPeca('e', 1, new Rei(tabuleiro, Cor.branco, this)); // auto referencia a PartidaDeXadrez
		colocaNovaPeca('f', 1, new Bispo(tabuleiro, Cor.branco));
		colocaNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.branco));
		colocaNovaPeca('h', 1, new Torre(tabuleiro, Cor.branco));
		colocaNovaPeca('a', 2, new Peao(tabuleiro, Cor.branco, this));
		colocaNovaPeca('b', 2, new Peao(tabuleiro, Cor.branco, this));
		colocaNovaPeca('c', 2, new Peao(tabuleiro, Cor.branco, this));
		colocaNovaPeca('d', 2, new Peao(tabuleiro, Cor.branco, this));
		colocaNovaPeca('e', 2, new Peao(tabuleiro, Cor.branco, this));
		colocaNovaPeca('f', 2, new Peao(tabuleiro, Cor.branco, this));
		colocaNovaPeca('g', 2, new Peao(tabuleiro, Cor.branco, this));
		colocaNovaPeca('h', 2, new Peao(tabuleiro, Cor.branco, this));

		colocaNovaPeca('a', 8, new Torre(tabuleiro, Cor.preto));
		colocaNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.preto));
		colocaNovaPeca('c', 8, new Bispo(tabuleiro, Cor.preto));
		colocaNovaPeca('d', 8, new Rainha(tabuleiro, Cor.preto));
        colocaNovaPeca('e', 8, new Rei(tabuleiro, Cor.preto, this));
        colocaNovaPeca('f', 8, new Bispo(tabuleiro, Cor.preto));
        colocaNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.preto));
        colocaNovaPeca('h', 8, new Torre(tabuleiro, Cor.preto));
        colocaNovaPeca('a', 7, new Peao(tabuleiro, Cor.preto, this));
        colocaNovaPeca('b', 7, new Peao(tabuleiro, Cor.preto, this));
        colocaNovaPeca('c', 7, new Peao(tabuleiro, Cor.preto, this));
        colocaNovaPeca('d', 7, new Peao(tabuleiro, Cor.preto, this));
        colocaNovaPeca('e', 7, new Peao(tabuleiro, Cor.preto, this));
        colocaNovaPeca('f', 7, new Peao(tabuleiro, Cor.preto, this));
        colocaNovaPeca('g', 7, new Peao(tabuleiro, Cor.preto, this));
        colocaNovaPeca('h', 7, new Peao(tabuleiro, Cor.preto, this));
       
	}
}

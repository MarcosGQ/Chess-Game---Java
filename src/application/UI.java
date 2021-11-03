package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoXadrez;
import java.util.List;

public class UI {

	// codigos especiais das cores para impressao no console
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	// cores de texto
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	// cores de background
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para limpar a tela antes de printar algo novo. dessa forma, nao sera
	// precisa ficar rolando a pagina para acompanhar o desenvolvimento da partida
	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void limpaTela() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para ler uma posicao do usuario
	// recebe um scanner do programa pricipal como parametro
	public static PosicaoXadrez lerPosicaoXadrez(Scanner sc) {
		// tratamento de erro, caso nao consiga ler a string
		try {
			// ler posicao no tabuleiro impresso: letra/numero
			String s= sc.nextLine(); // le uma linha do scanner e manda para s
			char col= s.charAt(0); // a coluna é o primeiro caractere do String s
			int lin= Integer.parseInt(s.substring(1)); // recorta a String a partir da posicao 1 e converte para int
			return new PosicaoXadrez(col, lin);
		}
		catch(RuntimeException e) {
			throw new InputMismatchException("Erro lendo a posicao do xadrez"); // excessao que ja tem no java (erro de entrada de dados)
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo para imprimir os dados da partida
	public static void printPartida(PartidaDeXadrez partida, List<PecaDeXadrez> capturadas) {
		
		printTabuleiro(partida.getPecas()); // imprime o tabuleiro
		System.out.println(); // pula uma linha
		printPecaCapturada(capturadas); // imprime a lista de peças capturadas
		System.out.println(); // pula uma linha
		System.out.println("Turno: " + partida.getTurno()); // imprime o nº do turno
		
		// antes de imprimir mensagem de turno do jogador, verifica se ha chequemate
		if(!partida.getChequeMate()) {
			
			System.out.println("Turno do jogador [" + partida.getJogadorAtual() + "] ");
			
			// a cada movimento testa se alguem esta em cheque e imprime a mensagem na tela
			if(partida.getCheque()) {
				System.out.println("jogador [" + partida.getJogadorAtual() + "] em Cheque!");
			}
		}
		else {
			System.out.println("CHEQUEMATE!");
			System.out.println("VENCEDOR: jogador [" + partida.getJogadorAtual() + "] ");
		}	
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo estatico para imprimir o tabuleiro
	// estatico = nao precisa instanciar um objeto para chamar o metodo
	public static void printTabuleiro(PecaDeXadrez[][] pecas) {
		
		// usando a funcao "length" pq sei que a matriz é quadrada (8x8)
		for(int i= 0; i<pecas.length; i++) { // percorre as linhas
			
			System.out.print((8-i) + " "); // imprime o valor de cada linha
			
			for(int j= 0; j<pecas.length; j++) { // percorre as colunas
				
				printPeca(pecas[i][j], false); // imprime cada peça com background falso porque esse nao é o caso de imprimir os movimentos possiveis da peça de origem
			}
			System.out.println(); // quebra de linha
		}
		System.out.println("  a b c d e f g h"); // imprime o valor de cada coluna
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// esse metodo imprime o tabuleiro mostrando os movimentos possiveis para a peça selecionada
	public static void printTabuleiro(PecaDeXadrez[][] pecas, boolean[][] movimentosPossiveis) {
		
		// usando a funcao "length" pq sei que a matriz é quadrada (8x8)
		for(int i= 0; i<pecas.length; i++) { // percorre as linhas
			
			System.out.print((8-i) + " "); // imprime o valor de cada linha
			
			for(int j= 0; j<pecas.length; j++) { // percorre as colunas
				
				printPeca(pecas[i][j], movimentosPossiveis[i][j]); // imprime cada peça
			}
			System.out.println(); // quebra de linha
		}
		System.out.println("  a b c d e f g h"); // imprime o valor de cada coluna
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo estatico auxiliar para imprimir uma posicao do tabuleiro
	// boolean background indica se devo ou nao colorir o fundo do tabuleiro
	// essa variavel é usada para colorir os movimentos possiveis de uma peça
	private static void printPeca(PecaDeXadrez peca, boolean background) {
    	if(background) {
    		System.out.print(ANSI_BLUE_BACKGROUND);
    	}
		if (peca == null) { // se esse objeto esta vazio
            System.out.print("-" + ANSI_RESET); // reseta a cor do traço caso tenha sido pintado de azul
        }
        else { // agora testa se a peça é branca ou preta
            if (peca.getCor() == Cor.branco) { // printa peça branca como branca
                System.out.print(ANSI_WHITE + peca + ANSI_RESET); // ansi_reset reseta as cores
            }
            else { // printa peça preta como amarela (pois o fundo é preto)
                System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
            }
        }
        System.out.print(" "); // dar um espaço na impressao de cada posicao
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	// metodo que imprime a lista de todas as peças capturadas na partida
	private static void printPecaCapturada(List<PecaDeXadrez> capturadas) {
		// criando uma lista apenas para peças brancas e ela vai receber a filtragem das
		// peças brancas da lista "capturadas", depois faz o mesmo para as pretas
		// x->x.getCor() é um predicado que significa pegar um elemento da lista e ver seu atributo
		List<PecaDeXadrez> brancas= capturadas.stream().filter(x->x.getCor() == Cor.branco).collect(Collectors.toList());
		List<PecaDeXadrez> pretas= capturadas.stream().filter(x->x.getCor() == Cor.preto).collect(Collectors.toList());
		
		System.out.println("Pecas capturadas:");
		
		// impressao das peças brancas
		System.out.print("Brancas:");
		System.out.print(ANSI_WHITE);
		System.out.println(Arrays.toString(brancas.toArray())); // metodo padrao de imprimir um vetor de valores no java
		System.out.print(ANSI_RESET);
		
		// impressao das peças pretas
		System.out.print("Pretas:");
		System.out.print(ANSI_YELLOW);
		System.out.println(Arrays.toString(pretas.toArray())); // metodo padrao de imprimir um vetor de valores no java
		System.out.print(ANSI_RESET);
	}
}

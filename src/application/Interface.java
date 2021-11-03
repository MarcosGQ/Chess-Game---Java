package application;

import java.util.InputMismatchException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import xadrez.ExcessaoXadrez;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoXadrez;

public class Interface {

	// programa principal: cria objetos e chama os metodos para iniciar o jogo
	public static void main(String[] args) {
		
		// a classe Scanner usa expressões regulares para analisar tipos primitivos e strings
		// separando a entrada digitada pelo usuário em blocos, que geram tokens (seqências
		// de caracteres separados por delimitadores (espaço em branco, por exemplo))
		
		// criando um objeto de Scanner para ler/capturar os dados do usuário
		Scanner sc= new Scanner(System.in);
		
		// criando um objeto de PartidaDeXadrez
		PartidaDeXadrez partidadexadrez= new PartidaDeXadrez();
		
		// criando lista para guardar as peças capturaras em jogo
		List<PecaDeXadrez> capturadas= new ArrayList<>();
		
		// enquanto nao tiver um chequemate, continua
		while(!partidadexadrez.getChequeMate()) {
			try {
				
				// limpa a tela
				UI.limpaTela();
				
				// imprime o tabuleiro
				UI.printPartida(partidadexadrez, capturadas);
				System.out.println();
				
				// leitura da posicao de origem
				System.out.print("Origem: ");
				PosicaoXadrez origem= UI.lerPosicaoXadrez(sc);
				
				// declarar uma matriz booleana que vai receber os movimentos possiveis
				// a partir da posicao de origem. Ir para PartidaDeXadrez linha 104
				boolean[][] movimentosPossiveis= partidadexadrez.movimentosPossiveis(origem);
				UI.limpaTela();
				
				// imprimir de novo o tabuleiro com sobrecarga (criando uma nova versao dele com novos parametros)
				// passando os movimentos possiveis da peça na posicao de origem
				// vamos colorir as posicoes possiveis de movimento
				UI.printTabuleiro(partidadexadrez.getPecas(), movimentosPossiveis);
				
				// posicao de destino
				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez destino= UI.lerPosicaoXadrez(sc);
				
				// pega a possivel peça retirada que estava na posicao de destino
				PecaDeXadrez pecaCapturada= partidadexadrez.facaMovimentoXadrez(origem, destino);
				
				// foi capturado alguma peça?
				if(pecaCapturada != null) {
					capturadas.add(pecaCapturada); // insere na lista da UI
				}
				
				// algum peão foi promovido?
				if(partidadexadrez.getPromocao() != null) {
					String tipo= "a";
					while(!tipo.equals("C") && !tipo.equals("B") && !tipo.equals("T") && !tipo.equals("Q")) {
						System.out.print("Escolha a promocao do peao (C/B/T/Q): ");
						tipo= sc.nextLine();
						tipo= tipo.toUpperCase(); // força o que o usuario digitou para maiusculo, para evitar erro
						if(!tipo.equals("C") && !tipo.equals("B") && !tipo.equals("T") && !tipo.equals("Q")) {
							System.out.println("Erro: nao ha peca com essa letra no jogo");
						}
					}
					
					
					partidadexadrez.trocaPecaPromovida(tipo);
				}
				
			} // refina o tratamento de erro, ao inves de fechar a aplicação, volta pro jogo
			catch(ExcessaoXadrez e) {
				System.out.println(e.getMessage());
				sc.nextLine(); // o programa aguarda a tecla "enter" ser pressionada
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); // o programa aguarda a tecla "enter" ser pressionada
			}
		} // fim while
		
		UI.limpaTela();
		UI.printPartida(partidadexadrez, capturadas);

	} // fim void main

} // fim class Interface

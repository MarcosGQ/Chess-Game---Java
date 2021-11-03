package xadrez;

import Tabuleiro.Excessao;

//extends RuntimeException para ser uma excessao opcional de ser tratada
//permite usar "throw new Excessao" para fazer tratamento de erro
//se "cai" no "throw new Excessao", o resto do código não é executado

// quando capturar ExcessaoXadrez, tb ira capturar Excessao, que é da camada Tabuleiro
// quando o metodo validarPosicaoRaiz da classe PartidaDeXadrez chamar o metodo
// temUmaPeca da classe Tabuleiro, nao havera problema nos chamados dos exceptions
public class ExcessaoXadrez extends Excessao{
	
	// numero de versao padrao
	private static final long serialVersionUID= 1L;
	
	// construtor que recebe a mensagem
	public ExcessaoXadrez(String msg) {
		super(msg); // repassa a mensagem para o construtor da super classe que é o RuntimeException
	}
}

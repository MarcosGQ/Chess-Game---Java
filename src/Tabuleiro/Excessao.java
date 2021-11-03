package Tabuleiro;

// extends RuntimeException para ser uma excessao opcional de ser tratada
// permite usar "throw new Excessao" para fazer tratamento de erro
// se "cai" no "throw new Excessao", o resto do código não é executado
public class Excessao extends RuntimeException{
	
	// numero de versao padrao
	private static final long serialVersionUID= 1L;
	
	// construtor que recebe a mensagem
	public Excessao(String msg) {
		super(msg); // repassa a mensagem para o construtor da super classe que é o RuntimeException
	}
}

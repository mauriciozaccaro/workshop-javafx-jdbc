package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) { //currentStage = Evento atual ou Palco atual
		return (Stage) ((Node) event.getSource()).getScene().getWindow();/* aqui � feito uma bagun�a.... tipo, a gente pega o 
		event;getSource(), mas ele � muito gen�rico ent�o fazemos um downCast para o tipo "Node" e a partir desse Node chamamos 
		o getScene().getWindow()  que � para pegar tela da cena. Como esse getWindow � uma SUPER classe do Stage, a gente faz um 
		novo DownCast para o tipo "Stage" */
	}
	
	public static Integer tryParseToInt(String str) {
		/* Esse m�todo � para tratar o que vem l�aaaa da TextField do Id, para que ele converta 
		 * o texto (String) do TextField para um numero Inteiro. E, caso n�o seja poss�vel fazer tal 
		 * convers�o, vai return null */
		try {
			return Integer.parseInt(str);
		}catch(NumberFormatException e ) {
			return null;
		}
	}
}

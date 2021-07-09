package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) { //currentStage = Evento atual ou Palco atual
		return (Stage) ((Node) event.getSource()).getScene().getWindow();/* aqui é feito uma bagunça.... tipo, a gente pega o 
		event;getSource(), mas ele é muito genérico então fazemos um downCast para o tipo "Node" e a partir desse Node chamamos 
		o getScene().getWindow()  que é para pegar tela da cena. Como esse getWindow é uma SUPER classe do Stage, a gente faz um 
		novo DownCast para o tipo "Stage" */
	}
}

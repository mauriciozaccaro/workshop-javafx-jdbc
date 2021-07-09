package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml"); // chama a tela About.fxml 
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized void loadView(String absoluteName) {
		// esse "synchronized" serve para não dar B.O com as multi-trheds durante a executação das telas
		/* Toda essa função loadView é para quando chamar uma outra cena (Scene) ele mudar os "filhos" sem mudar o tipo da Cena, 
		 *  algo assim, não entendi direito direito... quando entender mais eu volto a escrever...
		 * */
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0); // passa para o Node mainMenu o primeiro (0) filho do mainVBox
			mainVBox.getChildren().clear(); // limpa todos os filhos do mainVBox
			mainVBox.getChildren().add(mainMenu); // pega o primeiro filho que ficou armazenado no mainMenu e adiciona ao mainVBox novamente
			mainVBox.getChildren().addAll(newVBox.getChildren()); // adiciona todos os filhos do newVBox que é a outra tela VBox
			
			
			
		}catch(IOException e) {
			Alerts.showAlerts("IOException", "Erro ao carregar a tela", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String absoluteName) {
		// esse "synchronized" serve para não dar B.O com as multi-trheds durante a executação das telas
		/* Toda essa função loadView é para quando chamar uma outra cena (Scene) ele mudar os "filhos" sem mudar o tipo da Cena, 
		 *  algo assim, não entendi direito direito... quando entender mais eu volto a escrever...
		 * */
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0); // passa para o Node mainMenu o primeiro (0) filho do mainVBox
			mainVBox.getChildren().clear(); // limpa todos os filhos do mainVBox
			mainVBox.getChildren().add(mainMenu); // pega o primeiro filho que ficou armazenado no mainMenu e adiciona ao mainVBox novamente
			mainVBox.getChildren().addAll(newVBox.getChildren()); // adiciona todos os filhos do newVBox que é a outra tela VBox
			
			DepartmentListController controller = loader.getController();
			
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
			
		}catch(IOException e) {
			Alerts.showAlerts("IOException", "Erro ao carregar a tela", e.getMessage(), AlertType.ERROR);
		}
	}

}

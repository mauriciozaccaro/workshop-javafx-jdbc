package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
//--module-path C:\jdbc-libs\javafx-sdk\lib --add-modules=javafx.fxml,javafx.controls

public class Main extends Application {
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) { // Stage seria o palco
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			javafx.scene.control.ScrollPane scrollPane = loader.load();
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			mainScene = new Scene(scrollPane);// Scene seria a cena que ser� exibida no palco
			primaryStage.setScene(mainScene); // aqui a gente adiciona a cena ao palco
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();// aqui deixa o palco vis�vel para verem o espet�culo
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

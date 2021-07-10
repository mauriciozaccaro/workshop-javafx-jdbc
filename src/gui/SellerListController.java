package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import bd.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener{

	private SellerService depService; // isso cria uma dependência láaaaa da classe de serviço SellerService
	//private SellerDaoJDBC SellerDaojdbc = new SellerDaoJDBC(null);
	
	public void setSellerService(SellerService depService) {
		this.depService = depService; // INJEÇÃO de dependência
	}
	
	private ObservableList<Seller> obsList;
	
	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;

	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;

	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEdit;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		//Integer newId = depService.lastNumberSellerId();
		//Seller obj = new Seller(newId, "");
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();	
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // ainda não sei para que serve... fiquei em duvida
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); 
		/* basicamente, está pegando a propriedade de width do Stage 
		principal e incluindo no width preferencial da minha tableViewSeller */
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(depService == null) {
			throw new IllegalStateException("Serviço está NULO");
		}
		List<Seller> list = depService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList); 
		initEditButtons();// acrescenta um novo Button "Edit" em cada linha da tabela view
		initRemoveButtons();// acrescenta um novo Button "Remove" em cada linha da tabela view
	}
	
	private void createDialogForm(Seller obj, String absoluteName, Stage parantStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setSellerService(new SellerService());
			controller.subscribeDataChangeListener(this); /* aqui estamos INSCREVENDO esse método
			no DataChangeListener */
			
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller Data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parantStage); // esse "initOwner" é para dizer quem é o PAI da janela/palco, que no caso vem 
			// lá dos parametros passados no como Stage na criação do método
			dialogStage.initModality(Modality.WINDOW_MODAL); // isso é para "travar" a janela acima da janela de fundo..ou seja, 
			// não terá como mexer na janela de fundo se essa janela estiver aberta na frente..
			dialogStage.showAndWait(); // porque ShowAndWait e não Show? não sei!
			
			
		}catch(IOException e) {
			Alerts.showAlerts("IOException", "Error Loanding View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() { /* Classe móh dahora... rsrsr .. ela CRIA um Button "Edit" 
	dentro de cada linha da TableView e passa o Seller selecionado para a tela SellerForm */
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>(){
			private final Button button = new Button("Edit");
			
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if(obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml",
																Utils.currentStage(event)));
			}
			
		});
	}
	
	private void initRemoveButtons() { /* Classe móh dahora... rsrsr .. ela CRIA um Button "Edit" 
		dentro de cada linha da TableView e passa o Seller selecionado para a tela SellerForm */
			tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>(){
				private final Button button = new Button("Remove");
				
				protected void updateItem(Seller obj, boolean empty) {
					super.updateItem(obj, empty);
					
					if(obj == null) {
						setGraphic(null);
						return;
					}
					
					setGraphic(button);
					button.setOnAction(event -> removeEntity(obj));
				}
				
			});
		}

	private void removeEntity(Seller obj) {
		
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if(result.get() == ButtonType.OK) {
			if(depService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				depService.remove(obj);
				updateTableView();
			}catch(DbIntegrityException e){
				Alerts.showAlerts("Error removing Object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
 
package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{

	private DepartmentService depService; // isso cria uma depend�ncia l�aaaa da classe de servi�o DepartmentService
	//private DepartmentDaoJDBC departmentDaojdbc = new DepartmentDaoJDBC(null);
	
	public void setDepartmentService(DepartmentService depService) {
		this.depService = depService; // INJE��O de depend�ncia
	}
	
	private ObservableList<Department> obsList;
	
	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEdit;
	
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Integer newId = depService.lastNumberDepartmentId();
		//Department obj = new Department(newId, "");
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();	
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // ainda n�o sei para que serve... fiquei em duvida
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); 
		/* basicamente, est� pegando a propriedade de width do Stage 
		principal e incluindo no width preferencial da minha tableViewDepartment */
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(depService == null) {
			throw new IllegalStateException("Servi�o est� NULO");
		}
		List<Department> list = depService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList); 
		initEditButtons();// acrescenta um novo Button "Edit" em cada linha da tabela view
		initRemoveButtons();// acrescenta um novo Button "Remove" em cada linha da tabela view
	}
	
	private void createDialogForm(Department obj, String absoluteName, Stage parantStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this); /* aqui estamos INSCREVENDO esse m�todo
			no DataChangeListener */
			
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department Data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parantStage); // esse "initOwner" � para dizer quem � o PAI da janela/palco, que no caso vem 
			// l� dos parametros passados no como Stage na cria��o do m�todo
			dialogStage.initModality(Modality.WINDOW_MODAL); // isso � para "travar" a janela acima da janela de fundo..ou seja, 
			// n�o ter� como mexer na janela de fundo se essa janela estiver aberta na frente..
			dialogStage.showAndWait(); // porque ShowAndWait e n�o Show? n�o sei!
			
			
		}catch(IOException e) {
			Alerts.showAlerts("IOException", "Error Loanding View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() { /* Classe m�h dahora... rsrsr .. ela CRIA um Button "Edit" 
	dentro de cada linha da TableView e passa o Department selecionado para a tela DepartmentForm */
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Department, Department>(){
			private final Button button = new Button("Edit");
			
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if(obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/DepartmentForm.fxml",
																Utils.currentStage(event)));
			}
			
		});
	}
	
	private void initRemoveButtons() { /* Classe m�h dahora... rsrsr .. ela CRIA um Button "Edit" 
		dentro de cada linha da TableView e passa o Department selecionado para a tela DepartmentForm */
			tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>(){
				private final Button button = new Button("Remove");
				
				protected void updateItem(Department obj, boolean empty) {
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

	private void removeEntity(Department obj) {
		
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
 
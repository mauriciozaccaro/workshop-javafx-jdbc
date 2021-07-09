package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{

	private DepartmentService depService; // isso cria uma dependência láaaaa da classe de serviço DepartmentService
	
	public void setDepartmentService(DepartmentService depService) {
		this.depService = depService; // INJEÇÃO de dependência
	}
	
	private ObservableList<Department> obsList;
	
	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction()");
	}	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();	
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // ainda não sei para que serve... fiquei em duvida
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); 
		/* basicamente, está pegando a propriedade de width do Stage 
		principal e incluindo no width preferencial da minha tableViewDepartment */
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	

	public void updateTableView() {
		if(depService == null) {
			throw new IllegalStateException("Serviço está NULO");
		}
		
		List<Department> list = depService.findAll();
		obsList = FXCollections.observableArrayList(list);
		
		tableViewDepartment.setItems(obsList); 
	}
	

}

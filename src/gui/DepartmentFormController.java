package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {

	private Department entity; // entidade relacionada a esse formulário 
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label lblIdError;
	
	@FXML
	private Label lblNameError;
	
	@FXML 
	public void onBtSaveAction(ActionEvent event) {
		System.out.println("Botão Salvar");
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		System.out.println("Botão Cancelar");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();
		
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 20);
	}
	
	public void setDepartment(Department entity) {
		this.entity = entity; // injeção de dependência
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId())); // passando para o txtId os dados do Id que vierem do Department instaciado
		txtName.setText(String.valueOf(entity.getName()));
	}
}

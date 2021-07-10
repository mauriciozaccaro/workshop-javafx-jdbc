package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import bd.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationExceptions;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity; // entidade relacionada a esse formulário 
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label lblIdError;
	
	@FXML
	private Label lblNameError;
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener); // ainda não entendi para que vai servir.
		// parece que tem relação com colocar uma lista de objetos (botoes, labels, telas etc..) 
		// quepoderão receber alguns comandos... se não for isso eu apago o coment 
	}
	
	@FXML 
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);			
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			Alerts.showAlerts(null, null, "Cadastro salvo com Sucesso!", AlertType.INFORMATION);
			
			
		}catch(ValidationExceptions ve) {
			setErrorMessages(ve.getErrors()); // traz a coleção de erros do Ma<>
		}
		catch(DbException db) {
			Alerts.showAlerts("Erro ao Salvar dados", null, db.getMessage(), AlertType.ERROR);
		}
			
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationExceptions exceptions = new ValidationExceptions("Validation Error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exceptions.addError("name", "Field can't be empty"); // adicionando uma exceção caso exista
		}
		
		obj.setName(txtName.getText());
		if(exceptions.getErrors().size() > 0) { // se tiver pelo menos 1 erro
			throw exceptions; // retorna a exceção
		}
		return obj;
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			Alerts.showAlerts(null, null, errors.get("name"), AlertType.ERROR);
			lblNameError.setText(errors.get("name"));
		}
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 50);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	public void setSeller(Seller entity) {
		this.entity = entity; // injeção de dependência
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId())); // passando para o txtId os dados do Id que vierem do Seller instaciado
		if(entity.getId() == null) {
			txtName.setText("");
			txtEmail.setText("");
			txtBaseSalary.setText("");
			                                                                                                                                
		}else {
			txtName.setText(entity.getName());
			txtEmail.setText(entity.getEmail());
			Locale.setDefault(Locale.US);
			txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
			if(entity.getBirthDate() != null) {
				dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), 
																  ZoneId.systemDefault()));
			}
		}
	}
}

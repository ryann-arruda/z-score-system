package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import entities.Nutritionist;
import entities.service.NutritionistService;
import exceptions.FieldValidationException;
import exceptions.UserRegistrationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.Alerts;
import util.Constraints;
import util.Utils;

public class RegisterController implements Initializable{
	
	private NutritionistService service;
	
	private Nutritionist nutritionist;
	
	@FXML
	private TextField name;
	
	@FXML
	private DatePicker dateBirth;
	
	@FXML
	private TextField regionalCouncilNutritionists;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private PasswordField passwordConfirmation;
	
	@FXML
	private Label nameError;
	
	@FXML
	private Label dateBirthError;
	
	@FXML
	private Label regionalCouncilNutritionistsError;
	
	@FXML
	private Label usernameError;
	
	@FXML
	private Label passwordError;
	
	@FXML
	private Button register;
	
	@FXML
	private Button cancel;
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}
	
	@FXML
	public void onRegister(ActionEvent event) {		
		if(nutritionist == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			getFormData();
			
			if(service.save(nutritionist) == null) {
				throw new UserRegistrationException("Nome de usuário já existe!");
			}
			
			Alerts.showAlert(null, null, "Cadastro realizado com sucesso!", AlertType.CONFIRMATION);
			Utils.getCurrentStage(event).close();
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", null, "Não é possível cadastrar um novo usuário.", AlertType.ERROR);
		}
		catch(UserRegistrationException e) {
			Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onCancel(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}
	
	private boolean comparePasswords() {
		return (password.getText() != null) && (passwordConfirmation.getText() != null) &&
				(!password.getText().trim().equals("")) && (!passwordConfirmation.getText().trim().equals("")) &&
				(password.getText().equals(passwordConfirmation.getText()));
	}
	
	private void validateFormData() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(name.getText() == null || name.getText().trim().equals("")) {
			exception.addError("nameError", "Insira um nome válido!");
		}
		
		if(dateBirth.getValue() == null) {
			exception.addError("dateBirthError", "Insira uma data de nascimento válida!");
		}
		
		if(regionalCouncilNutritionists.getText() == null ||
		   regionalCouncilNutritionists.getText().trim().equals("")) {
			exception.addError("regionalCouncilNutritionistsError", "Insira um número de CRN válido!");
		}
		
		if(username.getText() == null || username.getText().trim().equals("")) {
			exception.addError("usernameError", "Insira um nome de usuário válido!");
		}
		
		if(!comparePasswords()) {
			exception.addError("passwordError", "Insira uma senha válida!");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
	}
	
	private Date getDateBirth() {
		LocalDate localDate = dateBirth.getValue();
		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	private void getFormData() {		
		validateFormData();
		
		nutritionist.setName(name.getText());
		nutritionist.setDateBirth(getDateBirth());
		nutritionist.setRegionalCouncilNutritionists(regionalCouncilNutritionists.getText());
		nutritionist.setUsername(username.getText());
		nutritionist.setPassword(password.getText());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("nameError")) {
			nameError.setText(errors.get("nameError"));
		}
		else {
			nameError.setText("");
		}
		
		if(fields.contains("dateBirthError")) {
			dateBirthError.setText(errors.get("dateBirthError"));
		}
		else {
			dateBirthError.setText("");
		}
		
		if(fields.contains("regionalCouncilNutritionistsError")) {
			regionalCouncilNutritionistsError.setText(errors.get("regionalCouncilNutritionistsError"));
		}
		else {
			regionalCouncilNutritionistsError.setText("");
		}
		
		if(fields.contains("usernameError")) {
			usernameError.setText(errors.get("usernameError"));
		}
		else {
			usernameError.setText("");
		}
		
		if(fields.contains("passwordError")) {
			passwordError.setText(errors.get("passwordError"));
		}
		else {
			passwordError.setText("");
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		dateBirth.getEditor().setDisable(true);
		Constraints.setTextFieldMaxLength(username, 30);
		Constraints.setPasswordFieldMaxLength(password, 30);
	}
}
